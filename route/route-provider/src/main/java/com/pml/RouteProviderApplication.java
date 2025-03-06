//package com.pml;
//
//import com.pml.route.business.sr.model.*;
//import com.pml.route.business.sr.model.Curve.Curve;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.jgrapht.GraphPath;
//import org.jgrapht.graph.DefaultWeightedEdge;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.concurrent.CompletableFuture;
//
//@SpringBootApplication
//@EnableEurekaClient
//// @ImportResource(locations = {"classpath:spring/spring-bean.xml"})
//@MapperScan({"com.pml.route.database.mapper", "com.pml.sdsn.topology.database.mapper"})
//public class RouteProviderApplication {
//    private static ConfigurableApplicationContext context;
//    public static final int DEFAULT_MAX_PER_ROUTE = 200;
//    public static final int VALIDATE_AFTER_INACTIVITY = 3000000;
//    public static final int SOCKET_TIMEOUT = 60000;
//    public static final int CONNECT_TIMEOUT = 10000;
//    public static final int CONNECTION_REQUEST_TIMEOUT = 10000;
//
//    @LoadBalanced
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(httpRequestFactory());
//    }
//
//    @Bean
//    public ClientHttpRequestFactory httpRequestFactory() {
//        return new HttpComponentsClientHttpRequestFactory(httpClient());
//    }
//
//    @Bean
//    public HttpClient httpClient() {
//        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", SSLConnectionSocketFactory.getSocketFactory())
//                .build();
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
//        //单个路由最大连接数
//        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
//        connectionManager.setMaxTotal(DEFAULT_MAX_PER_ROUTE);
//        // 最大空间时间
//        connectionManager.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);
//        RequestConfig requestConfig = RequestConfig.custom()
//                //服务器返回数据(response)的时间，超过抛出read timeout
//                .setSocketTimeout(SOCKET_TIMEOUT)
//                //连接上服务器(握手成功)的时间，超出抛出connect timeout
//                .setConnectTimeout(CONNECT_TIMEOUT)
//                //从连接池中获取连接的超时时间
//                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
//                .build();
//
//
//        return HttpClientBuilder.create()
//                .setDefaultRequestConfig(requestConfig)
//                .setConnectionManager(connectionManager)
//                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
//                .build();
//    }
//
//
//
//    public static void main(String[] args) throws IOException {
//
//
//        // 初始化输入、图和流
//        input Input = new input();
//        graph[] Graph = new graph[(int) Math.ceil(Input.simulationTime/Input.snapshotInterval)];
//        flow[] flow = new flow[input.numOfFlow];
//
//        // 初始化流到图的映射功能
//        flow2graph[] f2g = new flow2graph[(int) Math.ceil(Input.simulationTime/Input.snapshotInterval)];
//        for(int i = 0;i<(int) Math.ceil(Input.simulationTime/Input.snapshotInterval);i++){
//            Graph[i] = new graph(Input,i);
//            f2g[i] = new flow2graph(flow, Graph[i]);
//        }
//
//        // 初始化流的目标时延
//        for (int i = 0; i < input.numOfFlow; i++) {
//            flow[i] = initFlow.initFlow(Graph[0]);
//            System.out.println(initFlow.getMaxMinDelay(flow[i], Graph, Input));
//            flow[i].setTargetDelay(initFlow.getMaxMinDelay(flow[i],Graph,Input));
//        }
////        flow[0].setStartTime(40);
////        flow[0].setDeadTime(610);
////        flow[0].setSourceSat(Graph[0].getSatNodes()[26]);
////        flow[0].setTargetSat(Graph[0].getSatNodes()[192]);
////        flow[0].setSourceGroundStation(groundStationNode.builder().id(0).
////                longitude(flow[0].getSourceSat().getLongitude()).latitude(flow[0].getSourceSat().getLatitude()).build());
////        flow[0].setTargetGroundStation(groundStationNode.builder().id(0).
////                longitude(flow[0].getTargetSat().getLongitude()).latitude(flow[0].getTargetSat().getLatitude()).build());
////        flow[0].setTargetDelay(initFlow.getMaxMinDelay(flow[0],Graph,Input));
////
////        flow[1].setStartTime(670);
////        flow[1].setDeadTime(1000);
////        flow[1].setSourceSat(Graph[0].getSatNodes()[168]);
////        flow[1].setTargetSat(Graph[0].getSatNodes()[202]);
////        flow[1].setSourceGroundStation(groundStationNode.builder().id(0).
////                longitude(flow[1].getSourceSat().getLongitude()).latitude(flow[1].getSourceSat().getLatitude()).build());
////        flow[1].setTargetGroundStation(groundStationNode.builder().id(0).
////                longitude(flow[1].getTargetSat().getLongitude()).latitude(flow[1].getTargetSat().getLatitude()).build());
////        flow[1].setTargetDelay(initFlow.getMaxMinDelay(flow[1],Graph,Input));
//
//        // 遍历时间，每个时间给对应的流映射
//        boolean snapshotHandover = false;
//        Integer snapshotIndex = 0;
//        for (int t = 0; t < input.simulationTime; t++) {
//            System.out.println("current time: " + t);
//            snapshotHandover = t % input.snapshotInterval == 0 ? true : false;
//            snapshotIndex = (int) (t / input.snapshotInterval);
//            // 若切换快照了，则重新调整流的源终；给映射过的流重映射，给该映射的流正常映射
//            // 目前还差一步，就是根据目标时延，选择抖动最小的路径
//            if (snapshotHandover == true) {
//                f2g[snapshotIndex] = new flow2graph(f2g[snapshotIndex].getFlows(), Graph[snapshotIndex]);
//                for (int i = 0; i < input.numOfFlow; i++) {
//                    if (t <= flow[i].getDeadTime())
//                        flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex]);
//                }
//                for (int i = 0; i < input.numOfFlow; i++) {
//                    if ((flow[i].getPathStatus().equals("map Success")
//                            && flow[i].getStartTime() <= t && flow[i].getDeadTime() >= t)
//                            || (flow[i].getPathStatus().equals("to be map")
//                            && flow[i].getStartTime() == t)) {
//
//                        GraphPath<String, DefaultWeightedEdge> tempPath = Graph[snapshotIndex].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//                        graph satisfyedGraph = Graph[snapshotIndex].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * tempPath.getLength() * tempPath.getLength() / 2);
//                        tempPath = satisfyedGraph.getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//
//                        System.out.println("edgelist" + tempPath.getEdgeList());
//                        System.out.println("vertexlist" + tempPath.getVertexList());
//                        flow[i].setPath(satisfyedGraph.getPathDelay(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId())<flow[i].getMaxDelay()?tempPath:null);
//                        System.out.println("路径是" + tempPath);
//                        System.out.println("flow路径是" + flow[i].getPath());
//                        System.out.println("源地面站" + flow[i].getSourceGroundStation().getLatitude() + ";" + flow[i].getSourceGroundStation().getLongitude());
//                        System.out.println("终地面站" + flow[i].getTargetGroundStation().getLatitude() + ";" + flow[i].getTargetGroundStation().getLongitude());
//                        System.out.println(snapshotIndex + "快照下延迟是" + i + "流" + satisfyedGraph.getPathDelay(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId()));
//                        System.out.println("另一种方法延迟是" + tempPath.getWeight());
//                        flow[i].setNewPath(flow[i].getPath().getEdgeList());
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(105, 91));
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(120, 105));
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(135, 120));
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(150, 135));
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(165, 150));
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(180, 165));
////                        System.out.println("取单路延迟" + satisfyedGraph.getPathDelay(166, 180));
////                        System.out.println("取单路延迟总计" + satisfyedGraph.getPathDelay(105, 91)+satisfyedGraph.getPathDelay(120, 105)+satisfyedGraph.getPathDelay(135, 120)+satisfyedGraph.getPathDelay(150, 135)+satisfyedGraph.getPathDelay(165, 150)+satisfyedGraph.getPathDelay(180, 165)+satisfyedGraph.getPathDelay(166, 180));
//                        System.out.println("List" + flow[i].getNewPath());
//                        f2g[snapshotIndex].addFlowInGraph(flow[i], snapshotIndex);
//                    }
//                    else if (flow[i].getPathStatus().equals("map Success") && flow[i].getDeadTime() == t + 1) {
//                        f2g[snapshotIndex].remFlowInGraph(flow[i]);
//                    }
//                }
//            }
//            // 快照没有切换，则正常按照时间进行映射（到时映射
//            else {
//                for (int i = 0; i < input.numOfFlow; i++) {
//                    if (flow[i].getPathStatus().equals("to be map") && flow[i].getStartTime() == t) {
//                        System.out.println("source id:"+flow[i].getSourceSat().getId());
//                        System.out.println("target id:"+flow[i].getTargetSat().getId());
//                        GraphPath<String, DefaultWeightedEdge> tempPath = Graph[snapshotIndex].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//
//                        graph satisfyedGraph = Graph[snapshotIndex].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * tempPath.getLength() * tempPath.getLength() / 2);
//                        tempPath = satisfyedGraph.getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//                        flow[i].setPath(satisfyedGraph.getPathDelay(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId())<flow[i].getMaxDelay()?tempPath:null);
//
//                        f2g[snapshotIndex].addFlowInGraph(flow[i], snapshotIndex);
//                    }
//                    else if (flow[i].getPathStatus().equals("map Success") && flow[i].getDeadTime() == t + 1) {
//                        f2g[snapshotIndex].remFlowInGraph(flow[i]);
//                    }
//                }
//            }
//        }
//        f2g[snapshotIndex].output();
//    }
//
//    public static ConfigurableApplicationContext getContext() {
//        return context;
//    }
//}