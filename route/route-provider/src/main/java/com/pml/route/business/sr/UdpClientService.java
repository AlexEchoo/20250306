package com.pml.route.business.sr;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

@Configuration
public interface UdpClientService {
    void sendMsg(byte[] content, String host, int port, Integer sequence, boolean b) throws IOException;
}