package routeplanning;

import java.text.DecimalFormat;
import java.util.List;

/**
 * RoutePlanning.
 * @author CJC | AAA
 */
public class MainClass {
  /**
   * As requested in Exercise Sheet 2 - ex. 3.
   * @param network the original road network extracted from the osm file.
   */
  public static void try100Dijkstras(RoadNetwork network) {
    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = 
      network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component..."); 
    
    //Exercise Sheet 2 - ex. 3
    Integer totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    int lccArcs = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    
    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(largestComponent);
    for (int i = 0; i < 10; i++) {
      long start = System.currentTimeMillis();
      Integer sourceNodeId = largestComponent.getRandomNodeId();
      Integer targetNodeId = largestComponent.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = largestComponent.getRandomNodeId();
      }
      Integer cost = dijAlg.computeShortestPath(sourceNodeId, targetNodeId);
      List<Integer> settledNodeCosts = dijAlg.getVisitedNodes();
      for (int k = 0; k < settledNodeCosts.size(); k++) {
        if (settledNodeCosts.get(k) != null) {
          if (settledNodeCosts.get(k) > 0) {
            totalSettledNodes++;
          }
        }
      }
      totalSettledNodes = totalSettledNodes + 1;
      totalCost = totalCost + cost;
      long end = System.currentTimeMillis();
      totalExecutionTime = totalExecutionTime + (end - start);
      
      System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId 
          + " TO NODE: " + targetNodeId + " :::: " + cost);

    }
    System.out.println("1. NUMBER OF NODES OF LCC: "
      +  largestComponent.getNodeIds().size());
    
    List<List<Arc>> adjacentArcs = largestComponent.getAdjacentArcs();
    for (int k = 0; k < adjacentArcs.size(); k++) {
      List<Arc> arcs = adjacentArcs.get(k);
      lccArcs = lccArcs + arcs.size();
    }
    
    System.out.println("2. NUMBER OF ARCS OF LCC: " + (lccArcs / 2) / 100);
    
    
    System.out.println("AVERAGE RUNNING TIME: " 
        + Double.valueOf(twoDForm.format((totalExecutionTime * 1000) / 100)));
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes / 100);
    System.out.println("AVERAGE SP. COST: " + totalCost / 100);
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
    
    MainClass.try100Dijkstras(roadNet);
  }
}
