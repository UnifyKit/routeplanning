package routeplanning;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Main class for transportation network.
 */
public class NewMainClass {
  
  /**
   * As requested in Exercise Sheet 9 - ex. 2.
   * 
   * @param network
   *          the original road network extracted from the osm file.
   */
  public static void tryDijkstrasOnTransportationNet(
      TransportationNetwork network, int numberOfExecutions) {
//    System.out.println("Start from Largest Connected Component...");
//    TransportationNetwork largestComponent 
//      = network.reduceToLargestConnectedComponent();
//    System.out.println("End from Largest Connected Component...");

    System.out.println("1. NUMBER OF NODES OF LCC: "
        + network.getNodeIds().size());

    System.out.println("2. NUMBER OF ARCS OF LCC: "
        + network.getNumberOfArcs());

    Integer totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(network);

    for (int i = 0; i < numberOfExecutions; i++) {
      System.out.println("------------------------------------------------");
      Long sourceNodeId = network.getRandomNodeId();
      Long targetNodeId = network.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = network.getRandomNodeId();
      }
      // System.out.println("CALCULATING shortest path from Node "
      // + sourceNodeId + " TO Node " + targetNodeId);
      DijkstraAlgorithm newDijAlg = new DijkstraAlgorithm(network);
      long start = System.currentTimeMillis();
      Integer cost = newDijAlg.computeShortestPath(sourceNodeId, targetNodeId);
      long end = System.currentTimeMillis();
      Map<Long, Integer> settledNodeCosts = newDijAlg.getVisitedNodes();
      totalSettledNodes = totalSettledNodes + settledNodeCosts.size();
      totalCost = totalCost + cost;
      totalExecutionTime = totalExecutionTime + (end - start);

      System.out.println("SHORTEST PATH FROM NODE: " 
         + sourceNodeId
         + " TO NODE: " + targetNodeId + " :::: " + cost + " seconds");
      System.out.println("------------------------------------------------");
    }

    System.out.println("AVERAGE RUNNING TIME: "
        + Double.valueOf(twoDForm.format((totalExecutionTime)
            / numberOfExecutions)) + " milliseconds");
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes
        / numberOfExecutions);
    System.out.println("AVERAGE SP. COST: " + totalCost / numberOfExecutions);
  }

  /**
   * Main method.
   */
  public static void main(String[] args) {
    TransportationNetwork tNet = new TransportationNetwork();
    tNet.readFromGtfsFiles("E:/Documents/UNI/SS12/Efficient Route Planning/"
        + "groupRepository/src/routeplanning/resources", false);
    System.out.println("****************************"
        + "*****************************");
    NewMainClass.tryDijkstrasOnTransportationNet(tNet, 1000);
    System.out.println("****************************"
        + "*****************************");
  }

}
