package com.pml.route.business.JitterControl;

//import com.pml.JitterApplication2;
import com.pml.route.business.sr.model.*;
import io.swagger.models.auth.In;
import lombok.*;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class JitterControl {
    private static final Logger jitterControlLog = LoggerFactory.getLogger(JitterControl.class);


    /**
     * 找到路径pathSet[j]上，链路利用率低于usageUpperBound的最大链路索引
     * 返回该路径上链路利用率最大的边的索引号
     *
     * @param pathSet
     * @param j
     * @param f2gIndex
     * @param tempPath
     * @param f2g
     * @param usageUpperBound
     * @return
     */
    public static Integer findMaxUsageIndex(List<List<Integer>> pathSet, Integer j, Integer f2gIndex, List<Integer> tempPath, flow2graph[] f2g, Double usageUpperBound) {
        Double maxUsage = 0.0;
        Integer maxUsageIndex = 0;
//        for (int k = 0; k < pathSet.get(j).size() - 3; k = k + 2) {
        for (int k = 0; k < tempPath.size() - 3; k = k + 2) {
            Double usage = computeUsageByIndex(f2gIndex, tempPath, k, f2g);
            if (maxUsage < usage && usage < usageUpperBound) {
                maxUsage = usage;
                maxUsageIndex = k;
            }
        }
        if (maxUsageIndex % 2 == 0) {
            maxUsageIndex = maxUsageIndex - 1;
        }
        maxUsageIndex = maxUsageIndex - 1;
        if (maxUsageIndex < 0) {
            maxUsageIndex = 0;
        }
        return maxUsageIndex;
    }

    public static Double computeUsageByIndex(Integer f2gIndex, List<Integer> tempPath, Integer k, flow2graph[] f2g) {
        Double usage = 0.0;
        Integer thisNode = tempPath.get(k);
        Integer classNode = tempPath.get(k + 1);
        Integer nextNode = tempPath.get(k + 2);
        String thisClass = classNode - thisNode == f2g[f2gIndex].getGraph().getSatNodes().length - 1 ? "A" : "B";

        Integer thislinkIndex = f2g[f2gIndex].getGraph().getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
        link[] links = f2g[f2gIndex].getGraph().getLinks();
        link thisLink = links[thislinkIndex];
        usage = ((thisLink.getUsedRate()) / thisLink.getRate());
        return usage;
    }

    public static List<Integer> getOtherAdjNodes
            (List<List<Integer>> pathSet, Integer maxUsageIndex,
             Set<DefaultWeightedEdge> nearNodes, List<Object> edges,
             Integer j) {
        List<Integer> otherNodes = new ArrayList<>();
        if (maxUsageIndex > 0) {
            for (int k = 0; k < nearNodes.size(); k++) {
                String tempedge = edges.get(k).toString();
                String sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
                Integer sourceSatIndex = Integer.valueOf(sourceString);
                Integer nextNode = pathSet.get(j).get(maxUsageIndex + 2);
                Integer lastNode = pathSet.get(j).get(maxUsageIndex - 2);
                if (lastNode >= 0) {
                    if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))) {
                        //     if (thislinkIndex == -1){
                        ////       if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
                        //                continue;
                        //             }
                        otherNodes.add(sourceSatIndex);
                    }
                } else {
                    if (!sourceSatIndex.equals(nextNode)) {
                        if ((!sourceSatIndex.equals(lastNode)) && (!sourceSatIndex.equals(nextNode))) {
                            //     if (thislinkIndex == -1){
                            ////     if (f2g[snapshotIndex+j].getGraph().getPathDelay(pathSet.get(j).get(maxUsageIndex), sourceSatIndex)<0.0001){
                            //            continue;
                            //          }
                        }
                        otherNodes.add(sourceSatIndex);
                    }
                }
            }
        } else {
            for (int k = 0; k < nearNodes.size(); k++) {
                String tempedge = edges.get(k).toString();
                String sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
                Integer sourceSatIndex = Integer.valueOf(sourceString);
                Integer nextNode = pathSet.get(j).get(maxUsageIndex + 2);
                if (!sourceSatIndex.equals(nextNode)) {
                    otherNodes.add(sourceSatIndex);
                }
            }
        }
        return otherNodes;
    }

    public static List<List<Integer>> convertEdge2Path(Integer numOfSnapshot, GraphPath[] allTempPath) {
        List<List<Integer>> pathSet = new ArrayList<>();
        for (int j = 0; j < numOfSnapshot; j++) {
            List<DefaultWeightedEdge> edges = allTempPath[j].getEdgeList();
            List<Integer> pathInt = new ArrayList<>();
            // 如果源终节点为同一卫星，则只添加卫星节点
            if (edges.size() == 0) {
                String tempedge = allTempPath[j].getStartVertex().toString();
                String sourceString = tempedge.substring(tempedge.indexOf("sat") + 3);
                Integer sourceSatIndex = Integer.valueOf(sourceString);
                pathInt.add(sourceSatIndex);
            } else {
                for (int k = 0; k < edges.size(); k++) {
                    String tempedge = edges.get(k).toString();
                    String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                    Integer sourceSatIndex = Integer.valueOf(sourceString);
                    pathInt.add(sourceSatIndex);
                    if (k == edges.size() - 1) {
                        sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
                        sourceSatIndex = Integer.valueOf(sourceString);
                        pathInt.add(sourceSatIndex);
                    }
                }
            }
            pathSet.add(j, pathInt);
        }
        return pathSet;
    }

    public static Boolean findPathForJitter
            (List<List<Integer>> pathSet, Integer j, Integer snapshotIndex,
             flow2graph[] f2g, flow flow, Integer t,
             graph[] satisfyedGraph, List<Double> delaySet,
             Double maxDelay) {
        List<Integer> tempPath = new ArrayList<>(pathSet.get(j));

        // 找出链路利用率最大的链路，记录其索引为maxUsageIndex
        Double maxUsage = 1.0;
        Integer maxUsageIndex = 0;
        maxUsageIndex = JitterControl.findMaxUsageIndex(pathSet, j, snapshotIndex + j, tempPath, f2g, maxUsage);
        maxUsage = JitterControl.computeUsageByIndex(snapshotIndex + j, tempPath, maxUsageIndex, f2g);

        // 找出链路利用率最大的链路的卫星的邻接，但是也包含了之前的链路利用率最大的链路，该卫星的邻居卫星nearNodes和邻接链路edges
        Set<DefaultWeightedEdge> nearNodes = new HashSet<>();
        List<Object> edges = new ArrayList<>();
        if (pathSet.get(j).size() == 0) {
//                                    nearNodes = null;
        } else {
            nearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf
                    ("sat" + String.valueOf(pathSet.get(j).get(maxUsageIndex + 1)));
            Iterator it = nearNodes.iterator();
            while (it.hasNext()) {
                edges.add(it.next());
            }
        }
        // 找出链路利用率最大的链路的卫星的邻接，通过如下操作，去除先前的利用率最大的链路
        List<Integer> otherNodes = new ArrayList<>();
        otherNodes = JitterControl.getOtherAdjNodes(pathSet, maxUsageIndex, nearNodes, edges, j);

        // 邻居卫星找新路
        if (otherNodes.size() == 0) {
            jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "没有邻居卫星");
            return false;
        }
