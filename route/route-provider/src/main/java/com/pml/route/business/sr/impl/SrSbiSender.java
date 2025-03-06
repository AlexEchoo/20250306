package com.pml.route.business.sr.impl;

import com.pml.route.business.LabelForwardTable;
import com.pml.route.business.label.LabelBusiness;
import com.pml.route.business.sr.SrConstant;
//import com.pml.route.business.sr.UdpClientService;
import com.pml.route.business.sr.UdpClientService;
import com.pml.route.business.sr.model.SrTunnelService;
import com.pml.sdsn.common.model.Link;
import com.pml.sdsn.common.model.PathKey;
import com.pml.sdsn.common.model.Satellite;

import com.pml.sdsn.common.model.TunnelPath;
import com.pml.util.PacketTypeConstant;
import com.pml.util.SequenceResourceUtil;
import com.pml.util.SnapshotUtil;
import com.pml.util.TypeTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Component
public class SrSbiSender {

    @Autowired
    private UdpClientService udpDataSender;

    @Autowired
    private LabelBusiness labelBusiness;


    public void sendSrPaths(Satellite node, List<TunnelPath> srPaths,
                                   byte action, SrTunnelService srTunnelService, Satellite destSat, Integer sequence) throws IOException {
        Integer tunnelId = srTunnelService.getSrTunnel().getTunnelId();
        log.info("send sr path, tunnel {}, action {}, node {}, sequence {}, srPath {}",
                tunnelId, action, node.getName(), sequence, srPaths);

        String host = SnapshotUtil.getHostName(node);

        // 1.send SR path
        // 组UDP报文，报文存储着SR路径的信息
        byte[] content = getSrPathTcpPacket(srPaths, action, srTunnelService, destSat, sequence);
        log.info("UDP content {}", content);
        // 疑问：这里的sequence和true是什么意思呢？
        udpDataSender.sendMsg(content, host, 56789, sequence, true);

        byte[] CBSContent = calculateCBScomponents(1,(short)1,(short)2,
                (float)0.1,1024*1024,1500);
        // 2.how 2 send CBS configuration
        // get CBS datagram
        // log datagram
        log.info("UDP CBScontent {}", CBSContent);
        // send datagram
        udpDataSender.sendMsg(CBSContent, host, 56789, sequence, true);

    }

