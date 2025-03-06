package com.pml;

import com.pml.route.business.sr.model.input;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class littetest {
    public static void main(String[] args) throws IOException {
//        int[] haha = {1,2,3,4,5,6,7,8,9};
//        System.out.println(haha[haha.length-1]);


//        List<Integer> pathInt = new ArrayList<>();
//        pathInt.add(1);
//        pathInt.add(2);
//        System.out.println(pathInt);
//        List<Integer> newPath = new ArrayList<>();
//        newPath = pathInt;
//        System.out.println(newPath);
//        System.out.println(newPath.get(newPath.size()-1));

//        double a = 2.3;
//        double b = 2.5;
//        b = a;
//        System.out.println(b);
//        a = a -1;
//        System.out.println(b);
//        System.out.println(a);
//        System.out.println(a/b);

//        Integer a = 18;
//        int b = 18;
//        System.out.println(a != b);

//        List<Integer> a = new ArrayList<>();
//        System.out.println(a.size());


////    测试传播时延
//    Double wA = 0.0;
//    Double jA = 30.0 / 180.0 * Math.PI;
//    Double wB = 0.0;
//    Double jB = 0.0;
//    Double tpy = Math.sqrt(2 *
//                (Math.pow((input.rEarth + input.hSat), 2))
//                * (1 - Math.cos(2 * Math.PI / input.satOfOneOrbit))) / input.opticalSpeed;
//    Double tpx = 2 * (input.rEarth + input.hSat) * Math.asin(
//                Math.sqrt(Math.pow(Math.sin((wA - wB) / 2), 2) + Math.cos(wA) * Math.cos(wB) *
//                        Math.pow(Math.sin((jA - jB) / 2), 2))) / input.opticalSpeed;
//    Double[] res = {tpx, tpy};
//    System.out.println(res[0]+"and"+res[1]);


////        测试接收UDP报文
//        DatagramSocket socket = new DatagramSocket(10060);
//
//        while (true){
//            byte[] buf = new byte[1024];
//            DatagramPacket packet = new DatagramPacket(buf, buf.length);
//            socket.receive(packet);
//            String result = new String(buf,0,packet.getLength());
//            System.out.println(result);
//            if (result.equals("bye")){
//                break;
//            }
//        }
//        socket.close();
//
//
//        try {
//
//        }catch (Exception e){
//            System.out.println("error");
//        }finally {
//            socket.close();
//        }
//
//        int a = 9/2;
//        System.out.println(a);
        List<Integer> pathInt = new ArrayList<>();
        pathInt.add(1);
        pathInt.add(2);
        int b = pathInt.get(0);
        System.out.println(b);
        b++;
        System.out.println(b);
        System.out.println(pathInt.get(0));


//
//
    }
}
