package com.pml;

import com.pml.route.business.sr.impl.UdpClientServiceImpl;
import com.pml.route.business.sr.model.*;
import com.pml.route.business.sr.model.Curve.Curve;
import com.pml.route.business.sr.model.Curve.CurveInterval;
import com.pml.route.business.JitterControl.*;
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
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
// @ImportResource(locations = {"classpath:spring/spring-bean.xml"})
@MapperScan({"com.pml.route.database.mapper", "com.pml.sdsn.topology.database.mapper"})
public class JitterApplication3 {
    private static final Logger mappingLog = LoggerFactory.getLogger(JitterApplication3.class);

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

    private static CategoryDataset createDataset(List<Double> dataList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < dataList.size(); i++) {
            dataset.addValue(dataList.get(i), "Series 1", "Category " + (i + 1));
        }
        return dataset;
    }

    private static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "时敏业务确定性流调度抖动大小",
                "业务序号",
                "抖动大小/ms",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
                Font font = new Font("宋体", Font.PLAIN, 12);

                chart.getTitle().setFont(font);
                CategoryPlot plot = chart.getCategoryPlot();
                plot.getDomainAxis().setLabelFont(font);
                plot.getRangeAxis().setLabelFont(font);
                plot.getDomainAxis().setTickLabelFont(font);
                plot.getRangeAxis().setTickLabelFont(font);

                CategoryAxis domainAxis = plot.getDomainAxis();
                // 设置不显示横轴刻度
                domainAxis.setTickMarksVisible(false);
        return chart;
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

        // 设置故障节点（需要改生成流时的起止节点）


        // 初始化流的目标时延
        for (int i = 0; i < input.numOfFlow; i++) {
            flow[i] = initFlow.initFlow(Graph[0]);
            flow[i].setId(i);
//            mappingLog.info("init Flow" + i + ":" + initFlow.getMaxMinDelay(flow[i], Graph, Input));
            flow[i].setTargetDelay(initFlow.getMaxMinDelay(flow[i], Graph, Input));
            flow[i].setTypeOfService("111");
        }

        // 遍历时间，每个时间给对应的流映射
        boolean snapshotHandover = false;
        Integer snapshotIndex = 0;
        for (int t = 0; t < input.simulationTime; t++) {
//            System.out.println("current time: " + t);
            snapshotHandover = t % input.snapshotInterval.intValue() == 0 ? true : false;
            snapshotIndex = (int) (t / input.snapshotInterval.intValue());//0,1,2...
            // 若切换快照了，则重新调整流的源终；给映射过的流重映射，给该映射的流正常映射
            // 目前还差一步，就是根据目标时延，选择抖动最小的路径
            if (snapshotHandover == true) {
                f2g[snapshotIndex] = new flow2graph(f2g[snapshotIndex].getFlows(), Graph[snapshotIndex]);
                // 快照切换，则更新所有流的源终卫星
                for (int i = 0; i < input.numOfFlow; i++) {
                    if (t <= flow[i].getDeadTime())
                        flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex]);
                }
                for (int i = 0; i < input.numOfFlow; i++) {
                    if ((flow[i].getPathStatus().equals("to be map") && flow[i].getStartTime() == t)) {
                        int numOfSnapshot = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);
                        double[] allDelay = new double[numOfSnapshot];
                        GraphPath[] allTempPath = new GraphPath[numOfSnapshot];
                        int flowSnapshotIndex = (int) (t / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue());
                        int totalFlowSnapshotNum = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);

                        // 计算出流的目标时延
                        // 获得流i在所有快照下的初始路径时延
                        graph[] satisfyedGraph = new graph[numOfSnapshot];
                        try {
                            for (int j = 0; j < numOfSnapshot; j++) {
                                flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
                                allTempPath[j] = Graph[snapshotIndex + j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());

                                satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * input.TijB * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
                                allTempPath[j] = satisfyedGraph[j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
                                allDelay[j] = allTempPath[j].getWeight();
                            }
                        } catch (Exception e) {
                            mappingLog.error("time" + t + "flow" + i + "出现异常，没有路径");
                            flow[i].setPathStatus("map Failed, no Path");
                            continue;
                        }

                        long before = System.currentTimeMillis();

                        //把路存为ArrayList类型变量pathSet中，从(sat63,sat171),(sat171,sat72)变为[63,171,72]
                        List<List<Integer>> pathSet = JitterControl.convertEdge2Path(numOfSnapshot, allTempPath);
                        List<List<Integer>> oriPathSet = new ArrayList<>();
                        oriPathSet.addAll(pathSet);

                        // 比较出最大时延
                        double maxDelay = 0;
                        Integer maxDelaySnapshot = 0;
                        for (int j = 0; j < numOfSnapshot; j++) {
                            if (maxDelay < allDelay[j]) {
                                maxDelay = allDelay[j];
                                maxDelaySnapshot = j;
                            }
                        }

//                        // 输出初始路径的链路利用率
//                        for (int k = 0; k < pathSet.size(); k++) {
//                            List<Integer> tempPath = pathSet.get(k);
//                            List<Double> allUsage = new ArrayList<>();
//                            for (int l = 0; l < tempPath.size() - 3; l = l + 2) {
//                                Double usage = JitterControl.computeUsageByIndex(flow[i].getStartTime() / input.snapshotInterval.intValue() + k, tempPath, l, f2g);
//                                allUsage.add(usage);
//                            }
//                            try {
//                                mappingLog.info("第" + i + "条流更改路径前第" + k + "个快照下的链路利用率是" + allUsage + "最大值是" + Collections.max(allUsage));
//                            } catch (Exception e) {
//                                System.out.println("空数组");
//                            }
//                        }


                        List<Double> delaySet = new ArrayList<>();
                        for (int j = 0; j < numOfSnapshot; j++) {
                            delaySet.add(j, allDelay[j]);
                        }

                        // 更改其他快照下的路径
                        boolean jitterPathRes = true;
                        for (int j = 0; j < numOfSnapshot; j++) {
//                            satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
                            if (j != maxDelaySnapshot) {
                                // 更新流为此快照下的流
                                flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
                                if (!JitterControl.findPathForJitter(pathSet, j, snapshotIndex, f2g, flow[i], t, satisfyedGraph, delaySet, maxDelay)) {
//                                    mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "切换低路径失败");
                                    flow[i].setPathStatus("map Failed, no Path");
                                    jitterPathRes = false;
                                    break;
                                } else {
//                                    mappingLog.info("time" + t + "flow" + i + "snapshot" + j + "切换低路径成功");
                                }
                            }
                        }
                        // 没找到路径则失败，映射下条流吧
                        if (!jitterPathRes) {
                            continue;
                        }

//                        //抖动降级
//                        if ((maxDelay > flow[i].getTargetDelay()) && flow[i].getTypeOfService().equals("jitter degradation")){
//                            for (int j = 0; j < numOfSnapshot; j++){
//                                if (j != maxDelaySnapshot){
//                                    flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                                    if (!JitterControl.findPathForJitterJitterDegradation(pathSet, oriPathSet, j, snapshotIndex, f2g, flow[i], t, satisfyedGraph, delaySet, maxDelay, allDelay[j])) {
////                                        mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "切换低路径失败");
//                                        flow[i].setPathStatus("map Failed, no Path");
//                                        jitterPathRes = false;
//                                        break;
//                                    }else {
//                                        flow[i].setPathStatus("jitter degradation success");
//                                    }
//                                }
//                            }
//                        }
//
//                        //时延降级
//                        if (((Collections.max(delaySet) - Collections.min(delaySet)) > flow[i].getTargetJitter()) && (flow[i].getTypeOfService().equals("delay degradation"))){
//                            for (int j = 0; j < numOfSnapshot; j++){
//                                if (j != maxDelaySnapshot){
//                                    flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                                    if (!JitterControl.findPathForJitterDelayDegradation(pathSet, oriPathSet, j, snapshotIndex, f2g, flow[i], t, satisfyedGraph, delaySet, maxDelay, allDelay[j])) {
////                                        mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "切换低路径失败");
//                                        flow[i].setPathStatus("map Failed, no Path");
//                                        jitterPathRes = false;
//                                        break;
//                                    }else {
//                                        flow[i].setPathStatus("delay degradation success");
//                                    }
//                                }
//                            }
//                        }
                        if (!jitterPathRes) {
                            continue;
                        }

//                        // 输出更改后路径的链路利用率
//                        for (int k = 0; k < pathSet.size(); k++) {
//                            List<Integer> tempPath = pathSet.get(k);
//                            List<Double> allUsage = new ArrayList<>();
//                            for (int l = 0; l < tempPath.size() - 3; l = l + 2) {
//                                Double usage = JitterControl.computeUsageByIndex(flow[i].getStartTime() / input.snapshotInterval.intValue() + k, tempPath, l, f2g);
//                                allUsage.add(usage);
//                            }
//                            try {
//                                mappingLog.info("第" + i + "条流更改路径后第" + k + "个快照下的链路利用率是" + allUsage + "最大值是" + Collections.max(allUsage));
//                            } catch (Exception e) {
//                                System.out.println("空数组");
//                            }
//                        }

                        // 去除路径的最后一个节点，与映射函数的输入保持统一
                        List<Double> queueingDelay = new ArrayList<>();
                        for (int j = 0; j < pathSet.size(); j++) {
                            if (pathSet.get(j).size() != 0) {
                                queueingDelay.add(j, JitterControl.queueingDelay(pathSet.get(j), j ,flow[i].getStartTime(), f2g));
                                mappingLog.info("第" + i + "条流第" + j + "个快照下的路径是" + pathSet.get(j));
                                pathSet.get(j).remove(pathSet.get(j).size() - 1);
                            }
                        }

                        flow[i].setJitterPathSet(pathSet);
                        flow[i].setJitterDelaySet(delaySet);
                        flow[i].setQueueingDelay(queueingDelay);

                        // 在所有快照中映射该流，映射失败则设置流状态
                        Boolean addFlowOk = true;
                        int startSnapshot = flow[i].getStartTime() / input.snapshotInterval.intValue();
                        for (int j = 0; j < totalFlowSnapshotNum; j++) {
                            flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
                            addFlowOk = f2g[snapshotIndex + j].addFlowInGraphByBuffer(flow[i], flowSnapshotIndex + j);
                            if (addFlowOk == false) {
                                for (int k = 0; k < j; k++) {
                                    flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[startSnapshot + k]);
                                    f2g[startSnapshot + k].remFlowInGraph(flow[i], k);
                                }
                                mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "添加流失败");
                                flow[i].setPathStatus("map Failed, no Path");
                                break;
                            }
                        }
                        long after = System.currentTimeMillis();
                        mappingLog.info(flow[i] + "执行负载均衡时间是" + (after - before));

                        UdpClientServiceImpl udpClientService = new UdpClientServiceImpl();
                        udpClientService.sendPath(i, flow[i].getJitterPathSet().get(flowSnapshotIndex));

                    } else if (flow[i].getPathStatus().equals("map Success") && flow[i].getDeadTime() == t + 1) {
                        int numOfSnapshot = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);
                        int startSnapshot = flow[i].getStartTime() / input.snapshotInterval.intValue();
                        //分快照
                        for (int i2 = 0; i2 < numOfSnapshot; i2++) {
                            //每条链路
                            ArrayList<link> allLinks = new ArrayList<>();
                            for (int i3 = 0; i3 < flow[i].getJitterPathSet().get(i2).size() - 2; i3 = i3 + 2) {
                                Integer thisNode = flow[i].getJitterPathSet().get(i2).get(i3);
                                Integer classNode = flow[i].getJitterPathSet().get(i2).get(i3 + 1);
                                Integer nextNode = flow[i].getJitterPathSet().get(i2).get(i3 + 2);
                                String thisClass = classNode - thisNode == f2g[startSnapshot + i2].getGraph().getSatNodes().length - 1 ? "A" : "B";

                                Integer thislinkIndex = f2g[startSnapshot + i2].getGraph().getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
                                link[] links = f2g[startSnapshot + i2].getGraph().getLinks();
                                link thisLink = links[thislinkIndex];
                                allLinks.add(thisLink);
                            }
                            for (int i4 = 0; i4 < allLinks.size(); i4++) {
                                Curve head = allLinks.get(i4).getIngressCurve().head;
                                while (head != null) {
//                                    mappingLog.info("该(" + head.getPriNode().getId() + "," + head.getThisNode().getId() + "," + head.getNextNode().getId() + ")段上游链路的信息");
                                    CurveInterval curveInterval = head.getHead();
                                    while (curveInterval != null) {
//                                        mappingLog.info("该段曲线的信息 速率：" + curveInterval.getR() + "突发：" + curveInterval.getB() + "起始时间：" + curveInterval.getT0() + "终止时间：" + curveInterval.getT1());
                                        curveInterval = curveInterval.getNext();
                                    }
                                    head = head.getNextCurve();
                                }
//                                mappingLog.info("第" + i + "条流，第" + i2 + "个快照，第" + i4 + "条链路的带宽是" + allLinks.get(i4).getRate() + "已用带宽是" + allLinks.get(i4).getUsedRate() + "缓存是" + allLinks.get(i4).getBuffer() + "已用缓存是" + allLinks.get(i4).getUsedBuffer());
                            }
                        }
                        int flowSnapshotIndex = (int) (t / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue());
                        int totalFlowSnapshotNum = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);
                        for (int j = 0; j < totalFlowSnapshotNum; j++) {
                            flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[startSnapshot + j]);
                            f2g[startSnapshot + j].remFlowInGraph(flow[i], j);
                        }
                    }
                }
            }

            // 快照没有切换，则正常按照时间进行映射（到时映射
            else {
                for (int i = 0; i < input.numOfFlow; i++) {
                    if ((flow[i].getPathStatus().equals("to be map") && flow[i].getStartTime() == t)) {
                        int numOfSnapshot = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);
                        double[] allDelay = new double[numOfSnapshot];
                        GraphPath[] allTempPath = new GraphPath[numOfSnapshot];
                        int flowSnapshotIndex = (int) (t / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue());
                        int totalFlowSnapshotNum = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);

                        // 获得流i在所有快照下的初始路径时延
                        graph[] satisfyedGraph = new graph[numOfSnapshot];
                        try {
                            for (int j = 0; j < numOfSnapshot; j++) {
                                flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
                                allTempPath[j] = Graph[snapshotIndex + j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());

                                satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * input.TijB * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
                                allTempPath[j] = satisfyedGraph[j].getPath(flow[i].getSourceSat().getId(), flow[i].getTargetSat().getId());
                                allDelay[j] = allTempPath[j].getWeight();
                            }
                        } catch (Exception e) {
                            mappingLog.error("time" + t + "flow" + i + "map Failed, no Found Path");
                            flow[i].setPathStatus("map Failed, no Path");
                            continue;
                        }

                        long before = System.currentTimeMillis();

                        //把路存为ArrayList类型
                        List<List<Integer>> pathSet = JitterControl.convertEdge2Path(numOfSnapshot, allTempPath);
                        List<List<Integer>> oriPathSet = new ArrayList<>();
                        oriPathSet.addAll(pathSet);

                        // 比较出最大时延
                        double maxDelay = 0;
                        Integer maxDelaySnapshot = 0;
                        for (int j = 0; j < numOfSnapshot; j++) {
                            allDelay[j] = allDelay[j] + JitterControl.queueingDelay(pathSet.get(j), j, flow[i].getStartTime(), f2g);
                            if (maxDelay < allDelay[j]) {
                                maxDelay = allDelay[j];
                                maxDelaySnapshot = j;
                            }
                        }

                        List<Double> delaySet = new ArrayList<>();
                        for (int j = 0; j < numOfSnapshot; j++) {
                            delaySet.add(j, allDelay[j]);
                        }

//                        // 输出初始路径的链路利用率
//                        for (int k = 0; k < pathSet.size(); k++) {
//                            List<Integer> tempPath = pathSet.get(k);
//                            List<Double> allUsage = new ArrayList<>();
//                            for (int l = 0; l < tempPath.size() - 3; l = l + 2) {
//                                Double usage = JitterControl.computeUsageByIndex(flow[i].getStartTime() / input.snapshotInterval.intValue() + k, tempPath, l, f2g);
//                                allUsage.add(usage);
//                            }
//                            try {
//                                mappingLog.info("第" + i + "条流更改路径前第" + k + "个快照下的链路利用率是" + allUsage + "最大值是" + Collections.max(allUsage));
//                            } catch (Exception e) {
//                                System.out.println("空数组");
//                            }
//                        }


                        // 更改其他快照下的路径
                        boolean jitterPathRes = true;
                        for (int j = 0; j < numOfSnapshot; j++) {
//                            satisfyedGraph[j] = Graph[snapshotIndex + j].initSatisfiedDirectedGraph(flow[i].getRate(), flow[i].getBurst() + flow[i].getRate() * Input.getTijB() * allTempPath[j].getLength() * allTempPath[j].getLength() / 2);
                            if (j != maxDelaySnapshot) {
                                // 更新流为此快照下的流
                                flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
                                if (!JitterControl.findPathForJitter(pathSet, j, snapshotIndex, f2g, flow[i], t, satisfyedGraph, delaySet, maxDelay)) {
//                                    mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "切换低路径失败");
                                    flow[i].setPathStatus("map Failed, no Path");
                                    jitterPathRes = false;
                                    break;
                                } else {
//                                    mappingLog.info("time" + t + "flow" + i + "snapshot" + j + "切换低路径成功");
                                }
                            }
                        }
                        // 没找到路径则失败，映射下条流吧
                        if (!jitterPathRes) {
                            continue;
                        }

//                        //抖动降级
//                        if ((maxDelay > flow[i].getTargetDelay()) && flow[i].getTypeOfService().equals("jitter degradation")){
//                            for (int j = 0; j < numOfSnapshot; j++){
//                                if (j != maxDelaySnapshot){
//                                    flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                                    if (!JitterControl.findPathForJitterJitterDegradation(pathSet, oriPathSet, j, snapshotIndex, f2g, flow[i], t, satisfyedGraph, delaySet, maxDelay, allDelay[j])) {
////                                        mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "切换低路径失败");
//                                        flow[i].setPathStatus("map Failed, no Path");
//                                        jitterPathRes = false;
//                                        break;
//                                    }else {
//                                        flow[i].setPathStatus("jitter degradation success");
//                                    }
//                                }
//                            }
//                        }
//
//                        //时延降级
//                        if (((Collections.max(delaySet) - Collections.min(delaySet)) > flow[i].getTargetJitter()) && flow[i].getTypeOfService().equals("delay degradation")){
//                            for (int j = 0; j < numOfSnapshot; j++){
//                                if (j != maxDelaySnapshot){
//                                    flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
//                                    if (!JitterControl.findPathForJitterDelayDegradation(pathSet, oriPathSet, j, snapshotIndex, f2g, flow[i], t, satisfyedGraph, delaySet, maxDelay, allDelay[j])) {
////                                        mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "切换低路径失败");
//                                        flow[i].setPathStatus("map Failed, no Path");
//                                        jitterPathRes = false;
//                                        break;
//                                    }else {
//                                        flow[i].setPathStatus("delay degradation success");
//                                    }
//                                }
//                            }
//                        }
                        if (!jitterPathRes) {
                            continue;
                        }

//                        // 输出改动后路径的链路利用率
//                        for (int k = 0; k < pathSet.size(); k++) {
//                            List<Integer> tempPath = pathSet.get(k);
//                            List<Double> allUsage = new ArrayList<>();
//                            for (int l = 0; l < tempPath.size() - 3; l = l + 2) {
//                                Double usage = JitterControl.computeUsageByIndex(flow[i].getStartTime() / input.snapshotInterval.intValue() + k, tempPath, l, f2g);
//                                allUsage.add(usage);
//                            }
//                            try {
//                                mappingLog.info("第" + i + "条流更改路径后第" + k + "个快照下的链路利用率是" + allUsage + "最大值是" + Collections.max(allUsage));
//                            } catch (Exception e) {
//                                System.out.println("空数组");
//                            }
//                        }

                        // 去除路径的最后一个节点，与映射函数的输入保持统一
                        List<Double> queueingDelay = new ArrayList<>();
                        for (int j = 0; j < pathSet.size(); j++) {
                            if (pathSet.get(j).size() != 0) {
                                queueingDelay.add(j, JitterControl.queueingDelay(pathSet.get(j), j ,flow[i].getStartTime(), f2g));
//                                mappingLog.info("第" + i + "条流第" + j + "个快照下的路径是" + pathSet.get(j));
                                pathSet.get(j).remove(pathSet.get(j).size() - 1);
                            }
                        }

                        flow[i].setJitterPathSet(pathSet);
                        flow[i].setJitterDelaySet(delaySet);
                        flow[i].setQueueingDelay(queueingDelay);

                        // 在所有快照中映射该流，映射失败则设置流状态
                        Boolean addFlowOk = true;
                        int startSnapshot = flow[i].getStartTime() / input.snapshotInterval.intValue();
                        for (int j = 0; j < totalFlowSnapshotNum; j++) {
                            flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[snapshotIndex + j]);
                            addFlowOk = f2g[snapshotIndex + j].addFlowInGraphByBuffer(flow[i], flowSnapshotIndex + j);
                            if (addFlowOk == false) {
                                for (int k = 0; k < j; k++) {
                                    flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[startSnapshot + k]);
                                    f2g[startSnapshot + k].remFlowInGraph(flow[i], k);
                                }
                                mappingLog.error("time" + t + "flow" + i + "snapshot" + j + "添加流失败");
                                flow[i].setPathStatus("map Failed, no Path");
                                break;
                            }
                        }

                        long after = System.currentTimeMillis();
                        mappingLog.info(flow[i] + "执行负载均衡时间是" + (after - before));


                        UdpClientServiceImpl udpClientService = new UdpClientServiceImpl();
                        udpClientService.sendPath(i, flow[i].getJitterPathSet().get(flowSnapshotIndex));

                    } else if ((flow[i].getPathStatus().equals("map Success") || flow[i].getPathStatus().equals("delay degradation success"))  && flow[i].getDeadTime() == t + 1) {
                        int numOfSnapshot = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);
                        int startSnapshot = flow[i].getStartTime() / input.snapshotInterval.intValue();
                        //分快照
                        for (int i2 = 0; i2 < numOfSnapshot; i2++) {
                            //每条链路
                            ArrayList<link> allLinks = new ArrayList<>();
                            for (int i3 = 0; i3 < flow[i].getJitterPathSet().get(i2).size() - 2; i3 = i3 + 2) {
                                Integer thisNode = flow[i].getJitterPathSet().get(i2).get(i3);
                                Integer classNode = flow[i].getJitterPathSet().get(i2).get(i3 + 1);
                                Integer nextNode = flow[i].getJitterPathSet().get(i2).get(i3 + 2);
                                String thisClass = classNode - thisNode == f2g[startSnapshot + i2].getGraph().getSatNodes().length - 1 ? "A" : "B";

                                Integer thislinkIndex = f2g[startSnapshot + i2].getGraph().getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
                                link[] links = f2g[startSnapshot + i2].getGraph().getLinks();
                                link thisLink = links[thislinkIndex];
                                allLinks.add(thisLink);
                            }
                            for (int i4 = 0; i4 < allLinks.size(); i4++) {
                                Curve head = allLinks.get(i4).getIngressCurve().head;
                                while (head != null) {
//                                    mappingLog.info("该(" + head.getPriNode().getId() + "," + head.getThisNode().getId() + "," + head.getNextNode().getId() + ")段上游链路的信息");
                                    CurveInterval curveInterval = head.getHead();
                                    while (curveInterval != null) {
//                                        mappingLog.info("该段曲线的信息 速率：" + curveInterval.getR() + "突发：" + curveInterval.getB() + "起始时间：" + curveInterval.getT0() + "终止时间：" + curveInterval.getT1());
                                        curveInterval = curveInterval.getNext();
                                    }
                                    head = head.getNextCurve();
                                }
//                                mappingLog.info("第" + i + "条流，第" + i2 + "个快照，第" + i4 + "条链路的带宽是" + allLinks.get(i4).getRate() + "已用带宽是" + allLinks.get(i4).getUsedRate() + "缓存是" + allLinks.get(i4).getBuffer() + "已用缓存是" + allLinks.get(i4).getUsedBuffer());
                            }
                        }
                        int flowSnapshotIndex = (int) (t / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue());
                        int totalFlowSnapshotNum = (int) (flow[i].getDeadTime() / input.snapshotInterval.intValue() - flow[i].getStartTime() / input.snapshotInterval.intValue() + 1);
                        for (int j = 0; j < totalFlowSnapshotNum; j++) {
                            flow[i] = initFlow.flowUpdateSourceTarget(flow[i], Graph[startSnapshot + j]);
                            f2g[startSnapshot + j].remFlowInGraph(flow[i], j);
                        }
