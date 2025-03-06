package com.pml.route.business.path;

import com.pml.route.business.path.model.SnapshotPath;
import com.pml.sdsn.common.model.PathParam;

import java.util.List;

public interface PathBusiness {


    List<SnapshotPath> computeTunnelPath(Long oldTime, Long snapshotTime, List<PathParam> pathParams);

}
