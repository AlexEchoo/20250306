package com.pml.route.business.sr.model;


import lombok.*;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.Random;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
//@NoArgsConstructor

public class initFlow {

    private static int[] groundStationRandom() {
        Random random = new Random();
        int[] groundStationIndex = new int[2];
        groundStationIndex[0] = random.nextInt(input.groundStationNum);
        groundStationIndex[1] = random.nextInt(input.groundStationNum);
        while (groundStationIndex[0] == groundStationIndex[1]) {
            groundStationIndex[1] = random.nextInt(input.groundStationNum);
        }
        return groundStationIndex;
    }

    private static satNode getNearestSatByGroundStation(groundStationNode groundStation, graph graph) {


        satNode[] satNodes = graph.getSatNodes();
        int targetSat = 1;
        satNode sat = new satNode();
        double distance = Math.pow((satNodes[1].getLongitude() - groundStation.getLongitude()), 2) + Math.pow((satNodes[1].getLatitude() - groundStation.getLatitude()), 2);
        for (int i = 2; i < satNodes.length; i++) {
            double newDistance = Math.pow((satNodes[i].getLongitude() - groundStation.getLongitude()), 2) + Math.pow((satNodes[i].getLatitude() - groundStation.getLatitude()), 2);
            if (newDistance <= distance) {
                distance = newDistance;
                targetSat = i;
            }
        }
        sat = satNodes[targetSat];
        return sat;
    }


    private static satNode getNearestSatByGroundStationAvoidSatX(groundStationNode groundStation, graph graph, Integer satX) {


        satNode[] satNodes = graph.getSatNodes();
        int oriTargetSat = satX == 1 ? 2 : 1;
        int targetSat = oriTargetSat;
        satNode sat = new satNode();
        double distance = Math.pow((satNodes[1].getLongitude() - groundStation.getLongitude()), 2) + Math.pow((satNodes[1].getLatitude() - groundStation.getLatitude()), 2);
        for (int i = 1; i < satNodes.length; i++) {
            if (targetSat != satX && targetSat != oriTargetSat) {
                double newDistance = Math.pow((satNodes[i].getLongitude() - groundStation.getLongitude()), 2) + Math.pow((satNodes[i].getLatitude() - groundStation.getLatitude()), 2);
                if (newDistance <= distance) {
                    distance = newDistance;
                    targetSat = i;
                }
            }
        }
        sat = satNodes[targetSat];
        return sat;
    }

    private static int[] timeRandom() {
        Random random = new Random();
        int[] time = new int[2];
        time[0] = random.nextInt(input.simulationTime + 1);
        time[1] = random.nextInt(input.simulationTime + 1);
        while (time[0] == time[1]) {
            time[1] = random.nextInt(input.groundStationNum + 1);
        }
        int[] simulationTime = new int[2];
        if (time[0] < time[1]) {
            simulationTime[0] = time[0];
            simulationTime[1] = time[1];
        } else {
            simulationTime[0] = time[1];
            simulationTime[1] = time[0];
        }
        return simulationTime;
    }

    private static double maxDelayCompute(groundStationNode sourceGroundStation, groundStationNode targetGroundStation) {
        Double maxDelay = Math.max(0.2, delayCompute(sourceGroundStation, targetGroundStation));
        return maxDelay;
    }

    private static double delayCompute(groundStationNode sourceGroundStation, groundStationNode targetGroundStation) {
        double x1 = sourceGroundStation.getLongitude() * 3.1415926 / 180;
        double x2 = targetGroundStation.getLongitude() * 3.1415926 / 180;
        double y1 = sourceGroundStation.getLatitude() * 3.1415926 / 180;
        double y2 = targetGroundStation.getLatitude() * 3.1415926 / 180;
        Double r = 6371000.0;
        Double d = r * Math.acos(Math.cos(y1) * Math.cos(y2) * Math.cos(x1 - x2) + Math.sin(y1) * Math.sin(y2));
        Double maxDelay = d * 21e-08;
        return maxDelay;
    }