    public byte[] getSrPathTcpPacket(List<TunnelPath> srPaths, byte action, SrTunnelService srTunnelService,
                                     Satellite destSat, Integer sequence) {
        //  报头长度
        int headLength = Short.BYTES * 2 + SequenceResourceUtil.SEQ_LEN;
        //  数据块长度
        int dataBlockNumber = srPaths.size();
        int dataBlockNumberLength = Integer.BYTES;
        //  消息长度
        short messageLength = (short) (dataBlockNumberLength);
        //  包长度 = 内容长度 + 报头长度
        byte[] content = new byte[messageLength + headLength];
        //  message type
        short msgType = srTunnelService.getIsMovingTunnel() == null ? PacketTypeConstant.PACKET_TYPE_TUNNEL : PacketTypeConstant.PACKET_TYPE_MOVE_TUNNEL;
        //  short->Byte
        byte[] typeByte = TypeTransfer.transferShort2Byte(msgType);
        short position = 0;
        //  将typeByte从0开始复制length，至content的position处。将类型添加进包中
        System.arraycopy(typeByte, 0, content, position, typeByte.length);
        position += Short.BYTES;
        //message length
        byte[] messageLengthByte = TypeTransfer.transferShort2Byte(messageLength);
        //  将messageLengthByte从0开始复制length，至content的position处。将消息长度添加进包中
        System.arraycopy(messageLengthByte, 0, content, position, messageLengthByte.length);
        position += Short.BYTES;
        // 将顺序字节添加进包中
        // sequence
        byte[] seqByte = TypeTransfer.transferInt2Byte(sequence);
        System.arraycopy(seqByte, 0, content, position, seqByte.length);
        position += Integer.BYTES;
        // 将数据块添加进包中
        //message num
        byte[] dataBlockNumberByte = TypeTransfer.transferInt2Byte(dataBlockNumber);
        System.arraycopy(dataBlockNumberByte, 0, content, position, dataBlockNumberByte.length);
        position += Integer.BYTES;
        String vpnType = srTunnelService.getVpnType();
        int tunnelId = srTunnelService.getSrTunnel().getTunnelId();
        String tunnelIfName = "tunnel" + tunnelId;
        //  将SR路径中数据存入报文中
        for (TunnelPath srPath : srPaths) {

            // 隧道接口名称
            byte[] tunnelIfNameBytes = tunnelIfName.getBytes();
            System.arraycopy(tunnelIfNameBytes, 0, content, position, tunnelIfNameBytes.length);
            position += SrConstant.IFNAME_MAX_LEN;

            //  next hop
            if (Strings.isNotEmpty(srPath.getNextHop())) {
                byte[] nextHopByte = TypeTransfer.ipv4Address2BinaryArray(srPath.getNextHop());
                System.arraycopy(nextHopByte, 0, content, position, nextHopByte.length);
            }
            position += Integer.BYTES;

            //  出接口名
            if (Strings.isNotEmpty(srPath.getOutInterface())) {
                byte[] outIfName = srPath.getOutInterface().getBytes(StandardCharsets.UTF_8);
                System.arraycopy(outIfName, 0, content, position, outIfName.length);
            }
            position += SrConstant.IFNAME_MAX_LEN;

            //label num
            int labelSize = SrConstant.DEL_TUNNEL_TYPE == action || CollectionUtils.isEmpty(srPath.getLabels()) ? 0 : srPath.getLabels().size() ;
            byte[] labelsNums = TypeTransfer.transferInt2Byte(labelSize);
            System.arraycopy(labelsNums, 3, content, position, 1);
            position += Byte.BYTES;

            //option code
            byte[] opCode = new byte[]{action};
            System.arraycopy(opCode, 0, content, position, 1);
            position += Byte.BYTES;

            //role
            byte role = srPath.isIsMaster() ? SrConstant.PATH_ROLE_MASTER : SrConstant.PATH_ROLE_SLAVE;
            byte[] roleByte = new byte[]{role};
            System.arraycopy(roleByte, 0, content, position, roleByte.length);
            position += Byte.BYTES;

            //ttl
            position += Byte.BYTES;

            // qos
            if (srPath.getPriority() != null) {
                byte priorityByte = srPath.getPriority().byteValue();
                byte[] priority = new byte[]{priorityByte};
                System.arraycopy(priority, 0, content, position, priority.length);
            }
            position += Byte.BYTES;

            //vc id
            byte[] vcLabelByte = TypeTransfer.transferShort2Byte((short) tunnelId);
            System.arraycopy(vcLabelByte, 0, content, position, vcLabelByte.length);
            position += Short.BYTES;

            // peer IP = tunnel dest device ip
            byte[] destIpByte = TypeTransfer.ipv4Address2BinaryArray(SnapshotUtil.getHostByIp(destSat.getOvsIp()));
            System.arraycopy(destIpByte, 0, content, position, destIpByte.length);
            position += Integer.BYTES;

            // bandwidth
            long bandwidth = srTunnelService.getSrTunnel().getBandwidth() == null ? 0 : srTunnelService.getSrTunnel().getBandwidth();
            byte[] bandwidthByte = TypeTransfer.transferLong2Byte(bandwidth);
            System.arraycopy(bandwidthByte, 0, content, position, bandwidthByte.length);
            position += Long.BYTES;

            // SR的label存入报文中，把所有的label全都注入进报文中，maybe是SR-TE方法
            if (labelSize > 0) {
                for (Integer label : srPath.getLabels()) {
                    byte[] labelByte = TypeTransfer.transferInt2Byte(label);
                    System.arraycopy(labelByte, 0, content, position, labelByte.length);
                    position += Integer.BYTES;
                }
            }
        }

        return content;
    }

