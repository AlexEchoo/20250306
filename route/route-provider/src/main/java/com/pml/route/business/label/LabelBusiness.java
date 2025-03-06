package com.pml.route.business.label;


import com.pml.route.business.LabelForwardTable;

import java.util.List;

public interface LabelBusiness {


    /**
     * 查询所有的标签转发表
     * @return List<LabelForward>
     */
    List<LabelForwardTable> queryAllLabelForward();

}