    //    graph graph = new graph();
    public static flow initFlow(graph graph) {


        flow flow = new flow();

//        // 设置起止地面站
//        int[] groundStationIndex = groundStationRandom();
//        groundStationNode sourceGroundStationNode = groundStationNode.builder().id(groundStationIndex[0]).
//                longitude(input.gSLongitude[groundStationIndex[0]]).latitude(input.gSLatitude[groundStationIndex[0]]).build();
//        groundStationNode targetGroundStationNode = groundStationNode.builder().id(groundStationIndex[1]).
//                longitude(input.gSLongitude[groundStationIndex[1]]).latitude(input.gSLatitude[groundStationIndex[1]]).build();
//        flow.setSourceGroundStation(sourceGroundStationNode);
//        flow.setTargetGroundStation(targetGroundStationNode);
//        // 设置起止卫星
//        flow.setSourceSat(getNearestSatByGroundStation(sourceGroundStationNode, graph));
//        flow.setTargetSat(getNearestSatByGroundStation(targetGroundStationNode, graph));

        // 起止卫星直接随机选择
        Integer sourceIndex = (int) (Math.random() * input.numOfOrbit * input.satOfOneOrbit + 1);
        Integer targetIndex = (int) (Math.random() * input.numOfOrbit * input.satOfOneOrbit + 1);
        // 避免源终卫星相同
//        while (targetIndex.equals(sourceIndex) || sourceIndex.equals(20) || sourceIndex.equals(30) || sourceIndex.equals(40) || sourceIndex.equals(50) || sourceIndex.equals(60) || targetIndex.equals(20) || targetIndex.equals(30) || targetIndex.equals(40) || targetIndex.equals(50) || targetIndex.equals(60)) {
//            targetIndex = (int) (Math.random() * input.numOfOrbit * input.satOfOneOrbit + 1);
//            sourceIndex = (int) (Math.random() * input.numOfOrbit * input.satOfOneOrbit + 1);
//        }

        while (targetIndex.equals(sourceIndex)) {
            targetIndex = (int) (Math.random() * input.numOfOrbit * input.satOfOneOrbit + 1);
        }
        flow.setSourceSat(graph.getSatNodes()[sourceIndex]);
        flow.setTargetSat(graph.getSatNodes()[targetIndex]);
        groundStationNode sourceGroundStationNode = groundStationNode.builder().id(0).
                longitude(flow.getSourceSat().getLongitude()).latitude(flow.getSourceSat().getLatitude()).build();
        groundStationNode targetGroundStationNode = groundStationNode.builder().id(0).
                longitude(flow.getTargetSat().getLongitude()).latitude(flow.getTargetSat().getLatitude()).build();
        flow.setSourceGroundStation(sourceGroundStationNode);
        flow.setTargetGroundStation(targetGroundStationNode);

        // 设置速率
        flow.setRate(input.rFlow);

        // 设置突发
        flow.setBurst(input.bFlow);

        // 设置起止时间
        int[] simulationTime = timeRandom();
        flow.setStartTime(simulationTime[0]);
        flow.setDeadTime(simulationTime[1]);

        // 设置最大时延
        flow.setMaxDelay(maxDelayCompute(sourceGroundStationNode, targetGroundStationNode));

        // 设置目标时延
        flow.setTargetDelay(0.0);

        // 设置目标抖动
        flow.setTargetJitter(0.05);

        // 设置流状态
        flow.setPathStatus("to be map");

        // 设置流属性
//        flow.setTypeOfService("Delay Sensitive");

        return flow;
    }

    public static flow flowUpdateSourceTarget(flow Flow, graph Graph) {
        Flow.setSourceSat(getNearestSatByGroundStation(Flow.getSourceGroundStation(), Graph));
        Flow.setTargetSat(getNearestSatByGroundStationAvoidSatX(Flow.getTargetGroundStation(), Graph, Flow.getSourceSat().getId()));
        return Flow;
    }

    public static Double getMaxMinDelay(flow Flow, graph[] Graph, input Input) throws IOException {
        Integer snapshotNum = (int) Math.min((Flow.getDeadTime() / Input.snapshotInterval), (Input.simulationTime / Input.snapshotInterval) - 1)
                - (int) (Flow.getStartTime() / Input.snapshotInterval);
        Integer snapshotStart = (int) (Flow.getStartTime() / Input.snapshotInterval);
        Double targetDelay = 0.0;
        for (int snapshotIndex = snapshotStart; snapshotIndex < snapshotStart + snapshotNum + 1; snapshotIndex++) {
            Flow = initFlow.flowUpdateSourceTarget(Flow, Graph[snapshotIndex]);
            GraphPath<String, DefaultWeightedEdge> tempPath = Graph[snapshotIndex].getPath(Flow.getSourceSat().getId(), Flow.getTargetSat().getId());
//            graph satisfyedGraph = Graph[snapshotIndex].initSatisfiedDirectedGraph(Flow.getRate(), Flow.getBurst() + Flow.getRate() * Input.getTijB() * tempPath.getLength() * tempPath.getLength() / 2);
            graph satisfyedGraph = Graph[snapshotIndex].initSatisfiedDirectedGraph(Flow.getRate(), 0.0);
            Double tempTargetDelay =
                    satisfyedGraph.getPathDelay(Flow.getSourceSat().getId(), Flow.getTargetSat().getId());
            targetDelay = targetDelay < tempTargetDelay ? tempTargetDelay : targetDelay;
        }
        return targetDelay;
    }
}
