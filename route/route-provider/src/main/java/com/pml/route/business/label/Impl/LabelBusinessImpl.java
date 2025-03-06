package com.pml.route.business.label.Impl;

import com.pml.route.business.LabelForwardTable;
import com.pml.route.business.label.LabelBusiness;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelBusinessImpl implements LabelBusiness {

    @Override
    public List<LabelForwardTable> queryAllLabelForward()
    {
        // 设置label表的返回值
        LabelForwardTable tempTable = new LabelForwardTable();
        tempTable.setLabel(123);
        return (List<LabelForwardTable>) tempTable;
    }

}