    public TunnelPath translate2TunnelPath(List<String> path, Map<String, Link> linkMap, boolean isMaster, Integer priority) throws Exception {
        TunnelPath tunnelPath = new TunnelPath();
        if (path == null || org.apache.commons.collections.CollectionUtils.isEmpty(path)) {
            log.error("The path is empty.");
            tunnelPath.setIsMaster(isMaster);
            return tunnelPath;
        }
        List<LabelForwardTable> labelTables = labelBusiness.queryAllLabelForward();
        if (org.apache.commons.collections.CollectionUtils.isEmpty(labelTables)) {
            log.error("The labelForward table is empty.");
            return tunnelPath;
        }
        Map<PathKey, LabelForwardTable> labelForwardMap = labelTables.stream()
                .collect(Collectors.toMap(labelForward -> buildPathKey(labelForward.getSatelliteId(),
                        labelForward.getNextSatellite()), labelForward -> labelForward));
        List<LabelForwardTable> srForwardTablePath = path.stream().map(linkId -> {
            Link link = linkMap.get(linkId);
            PathKey pathKey = buildPathKey(link.getSource(), link.getDestination());
            return labelForwardMap.get(pathKey);
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (path.size() != srForwardTablePath.size()) {
            throw new Exception("Not all the path links have label forward table.");
        }
        LabelForwardTable firstLabelForward = srForwardTablePath.get(0);
        tunnelPath.setOutInterface(firstLabelForward.getOutInterfaceName());
        tunnelPath.setNextHop(firstLabelForward.getNextHop());
        List<Integer> labelPath = srForwardTablePath.stream().map(LabelForwardTable::getLabel).collect(Collectors.toList());
        tunnelPath.setLabels(labelPath);
        tunnelPath.setPriority(priority);
        tunnelPath.setIsMaster(isMaster);
        return tunnelPath;
    }

    private PathKey buildPathKey(String source, String dest) {
        PathKey pathKey = new PathKey();
        pathKey.setSourceUuid(source);
        pathKey.setDestUuid(dest);
        return pathKey;
    }

    private byte[] getCBSContent(int orderType,short dataFiledLength,int TransID,short interMessageNum,short lineCardID,
                                short targetLineCardID,short IsCBSCounter,int idleSlope,int sendSlope,
                                short queueBandwidth,int hiCredit,int loCredit)
    {
        //  消息长度：头部（指令类型 4 + 数据域长度 2）+ 5 + 37*N个消息(maybe N=1?)

        //  报头长度
        int headLength = Integer.BYTES + Short.BYTES;
        //  消息长度
        short messageLength = (short) (dataFiledLength);
        //  包长度 = 内容长度 + 报头长度
        byte[] content = new byte[messageLength + headLength];

        //  指令类型 4
        byte[] orderTypeByte = TypeTransfer.transferInt2Byte(orderType);
        short position = 0;
        System.arraycopy(orderTypeByte, 0, content, position, orderTypeByte.length);
        position += Integer.BYTES;

        //  数据域长度 2
        byte[] dataBlockNumberByte = TypeTransfer.transferShort2Byte(dataFiledLength);
        System.arraycopy(dataBlockNumberByte, 0, content, position, dataBlockNumberByte.length);
        position += Short.BYTES;

        //  事物ID TransId 4
        byte[] TransIDByte = TypeTransfer.transferInt2Byte(TransID);
        System.arraycopy(TransIDByte, 0, content, position, TransIDByte.length);
        position += Integer.BYTES;

        //  内含消息个数  1
        byte[] interMessageNumByte = TypeTransfer.transferInt2Byte(interMessageNum);
        System.arraycopy(interMessageNumByte, 0, content, position, 1);
        position += 1;

        //  线卡编号 1
        byte[] lineCardIDByte = TypeTransfer.transferInt2Byte(lineCardID);
        System.arraycopy(lineCardIDByte, 0, content, position, 1);
        position += 1;

        //  目标线卡的编号  1
        byte[] targetLineCardIDByte = TypeTransfer.transferInt2Byte(targetLineCardID);
        System.arraycopy(targetLineCardIDByte, 0, content, position, 1);
        position += 1;

        //  是否启用CBS counter  1
        byte[] IsCBSCounterByte = TypeTransfer.transferInt2Byte(IsCBSCounter);
        System.arraycopy(IsCBSCounterByte, 0, content, position, 1);
        position += 1;

        //  Idle_Slope bps  8
        byte[] idleSlopeByte = TypeTransfer.transferInt2Byte(idleSlope);
        System.arraycopy(idleSlopeByte, 0, content, position, idleSlopeByte.length);
        position += Integer.BYTES;

        //  Send_Slope bps  8
        byte[] sendSlopeByte = TypeTransfer.transferInt2Byte(sendSlope);
        System.arraycopy(sendSlopeByte, 0, content, position, sendSlopeByte.length);
        position += Integer.BYTES;

        //  Queue_Bandwidth bps  2
        byte[] queueBandwidthByte = TypeTransfer.transferShort2Byte(queueBandwidth);
        System.arraycopy(queueBandwidthByte, 0, content, position, queueBandwidthByte.length);
        position += Short.BYTES;

        //  Hicredit bps  8
        byte[] hiCreditByte = TypeTransfer.transferInt2Byte(hiCredit);
        System.arraycopy(hiCreditByte, 0, content, position, hiCreditByte.length);
        position += Integer.BYTES;

        //  Locredit bps  8
        byte[] loCreditByte = TypeTransfer.transferInt2Byte(loCredit);
        System.arraycopy(loCreditByte, 0, content, position, loCreditByte.length);
        position += Integer.BYTES;

        return content;

    }

    // 配置一个CBS队列的速率
    private byte[] calculateCBScomponents(int TransID,short lineCardID,short targetLineCardID, float bandwidthFraction,
                                          int portTransmitRate,int maxInterferenceSize)
    {
        int idleSlope = (int)(bandwidthFraction * portTransmitRate);
        int sendSlope = idleSlope - portTransmitRate;
        int hiCredit = maxInterferenceSize * (idleSlope/portTransmitRate);
        int loCredit = maxInterferenceSize * (sendSlope/portTransmitRate);
        short queueBandwidth = (short)(bandwidthFraction * portTransmitRate);
        int CBSOrderType = 0x0008;
        byte[] content = getCBSContent(CBSOrderType,(short)42,TransID,(short)1, lineCardID,
                targetLineCardID,(short) 1,idleSlope,sendSlope,queueBandwidth,hiCredit,loCredit);
        return content;
    }

    public String getTestString (int flowID, List<Integer> path){
        String content = "第" + flowID + "条流的路径是：";
//        List<Integer> testList = new ArrayList<>();
//        testList.add(2);
//        testList.add(45);
//        testList.add(33);
        for (int i=0;i<path.size();i++){
            content = content + path.get(i)+",";
        }
        return content;
    }

}
