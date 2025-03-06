package com.pml.route.business.topo;

import com.pml.sdsn.common.model.Link;

import java.util.Map;

public interface TopoBusiness {


    Map<String, Link> queryAllLinkMap();
}