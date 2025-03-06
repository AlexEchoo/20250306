package com.pml.util;

import java.util.List;
import java.util.Map;

public interface SequenceResourceUtil {

    public static final int SEQ_LEN = 4;


    Map<String, List<Integer>> getSequences(Map<String, Integer> sat2NumMap);
}
