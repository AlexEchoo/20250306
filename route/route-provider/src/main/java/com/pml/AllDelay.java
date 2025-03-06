package com.pml;

import com.pml.route.business.sr.model.*;
import com.pml.route.business.sr.model.Curve.Curve;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableEurekaClient
// @ImportResource(locations = {"classpath:spring/spring-bean.xml"})
@MapperScan({"com.pml.route.database.mapper", "com.pml.sdsn.topology.database.mapper"})
public class AllDelay {
    private static ConfigurableApplicationContext context;
    public static final int DEFAULT_MAX_PER_ROUTE = 200;
    public static final int VALIDATE_AFTER_INACTIVITY = 3000000;
    public static final int SOCKET_TIMEOUT = 60000;
    public static final int CONNECT_TIMEOUT = 10000;
    public static final int CONNECTION_REQUEST_TIMEOUT = 10000;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //单个路由最大连接数
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        connectionManager.setMaxTotal(DEFAULT_MAX_PER_ROUTE);
        // 最大空间时间
        connectionManager.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);
        RequestConfig requestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间，超过抛出read timeout
                .setSocketTimeout(SOCKET_TIMEOUT)
                //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectTimeout(CONNECT_TIMEOUT)
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();


        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
    }


    public static void main(String[] args) throws IOException {
        // 初始化输入、图和流
        input Input = new input();
        graph[] Graph = new graph[(int) Math.ceil(Input.simulationTime / Input.snapshotInterval)];
        flow[] flow = new flow[input.numOfFlow];

        // 初始化流到图的映射功能
        flow2graph[] f2g = new flow2graph[(int) Math.ceil(Input.simulationTime / Input.snapshotInterval)];
        for (int i = 0; i < (int) Math.ceil(Input.simulationTime / Input.snapshotInterval); i++) {
            Graph[i] = new graph(Input, i);
            f2g[i] = new flow2graph(flow, Graph[i]);
        }

        // 初始化流的目标时延
        for (int i = 0; i < input.numOfFlow; i++) {
            flow[i] = initFlow.initFlow(Graph[0]);
            System.out.println(initFlow.getMaxMinDelay(flow[i], Graph, Input));
            flow[i].setTargetDelay(initFlow.getMaxMinDelay(flow[i], Graph, Input));
        }


        link[] links = Graph[0].getLinks();
        for (int i = 0;i < links.length; i++) {
            System.out.println("ID为"+ links[i].getId() + "的链路传播时延是" + links[i].getPropagationDelay());
            if (links[i].getPropagationDelay() != null) {
                System.out.println("ID为" + links[i].getId() + "的经度是" + links[i].getSourceSatellite().getLongitude() + "," + links[i].getDestSatellite().getLongitude() + "纬度是" + links[i].getSourceSatellite().getLatitude() + "," + links[i].getDestSatellite().getLatitude());
            }
        }
    }
}