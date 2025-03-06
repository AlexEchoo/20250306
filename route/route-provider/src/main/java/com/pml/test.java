package com.pml;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
//        String tempedge = "sat67";
//        System.out.println(tempedge.indexOf("sat"));
//        String sourceString = tempedge.substring(tempedge.indexOf("sat") + 3);
//        System.out.println(sourceString);


        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(15);
        list1.add(16);
        System.out.println(list1);
        ArrayList<Integer> list2 = new ArrayList<>();
        ArrayList<Integer> list3 = new ArrayList<>();
//        list2 = list1;
        list2.addAll(list1);
        list1.add(17);
        System.out.println(list1);
        System.out.println(list2);
    }

    public static void add(List<Integer> a){
        a.add(a.size(),a.get(0));
    }
}