//        Integer newNode = otherNodes.get(0);
        Random random = new Random();
        Integer newNode = otherNodes.get(random.nextInt(otherNodes.size()));
        Double newDelay = delaySet.get(j);
        for (int k = 0; k < 12; k++) {
            Double tempDelay = 0.0;
            // 记录原来路径到最大利用节点的延迟和节点，起始节点到maxUsageIndex
            List<Integer> tempAgrPath = new ArrayList<>();
            for (int l = 0; l < maxUsageIndex; l++) {
                tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(l), pathSet.get(j).get(l + 1));
//                                        System.out.println(tempDelay);
                tempAgrPath.add(pathSet.get(j).get(l));
            }
            if (maxUsageIndex + 1 >= pathSet.get(j).size()) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "12个节点算法" + k + "tempPath超限");
                break;
            }

            // 记录从maxUsageIndex到newNode的路径
            List<DefaultWeightedEdge> tempAddEdge;
            try {
                GraphPath midPath = satisfyedGraph[j].getPath(pathSet.get(j).get(maxUsageIndex), newNode);
                List<DefaultWeightedEdge> tempMidAddEdge = midPath.getEdgeList();
                for (int l = 0; l < tempMidAddEdge.size(); l++) {
                    String tempedge = tempMidAddEdge.get(l).toString();
                    String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                    Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        tempAddPathInt.add(sourceSatIndex);
                    tempAgrPath.add(sourceSatIndex);
                }

                // 记录时延改变的多少
                tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(pathSet.get(j).get(maxUsageIndex), newNode);
                // 记录改变的路径的延迟，从newNode到targetNode
                GraphPath tempAddPath = satisfyedGraph[j].getPath(newNode, flow.getTargetSat().getId());
                tempDelay = tempDelay + tempAddPath.getWeight();
                // 记录改变的路径的节点
                tempAddEdge = tempAddPath.getEdgeList();
            } catch (Exception e) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "node" + k + "change Failed, no Path");
                continue;
            }

            // 记录改变的路径的延迟，从newNode到targetNode
            for (int l = 0; l < tempAddEdge.size(); l++) {
                String tempedge = tempAddEdge.get(l).toString();
                String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        tempAddPathInt.add(sourceSatIndex);
                tempAgrPath.add(sourceSatIndex);
                if (l == tempAddEdge.size() - 1) {
                    sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
                    sourceSatIndex = Integer.valueOf(sourceString);
//                                            tempAddPathInt.add(sourceSatIndex);
                    tempAgrPath.add(sourceSatIndex);
                }
            }

            tempDelay = tempDelay + queueingDelay(tempAgrPath, j ,flow.getStartTime(), f2g);

            jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次的路径是" + pathSet.get(j) + "时延是" + newDelay);
            jitterControlLog.info("最大时延是" + maxDelay + "改路后时延是" + tempDelay);
            //更改为新的路径，如果超过最大时延，break
            if ((tempDelay <= maxDelay) && (tempDelay >= newDelay)) {
                pathSet.remove((int) j);
                pathSet.add(j, tempAgrPath);
                newDelay = tempDelay;
                jitterControlLog.info("更改路径！第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + pathSet.get(j) + "新的时延是" + newDelay);
            }
//            if (Math.abs(newDelay - maxDelay) < flow.getTargetJitter()) {
//                jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + pathSet.get(j) + "新的时延是" + newDelay + "此路径抖动符合流需求");
//                break;
//            }
            if (tempDelay > maxDelay) {
                jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + pathSet.get(j) + "新的时延是" + newDelay + "此路径新时延大于目标时延");
                break;
            }
            //寻找新节点的相邻节点
