package com.pml.route.business.path.model;

import com.pml.sdsn.common.model.PathResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SnapshotPath {
    private PathResult pathResult;
}