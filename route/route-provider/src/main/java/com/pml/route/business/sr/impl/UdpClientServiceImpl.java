package com.pml.route.business.sr.impl;

import com.pml.route.business.sr.UdpClientService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Component
public class UdpClientServiceImpl implements UdpClientService {
    private DatagramSocket socket;
    private InetAddress address;
    @Override
    public void sendMsg(byte[] content, String host, int port, Integer sequence, boolean b) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        try {
            address = InetAddress.getByName(host);
            String str = "Hello World";
            String str1 = new String(content);
//            System.out.println(str1);

            //列个表，需要那部分找他们发
            //新增接口 CBS的东西+路径+引流策略
            //再找文献计算idleslope和sendslope
            DatagramPacket packet = new DatagramPacket(content, content.length, address, port);
//            DatagramPacket packet = new DatagramPacket(str1.getBytes(), str1.getBytes().length, address, port);
            socket.send(packet);
        }catch (Exception e){
            System.out.println("exception now");
        }finally {
            socket.close();
        }


    };


    // 发送程序
    public void sendPath(int flowID, List<Integer> path) throws IOException {

        SrSbiSender srSbiSender = new SrSbiSender();
        String str2 = srSbiSender.getTestString(flowID, path);

        String host = "localhost";
        int port = 10060;
        Integer seq = 1;
        boolean b = true;

        sendMsg(str2.getBytes(), host, port, seq, b);

    }

}