//            maxUsageIndex = JitterControl.findMaxUsageIndex(pathSet, j, snapshotIndex + j, tempPath, f2g, maxUsage);
//            maxUsage = JitterControl.computeUsageByIndex(snapshotIndex + j, tempPath, maxUsageIndex, f2g);
            maxUsageIndex = maxUsageIndex + 2;

            // 满足抖动要求后，终止寻找新节点
            if (maxUsageIndex + 1 >= pathSet.get(j).size()) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "12个节点算法" + k + "pathSet超限");
                break;
            }
            Set<DefaultWeightedEdge> nextNearNodes = new HashSet<>();
            List<Object> nextEdges = new ArrayList<>();
            nextNearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf
                    ("sat" + String.valueOf(pathSet.get(j).get(maxUsageIndex + 1)));
            Iterator nextIt = nextNearNodes.iterator();
            while (nextIt.hasNext()) {
                nextEdges.add(nextIt.next());
            }
            otherNodes.clear();
            otherNodes = JitterControl.getOtherAdjNodes(pathSet, maxUsageIndex, nextNearNodes, nextEdges, j);
            if (otherNodes.size() == 0) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "没有邻居卫星");
                return false;
            }
            newNode = otherNodes.get(random.nextInt(otherNodes.size()));
        }
        delaySet.remove((int)j);
        delaySet.add(j, newDelay);
        return true;
    }

    public static Boolean findPathForJitterDelayDegradation
            (List<List<Integer>> pathSet, List<List<Integer>> oriPathSet, Integer j, Integer snapshotIndex,
             flow2graph[] f2g, flow flow, Integer t,
             graph[] satisfyedGraph, List<Double> delaySet,
             Double maxDelay, Double oriDelay){
        List<Integer> tempPath = new ArrayList<>(oriPathSet.get(j));

        // 找出链路利用率最大的链路，记录其索引为maxUsageIndex
        Double maxUsage = 1.0;
        Integer maxUsageIndex = 0;
        maxUsageIndex = JitterControl.findMaxUsageIndex(oriPathSet, j, snapshotIndex + j, tempPath, f2g, maxUsage);
        maxUsage = JitterControl.computeUsageByIndex(snapshotIndex + j, tempPath, maxUsageIndex, f2g);

        // 找出链路利用率最大的链路的卫星的邻接，但是也包含了之前的链路利用率最大的链路，该卫星的邻居卫星nearNodes和邻接链路edges
        Set<DefaultWeightedEdge> nearNodes = new HashSet<>();
        List<Object> edges = new ArrayList<>();
        if (oriPathSet.get(j).size() == 0) {
//                                    nearNodes = null;
        } else {
            nearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf
                    ("sat" + String.valueOf(oriPathSet.get(j).get(maxUsageIndex + 1)));
            Iterator it = nearNodes.iterator();
            while (it.hasNext()) {
                edges.add(it.next());
            }
        }
        // 找出链路利用率最大的链路的卫星的邻接，通过如下操作，去除先前的利用率最大的链路
        List<Integer> otherNodes = new ArrayList<>();
        otherNodes = JitterControl.getOtherAdjNodes(oriPathSet, maxUsageIndex, nearNodes, edges, j);

        // 邻居卫星找新路
        if (otherNodes.size() == 0) {
            jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "没有邻居卫星");
            return false;
        }
