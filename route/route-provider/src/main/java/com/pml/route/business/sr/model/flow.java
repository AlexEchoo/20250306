package com.pml.route.business.sr.model;

import com.pml.sdsn.common.model.Path;
import com.pml.sdsn.common.model.Satellite;
import lombok.*;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class flow {
    private Integer id;
    private groundStationNode sourceGroundStation;
    private groundStationNode targetGroundStation;
    private satNode sourceSat;
    private satNode targetSat;
    private Double rate;
    private Double burst;
    private Integer startTime;
    private Integer deadTime;
    private Double maxDelay;
    private List<Double> queueingDelay;
    private GraphPath<String, DefaultWeightedEdge> path;
    private Double pathBoundedDelay;
    private String pathStatus;
    private String TypeOfService;
    private Double targetDelay;
    private Double targetJitter;
    private List<DefaultWeightedEdge> newPath;
    private List<List<Integer>> jitterPathSet;
    private List<Double> jitterDelaySet;

}
