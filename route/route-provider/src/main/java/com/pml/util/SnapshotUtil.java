package com.pml.util;

import com.pml.sdsn.common.model.LinkPath;
import com.pml.sdsn.common.model.Path;
import com.pml.sdsn.common.model.Satellite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public final class SnapshotUtil {

    public static LinkPath translate2LinkPath(Path path) {
        LinkPath linkPath = new LinkPath();
        if (path == null || CollectionUtils.isEmpty(path.getPath())) {
            return linkPath;
        }
        linkPath.setPath(path.getPath());
        linkPath.setDelay(path.getDelay());
        linkPath.setHop(path.getHop());
        linkPath.setMetric(path.getMetric());
        return linkPath;
    }

    public static String getHostByIp(String ip) {
        if (ip.contains("/")) {
            return ip.substring(0, ip.indexOf("/"));
        }
        return ip;
    }

    public static String getHostName(Satellite satellite) {
        String ovsIp = satellite.getOvsIp();
        return ovsIp.substring(0, ovsIp.indexOf("/"));
    }
}