//        Integer newNode = otherNodes.get(0);
        Random random = new Random();
        Integer newNode = otherNodes.get(random.nextInt(otherNodes.size()));
        Double newDelay = oriDelay;
        for (int k = 0; k < 12; k++) {
            Double tempDelay = 0.0;
            // 记录原来路径到最大利用节点的延迟和节点，起始节点到maxUsageIndex
            List<Integer> tempAgrPath = new ArrayList<>();
            for (int l = 0; l < maxUsageIndex; l++) {
                tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(oriPathSet.get(j).get(l), oriPathSet.get(j).get(l + 1));
//                                        System.out.println(tempDelay);
                tempAgrPath.add(oriPathSet.get(j).get(l));
            }
            if (maxUsageIndex + 1 >= oriPathSet.get(j).size()) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "12个节点算法" + k + "tempPath超限");
                break;
            }

            // 记录从maxUsageIndex到newNode的路径
            List<DefaultWeightedEdge> tempAddEdge;
            try {
                GraphPath midPath = satisfyedGraph[j].getPath(oriPathSet.get(j).get(maxUsageIndex), newNode);
                List<DefaultWeightedEdge> tempMidAddEdge = midPath.getEdgeList();
                for (int l = 0; l < tempMidAddEdge.size(); l++) {
                    String tempedge = tempMidAddEdge.get(l).toString();
                    String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                    Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        tempAddPathInt.add(sourceSatIndex);
                    tempAgrPath.add(sourceSatIndex);
                }

                // 记录时延改变的多少
                tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(oriPathSet.get(j).get(maxUsageIndex), newNode);
                // 记录改变的路径的延迟，从newNode到targetNode
                GraphPath tempAddPath = satisfyedGraph[j].getPath(newNode, flow.getTargetSat().getId());
                tempDelay = tempDelay + tempAddPath.getWeight();
                // 记录改变的路径的节点
                tempAddEdge = tempAddPath.getEdgeList();
            } catch (Exception e) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "node" + k + "change Failed, no Path");
                continue;
            }

            // 记录改变的路径的延迟，从newNode到targetNode
            for (int l = 0; l < tempAddEdge.size(); l++) {
                String tempedge = tempAddEdge.get(l).toString();
                String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        tempAddPathInt.add(sourceSatIndex);
                tempAgrPath.add(sourceSatIndex);
                if (l == tempAddEdge.size() - 1) {
                    sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
                    sourceSatIndex = Integer.valueOf(sourceString);
//                                            tempAddPathInt.add(sourceSatIndex);
                    tempAgrPath.add(sourceSatIndex);
                }
            }

            tempDelay = tempDelay + queueingDelay(tempAgrPath, j ,flow.getStartTime(), f2g);

            jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次的路径是" + oriPathSet.get(j) + "时延是" + newDelay);
            jitterControlLog.info("最大时延是" + maxDelay + "改路后时延是" + tempDelay);
            //更改为新的路径，如果超过最大时延，break
            if ((tempDelay <= 2 * maxDelay) && (Math.abs(tempDelay - maxDelay) <= Math.abs(newDelay - maxDelay))) {
                oriPathSet.remove((int) j);
                oriPathSet.add(j, tempAgrPath);
                newDelay = tempDelay;
                jitterControlLog.info("更改路径！第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + oriPathSet.get(j) + "新的时延是" + newDelay);
            }
//            if (Math.abs(newDelay - maxDelay) < flow.getTargetJitter()) {
//                jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + oriPathSet.get(j) + "新的时延是" + newDelay + "此路径抖动符合流需求");
//                break;
//            }
            if (tempDelay > maxDelay) {
                jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + oriPathSet.get(j) + "新的时延是" + newDelay + "此路径新时延大于目标时延");
                break;
            }
            //寻找新节点的相邻节点
