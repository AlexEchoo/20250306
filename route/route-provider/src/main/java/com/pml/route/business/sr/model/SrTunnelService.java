package com.pml.route.business.sr.model;

import com.pml.sdsn.common.model.SRTunnel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SrTunnelService {
    private String sourceCliAddr;
    private String sourceUserAddr;
    private String sourceUuid;
    private String destCliAddr;
    private String destUserAddr;
    private String destUuid;
    private String accessType;
    private SRTunnel srTunnel;
    private String vpnType;
    private String vpnName;
    // 是否是移动性请求的隧道
    private Boolean isMovingTunnel;
}