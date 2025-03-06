package com.pml.route.business.sr.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pml.route.business.path.PathBusiness;
import com.pml.route.business.path.model.SnapshotPath;
import com.pml.route.business.sr.SrConstant;
import com.pml.route.business.sr.model.SrTunnelService;
import com.pml.route.business.sr.model.TunnelSatellite;
import com.pml.route.business.topo.TopoBusiness;

import com.pml.sdsn.common.model.Link;
import com.pml.sdsn.common.model.Path;
import com.pml.sdsn.common.model.PathParam;
import com.pml.sdsn.common.model.SRTunnel;
import com.pml.sdsn.common.model.Satellite;
import com.pml.sdsn.common.model.TunnelParam;
import com.pml.sdsn.common.model.TunnelPath;
import com.pml.util.ResourcePool;
import com.pml.util.SequenceResourceUtil;
import com.pml.util.SnapshotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


@Slf4j
public class SrImpl {

    @Autowired
    private SrSbiSender sbiSender;

    @Autowired
    private TopoBusiness topoBusiness;

    @Autowired
    private ResourcePool resourcePool;

    @Autowired
    PathBusiness pathBusiness;

    @Autowired
    private SequenceResourceUtil sequenceResourceUtil;

    //  创建SR隧道，一个SR隧道包含源、目的卫星两个卫星。
    public void createSrPath(TunnelSatellite tunnelSatellite) throws Exception {

        //  建立SR隧道
        TunnelParam tunnelParam = buildTunnelParam(tunnelSatellite.getSourceSatellite(),
                tunnelSatellite.getDestSatellite());

        //  快照时间
        Long snapshotTime = System.currentTimeMillis() / 1000;
        //  隧道参数
        PathParam tunnelPathParam = buildTunnelPathParam(tunnelSatellite.getSourceSatellite().getUuid(), tunnelSatellite.getDestSatellite().getUuid());
        Integer tunnelId = resourcePool.requestResource(ResourcePool.ResourceType.TUNNEL);
        tunnelPathParam.setTunnelUuid(tunnelId + "");
        //  计算路径
        List<SnapshotPath> snapshotPath = pathBusiness.computeTunnelPath(null, snapshotTime,
                Lists.newArrayList(tunnelPathParam));
        SrTunnelService srTunnelService = buildUserSrTunnel(tunnelParam, tunnelSatellite, tunnelId);
        setTunnelPath(srTunnelService, snapshotPath.get(0));
        //  获得计算得到的SR路径
        TunnelPath srPath = srTunnelService.getSrTunnel().getMasterSrPath();
        Map<String, Integer> sat2NumMap = Maps.newConcurrentMap();
        sat2NumMap.put(srTunnelService.getSrTunnel().getSourceSatellite(), 1);
        Map<String, List<Integer>> sat2Seq = sequenceResourceUtil.getSequences(sat2NumMap);
        //  下发路径
        sbiSender.sendSrPaths(tunnelSatellite.getSourceSatellite(), Lists.newArrayList(srPath),
                SrConstant.CREATE_TUNNEL_TYPE, srTunnelService, tunnelSatellite.getDestSatellite(), sat2Seq.get(srTunnelService.getSrTunnel().getSourceSatellite()).get(0));

    }


    private TunnelParam buildTunnelParam(Satellite source, Satellite dest) {
        TunnelParam tunnelParam = new TunnelParam();
        tunnelParam.setSourceUuid(source.getUuid());
        tunnelParam.setDestUuid(dest.getUuid());
        return tunnelParam;
    }


    private PathParam buildTunnelPathParam(String source, String dest) {
        return new PathParam().sourceUuid(source).destUuid(dest);
    }


    private SrTunnelService buildUserSrTunnel(TunnelParam tunnelParam, TunnelSatellite tunnelSatellite, Integer tunnelId) {
        SRTunnel srTunnel = new SRTunnel();
        Satellite sourceSatellite = tunnelSatellite.getSourceSatellite();
        Satellite destSatellite = tunnelSatellite.getDestSatellite();
        srTunnel.setTunnelId(tunnelId);
        srTunnel.setBandwidth(tunnelParam.getBandwidth());
        srTunnel.setSourceSatellite(sourceSatellite.getUuid());
        srTunnel.setSourceSatelliteName(sourceSatellite.getName());
        srTunnel.setDestSatellite(destSatellite.getUuid());
        srTunnel.setDestSatelliteName(destSatellite.getName());
        srTunnel.setStatus(SRTunnel.StatusEnum.NORMAL);
        srTunnel.setPriority(tunnelParam.getPriority());
        srTunnel.setSupportHsb(tunnelParam.isSupportHsb());
        srTunnel.setDelay(tunnelParam.getDelay());
        srTunnel.setCalcPolicy(tunnelParam.getCalcPolicy());
        srTunnel.setConstraint(tunnelParam.getConstraint());
        return SrTunnelService.builder()
                .sourceUserAddr(tunnelParam.getSourceUserAddr())
                .sourceCliAddr(tunnelParam.getSourceCli())
                .destUserAddr(tunnelParam.getDestUserAddr())
                .destCliAddr(tunnelParam.getDestCli())
                .accessType(tunnelParam.getAccessType().toString())
                .sourceUuid(tunnelParam.getSourceUuid())
                .destUuid(tunnelParam.getDestUuid())
                .vpnType(tunnelParam.getVpnType())
                .vpnName(tunnelParam.getVpnName())
                .srTunnel(srTunnel)
                .build();
    }

    private void setTunnelPath(SrTunnelService srTunnelService, SnapshotPath snapshotPath) throws Exception {
        SRTunnel srTunnel = srTunnelService.getSrTunnel();
        log.info("Set tunnel path, source {} dest {}", srTunnel.getSourceSatelliteName(), srTunnel.getDestSatelliteName());
        Map<String, Link> linkMap = topoBusiness.queryAllLinkMap();
        Path masterPath = snapshotPath.getPathResult().getPrimaryPath();
        srTunnel.setMasterPath(SnapshotUtil.translate2LinkPath(masterPath));
        srTunnel.setMasterSrPath(translate2TunnelPath(masterPath, linkMap, true, srTunnel.getPriority()));
        if (srTunnel.isSupportHsb() != null && srTunnel.isSupportHsb()) {
            Path slavePath = snapshotPath.getPathResult().getSlavePath();
            srTunnel.setSlavePath(SnapshotUtil.translate2LinkPath(slavePath));
            srTunnel.setSlaveSrPath(translate2TunnelPath(slavePath, linkMap, false, srTunnel.getPriority()));
        }
    }

    private TunnelPath translate2TunnelPath(Path path, Map<String, Link> linkMap, boolean isMaster, Integer priority) throws Exception {
        TunnelPath tunnelPath = new TunnelPath();
        if (path == null || CollectionUtils.isEmpty(path.getPath())) {
            log.error("The path is empty.");
            tunnelPath.setIsMaster(isMaster);
            return tunnelPath;
        }
        return sbiSender.translate2TunnelPath(path.getPath(), linkMap, isMaster, priority);
    }


}
