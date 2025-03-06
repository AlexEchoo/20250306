package com.pml;

import com.pml.route.business.sr.impl.SrSbiSender;
import com.pml.route.business.sr.impl.UdpClientServiceImpl;
import com.pml.route.business.sr.model.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendMessage {
    public static void main(String[] args) throws IOException {

        UdpClientServiceImpl udpClientService = new UdpClientServiceImpl();

//        byte[] result = new byte[4];
//        result[0] = (byte)((1 >> 24) & 0xFF);
//        result[1] = (byte)((56 >> 16) & 0xFF);
//        result[2] = (byte)((32 >> 8) & 0xFF);
//        result[3] = (byte)(6 & 0xFF);
//        System.out.println(result[0]+","+result[1]+","+result[2]+","+result[3]);
//
//        byte[] ssr = {49,50,51,52};
//        System.out.println(ssr[0]+","+ssr[1]+","+ssr[2]);
//        String str = new String(ssr);
//        System.out.println(str);




//        String str1 = "123456";
//        byte[] str1tobyte = str1.getBytes();
//        for (int i=0; i < str1tobyte.length; i++){
//            System.out.println(str1tobyte[i]);
//        }
//        String str1back = new String(str1tobyte);
//        System.out.println(str1back);
//
//        SrSbiSender srSbiSender = new SrSbiSender();
//        String str2 = srSbiSender.getTestString(2);
//
//
//        String host = "localhost";
//        int port = 10060;
//        Integer seq = 1;
//        boolean b = true;
//        udpClientService.sendMsg(str2.getBytes(), host, port, seq, b);

    }
}