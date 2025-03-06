package com.pml.route.business.sr.model;

import com.pml.sdsn.common.model.Satellite;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class satNode {
    private Integer id = null;
    private Satellite sourceSatellite = null;
    private Double longitude = null;
    private Double latitude = null;
    private groundStationNode groundStation = null;
}