//            maxUsageIndex = JitterControl.findMaxUsageIndex(oriPathSet, j, snapshotIndex + j, tempPath, f2g, maxUsage);
//            maxUsage = JitterControl.computeUsageByIndex(snapshotIndex + j, tempPath, maxUsageIndex, f2g);
            maxUsageIndex = maxUsageIndex + 2;

            // 满足抖动要求后，终止寻找新节点
            if (maxUsageIndex + 1 >= oriPathSet.get(j).size()) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "12个节点算法" + k + "oriPathSet超限");
                break;
            }
            Set<DefaultWeightedEdge> nextNearNodes = new HashSet<>();
            List<Object> nextEdges = new ArrayList<>();
            nextNearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf
                    ("sat" + String.valueOf(oriPathSet.get(j).get(maxUsageIndex + 1)));
            Iterator nextIt = nextNearNodes.iterator();
            while (nextIt.hasNext()) {
                nextEdges.add(nextIt.next());
            }
            otherNodes.clear();
            otherNodes = JitterControl.getOtherAdjNodes(oriPathSet, maxUsageIndex, nextNearNodes, nextEdges, j);
            if (otherNodes.size() == 0) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "没有邻居卫星");
                return false;
            }
            newNode = otherNodes.get(random.nextInt(otherNodes.size()));
        }
        pathSet.clear();
        pathSet.addAll(oriPathSet);
        delaySet.remove((int)j);
        delaySet.add(j, newDelay);
        return true;
    }


    public static Boolean findPathForJitterJitterDegradation
            (List<List<Integer>> pathSet, List<List<Integer>> oriPathSet, Integer j, Integer snapshotIndex,
             flow2graph[] f2g, flow flow, Integer t,
             graph[] satisfyedGraph, List<Double> delaySet,
             Double maxDelay, Double oriDelay){
        List<Integer> tempPath = new ArrayList<>(oriPathSet.get(j));

        // 找出链路利用率最大的链路，记录其索引为maxUsageIndex
        Double maxUsage = 1.0;
        Integer maxUsageIndex = 0;
        maxUsageIndex = JitterControl.findMaxUsageIndex(oriPathSet, j, snapshotIndex + j, tempPath, f2g, maxUsage);
        maxUsage = JitterControl.computeUsageByIndex(snapshotIndex + j, tempPath, maxUsageIndex, f2g);

        // 找出链路利用率最大的链路的卫星的邻接，但是也包含了之前的链路利用率最大的链路，该卫星的邻居卫星nearNodes和邻接链路edges
        Set<DefaultWeightedEdge> nearNodes = new HashSet<>();
        List<Object> edges = new ArrayList<>();
        if (oriPathSet.get(j).size() == 0) {
//                                    nearNodes = null;
        } else {
            nearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf
                    ("sat" + String.valueOf(oriPathSet.get(j).get(maxUsageIndex + 1)));
            Iterator it = nearNodes.iterator();
            while (it.hasNext()) {
                edges.add(it.next());
            }
        }
        // 找出链路利用率最大的链路的卫星的邻接，通过如下操作，去除先前的利用率最大的链路
        List<Integer> otherNodes = new ArrayList<>();
        otherNodes = JitterControl.getOtherAdjNodes(oriPathSet, maxUsageIndex, nearNodes, edges, j);

        // 邻居卫星找新路
        if (otherNodes.size() == 0) {
            jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "没有邻居卫星");
            return false;
        }
