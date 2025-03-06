//package com.pml;
//
//import com.pml.route.business.sr.impl.UdpClientServiceImpl;
//import com.pml.route.business.sr.model.*;
//import com.pml.route.business.sr.model.Curve.Curve;
//import io.swagger.models.auth.In;
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
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//
//@SpringBootApplication
//@EnableEurekaClient
//// @ImportResource(locations = {"classpath:spring/spring-bean.xml"})
//@MapperScan({"com.pml.route.database.mapper", "com.pml.sdsn.topology.database.mapper"})
//public class JitterApplication {
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
////            System.out.println("流"+i+"的初始参数为");
////            System.out.println(flow[i].getStartTime());
////            System.out.println(flow[i].getDeadTime());
////            System.out.println(flow[i].getSourceSat().getId());
////            System.out.println(flow[i].getSourceSat().getLatitude());
////            System.out.println(flow[i].getSourceSat().getLongitude());
////            System.out.println(flow[i].getTargetSat().getId());
////            System.out.println(flow[i].getTargetSat().getLatitude());
////            System.out.println(flow[i].getTargetSat().getLongitude());
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
////            System.out.println("current time: " + t);
//            snapshotHandover = t % input.snapshotInterval.intValue() == 0 ? true : false;
//            snapshotIndex = (int) (t / input.snapshotInterval.intValue());
//            // 若切换快照了，则重新调整流的源终；给映射过的流重映射，给该映射的流正常映射
//            // 目前还差一步，就是根据目标时延，选择抖动最小的路径
//            if (snapshotHandover == true) {
//                f2g[snapshotIndex] = new flow2graph(f2g[snapshotIndex].getFlows(), Graph[snapshotIndex]);
//                for (int i = 0; i < input.numOfFlow; i++) {
//                    if (t <= flow[i].getDeadTime())
//                        flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex]);
//                }
//                for (int i = 0; i < input.numOfFlow; i++) {
//                    if ((flow[i].getPathStatus().equals("to be map") && flow[i].getStartTime() == t)) {
//                        int numOfSnapshot = (int) (flow[i].getDeadTime()/input.snapshotInterval.intValue() - flow[i].getStartTime()/input.snapshotInterval.intValue() +1);
//                        double[] allDelay = new double[numOfSnapshot];
//                        GraphPath[] allTempPath = new GraphPath[numOfSnapshot];
//                        int flowSnapshotIndex = (int) (t/input.snapshotInterval.intValue() - flow[i].getStartTime()/input.snapshotInterval.intValue());
//
//
//                        // 获得流i在所有快照下的初始路径时延
//                        graph[] satisfyedGraph = new graph[numOfSnapshot];
//                        for (int j = 0; j < numOfSnapshot; j++) {
//                            flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                            allTempPath[j] = Graph[snapshotIndex + j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//
//                            satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
//                            allTempPath[j] = satisfyedGraph[j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//                            allDelay[j] = allTempPath[j].getWeight();
//                        }
//
//                        // 比较出最大时延
//                        double maxDelay = 0;
//                        Integer maxDelaySnapshot = 0;
//                        for (int j = 0; j < numOfSnapshot; j++) {
//                            if (maxDelay < allDelay[j]){
//                                maxDelay = allDelay[j];
//                                maxDelaySnapshot = j;
//                            }
//                        }
//
//                        //把路存为ArrayList类型
//                        List<List<Integer>> pathSet = new ArrayList<>();
//                        List<Double> delaySet = new ArrayList<>();
//                        for (int j = 0; j < numOfSnapshot; j++){
//                            List<DefaultWeightedEdge> edges = allTempPath[j].getEdgeList();
//                            List<Integer> pathInt = new ArrayList<>();
//                            for (int k = 0; k < edges.size(); k++) {
//                                String tempedge = edges.get(k).toString();
//                                String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
//                                Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                pathInt.add(sourceSatIndex);
//                                if (k == edges.size()-1){
//                                    sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                    sourceSatIndex = Integer.valueOf(sourceString);
//                                    pathInt.add(sourceSatIndex);
//                                }
//                            }
//                            pathSet.add(j, pathInt);
//                            delaySet.add(j,allDelay[j]);
//                        }
//
//                        // 更改其他快照下的路径
//                        for (int j = 0; j < numOfSnapshot; j++){
////                            satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
//                            if (j != maxDelaySnapshot){
//                                List<Integer> tempPath = new ArrayList<>(pathSet.get(j));
//
//                                // 找出链路利用率最大的链路
//                                Double maxUsage = 0.0;
//                                Integer maxUsageIndex = 0;
//                                for (int k = 0; k < pathSet.get(j).size()-3; k = k+2){
//                                    Integer thisNode =tempPath.get(k);
//                                    Integer classNode = tempPath.get(k+1);
//                                    Integer nextNode = tempPath.get(k+2);
//                                    String thisClass = classNode - thisNode == f2g[snapshotIndex+j].getGraph().getSatNodes().length ? "A" : "B";
//
//                                    Integer thislinkIndex = f2g[snapshotIndex+j].getGraph().getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
//                                    link[] links = f2g[snapshotIndex+j].getGraph().getLinks();
//                                    link thisLink = links[thislinkIndex];
//                                    if (maxUsage < ((thisLink.getRate() - thisLink.getUsedRate())/thisLink.getRate())){
//                                        maxUsage = (thisLink.getRate() - thisLink.getUsedRate())/thisLink.getRate();
//                                        maxUsageIndex = k;
//                                    }
//                                }
//                                maxUsageIndex = ((pathSet.get(j).size()-1)/4)*2;
//
//                                // 找出邻居卫星
//                                Set<DefaultWeightedEdge> nearNodes = new HashSet<>();
//                                List<Object> edges = new ArrayList<>();
//                                nearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf("sat" + String.valueOf(pathSet.get(j).get(maxUsageIndex+1)));
//                                Iterator it = nearNodes.iterator();
//                                while (it.hasNext()){
//                                    edges.add(it.next());
//                                }
//
//                                List<Integer> otherNodes = new ArrayList<>();
//                                for (int k=0; k < nearNodes.size(); k++){
//                                    String tempedge = edges.get(k).toString();
//                                    String sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                    Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                    Integer nextNode = pathSet.get(j).get(maxUsageIndex+2);
//                                    Integer lastNode = pathSet.get(j).get(maxUsageIndex-2);
//                                    String thisClass = pathSet.get(j).get(maxUsageIndex+1) - pathSet.get(j).get(maxUsageIndex) == f2g[snapshotIndex+j].getGraph().getSatNodes().length ? "A" : "B";
//                                    Integer thislinkIndex = f2g[snapshotIndex+j].getGraph().getLinkIndexBySourceDes(pathSet.get(j).get(maxUsageIndex), sourceSatIndex, thisClass);
//                                    if (lastNode>=0){
//                                        if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))){
////                                            if (thislinkIndex == -1){
//////                                            if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
////                                                continue;
////                                            }
//                                            otherNodes.add(sourceSatIndex);
//                                        }
//                                    }
//                                    else {
//                                        if (!sourceSatIndex.equals(nextNode)){
//                                            if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))) {
////                                                if (thislinkIndex == -1){
//////                                                if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
////                                                    continue;
////                                                }
//                                            }
//                                            otherNodes.add(sourceSatIndex);
//                                        }
//                                    }
//                                }
////                                System.out.println(pathSet.get(j).get(maxUsageIndex)+"的邻居卫星"+otherNodes);
//
//                                // 邻居卫星找新路
//                                flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                                if (otherNodes.size()==0){
//                                    System.out.println("没有邻居卫星！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
//                                    System.out.println("第"+ i +"条流" + "第" + j + "个快照");
//                                    continue;
//                                }
//                                Integer newNode = otherNodes.get(0);
//                                Double newDelay = delaySet.get(j);
//                                for (int k=0; k < 6; k++){
//                                    Double tempDelay = 0.0;
//
//                                    // 记录原来路径到最大利用节点的延迟和节点
//                                    List<Integer> tempAgrPath = new ArrayList<>();
//                                    for (int l=0; l < maxUsageIndex; l++){
//                                        tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(l), pathSet.get(j).get(l+1));
////                                        System.out.println(tempDelay);
//                                        tempAgrPath.add(tempPath.get(l));
//                                    }
//                                    if (maxUsageIndex+1>=tempPath.size()){
//                                        break;
//                                    }
//                                    tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex-1), pathSet.get(j).get(maxUsageIndex));
//
////                                    tempAgrPath.remove(maxUsageIndex);
//                                    GraphPath midPath = satisfyedGraph[j].getPath(pathSet.get(j).get(maxUsageIndex), newNode);
//                                    List<DefaultWeightedEdge> tempMidAddEdge = midPath.getEdgeList();
//                                    for (int l = 0; l < tempMidAddEdge.size(); l++) {
//                                        String tempedge = tempMidAddEdge.get(l).toString();
//                                        String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
//                                        Integer sourceSatIndex = Integer.valueOf(sourceString);
////                                        tempAddPathInt.add(sourceSatIndex);
//                                        tempAgrPath.add(sourceSatIndex);
//                                    }
//                                    tempDelay = tempDelay + midPath.getWeight();
//
////                                    tempAgrPath.add(tempPath.get(maxUsageIndex+1));
//                                    // 记录改变的路径的延迟
//                                    tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex), newNode);
////                                    System.out.println(tempDelay);
//                                    GraphPath tempAddPath = satisfyedGraph[j].getPath(newNode, flow[i].getTargetSat().getId());
//                                    tempDelay = tempDelay + tempAddPath.getWeight();
////                                    System.out.println("end" +tempDelay);
//                                    // 记录改变的路径的节点
//                                    List<DefaultWeightedEdge> tempAddEdge = tempAddPath.getEdgeList();
////                                    List<Integer> tempAddPathInt = new ArrayList<>();
//                                    for (int l = 0; l < tempAddEdge.size(); l++) {
//                                        String tempedge = tempAddEdge.get(l).toString();
//                                        String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
//                                        Integer sourceSatIndex = Integer.valueOf(sourceString);
////                                        tempAddPathInt.add(sourceSatIndex);
//                                        tempAgrPath.add(sourceSatIndex);
//                                        if (l == tempAddEdge.size()-1){
//                                            sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                            sourceSatIndex = Integer.valueOf(sourceString);
////                                            tempAddPathInt.add(sourceSatIndex);
//                                            tempAgrPath.add(sourceSatIndex);
//                                        }
//                                    }
//                                    //更改为新的路径，如果超过最大时延，break
//                                    if ((tempDelay <= maxDelay) && (tempDelay > newDelay)){
//                                        System.out.println("变！");
//                                        pathSet.remove(j);
//                                        pathSet.add(j,tempAgrPath);
//                                        newDelay = tempDelay;
//                                    }
//                                    if (tempDelay > maxDelay){
//                                        break;
//                                    }
//                                    //寻找新节点的相邻节点
//                                    maxUsageIndex = maxUsageIndex + 2;
//                                    Set<DefaultWeightedEdge> nextNearNodes = new HashSet<>();
//                                    List<Object> nextEdges = new ArrayList<>();
//                                    nextNearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf("sat" + String.valueOf(pathSet.get(j).get(maxUsageIndex+1)));
//                                    Iterator nextIt = nextNearNodes.iterator();
//                                    while (nextIt.hasNext()){
//                                        nextEdges.add(nextIt.next());
//                                    }
//
//                                    otherNodes.clear();
//                                    for (int l=0; l < nextNearNodes.size(); l++){
//                                        String tempedge = nextEdges.get(l).toString();
//                                        String sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                        Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        Integer nextNode = pathSet.get(j).get(maxUsageIndex+2);
//                                        Integer lastNode = pathSet.get(j).get(maxUsageIndex-2);
//                                        String thisClass = pathSet.get(j).get(maxUsageIndex+1) - pathSet.get(j).get(maxUsageIndex) == f2g[snapshotIndex+j].getGraph().getSatNodes().length ? "A" : "B";
//                                        Integer thislinkIndex = f2g[snapshotIndex+j].getGraph().getLinkIndexBySourceDes(pathSet.get(j).get(maxUsageIndex), sourceSatIndex, thisClass);
//                                        if (lastNode>=0){
//                                            if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))){
////                                                if (thislinkIndex == -1){
//////                                                if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
//////                                                    System.out.println(satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex));
////                                                    continue;
////                                                }
//                                                otherNodes.add(sourceSatIndex);
//                                            }
//                                        }
//                                        else {
//                                            if (!sourceSatIndex.equals(nextNode)){
////                                                if (thislinkIndex == -1){
//////                                                if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
////                                                    continue;
////                                                }
//                                                otherNodes.add(sourceSatIndex);
//                                            }
//                                        }
//                                        System.out.println("第"+k+otherNodes);
//                                    }
//
//
//
//                                }
//                                delaySet.remove(j);
//                                delaySet.add(j, newDelay);
//
//                            }
//
//                        }
//                        for (int j=0;j<pathSet.size();j++){
//                            pathSet.get(j).remove(pathSet.get(j).size()-1);
//                        }
//
//                        flow[i].setJitterPathSet(pathSet);
//                        flow[i].setJitterDelaySet(delaySet);
//                        flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex]);
//                        f2g[snapshotIndex].addFlowInGraph(flow[i], flowSnapshotIndex);
//
//                        UdpClientServiceImpl udpClientService = new UdpClientServiceImpl();
//                        udpClientService.sendPath(i,flow[i].getJitterPathSet().get(flowSnapshotIndex));
//
//                    }
//                    else if (flow[i].getPathStatus().equals("map Success")
//                            && flow[i].getStartTime() <= t && flow[i].getDeadTime() >= t){
//                        int flowSnapshotIndex = (int) (t/input.snapshotInterval.intValue()) - (int) (flow[i].getStartTime()/input.snapshotInterval.intValue());
//                        flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex]);
////                        if (snapshotIndex == 5 || snapshotIndex == 13){
////                            continue;
////                        }
//                        f2g[snapshotIndex].addFlowInGraph(flow[i], flowSnapshotIndex);
//
//                        UdpClientServiceImpl udpClientService = new UdpClientServiceImpl();
//                        udpClientService.sendPath(i,flow[i].getJitterPathSet().get(flowSnapshotIndex));
//
//                        System.out.println("当前路径" + flow[i].getJitterPathSet().get(flowSnapshotIndex));
//                    }
//                    else if (flow[i].getPathStatus().equals("map Success") && flow[i].getDeadTime() == t + 1) {
//                        f2g[snapshotIndex].remFlowInGraph(flow[i]);
//                    }
//
//                }
//            }
//
//            // 快照没有切换，则正常按照时间进行映射（到时映射
//            else {
//                for (int i = 0; i < input.numOfFlow; i++) {
//                    if ((flow[i].getPathStatus().equals("to be map") && flow[i].getStartTime() == t)) {
//                        int numOfSnapshot = (int) (flow[i].getDeadTime()/input.snapshotInterval.intValue() - flow[i].getStartTime()/input.snapshotInterval.intValue() +1);
//                        double[] allDelay = new double[numOfSnapshot];
//                        GraphPath[] allTempPath = new GraphPath[numOfSnapshot];
//                        int flowSnapshotIndex = (int) (t/input.snapshotInterval.intValue() - flow[i].getStartTime()/input.snapshotInterval.intValue());
//
//
//                        // 获得流i在所有快照下的初始路径时延
//                        graph[] satisfyedGraph = new graph[numOfSnapshot];
//                        for (int j = 0; j < numOfSnapshot; j++) {
//                            flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                            allTempPath[j] = Graph[snapshotIndex + j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//
//                            satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
//                            allTempPath[j] = satisfyedGraph[j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
//                            allDelay[j] = allTempPath[j].getWeight();
//                        }
//
//                        // 比较出最大时延
//                        double maxDelay = 0;
//                        Integer maxDelaySnapshot = 0;
//                        for (int j = 0; j < numOfSnapshot; j++) {
//                            if (maxDelay < allDelay[j]){
//                                maxDelay = allDelay[j];
//                                maxDelaySnapshot = j;
//                            }
//                        }
//
//                        //把路存为ArrayList类型
//                        List<List<Integer>> pathSet = new ArrayList<>();
//                        List<Double> delaySet = new ArrayList<>();
//                        for (int j = 0; j < numOfSnapshot; j++){
//                            List<DefaultWeightedEdge> edges = allTempPath[j].getEdgeList();
//                            List<Integer> pathInt = new ArrayList<>();
//                            for (int k = 0; k < edges.size(); k++) {
//                                String tempedge = edges.get(k).toString();
//                                String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
//                                Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                pathInt.add(sourceSatIndex);
//                                if (k == edges.size()-1){
//                                    sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                    sourceSatIndex = Integer.valueOf(sourceString);
//                                    pathInt.add(sourceSatIndex);
//                                }
//                            }
//                            pathSet.add(j, pathInt);
//                            delaySet.add(j,allDelay[j]);
//                        }
//
//                        // 更改其他快照下的路径
//                        for (int j = 0; j < numOfSnapshot; j++){
////                            satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
//                            if (j != maxDelaySnapshot){
//                                List<Integer> tempPath = new ArrayList<>(pathSet.get(j));
//
//                                // 找出链路利用率最大的链路
//                                Double maxUsage = 0.0;
//                                Integer maxUsageIndex = 0;
//                                for (int k = 0; k < pathSet.get(j).size()-3; k = k+2){
//                                    Integer thisNode =tempPath.get(k);
//                                    Integer classNode = tempPath.get(k+1);
//                                    Integer nextNode = tempPath.get(k+2);
//                                    String thisClass = classNode - thisNode == f2g[snapshotIndex+j].getGraph().getSatNodes().length ? "A" : "B";
//
//                                    Integer thislinkIndex = f2g[snapshotIndex+j].getGraph().getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
//                                    link[] links = f2g[snapshotIndex+j].getGraph().getLinks();
//                                    link thisLink = links[thislinkIndex];
//                                    if (maxUsage < ((thisLink.getRate() - thisLink.getUsedRate())/thisLink.getRate())){
//                                        maxUsage = (thisLink.getRate() - thisLink.getUsedRate())/thisLink.getRate();
//                                        maxUsageIndex = k;
//                                    }
//                                }
//                                maxUsageIndex = pathSet.get(j).size()/2;
//                                if (maxUsageIndex%2 == 0){
//                                    maxUsageIndex = maxUsageIndex - 1;
//                                }
//                                maxUsageIndex = maxUsageIndex - 1;
//
//
//                                // 找出邻居卫星
//                                Set<DefaultWeightedEdge> nearNodes = new HashSet<>();
//                                List<Object> edges = new ArrayList<>();
//                                nearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf("sat" + String.valueOf(pathSet.get(j).get(maxUsageIndex+1)));
//                                Iterator it = nearNodes.iterator();
//                                while (it.hasNext()){
//                                    edges.add(it.next());
//                                }
//
//                                List<Integer> otherNodes = new ArrayList<>();
//                                for (int k=0; k < nearNodes.size(); k++){
//                                    String tempedge = edges.get(k).toString();
//                                    String sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                    Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                    Integer nextNode = pathSet.get(j).get(maxUsageIndex+2);
//                                    Integer lastNode = pathSet.get(j).get(maxUsageIndex-2);
//                                    String thisClass = pathSet.get(j).get(maxUsageIndex+1) - pathSet.get(j).get(maxUsageIndex) == f2g[snapshotIndex+j].getGraph().getSatNodes().length ? "A" : "B";
//                                    Integer thislinkIndex = f2g[snapshotIndex+j].getGraph().getLinkIndexBySourceDes(pathSet.get(j).get(maxUsageIndex), sourceSatIndex, thisClass);
//                                    if (lastNode>=0){
//                                        if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))){
////                                            if (thislinkIndex == -1){
//////                                            if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
////                                                continue;
////                                            }
//                                            otherNodes.add(sourceSatIndex);
//                                        }
//                                    }
//                                    else {
//                                        if (!sourceSatIndex.equals(nextNode)){
//                                            if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))) {
////                                                if (thislinkIndex == -1){
//////                                                if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
////                                                    continue;
////                                                }
//                                            }
//                                            otherNodes.add(sourceSatIndex);
//                                        }
//                                    }
//                                }
////                                System.out.println(pathSet.get(j).get(maxUsageIndex)+"的邻居卫星"+otherNodes);
//
//                                // 邻居卫星找新路
//                                flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                                if (otherNodes.size()==0){
//                                    System.out.println("没有邻居卫星！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
//                                    System.out.println("第"+ i +"条流" + "第" + j + "个快照");
//                                    continue;
//                                }
//                                Integer newNode = otherNodes.get(0);
//                                Double newDelay = delaySet.get(j);
//                                for (int k=0; k < 12; k++){
////                                    if (true){
////                                        break;
////                                    }
//                                    Double tempDelay = 0.0;
//                                    // 记录原来路径到最大利用节点的延迟和节点
//                                    List<Integer> tempAgrPath = new ArrayList<>();
//                                    for (int l=0; l < maxUsageIndex; l++){
//                                        tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(l), pathSet.get(j).get(l+1));
////                                        System.out.println(tempDelay);
//                                        tempAgrPath.add(pathSet.get(j).get(l));
//                                    }
//                                    if (maxUsageIndex+1>=pathSet.get(j).size()){
//                                        System.out.println("tempPath超限");
//                                        break;
//                                    }
//                                    // 感觉这里多算了一次时延
////                                    tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex-1), pathSet.get(j).get(maxUsageIndex));
//
////                                    tempAgrPath.remove(maxUsageIndex);
//                                    GraphPath midPath = satisfyedGraph[j].getPath(pathSet.get(j).get(maxUsageIndex), newNode);
//                                    List<DefaultWeightedEdge> tempMidAddEdge = midPath.getEdgeList();
//                                    for (int l = 0; l < tempMidAddEdge.size(); l++) {
//                                        String tempedge = tempMidAddEdge.get(l).toString();
//                                        String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
//                                        Integer sourceSatIndex = Integer.valueOf(sourceString);
////                                        tempAddPathInt.add(sourceSatIndex);
//                                        tempAgrPath.add(sourceSatIndex);
//                                    }
//                                    // 多算了一次
////                                    tempDelay = tempDelay + midPath.getWeight();
//
////                                    tempAgrPath.add(tempPath.get(maxUsageIndex+1));
//                                    // 记录改变的路径的延迟
//                                    tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex), newNode);
////                                    记录每一跳后时延改变多少
////                                    System.out.println(tempDelay);
//                                    GraphPath tempAddPath = satisfyedGraph[j].getPath(newNode, flow[i].getTargetSat().getId());
//                                    tempDelay = tempDelay + tempAddPath.getWeight();
////                                    System.out.println("end" +tempDelay);
//                                    // 记录改变的路径的节点
//                                    List<DefaultWeightedEdge> tempAddEdge = tempAddPath.getEdgeList();
////                                    List<Integer> tempAddPathInt = new ArrayList<>();
//                                    for (int l = 0; l < tempAddEdge.size(); l++) {
//                                        String tempedge = tempAddEdge.get(l).toString();
//                                        String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
//                                        Integer sourceSatIndex = Integer.valueOf(sourceString);
////                                        tempAddPathInt.add(sourceSatIndex);
//                                        tempAgrPath.add(sourceSatIndex);
//                                        if (l == tempAddEdge.size()-1){
//                                            sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                            sourceSatIndex = Integer.valueOf(sourceString);
////                                            tempAddPathInt.add(sourceSatIndex);
//                                            tempAgrPath.add(sourceSatIndex);
//                                        }
//                                    }
//                                    System.out.println("第"+i+"条流"+"第"+j+"个快照下路径，第"+k+"次的路径是"+pathSet.get(j)+"时延是"+newDelay);
//                                    System.out.println("最大时延是"+maxDelay+"改路后时延是"+tempDelay);
//                                    //更改为新的路径，如果超过最大时延，break
//                                    if ((tempDelay <= maxDelay) && (tempDelay >= newDelay)){
//                                        System.out.println("变！");
//                                        pathSet.remove(j);
//                                        pathSet.add(j,tempAgrPath);
//                                        newDelay = tempDelay;
//                                        System.out.println("第"+i+"条流"+"第"+j+"个快照下路径，第"+k+"次更改后的新路径是"+pathSet.get(j)+"新的时延是"+newDelay);
//                                    }
//                                    if (tempDelay > maxDelay){
//                                        break;
//                                    }
//                                    //寻找新节点的相邻节点
//                                    maxUsageIndex = maxUsageIndex + 2;
//                                    if (maxUsageIndex+1>=pathSet.get(j).size()){
//                                        System.out.println("pathSet超限");
//                                        break;
//                                    }
//                                    Set<DefaultWeightedEdge> nextNearNodes = new HashSet<>();
//                                    List<Object> nextEdges = new ArrayList<>();
//                                    nextNearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf("sat" + String.valueOf(pathSet.get(j).get(maxUsageIndex+1)));
//                                    Iterator nextIt = nextNearNodes.iterator();
//                                    while (nextIt.hasNext()){
//                                        nextEdges.add(nextIt.next());
//                                    }
//
//                                    otherNodes.clear();
//                                    for (int l=0; l < nextNearNodes.size(); l++){
//                                        String tempedge = nextEdges.get(l).toString();
//                                        String sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
//                                        Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        Integer nextNode = pathSet.get(j).get(maxUsageIndex+2);
//                                        Integer lastNode = pathSet.get(j).get(maxUsageIndex-2);
//                                        String thisClass = pathSet.get(j).get(maxUsageIndex+1) - pathSet.get(j).get(maxUsageIndex) == f2g[snapshotIndex+j].getGraph().getSatNodes().length ? "A" : "B";
//                                        Integer thislinkIndex = f2g[snapshotIndex+j].getGraph().getLinkIndexBySourceDes(pathSet.get(j).get(maxUsageIndex), sourceSatIndex, thisClass);
//                                        if (lastNode>=0){
//                                            if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))){
////                                                if (thislinkIndex == -1){
//////                                                if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
//////                                                    System.out.println(satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex));
////                                                    continue;
////                                                }
//                                                otherNodes.add(sourceSatIndex);
//                                            }
//                                        }
//                                        else {
//                                            if (!sourceSatIndex.equals(nextNode)){
////                                                if (thislinkIndex == -1){
//////                                                if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
////                                                    continue;
////                                                }
//                                                otherNodes.add(sourceSatIndex);
//                                            }
//                                        }
////                                        System.out.println("第"+k+otherNodes);
//                                    }
//
//
//
//                                }
//                                delaySet.remove(j);
//                                delaySet.add(j, newDelay);
//
//                            }
//
//                        }
//                        for (int j=0;j<pathSet.size();j++){
//                            pathSet.get(j).remove(pathSet.get(j).size()-1);
//                        }
//
//                        flow[i].setJitterPathSet(pathSet);
//                        flow[i].setJitterDelaySet(delaySet);
//
//
//
//                        flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex]);
//                        f2g[snapshotIndex].addFlowInGraph(flow[i], flowSnapshotIndex);
//
//                        UdpClientServiceImpl udpClientService = new UdpClientServiceImpl();
//                        udpClientService.sendPath(i,flow[i].getJitterPathSet().get(flowSnapshotIndex));
//
//                    }
//                    else if (flow[i].getPathStatus().equals("map Success") && flow[i].getDeadTime() == t + 1) {
//                        f2g[snapshotIndex].remFlowInGraph(flow[i]);
//                    }
//                }
//            }
//        }
//        f2g[snapshotIndex].output();
//        for (int i = 0;i < input.numOfFlow;i++){
//            System.out.println("第" + i + "条流在各个快照下的时延" + flow[i].getJitterDelaySet());
//            System.out.println("第" + i + "条流的最大时延" + Collections.max(flow[i].getJitterDelaySet()));
//            System.out.println("第" + i + "条流的最小时延" + Collections.min(flow[i].getJitterDelaySet()));
//            System.out.println("第" + i + "条流的抖动" + (Collections.max(flow[i].getJitterDelaySet()) - Collections.min(flow[i].getJitterDelaySet())));
//        }
//    }
//
//    public static ConfigurableApplicationContext getContext() {
//        return context;
//    }
//}