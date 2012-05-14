package routeplanning;


import java.text.DecimalFormat;
import java.util.List;

/**
 * RoutePlanning.
 * @author CJC | AAA
 */
public class MainClass {

  /**
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
     //ReduceFileSize rfs = new
     //ReduceFileSize("D:/workspace/routeplanning/src/routeplanning/resources/saarland.osm",
     //"D:/workspace/routeplanning/src/routeplanning/resources/test2_reduced.osm");
     //rfs.process();
    RoadNetwork rn2 = new RoadNetwork();
    rn2.readFromOsmFile("D:/workspace/routeplanning/src/routeplanning/resources/saarland_reduced.osm");
    //System.out.println("ROAD NETWORK: " + rn.asString());
    DijkstraAlgorithm2 dij2 = new DijkstraAlgorithm2(rn2);
    //System.out.println(dij2.computeShortestPath(1, 2));
    System.out.println(dij2.computeShortestPath(385925420, 259000790));
    //System.out.println(dij.computeShortestPath(338820305, 835615364));
    //System.out.println(dij2.computeShortestPath(835615364, 835662040));
    //System.out.println(rn.nodes);
	    //MainClass.try100Dijkstras(roadNet);
    //RoadNetwork lc;
    //lc = rn.reduceToLargestConnectedComponent();
    //System.out.println("LCC: " + lc.asString());
    RoadNetwork rn = new RoadNetwork();
    
    Node node0 = new Node(0, 1.0, 1.0);
    Node node1 = new Node(1, 1.0, 1.0);
    Node node2 = new Node(2, 1.0, 1.0);
    Node node3 = new Node(3, 1.0, 1.0);
    Node node4 = new Node(4, 1.0, 1.0);
    Node node5 = new Node(5, 1.0, 1.0);
    
    //Arc newArc00 = new Arc(node0, 0);
    Arc newArc01 = new Arc(node1, 1);
    Arc newArc02 = new Arc(node2, 4);
    Arc newArc03 = new Arc(node3, 3);
    Arc newArc04 = new Arc(node4, 10);
    
    //Arc newArc11 = new Arc(node1, 0);
    Arc newArc10 = new Arc(node0, 1);
    Arc newArc13 = new Arc(node3, 1);
    
    //Arc newArc22 = new Arc(node2, 0);
    Arc newArc20 = new Arc(node0, 4);
    Arc newArc23 = new Arc(node3, 5);
    Arc newArc25 = new Arc(node5, 3);
    
    //Arc newArc33 = new Arc(node3, 0);
    Arc newArc30 = new Arc(node0, 3);
    Arc newArc31 = new Arc(node1, 1);
    Arc newArc32 = new Arc(node2, 5);
    Arc newArc34 = new Arc(node4, 1);
    
    //Arc newArc44 = new Arc(node4, 0);
    Arc newArc40 = new Arc(node0, 10);
    Arc newArc43 = new Arc(node3, 1);
    Arc newArc45 = new Arc(node5, 1);
    
    //Arc newArc55 = new Arc(node5, 0);
    Arc newArc52 = new Arc(node2, 3);
    Arc newArc54 = new Arc(node4, 1);
    
    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(node2);
    rn.addNodeToGraph(node3);
    rn.addNodeToGraph(node4);
    rn.addNodeToGraph(node5);
    
    //rn.addAdjacentArc(node0, newArc00);
    rn.addAdjacentArc(node0, newArc01);
    rn.addAdjacentArc(node0, newArc02);
    rn.addAdjacentArc(node0, newArc03);
    rn.addAdjacentArc(node0, newArc04);
    
    //rn.addAdjacentArc(node1, newArc11);
    rn.addAdjacentArc(node1, newArc10);
    rn.addAdjacentArc(node1, newArc13);

    //rn.addAdjacentArc(node2, newArc22);
    rn.addAdjacentArc(node2, newArc20);
    rn.addAdjacentArc(node2, newArc23);
    rn.addAdjacentArc(node2, newArc25);
    
    //rn.addAdjacentArc(node3, newArc33);
    rn.addAdjacentArc(node3, newArc30);
    rn.addAdjacentArc(node3, newArc31);
    rn.addAdjacentArc(node3, newArc32);
    rn.addAdjacentArc(node3, newArc34);

    //rn.addAdjacentArc(node4, newArc44);
    rn.addAdjacentArc(node4, newArc40);
    rn.addAdjacentArc(node4, newArc43);
    rn.addAdjacentArc(node4, newArc45);

    //rn.addAdjacentArc(node5, newArc55);   
    rn.addAdjacentArc(node5, newArc52);       
    rn.addAdjacentArc(node5, newArc54);  

    //DijkstraAlgorithm2 dijx = new DijkstraAlgorithm2(rn);
    //int cost = dijx.computeShortestPath(0, 5);
    
  }

  /**
   * As requested in Exercise Sheet 2 - ex. 3.
   * @param network the original road network extracted from the osm file.
   */
  public static void tryDijkstras(RoadNetwork network, int numberOfExecutions) {
    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = 
      network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component..."); 
    
    System.out.println("1. NUMBER OF NODES OF LCC: "
        +  largestComponent.getNodeIds().size());
    
    System.out.println("2. NUMBER OF ARCS OF LCC: " 
        + largestComponent.getNumberOfArcs());
    
    //Exercise Sheet 2 - ex. 3
    Integer totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    
    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(largestComponent);
    
    for (int i = 0; i < numberOfExecutions; i++) {

      Integer sourceNodeId = largestComponent.getRandomNodeId();
      Integer targetNodeId = largestComponent.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = largestComponent.getRandomNodeId();
      }
      System.out.println("CALCULATING shortest path from Node " 
          + sourceNodeId + " TO Node " + targetNodeId);
      DijkstraAlgorithm newDijAlg = new DijkstraAlgorithm(largestComponent);
      long start = System.currentTimeMillis();
      Integer cost = newDijAlg.computeShortestPath(sourceNodeId, targetNodeId);
      long end = System.currentTimeMillis();      
      List<Integer> settledNodeCosts = newDijAlg.getVisitedNodes();
      for (int k = 0; k < settledNodeCosts.size(); k++) {
        if (settledNodeCosts.get(k) != null) {
          if (settledNodeCosts.get(k) > 0) {
            totalSettledNodes++;
          }
        }
      }
      totalSettledNodes = totalSettledNodes + 1;
      totalCost = totalCost + cost;
      totalExecutionTime = totalExecutionTime + (end - start);
      
      System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId 
          + " TO NODE: " + targetNodeId + " :::: " + cost + " seconds");

    }
    
    System.out.println("AVERAGE RUNNING TIME: " 
        + Double.valueOf(twoDForm.format((totalExecutionTime / 1000) 
            / numberOfExecutions)) + " seconds");
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes 
        / numberOfExecutions);
    System.out.println("AVERAGE SP. COST: " + totalCost / numberOfExecutions);
  }

  /**
   * Main method.
   * @param args
   */
  public static void main(String[] args) {
    //If one wants to reduce the file size.
    ReduceFileSize rfs = new ReduceFileSize(
        "E:/Documents/UNI/SS12/Efficient Route Planning/groupRepository/"
          + "src/routeplanning/resources/saarland.osm",
        "E:/Documents/UNI/SS12/Efficient Route Planning/groupRepository/"
          +  "src/routeplanning/resources/saarland_reduced.osm");
    rfs.process();
    RoadNetwork roadNet = new RoadNetwork();
    roadNet.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
      + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
    
    MainClass.tryDijkstras(roadNet, 100);
  }
}