//        Integer newNode = otherNodes.get(0);
        Random random = new Random();
        Integer newNode = otherNodes.get(random.nextInt(otherNodes.size()));
        Double newDelay = oriDelay;
        for (int k = 0; k < 12; k++) {
            Double tempDelay = 0.0;
            // 记录原来路径到最大利用节点的延迟和节点，起始节点到maxUsageIndex
            List<Integer> tempAgrPath = new ArrayList<>();
            for (int l = 0; l < maxUsageIndex; l++) {
                tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(oriPathSet.get(j).get(l), oriPathSet.get(j).get(l + 1));
//                                        System.out.println(tempDelay);
                tempAgrPath.add(oriPathSet.get(j).get(l));
            }
            if (maxUsageIndex + 1 >= oriPathSet.get(j).size()) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "12个节点算法" + k + "tempPath超限");
                break;
            }

            // 记录从maxUsageIndex到newNode的路径
            List<DefaultWeightedEdge> tempAddEdge;
            try {
                GraphPath midPath = satisfyedGraph[j].getPath(oriPathSet.get(j).get(maxUsageIndex), newNode);
                List<DefaultWeightedEdge> tempMidAddEdge = midPath.getEdgeList();
                for (int l = 0; l < tempMidAddEdge.size(); l++) {
                    String tempedge = tempMidAddEdge.get(l).toString();
                    String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                    Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        tempAddPathInt.add(sourceSatIndex);
                    tempAgrPath.add(sourceSatIndex);
                }

                // 记录时延改变的多少
                tempDelay = tempDelay + satisfyedGraph[j].getPathDelay(oriPathSet.get(j).get(maxUsageIndex), newNode);
                // 记录改变的路径的延迟，从newNode到targetNode
                GraphPath tempAddPath = satisfyedGraph[j].getPath(newNode, flow.getTargetSat().getId());
                tempDelay = tempDelay + tempAddPath.getWeight();
                // 记录改变的路径的节点
                tempAddEdge = tempAddPath.getEdgeList();
            } catch (Exception e) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "node" + k + "change Failed, no Path");
                continue;
            }

            // 记录改变的路径的延迟，从newNode到targetNode
            for (int l = 0; l < tempAddEdge.size(); l++) {
                String tempedge = tempAddEdge.get(l).toString();
                String sourceString = tempedge.substring(tempedge.indexOf("(sat") + 4, tempedge.indexOf(":") - 1);
                Integer sourceSatIndex = Integer.valueOf(sourceString);
//                                        tempAddPathInt.add(sourceSatIndex);
                tempAgrPath.add(sourceSatIndex);
                if (l == tempAddEdge.size() - 1) {
                    sourceString = tempedge.substring(tempedge.indexOf(":") + 5, tempedge.indexOf(")"));
                    sourceSatIndex = Integer.valueOf(sourceString);
//                                            tempAddPathInt.add(sourceSatIndex);
                    tempAgrPath.add(sourceSatIndex);
                }
            }

            tempDelay = tempDelay + queueingDelay(tempAgrPath, j ,flow.getStartTime(), f2g);

            jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次的路径是" + oriPathSet.get(j) + "时延是" + newDelay);
            jitterControlLog.info("最大时延是" + maxDelay + "改路后时延是" + tempDelay);
            //更改为新的路径，如果超过最大时延，break
            if ((tempDelay <= maxDelay) && (tempDelay >= newDelay)) {
                oriPathSet.remove((int) j);
                oriPathSet.add(j, tempAgrPath);
                newDelay = tempDelay;
                jitterControlLog.info("更改路径！第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + oriPathSet.get(j) + "新的时延是" + newDelay);
            }
            if (Math.abs(newDelay - maxDelay) < flow.getTargetJitter()) {
                jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + oriPathSet.get(j) + "新的时延是" + newDelay + "此路径抖动符合流需求");
                break;
            }
            if (tempDelay > maxDelay) {
                jitterControlLog.info("第" + flow.getId() + "条流" + "第" + j + "个快照下路径，第" + k + "次更改后的新路径是" + oriPathSet.get(j) + "新的时延是" + newDelay + "此路径新时延大于目标时延");
                break;
            }
            //寻找新节点的相邻节点