//                        f2g[snapshotIndex].remFlowInGraph(flow[i]);
                    }
                }

            }
        }

        // 打印结果 & 打印到日志上
//        f2g[snapshotIndex].output();
        int failedFlowsNum = 0;
        int delayDegradationNum = 0;
        int jitterDegradationNum = 0;
        List<Double> jitterout = new ArrayList<>();
        for (int i = 0; i < input.numOfFlow; i++) {
            failedFlowsNum = flow[i].getPathStatus().equals("map Failed, no Path") ? failedFlowsNum + 1 : failedFlowsNum;
            delayDegradationNum = flow[i].getPathStatus().equals("delay degradation success") ? delayDegradationNum + 1 : delayDegradationNum;
            jitterDegradationNum = flow[i].getPathStatus().equals("jitter degradation success") ? jitterDegradationNum + 1 : jitterDegradationNum;
            if (flow[i].getJitterPathSet() != null) {
                if ((Collections.max(flow[i].getJitterDelaySet()) - Collections.min(flow[i].getJitterDelaySet())) > 0.05){
                    failedFlowsNum++;
                }
                for (int j = 0; j < flow[i].getJitterPathSet().size(); j++) {
                    mappingLog.info("第" + i + "条流的第" + j + "个快照下的路径是" + flow[i].getJitterPathSet().get(j));
                }
                mappingLog.info("第" + i + "条流在各个快照下的时延" + flow[i].getJitterDelaySet());
                mappingLog.info("第" + i + "条流的最大时延" + Collections.max(flow[i].getJitterDelaySet()));
                mappingLog.info("第" + i + "条流的最小时延" + Collections.min(flow[i].getJitterDelaySet()));
//                mappingLog.info("第" + i + "条流的目标时延" + flow[i].getTargetDelay());
                mappingLog.info("第" + i + "条流的抖动" + (Collections.max(flow[i].getJitterDelaySet()) - Collections.min(flow[i].getJitterDelaySet())));
//                mappingLog.info("第" + i + "条流的带宽" + flow[i].getRate() + "突发" + flow[i].getBurst());
//                mappingLog.info("第" + i + "条流在各个快照下的排队时延" + flow[i].getQueueingDelay());
//                mappingLog.info("第" + i + "条流的排队抖动" + (Collections.max(flow[i].getQueueingDelay()) - Collections.min(flow[i].getQueueingDelay())));
//                mappingLog.info("第" + i + "条流的起始时间是" + flow[i].getStartTime());
                jitterout.add(Collections.max(flow[i].getJitterDelaySet()) - Collections.min(flow[i].getJitterDelaySet()));
            }
        }
        CategoryDataset dataset = createDataset(jitterout);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1500, 900));

        JFrame frame = new JFrame("Bar Chart Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);


//        mappingLog.info("总共的流数" + input.numOfFlow + "失败映射流数:" + failedFlowsNum + "抖动降级流数" + jitterDegradationNum + "时延降级流数" + delayDegradationNum);
//        System.out.println("总共流数：700 成功规划：636");
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}