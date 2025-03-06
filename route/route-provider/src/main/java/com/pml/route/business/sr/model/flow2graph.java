package com.pml.route.business.sr.model;

import io.swagger.models.auth.In;
import lombok.*;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class flow2graph {
    private flow[] flows = null;
    private graph graph = null;

    public flow2graph(flow[] flows, graph graph) {
        this.flows = flows;
        this.graph = graph;
    }

    private Integer[] parsePath(GraphPath<String, DefaultWeightedEdge> path) {
        List<DefaultWeightedEdge> edges = path.getEdgeList();
        Integer[] pathInt = new Integer[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            DefaultWeightedEdge tempedge = edges.get(i);
            String tempedge2 = tempedge.toString();
            String sourceString = tempedge2.substring(tempedge2.indexOf("(sat") + 4, tempedge2.indexOf(":") - 1);
            Integer sourceSatIndex = Integer.valueOf(sourceString);    //thisnode
            pathInt[i] = sourceSatIndex;
        }
        return pathInt;
    }

//    public void addFlowInGraph(flow flow, Integer snapShotIndex) {
//        Integer canTrans = 1;
//        Integer cost = 0;
//        Double tempBurst = flow.getBurst();
//        Double tempRate = flow.getRate();
////        if (flow.getPath() != null) {
//        if (flow.getJitterPathSet().get(snapShotIndex) != null) {
////            Integer[] pathInt = parsePath(flow.getPath());
//            Integer[] pathInt = new Integer[flow.getJitterPathSet().get(snapShotIndex).size()];
//            for (int i = 0; i < pathInt.length; i++){
//                pathInt[i] = flow.getJitterPathSet().get(snapShotIndex).get(i);
//            }
//            System.out.println("开始将流添加进网络中，流路径为:" + flow.getPath());
//            if (pathInt.length == 1) {
//                flow.setPathStatus("map Success");
//                // 路径只有一跳时，返回成功。
//            }
//            // 每次遍历路径上的两条边
//            for (int i = 0; i < pathInt.length - 2; i = i + 2) {
//                // 路径举例： 1, 226, 15，240， 30，255， 45，270
//                Integer priClassNode = 0;
//                Integer priNode = 0;
//                Integer priClassIndex = 0;
//                String priClass = "";
//                if (i == 0) {
//                    priClassNode = 0;
//                    priNode = 0;
//                    priClassIndex = 0;
//                    priClass = "in";
//                } else {
//                    priClassNode = pathInt[i - 1];
//                    priNode = priClassNode % (graph.getSatNodes().length - 1);
//                    priClassIndex = (graph.getSatNodes().length - 1) < priClassNode && priClassNode <= (graph.getSatNodes().length - 1) * 2 ? 1 : 2;
//                    priClass = priClassNode - priNode == (graph.getSatNodes().length - 1) ? "A" : "B";
//                }
//                Integer thisNode = pathInt[i];
//                Integer classNode = pathInt[i + 1];
//                Integer nextNode = pathInt[i + 2];
//                String thisClass = classNode - thisNode == (graph.getSatNodes().length - 1) ? "A" : "B";
//
//                Integer thislinkIndex = graph.getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
//                link[] links = graph.getLinks();
//                link thisLink = links[thislinkIndex];
//                satNode priSatNode = graph.getSatNodes()[graph.getSatNodeByID(priNode)];
////                System.out.println("添加进流之前" + thisLink.getUsedBuffer());
//                thisLink.addFlow(priNode, priClassIndex, tempRate, tempBurst, priSatNode, priClass);
////                System.out.println("添加进流之后" + thisLink.getUsedBuffer());
//                tempBurst = thisLink.getCbsServerWaitingTime() * tempRate + tempBurst;
////                System.out.println("看一下"+graph.getLinks()[thislinkIndex].getUsedBuffer());
//            }
//            flow.setPathStatus("map Success");
//        } else {
//            System.out.println("此流路径为空，流路径为:" + flow.getPath());
//            flow.setPathStatus("map Failed, no Path");
//        }
//    }

    public void addFlowInGraph(flow flow, Integer snapShotIndex) {
        Integer canTrans = 1;
        Integer cost = 0;
        Double tempBurst = flow.getBurst();
        Double tempRate = flow.getRate();
//        if (flow.getPath() != null) {
        if (flow.getJitterPathSet().get(snapShotIndex) != null) {
//            Integer[] pathInt = parsePath(flow.getPath());
            Integer[] pathInt = new Integer[flow.getJitterPathSet().get(snapShotIndex).size()];
            for (int i = 0; i < pathInt.length; i++) {
                pathInt[i] = flow.getJitterPathSet().get(snapShotIndex).get(i);
            }
            System.out.println("开始将快照" + snapShotIndex + "下的流" + flow.getId() + "添加进网络中，流路径为:" + flow.getJitterPathSet().get(snapShotIndex));
            if (pathInt.length == 1) {
                flow.setPathStatus("map Success");
                // 路径只有一跳时，返回成功。
            }
            // 每次遍历路径上的两条边
            for (int i = 0; i < pathInt.length - 2; i = i + 2) {
                // 路径举例： 1, 226, 15，240， 30，255， 45，270
                Integer priClassNode = 0;
                Integer priNode = 0;
                Integer priClassIndex = 0;
                String priClass = "";
                if (i == 0) {
                    priClassNode = 0;
                    priNode = 0;
                    priClassIndex = 0;
                    priClass = "in";
                } else {
                    priClassNode = pathInt[i - 1];
                    priNode = (priClassNode - 1) % (this.graph.getSatNodes().length - 1) + 1;
                    priClassIndex = (this.graph.getSatNodes().length - 1) < (priClassNode - 1) && (priClassNode - 1) <= (this.graph.getSatNodes().length - 1) * 2 ? 1 : 2;
                    priClass = priClassNode - priNode == this.graph.getSatNodes().length - 1 ? "A" : "B";
                }
                Integer thisNode = pathInt[i];
                Integer classNode = pathInt[i + 1];
                Integer nextNode = pathInt[i + 2];
                String thisClass = classNode - thisNode == this.graph.getSatNodes().length - 1 ? "A" : "B";

                Integer thislinkIndex = this.graph.getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
                link[] links = this.graph.getLinks();
                link thisLink = links[thislinkIndex];
                satNode priSatNode = this.graph.getSatNodes()[this.graph.getSatNodeByID(priNode)];
//                System.out.println("添加进流之前" + thisLink.getUsedBuffer());
                thisLink.addFlow(priNode, priClassIndex, tempRate, tempBurst, priSatNode, priClass);

//                System.out.println("添加进流之后" + thisLink.getUsedBuffer());
                tempBurst = thisLink.getCbsServerWaitingTime() * tempRate + tempBurst;
//                System.out.println("看一下"+graph.getLinks()[thislinkIndex].getUsedBuffer());
            }
            flow.setPathStatus("map Success");
        } else {
            System.out.println("此流路径为空，流路径为:" + flow.getJitterPathSet());
            flow.setPathStatus("map Failed, no Path");
        }
    }

    public Boolean addFlowInGraphByBuffer(flow flow, Integer snapShotIndex) {
        Integer canTrans = 1;
        Integer cost = 0;
        Double tempBurst = flow.getBurst();
        Double tempRate = flow.getRate();
//        if (flow.getPath() != null) {
        if (flow.getJitterPathSet().get(snapShotIndex) != null) {
//            Integer[] pathInt = parsePath(flow.getPath());
            Integer[] pathInt = new Integer[flow.getJitterPathSet().get(snapShotIndex).size()];
            for (int i = 0; i < pathInt.length; i++) {
                pathInt[i] = flow.getJitterPathSet().get(snapShotIndex).get(i);
            }
            System.out.println("开始将快照" + snapShotIndex + "下的流" + flow.getId() + "添加进网络中，流路径为:" + flow.getJitterPathSet().get(snapShotIndex));
            if (pathInt.length == 1) {
                flow.setPathStatus("map Success");
                // 路径只有一跳时，返回成功。
            }
            // 每次遍历路径上的两条边
            Boolean addOk = true;
            for (int i = 0; i < pathInt.length - 2; i = i + 2) {
                // 路径举例： 1, 226, 15，240， 30，255， 45，270
                Integer priClassNode = 0;
                Integer priNode = 0;
                Integer priClassIndex = 0;
                String priClass = "";
                if (i == 0) {
                    priClassNode = 0;
                    priNode = 0;
                    priClassIndex = 0;
                    priClass = "in";
                } else {
                    priClassNode = pathInt[i - 1];
                    priNode = (priClassNode - 1) % (this.graph.getSatNodes().length - 1) + 1;
                    priClassIndex = (this.graph.getSatNodes().length - 1) < (priClassNode - 1) && (priClassNode - 1) <= (this.graph.getSatNodes().length - 1) * 2 ? 1 : 2;
                    priClass = priClassNode - priNode == this.graph.getSatNodes().length - 1 ? "A" : "B";
                }
                Integer thisNode = pathInt[i];
                Integer classNode = pathInt[i + 1];
                Integer nextNode = pathInt[i + 2];
                String thisClass = classNode - thisNode == this.graph.getSatNodes().length - 1 ? "A" : "B";

                Integer thislinkIndex = this.graph.getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
                link[] links = this.graph.getLinks();
                link thisLink = links[thislinkIndex];
                satNode priSatNode = this.graph.getSatNodes()[this.graph.getSatNodeByID(priNode)];
//                System.out.println("添加进流之前" + thisLink.getUsedBuffer());
//                thisLink.addFlow(priNode, priClassIndex, tempRate, tempBurst, priSatNode, priClass);
                addOk = thisLink.addFlowByBuffer(priNode, priClassIndex, tempRate, tempBurst, priSatNode, priClass);
                if (addOk == false) {
                    tempBurst = flow.getBurst();
                    for (int j = 0; j <= i; j = j + 2) {
                        // 路径举例： 1, 226, 15，240， 30，255， 45，270
                        Integer tempPriClassNode = 0;
                        Integer tempPriNode = 0;
                        Integer tempPriClassIndex = 0;
                        String tempPriClass = "";
                        if (j == 0) {
                            tempPriClassNode = 0;
                            tempPriNode = 0;
                            tempPriClassIndex = 0;
                            tempPriClass = "in";
                        } else {
                            tempPriClassNode = pathInt[j - 1];
                            tempPriNode = (tempPriClassNode - 1) % (graph.getSatNodes().length - 1) + 1;
                            tempPriClassIndex = (graph.getSatNodes().length - 1) < (tempPriClassNode - 1) && (tempPriClassNode - 1) <= (graph.getSatNodes().length - 1) * 2 ? 1 : 2;
                            tempPriClass = tempPriClassNode - tempPriNode == graph.getSatNodes().length - 1 ? "A" : "B";
                        }
                        Integer tempThisNode = pathInt[j];
                        Integer tempClassNode = pathInt[j + 1];
                        Integer tempNextNode = pathInt[j + 2];
                        String tempThisClass = tempClassNode - tempThisNode == graph.getSatNodes().length - 1 ? "A" : "B";

                        Integer tempThislinkIndex = graph.getLinkIndexBySourceDes(tempThisNode, tempNextNode, tempThisClass);
                        link[] tempLinks = graph.getLinks();
                        link tempThisLink = tempLinks[tempThislinkIndex];
                        satNode tempPriSatNode = graph.getSatNodes()[graph.getSatNodeByID(tempPriNode)];

                        tempThisLink.remFlow(tempPriNode, tempPriClassIndex, tempRate, tempBurst, tempPriSatNode, tempPriClass);

                        tempBurst = tempThisLink.getCbsServerWaitingTime() * tempRate + tempBurst;
                    }
                    return false;
                }

//                System.out.println("添加进流之后" + thisLink.getUsedBuffer());
                tempBurst = thisLink.getCbsServerWaitingTime() * tempRate + tempBurst;
//                System.out.println("看一下"+graph.getLinks()[thislinkIndex].getUsedBuffer());
            }
            if (!(flow.getPathStatus().equals("delay degradation success") || flow.getPathStatus().equals("jitter degradation success"))){
                flow.setPathStatus("map Success");
            }
        } else {
            System.out.println("此流路径为空，流路径为:" + flow.getJitterPathSet());
            flow.setPathStatus("map Failed, no Path");
        }
        return true;
    }

//    public boolean checkFlowInGraph(flow flow) {
//        boolean canTrans = true;
//        Integer cost = 0;
//        Double tempBurst = flow.getBurst();
//        Double tempRate = flow.getRate();
//        this.addFlowInGraph(flow);
//        if (flow.getPath() != null) {
//            Integer[] pathInt = parsePath(flow.getPath());
//            if (pathInt.length == 1) {
//                // 路径只有一跳时，返回成功。
//            }
//            // 每次遍历路径上的两条边,遍历使用的缓存or使用的带宽是否大于预设的
//            for (int i = 0; i < pathInt.length - 2; i = i + 2) {
//                Integer priClassNode = 0;
//                Integer priNode = 0;
//                Integer priClassIndex = 0;
//                String priClass = "";
//                if (i == 0) {
//                    priClassNode = 0;
//                    priNode = 0;
//                    priClassIndex = 0;
//                    priClass = "in";
//                } else {
//                    priClassNode = pathInt[i - 1];
//                    priNode = priClassNode % (graph.getSatNodes().length - 1);
//                    priClassIndex = (graph.getSatNodes().length - 1) < priClassNode && priClassNode <= (graph.getSatNodes().length - 1) * 2 ? 1 : 2;
//                    priClass = priClassIndex - priNode == (graph.getSatNodes().length - 1) ? "A" : "B";
//                }
//                Integer thisNode = pathInt[i];
//                Integer classNode = pathInt[i + 1];
//                Integer nextNode = pathInt[i + 2];
//                String thisClass = classNode - thisNode == (graph.getSatNodes().length - 1) ? "A" : "B";
//
//                Integer thislinkIndex = graph.getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
//                link[] links = graph.getLinks();
//                link thisLink = links[thislinkIndex];
//                if (thisLink.getUsedBuffer() > thisLink.getBuffer() || thisLink.getUsedRate() > thisLink.getRate()) {
//                    canTrans = false;
//                }
//            }
//        }
//        this.remFlowInGraph(flow);
//        return canTrans;
//    }

    public void remFlowInGraph(flow flow, Integer snapShotIndex) {
        Integer canTrans = 1;
        Integer cost = 0;
        Double tempBurst = flow.getBurst();
        Double tempRate = flow.getRate();

//        if (flow.getPath() != null) {
        if (flow.getJitterPathSet().get(snapShotIndex) != null) {
//            Integer[] pathInt = parsePath(flow.getPath());
            Integer[] pathInt = new Integer[flow.getJitterPathSet().get(snapShotIndex).size()];
            for (int i = 0; i < pathInt.length; i++) {
                pathInt[i] = flow.getJitterPathSet().get(snapShotIndex).get(i);
            }
            if (pathInt.length == 1) {
                flow.setPathStatus("rem Success");
                // 路径只有一跳时，返回成功。
            }
            // 每次遍历路径上的两条边
            for (int i = 0; i < pathInt.length - 2; i = i + 2) {
                // 路径举例： 1, 226, 15，240， 30，255， 45，270
                Integer priClassNode = 0;
                Integer priNode = 0;
                Integer priClassIndex = 0;
                String priClass = "";
                if (i == 0) {
                    priClassNode = 0;
                    priNode = 0;
                    priClassIndex = 0;
                    priClass = "in";
                } else {
                    priClassNode = pathInt[i - 1];
                    priNode = (priClassNode - 1) % (graph.getSatNodes().length - 1) + 1;
                    priClassIndex = (graph.getSatNodes().length - 1) < (priClassNode - 1) && (priClassNode - 1) <= (graph.getSatNodes().length - 1) * 2 ? 1 : 2;
                    priClass = priClassIndex - priNode == graph.getSatNodes().length - 1 ? "A" : "B";
                }
                Integer thisNode = pathInt[i];
                Integer classNode = pathInt[i + 1];
                Integer nextNode = pathInt[i + 2];
                String thisClass = classNode - thisNode == graph.getSatNodes().length - 1 ? "A" : "B";

                Integer thislinkIndex = graph.getLinkIndexBySourceDes(thisNode, nextNode, thisClass);
                link[] links = graph.getLinks();
                link thisLink = links[thislinkIndex];
                satNode priSatNode = graph.getSatNodes()[graph.getSatNodeByID(priNode)];

                thisLink.remFlow(priNode, priClassIndex, tempRate, tempBurst, priSatNode, priClass);

                tempBurst = thisLink.getCbsServerWaitingTime() * tempRate + tempBurst;
            }
            if (!(flow.getPathStatus().equals("delay degradation success") || flow.getPathStatus().equals("jitter degradation success"))){
                flow.setPathStatus("rem Success");
            }
        }
    }

    public void output() {
        Integer remSucc = 0;
        Integer mapSucc = 0;
        Integer mapFailed = 0;
        Integer toBeMap = 0;
        Integer delayDegradation = 0;
        Integer jitterDegradation = 0;
        for (int i = 0; i < this.flows.length; i++) {
            switch (this.flows[i].getPathStatus()) {
                case "to be map":
                    toBeMap++;
                    break;
                case "map Success":
                    mapSucc++;
                    break;
                case "rem Success":
                    remSucc++;
                    break;
                case "map Failed, no Path":
                    mapFailed++;
                    break;
                case "delay degradation success":
                    delayDegradation++;
                    break;
                case "jitter degradation success":
                    jitterDegradation++;
                    break;
            }
        }
        System.out.println("总共流数:" + flows.length + "成功映射:" + mapSucc + " 抖动降级" + jitterDegradation  + " 时延降级" + delayDegradation + "失败映射:" + mapFailed + " 待映射:" + toBeMap + " 映射完成且成功移除:" + remSucc);
    }
}