//            maxUsageIndex = JitterControl.findMaxUsageIndex(oriPathSet, j, snapshotIndex + j, tempPath, f2g, maxUsage);
//            maxUsage = JitterControl.computeUsageByIndex(snapshotIndex + j, tempPath, maxUsageIndex, f2g);
            maxUsageIndex = maxUsageIndex + 2;

            // 满足抖动要求后，终止寻找新节点
            if (maxUsageIndex + 1 >= oriPathSet.get(j).size()) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "12个节点算法" + k + "oriPathSet超限");
                break;
            }
            Set<DefaultWeightedEdge> nextNearNodes = new HashSet<>();
            List<Object> nextEdges = new ArrayList<>();
            nextNearNodes = satisfyedGraph[j].getDirectedweightedGraph().outgoingEdgesOf
                    ("sat" + String.valueOf(oriPathSet.get(j).get(maxUsageIndex + 1)));
            Iterator nextIt = nextNearNodes.iterator();
            while (nextIt.hasNext()) {
                nextEdges.add(nextIt.next());
            }
            otherNodes.clear();
            otherNodes = JitterControl.getOtherAdjNodes(oriPathSet, maxUsageIndex, nextNearNodes, nextEdges, j);
            if (otherNodes.size() == 0) {
                jitterControlLog.error("time" + t + "flow" + flow.getId() + "快照" + j + "没有邻居卫星");
                return false;
            }
            newNode = otherNodes.get(random.nextInt(otherNodes.size()));
        }
        pathSet.clear();
        pathSet.addAll(oriPathSet);
        delaySet.remove((int)j);
        delaySet.add(j, newDelay);
        return true;
    }

    public static Double queueingDelay(List<Integer> path, Integer numOfSnapshot, Integer startTime, flow2graph[] f2g){
        Double delay = 0.0;
        int startSnapshot = startTime / input.snapshotInterval.intValue();
        for (int i3 = 0; i3 < path.size() - 2; i3 = i3 + 2) {
            Integer thisNode = path.get(i3);
            Integer classNode = path.get(i3 + 1);
            Integer nextNode = path.get(i3 + 2);
            String thisClass = classNode - thisNode == f2g[startSnapshot + numOfSnapshot].getGraph().getSatNodes().length - 1 ? "A" : "B";

            Integer thislinkIndex = f2g[startSnapshot + numOfSnapshot].getGraph().getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
            link[] links = f2g[startSnapshot + numOfSnapshot].getGraph().getLinks();
            link thisLink = links[thislinkIndex];
            if (thisClass.equals("A")){
//                delay = delay + (thisLink.getUsedBuffer() + input.bFlow) / input.RijA + input.TijA;
                delay = delay + thisLink.getUsedBuffer() / input.RijA;
            }else {
//                delay = delay + (thisLink.getUsedBuffer() + input.bFlow) / input.RijB + input.TijB;
                delay = delay + thisLink.getUsedBuffer() / input.RijB;
            }

        }
        return delay;
    }
}

