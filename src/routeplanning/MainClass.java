package routeplanning;

import java.text.DecimalFormat;

/**
 * RoutePlanning.
 */
public class MainClass {
  
  /**
   * Main method.
   * @param args
   */
  public static void main(String[] args) {
    /*ReduceFileSize rfs = new ReduceFileSize(
        "E:/Documents/UNI/SS12/Efficient Route Planning/groupRepository/"
          + "src/routeplanning/resources/osm-sample.osm",
        "E:/Documents/UNI/SS12/Efficient Route Planning/groupRepository/"
          +  "src/routeplanning/resources/osm-sample_reduced.osm");
    rfs.process();*/
    RoadNetwork roadNet = new RoadNetwork();
    roadNet.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
      + "groupRepository/src/routeplanning/resources/osm-sample_reduced.osm");
    //System.out.println("ROAD NETWORK: " + roadNet.asString());
    
    //TODO
    //Instead of using roadNet using its biggest component
    
    //Exercise Sheet 2 - ex. 3
    int totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    
    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(roadNet);
    for (int i = 0; i < 100; i++) {
      long start = System.currentTimeMillis();
      Integer sourceNodeId = roadNet.getRandomNodeId();
      Integer targetNodeId = roadNet.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = roadNet.getRandomNodeId();
      }
      int cost = dijAlg.computeShortestPath(sourceNodeId, targetNodeId);
      List<Integer> settledNodeCosts = dijAlg.getVisitedNodes();
      for (int k = 0; k < settledNodeCosts.size(); k++) {
        if (settledNodeCosts.get(k) > 0) {
          totalSettledNodes++;
        }
      }
      
      System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId 
          + " TO NODE: " + targetNodeId + " :::: " + cost);
      totalCost = totalCost + cost;
      long end = System.currentTimeMillis();
      totalExecutionTime = totalExecutionTime + (end - start);
    }
    System.out.println("AVERAGE RUNNING TIME: " 
        + Double.valueOf(twoDForm.format((totalExecutionTime * 1000) / 100)));
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes / 100);
    System.out.println("AVERAGE SP. COST: " + totalCost / 100);
  }
}
