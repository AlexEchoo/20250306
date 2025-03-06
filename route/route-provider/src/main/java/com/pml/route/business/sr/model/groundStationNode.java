package com.pml.route.business.sr.model;

import com.pml.sdsn.common.model.Satellite;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class groundStationNode {
    private Integer id = null;
    private Double longitude = null;
    private Double latitude = null;
}
