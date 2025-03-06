package com.pml.route.business.sr.model;

import com.pml.JitterApplication3;
import lombok.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.io.*;
import java.util.*;

import org.jgrapht.alg.shortestpath.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class graph {
    public Graph<String, DefaultWeightedEdge> directedweightedGraph =
            new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private Integer snapshotIndex;
    private Integer snapshotStartTime;
    private input input = new input();
    private satNode[] satNodes = new satNode[input.satOfOneOrbit * input.numOfOrbit + 1];


    public Integer getSatNodeByID(Integer id) {
        for (int i = 0; i < satNodes.length; i++) {
            if (satNodes[i].getId() != null) {
                if (satNodes[i].getId().equals(id))
//                    directedweightedGraph.getEdge();
                    return i;
            }
        }
        return -1;
    }

    public void deleteSatNodeByID(Integer id) {

    }

    private link[] links = new link[input.satOfOneOrbit * input.numOfOrbit * 8 + 1];

    public link[] getLinks() {
        return this.links;
    }

    public Integer getLinkByID(Integer id) {
        for (int i = 0; i < links.length; i++) {
            if (links[i].getId() != null) {
                if (links[i].getId().equals(id))
                    return i;
            }
        }
        return -1;
    }

    public graph(input input) throws IOException {
        this.input = input;
        this.snapshotIndex = this.input.defaultSnapshotIndex;
        initSatNode();
        initLinks();
        initDirectedGraph();
    }

    public graph(input Input, Integer snapshotIndex) throws IOException {
        this.input = Input;
        this.snapshotIndex = snapshotIndex;
        this.snapshotStartTime = snapshotIndex * Input.snapshotInterval.intValue();
        initSatNode();
        initLinks();
        initDirectedGraph();
    }

    public Double[] getDelay(Double wA, Double jA, Double wB, Double jB, input input) {
        jA = jA / 180.0 * Math.PI;
        jB = jB / 180.0 * Math.PI;
        wA = wA / 180.0 * Math.PI;
        wB = wB / 180.0 * Math.PI;

        Double tpx = Math.sqrt(2 *
                (Math.pow((input.rEarth + input.hSat), 2))
                * (1 - Math.cos(2 * Math.PI / input.satOfOneOrbit))) / input.opticalSpeed;
        Double tpy = 2 * (input.rEarth + input.hSat) * Math.asin(
                Math.sqrt(Math.pow(Math.sin((wA - wB) / 2), 2) + Math.cos(wA) * Math.cos(wB) *
                        Math.pow(Math.sin((jA - jB) / 2), 2))) / input.opticalSpeed;
        Double[] res = {tpx, tpy};
        return res;
    }

    public void initDirectedGraph() throws IOException {
        input input = this.input;
        File file = new File(input.filePath);
//        File file = new File(JitterApplication3.class.getClassLoader().getResource(input.filePath).getPath());
        Scanner sc = new Scanner(file);
        satNode[] satNodes = this.satNodes;
        link[] links = this.links;
        Double[][] txtdata = new Double[input.satOfOneOrbit * input.numOfOrbit + 1][9];
        DefaultWeightedEdge tempEdge = new DefaultWeightedEdge();

        // 添加节点
        Integer classAIndex = input.satOfOneOrbit * input.numOfOrbit;
        Integer classBIndex = 2 * input.satOfOneOrbit * input.numOfOrbit;
        for (int index = 1; index < 1 + input.satOfOneOrbit * input.numOfOrbit; index++) {
            this.directedweightedGraph.addVertex("sat" + index);
            this.directedweightedGraph.addVertex("sat" + (index + classAIndex));
            this.directedweightedGraph.addVertex("sat" + (index + classBIndex));
        }

        // 添加链路
        for (int index = 1; index < satNodes.length; index++) {
            Integer sourceId = satNodes[index].getId();
            tempEdge = this.directedweightedGraph.addEdge("sat" + sourceId, "sat" + (sourceId + classAIndex));
            directedweightedGraph.setEdgeWeight(tempEdge, 0);
            tempEdge = this.directedweightedGraph.addEdge("sat" + sourceId, "sat" + (sourceId + classBIndex));
            directedweightedGraph.setEdgeWeight(tempEdge, 0);
        }
        for (int index = 0; index < links.length; index++) {
            if (links[index].getRate() != null) {
                Integer sourceId = links[index].getSourceSatellite().getId();
                Integer destId = links[index].getDestSatellite().getId();
                if (links[index].getLinkClass() == "A") {
                    tempEdge = this.directedweightedGraph.addEdge("sat" + (sourceId + classAIndex), "sat" + destId);
                    directedweightedGraph.setEdgeWeight(tempEdge, links[index].getPropagationDelay());
                } else if (links[index].getLinkClass() == "B") {
                    tempEdge = this.directedweightedGraph.addEdge("sat" + (sourceId + classBIndex), "sat" + destId);
                    directedweightedGraph.setEdgeWeight(tempEdge, links[index].getPropagationDelay());
                }
            }
        }
    }


    // 在这里进行故障节点设置
    public graph initSatisfiedDirectedGraph(Double rate, Double burst) throws IOException {
        graph res = new graph(this.input);
        input input = this.input;
        Graph<String, DefaultWeightedEdge> newGraph =
                new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        File file = new File(input.filePath);
//        File file = new File(JitterApplication3.class.getClassLoader().getResource(input.filePath).getPath());
        Scanner sc = new Scanner(file);
        satNode[] satNodes = this.satNodes;
//        satNode[] newSatNodes = this.satNodes;
//        satNode[] satNodes = new satNode[newSatNodes.length - 5];
//        for (int i = 0, j = 0; i < newSatNodes.length; i++) {
//            // 设置故障节点
//            if ((i != 20) && (i != 30) &&(i != 40) &&(i != 50) &&(i != 60)) {
//                satNodes[j++] = newSatNodes[i];
//            }
//        }
        link[] links = this.links;
        Double[][] txtdata = new Double[input.satOfOneOrbit * input.numOfOrbit + 1][9];
        DefaultWeightedEdge tempEdge = new DefaultWeightedEdge();

        // 添加节点
        Integer classAIndex = input.satOfOneOrbit * input.numOfOrbit;
        Integer classBIndex = 2 * input.satOfOneOrbit * input.numOfOrbit;
//        for (satNode satNode : satNodes) {
//            newGraph.addVertex("sat" + satNode.getId());
//            newGraph.addVertex("sat" + (satNode.getId() + classAIndex));
//            newGraph.addVertex("sat" + (satNode.getId() + classBIndex));
//        }
        for (int index = 1; index < 1 + input.satOfOneOrbit * input.numOfOrbit; index++) {
            newGraph.addVertex("sat" + index);
            newGraph.addVertex("sat" + (index + classAIndex));
            newGraph.addVertex("sat" + (index + classBIndex));
        }

        // 添加链路


//        for (satNode satNode : satNodes) {
//            Integer sourceId = satNode.getId();
//            tempEdge = newGraph.addEdge("sat" + sourceId, "sat" + (sourceId + classAIndex));
//            newGraph.setEdgeWeight(tempEdge, 0);
//            tempEdge = newGraph.addEdge("sat" + sourceId, "sat" + (sourceId + classBIndex));
//            newGraph.setEdgeWeight(tempEdge, 0);
//        }
//        for (int index = 0; index < links.length; index++) {
//            if (links[index].getRate() != null) {
//                if (Arrays.asList(satNodes).contains(links[index].getSourceSatellite()) && Arrays.asList(satNodes).contains(links[index].getDestSatellite())){
//                    if (links[index].getAllocatedRate() > rate && links[index].getAllocatedBuffer() > burst) {
//                        Integer sourceId = links[index].getSourceSatellite().getId();
//                        Integer destId = links[index].getDestSatellite().getId();
//                        if (links[index].getLinkClass() == "A") {
//                            tempEdge = newGraph.addEdge("sat" + (sourceId + classAIndex), "sat" + destId);
//                            newGraph.setEdgeWeight(tempEdge, links[index].getPropagationDelay());
//                        } else if (links[index].getLinkClass() == "B") {
//                            tempEdge = newGraph.addEdge("sat" + (sourceId + classBIndex), "sat" + destId);
//                            newGraph.setEdgeWeight(tempEdge, links[index].getPropagationDelay());
//                        }
//                    }
//                }
//            }
//        }

//        原来的代码
        for (int index = 1; index < satNodes.length; index++) {
            Integer sourceId = satNodes[index].getId();
            tempEdge = newGraph.addEdge("sat" + sourceId, "sat" + (sourceId + classAIndex));
            newGraph.setEdgeWeight(tempEdge, 0);
            tempEdge = newGraph.addEdge("sat" + sourceId, "sat" + (sourceId + classBIndex));
            newGraph.setEdgeWeight(tempEdge, 0);
        }
        for (int index = 0; index < links.length; index++) {
            if (links[index].getRate() != null) {
                if (links[index].getAllocatedRate() > rate && links[index].getAllocatedBuffer() > burst) {
                    Integer sourceId = links[index].getSourceSatellite().getId();
                    Integer destId = links[index].getDestSatellite().getId();
                    if (links[index].getLinkClass() == "A") {
                        tempEdge = newGraph.addEdge("sat" + (sourceId + classAIndex), "sat" + destId);
                        newGraph.setEdgeWeight(tempEdge, links[index].getPropagationDelay());
                    } else if (links[index].getLinkClass() == "B") {
                        tempEdge = newGraph.addEdge("sat" + (sourceId + classBIndex), "sat" + destId);
                        newGraph.setEdgeWeight(tempEdge, links[index].getPropagationDelay());
                    }
                }
            }
        }
        res.setDirectedweightedGraph(newGraph);
        return res;
    }

    public GraphPath<String, DefaultWeightedEdge> getPath(Integer source, Integer des) {
        BellmanFordShortestPath<String, DefaultWeightedEdge> bellmanAlg =
                new BellmanFordShortestPath<>(this.directedweightedGraph);
        GraphPath<String, DefaultWeightedEdge> path = bellmanAlg.getPath("sat" + source, "sat" + des);
//        YenKShortestPath<String, DefaultWeightedEdge> YenKSP = new YenKShortestPath<>(this.directedweightedGraph);
//        java.util.List<org.jgrapht.GraphPath<String, DefaultWeightedEdge>> allPath = YenKSP.getPaths("sat" + source,"sat" + des,100);
//        for(int i =0;i<allPath.size();i++)
//        {
//            System.out.println(allPath.get(i).getWeight());
//        }
//        AllDirectedPaths<String, DefaultWeightedEdge> allPaths = new AllDirectedPaths<>(this.directedweightedGraph);
//        java.util.List<org.jgrapht.GraphPath<String, DefaultWeightedEdge>> allPath= allPaths.getAllPaths("sat" + source, "sat" + des,true,(int)(path.getLength()*1.1));
        return path;
    }

    public Double getPathDelay(Integer source, Integer des) {
        BellmanFordShortestPath<String, DefaultWeightedEdge> bellmanAlg =
                new BellmanFordShortestPath<>(this.directedweightedGraph);
        Double delay = bellmanAlg.getPathWeight("sat" + source, "sat" + des);
        return delay;
    }

    public void initSatNode() throws FileNotFoundException {
        for (int i = 0; i < this.satNodes.length; i++) {
            this.satNodes[i] = new satNode();
        }
        input input = this.input;
        Integer snapshotIndex = this.snapshotIndex;
        File file = new File(input.filePath);
//        System.out.println(graph.class.getClass().getResource(input.filePath).getPath());
//        System.out.println(JitterApplication3.class.getClass().getResource(input.filePath).getPath());
//        System.out.println(this.getClass().getResource(input.filePath).getPath());
//        File file = new File(JitterApplication3.class.getClass().getClassLoader().getResourceAsStream(input.filePath).toString());
        Scanner sc = new Scanner(file);
        Double[][] txtdata = new Double[input.satOfOneOrbit * input.numOfOrbit + 1][9];
        for (int index = 1; index < 1 + (snapshotIndex + 1) * input.satOfOneOrbit * input.numOfOrbit; index++) {
            if (sc.hasNextLine()) {
                String tempstring = sc.nextLine();
                String[] str = tempstring.toString().split("\\s+");
                if (index > snapshotIndex * input.satOfOneOrbit * input.numOfOrbit) {
                    for (int i = 0; i < 9; i++) {
                        txtdata[index - snapshotIndex * input.satOfOneOrbit * input.numOfOrbit][i] = Double.valueOf(str[i + 1]);
                    }
                }
            }
        }
        this.satNodes[0] = satNode.builder().id(0).sourceSatellite(null).longitude(0.0).latitude(0.0).groundStation(null).build();
        for (int index = 1; index < 1 + input.satOfOneOrbit * input.numOfOrbit; index++) {
            this.satNodes[index] = satNode.builder().id(index).sourceSatellite(null).longitude(txtdata[index][4]).latitude(txtdata[index][3]).groundStation(null).build();
        }
    }

    public void initLinks() throws FileNotFoundException {
        for (int i = 0; i < this.links.length; i++) {
            this.links[i] = new link();
        }
        input input = this.input;
        File file = new File(input.filePath);
//        File file = new File(JitterApplication3.class.getClassLoader().getResource(input.filePath).getPath());
        Scanner sc = new Scanner(file);
        Integer snapshotIndex = this.snapshotIndex;
        Double[][] txtdata = new Double[input.satOfOneOrbit * input.numOfOrbit + 1][9];
        for (int index = 1; index < 1 + (snapshotIndex + 1) * input.satOfOneOrbit * input.numOfOrbit; index++) {
            if (sc.hasNextLine()) {
                String tempstring = sc.nextLine();
                String[] str = tempstring.toString().split("\\s+");
                if (index > snapshotIndex * input.satOfOneOrbit * input.numOfOrbit) {
                    for (int i = 0; i < 9; i++) {
                        txtdata[index - snapshotIndex * input.satOfOneOrbit * input.numOfOrbit][i] = Double.valueOf(str[i + 1]);
                    }
                }
            }
        }
        for (int index = 1; index < 1 + input.satOfOneOrbit * input.numOfOrbit; index++) {
            // 与左侧节点的链路
            if ((index > input.satOfOneOrbit) && (txtdata[index][7] + txtdata[index][8] > 0)) {
                Double[] delay = getDelay(txtdata[index][3], txtdata[index][4], txtdata[index - input.satOfOneOrbit][3], txtdata[index - input.satOfOneOrbit][4], input);
                this.links[(index - 1) * 8 + 1] = new link((index - 1) * 8 + 1, satNodes[index],
                        satNodes[index - input.satOfOneOrbit], input.RijA, input.satBuffer, delay[1],
                        input.satBuffer / input.RijA + input.TijA,
                        "A", input.RijA, input.TijA, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
                this.links[(index - 1) * 8 + 2] = new link((index - 1) * 8 + 2, satNodes[index],
                        satNodes[index - input.satOfOneOrbit], input.RijB, input.satBuffer, delay[1],
                        input.satBuffer / input.RijB + input.TijB,
                        "B", input.RijB, input.TijB, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
            }
            // 与右侧节点的链路
            if ((index < (input.satOfOneOrbit * (input.numOfOrbit - 1) + 1)) && (txtdata[index][5] + txtdata[index][6] > 0)) {
                Double[] delay = getDelay(txtdata[index][3], txtdata[index][4], txtdata[index + input.satOfOneOrbit][3], txtdata[index + input.satOfOneOrbit][4], input);
                this.links[(index - 1) * 8 + 3] = new link((index - 1) * 8 + 3, satNodes[index],
                        satNodes[index + input.satOfOneOrbit], input.RijA, input.satBuffer, delay[1],
                        input.satBuffer / input.RijA + input.TijA,
                        "A", input.RijA, input.TijA, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
                this.links[(index - 1) * 8 + 4] = new link((index - 1) * 8 + 4, satNodes[index],
                        satNodes[index + input.satOfOneOrbit], input.RijB, input.satBuffer, delay[1],
                        input.satBuffer / input.RijB + input.TijB,
                        "B", input.RijB, input.TijB, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
            }
            // 与上下节点的链路
            Double[] delay = getDelay(txtdata[index][3], txtdata[index][4], txtdata[(int) (Math.floor((index - 1) / input.satOfOneOrbit) * input.satOfOneOrbit + (index - 1 + input.satOfOneOrbit - 1) % input.satOfOneOrbit + 1)][3], txtdata[(int) (Math.floor((index - 1) / input.satOfOneOrbit) * input.satOfOneOrbit + (index - 1 + input.satOfOneOrbit - 1) % input.satOfOneOrbit + 1)][4], input);
            this.links[(index - 1) * 8 + 5] = new link((index - 1) * 8 + 5, satNodes[index],
                    satNodes[(int) (Math.floor((index - 1) / input.satOfOneOrbit) * input.satOfOneOrbit + (index - 1 + input.satOfOneOrbit - 1) % input.satOfOneOrbit + 1)], input.RijA, input.satBuffer, delay[0],
                    input.satBuffer / input.RijA + input.TijA,
                    "A", input.RijA, input.TijA, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
            this.links[(index - 1) * 8 + 6] = new link((index - 1) * 8 + 6, satNodes[index],
                    satNodes[(int) (Math.floor((index - 1) / input.satOfOneOrbit) * input.satOfOneOrbit + (index - 1 + input.satOfOneOrbit - 1) % input.satOfOneOrbit + 1)], input.RijB, input.satBuffer, delay[0],
                    input.satBuffer / input.RijB + input.TijB,
                    "B", input.RijB, input.TijB, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);

            this.links[(index - 1) * 8 + 7] = new link((index - 1) * 8 + 7, satNodes[index],
                    satNodes[(int) (Math.floor((index - 1) / input.satOfOneOrbit) * input.satOfOneOrbit + (index % input.satOfOneOrbit) + 1)], input.RijA, input.satBuffer, delay[0],
                    input.satBuffer / input.RijA + input.TijA,
                    "A", input.RijA, input.TijA, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
            this.links[(index - 1) * 8 + 8] = new link((index - 1) * 8 + 8, satNodes[index],
                    satNodes[(int) (Math.floor((index - 1) / input.satOfOneOrbit) * input.satOfOneOrbit + (index % input.satOfOneOrbit) + 1)], input.RijB, input.satBuffer, delay[0],
                    input.satBuffer / input.RijB + input.TijB,
                    "B", input.RijB, input.TijB, input.maxPacketLength, input.RijA, input.TijA, input.RijB, input.TijB);
        }
    }

    public Integer getLinkIndexBySourceDes(Integer source, Integer dest, String Class) {
        for (int i = 0; i < this.links.length; i++) {
            if (this.links[i].getRate() != null) {
                Integer linkSource = this.links[i].getSourceSatellite().getId();
                Integer linkDest = this.links[i].getDestSatellite().getId();
                String linkClass = this.links[i].getLinkClass();
                if (linkSource.equals(source) && linkDest.equals(dest) && linkClass.equals(Class)) {
                    return i;
                }
            }
        }
//        System.out.println(source+"ha"+dest+"ha"+Class);
        return -1;

//        return 101;
    }

    public void graphTest() {
        GraphPath<String, DefaultWeightedEdge> path = getPath(1, 54);
        List<DefaultWeightedEdge> edges = path.getEdgeList();
        DefaultWeightedEdge tempedge = edges.get(0);
        System.out.println(tempedge.toString());

        String tempedge2 = tempedge.toString();
        String sourceString = tempedge2.substring(tempedge2.indexOf("(sat") + 4, tempedge2.indexOf(":") - 1);
        String desString = tempedge2.substring(tempedge2.indexOf(": sat") + 5, tempedge2.indexOf(")"));
        Integer sourceSatIndex = Integer.valueOf(sourceString);
        Integer desSatIndex = Integer.valueOf(desString);
        System.out.println(getPathDelay(1, 54));
    }

}
