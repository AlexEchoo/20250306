package com.pml.route.business.sr.model;

import com.pml.sdsn.common.model.Satellite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TunnelSatellite {
    private Satellite sourceSatellite;
    private Satellite destSatellite;
}
