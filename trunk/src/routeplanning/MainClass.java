package routeplanning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * RoutePlanning.
 * 
 * @author CJC | AAA
 */
public class MainClass {

  /**
   * @param args
   *          the command line arguments
   */
  // public static void main(String[] args) {
  // //ReduceFileSize rfs = new
  // //ReduceFileSize("D:/workspace/routeplanning/
  // src/routeplanning/resources/saarland.osm",
  // //"D:/workspace/routeplanning/src/routeplanning/
  // resources/test2_reduced.osm");
  // //rfs.process();
  // RoadNetwork rn2 = new RoadNetwork();
  // rn2.readFromOsmFile("D:/workspace/routeplanning/
  // src/routeplanning/resources/saarland_reduced.osm");
  //
  // //tryDijkstrasWithSimpleHeuristic(rn2,100);
  // //System.out.println("ROAD NETWORK: " + rn.asString());
  //
  // //System.out.println(dij2.computeShortestPath(0, 4));
  //
  // //System.out.println("heuristic: " + heuristic
  // .get(rn2.getNodeIds().indexOf(385925420)) + " for node: 385925420");
  // //System.out.println(heuristic.toString());
  // //dij2.setHeuristic(heuristic);
  //
  // //System.out.println(dij2.computeShortestPath(835615364, -1));
  //
  // //System.out.println(dij2.computeShortestPath(338820305, 835615364));
  // //System.out.println(dij2.computeShortestPath(835615364, 835662040));
  //
  // DijkstraAlgorithm dij2 = new DijkstraAlgorithm(rn2);
  //
  // System.out.println("A* without heuristic SP:"
  // + dij2.computeShortestPath(385925420, 259000790));
  // System.out.println("A* without heuristic nodes settled: "
  // + dij2.getVisitedNodes().size());
  //
  // List<Integer> heuristic;
  // heuristic = rn2.computeStraightLineHeuristic(259000790);
  // dij2.setHeuristic(heuristic);
  //
  // System.out.println("A* SL SP:" + dij2
  // .computeShortestPath(385925420, 259000790));
  // System.out.println("A* SL nodes settled: "
  // + dij2.getVisitedNodes().size());
  //
  // LandmarkAlgorithm lm = new LandmarkAlgorithm(rn2);
  // lm.selectLandmarks(16);
  // System.out.println("A* Landmark SP:"
  // + lm.computeShortestPath(385925420, 259000790));
  //
  // //System.out.println(lm.heuristic.toString());
  // System.out.println("A* Landmark nodes settled:"
  // + lm.getVisitedNodes().size());
  // //System.out.println(rn.nodes);
  // MainClass.tryDijkstras(rn2,10);
  // //RoadNetwork lc;
  // //lc = rn2.reduceToLargestConnectedComponent();
  // //System.out.println("LCC: " + lc.asString());
  // RoadNetwork rn = new RoadNetwork();
  //
  // Node node0 = new Node(0, 1.0, 1.0);
  // Node node1 = new Node(1, 1.0, 1.0);
  // Node node2 = new Node(2, 1.0, 1.0);
  // Node node3 = new Node(3, 1.0, 1.0);
  // Node node4 = new Node(4, 1.0, 1.0);
  // Node node5 = new Node(5, 1.0, 1.0);
  //
  // //Arc newArc00 = new Arc(node0, 0);
  // Arc newArc01 = new Arc(node1, 1);
  // Arc newArc02 = new Arc(node2, 4);
  // Arc newArc03 = new Arc(node3, 3);
  // Arc newArc04 = new Arc(node4, 10);
  //
  // //Arc newArc11 = new Arc(node1, 0);
  // Arc newArc10 = new Arc(node0, 1);
  // Arc newArc13 = new Arc(node3, 1);
  //
  // //Arc newArc22 = new Arc(node2, 0);
  // Arc newArc20 = new Arc(node0, 4);
  // Arc newArc23 = new Arc(node3, 5);
  // Arc newArc25 = new Arc(node5, 3);
  //
  // //Arc newArc33 = new Arc(node3, 0);
  // Arc newArc30 = new Arc(node0, 3);
  // Arc newArc31 = new Arc(node1, 1);
  // Arc newArc32 = new Arc(node2, 5);
  // Arc newArc34 = new Arc(node4, 1);
  //
  // //Arc newArc44 = new Arc(node4, 0);
  // Arc newArc40 = new Arc(node0, 10);
  // Arc newArc43 = new Arc(node3, 1);
  // Arc newArc45 = new Arc(node5, 1);
  //
  // //Arc newArc55 = new Arc(node5, 0);
  // Arc newArc52 = new Arc(node2, 3);
  // Arc newArc54 = new Arc(node4, 1);
  //
  // rn.addNodeToGraph(node0);
  // rn.addNodeToGraph(node1);
  // rn.addNodeToGraph(node2);
  // rn.addNodeToGraph(node3);
  // rn.addNodeToGraph(node4);
  // rn.addNodeToGraph(node5);
  //
  // //rn.addAdjacentArc(node0, newArc00);
  // rn.addAdjacentArc(node0, newArc01);
  // rn.addAdjacentArc(node0, newArc02);
  // rn.addAdjacentArc(node0, newArc03);
  // rn.addAdjacentArc(node0, newArc04);
  //
  // //rn.addAdjacentArc(node1, newArc11);
  // rn.addAdjacentArc(node1, newArc10);
  // rn.addAdjacentArc(node1, newArc13);
  // }

  /**
   * As requested in Exercise Sheet 2 - ex. 3.
   * 
   * @param network
   *          the original road network extracted from the osm file.
   */
  public static void tryDijkstras(RoadNetwork network, int numberOfExecutions) {
    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component...");

    System.out.println("1. NUMBER OF NODES OF LCC: "
        + largestComponent.getNodeIds().size());

    System.out.println("2. NUMBER OF ARCS OF LCC: "
        + largestComponent.getNumberOfArcs());

    // Exercise Sheet 2 - ex. 3
    Integer totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(largestComponent);

    for (int i = 0; i < numberOfExecutions; i++) {
      // System.out.println("------------------------------------------------");
      Integer sourceNodeId = largestComponent.getRandomNodeId();
      Integer targetNodeId = largestComponent.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = largestComponent.getRandomNodeId();
      }
      // System.out.println("CALCULATING shortest path from Node "
      // + sourceNodeId + " TO Node " + targetNodeId);
      DijkstraAlgorithm newDijAlg = new DijkstraAlgorithm(largestComponent);
      long start = System.currentTimeMillis();
      Integer cost = newDijAlg.computeShortestPath(sourceNodeId, targetNodeId);
      long end = System.currentTimeMillis();
      Map<Integer, Integer> settledNodeCosts = newDijAlg.getVisitedNodes();
      totalSettledNodes = totalSettledNodes + settledNodeCosts.size();
      totalCost = totalCost + cost;
      totalExecutionTime = totalExecutionTime + (end - start);

      // System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId
      // + " TO NODE: " + targetNodeId + " :::: " + cost + " seconds");
      // System.out.println("------------------------------------------------");
    }

    System.out.println("AVERAGE RUNNING TIME: "
        + Double.valueOf(twoDForm.format((totalExecutionTime / 1000)
            / numberOfExecutions)) + " seconds");
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes
        / numberOfExecutions);
    System.out.println("AVERAGE SP. COST: " + totalCost / numberOfExecutions);
  }

  /**
   * As requested in Exercise Sheet 3 - ex. 3.
   * 
   * @param network
   *          the original road network extracted from the osm file.
   */
  public static void tryDijkstrasWithSimpleHeuristic(RoadNetwork network,
      int numberOfExecutions) {
    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component...");

    System.out.println("1. NUMBER OF NODES OF LCC: "
        + largestComponent.getNodeIds().size());

    System.out.println("2. NUMBER OF ARCS OF LCC: "
        + largestComponent.getNumberOfArcs());

    Integer totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(largestComponent);

    for (int i = 0; i < numberOfExecutions; i++) {
      // System.out.println("------------------------------------------------");
      Integer sourceNodeId = largestComponent.getRandomNodeId();
      Integer targetNodeId = largestComponent.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = largestComponent.getRandomNodeId();
      }
      // System.out.println("CALCULATING shortest path from Node "
      // + sourceNodeId + " TO Node " + targetNodeId);
      long start = System.currentTimeMillis();
      List<Integer> heuristic;
      heuristic = largestComponent.computeStraightLineHeuristic(targetNodeId);
      dijAlg.setHeuristic(heuristic);
      Integer cost = dijAlg.computeShortestPath(sourceNodeId, targetNodeId);
      long end = System.currentTimeMillis();
      Map<Integer, Integer> settledNodeCosts = dijAlg.getVisitedNodes();
      totalSettledNodes = totalSettledNodes + settledNodeCosts.size();
      totalCost = totalCost + cost;
      totalExecutionTime = totalExecutionTime + (end - start);

      // System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId
      // + " TO NODE: " + targetNodeId + " :::: " + cost + " seconds");

      // System.out.println("------------------------------------------------");
    }
    System.out.println("STRAIGHT LINE HEURISTIC");
    System.out.println("AVERAGE RUNNING TIME: "
        + Double.valueOf(twoDForm.format((totalExecutionTime)
            / numberOfExecutions)) + " milliseconds");
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes
        / numberOfExecutions);
    System.out.println("AVERAGE SP. COST: " + totalCost / numberOfExecutions);
  }

  /**
   * As requested in Exercise Sheet 3 - ex. 3.
   * 
   * @param network
   *          the original road network extracted from the osm file.
   */
  public static void tryDijkstrasWithLandmarks(RoadNetwork network,
      int numberOfExecutions) {
    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component...");

    System.out.println("1. NUMBER OF NODES OF LCC: "
        + largestComponent.getNodeIds().size());

    System.out.println("2. NUMBER OF ARCS OF LCC: "
        + largestComponent.getNumberOfArcs());

    Integer totalCost = 0;
    long totalExecutionTime = 0;
    long heuristicTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    LandmarkAlgorithm landAlg = new LandmarkAlgorithm(largestComponent);
    long startPre = System.currentTimeMillis();
    landAlg.selectLandmarks(16);
    long endPre = System.currentTimeMillis();
    long preComputationTime = endPre - startPre;

    for (int i = 0; i < numberOfExecutions; i++) {
      // System.out.println("------------------------------------------------");
      Integer sourceNodeId = largestComponent.getRandomNodeId();
      Integer targetNodeId = largestComponent.getRandomNodeId();
      while (sourceNodeId == targetNodeId) {
        targetNodeId = largestComponent.getRandomNodeId();
      }
      // System.out.println("CALCULATING shortest path from Node "
      // + sourceNodeId + " TO Node " + targetNodeId);
      long start = System.currentTimeMillis();
      Integer cost = landAlg.computeShortestPath(sourceNodeId, targetNodeId);
      long end = System.currentTimeMillis();
      Map<Integer, Integer> settledNodeCosts = landAlg.getVisitedNodes();
      totalSettledNodes = totalSettledNodes + settledNodeCosts.size();
      totalCost = totalCost + cost;
      totalExecutionTime = totalExecutionTime + (end - start)
          - landAlg.getHeuristicTimeExection();
      heuristicTime = heuristicTime + landAlg.getHeuristicTimeExection();

      // System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId
      // + " TO NODE: " + targetNodeId + " :::: " + cost + " seconds");

      // System.out.println("------------------------------------------------");
    }
    System.out.println("RUNNING TIME FOR PRECOMPUTATION OF LANDMARKS: "
        + Double.valueOf(twoDForm.format(preComputationTime + heuristicTime))
        + " milliseconds");
    System.out.println("LANDMARK HEURISTIC");
    System.out.println("AVERAGE RUNNING TIME: "
        + Double.valueOf(twoDForm.format((totalExecutionTime)
            / numberOfExecutions)) + " milliseconds");
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes
        / numberOfExecutions);
    System.out.println("AVERAGE SP. COST: " + totalCost / numberOfExecutions);
  }

  /**
   * As requested in Exercise Sheet 4 - ex. 2.
   * 
   * @param network
   *          the original road network extracted from the osm file.
   */
  public static void tryArcFlags(RoadNetwork network, int numberOfExecutions) {
    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component...");

    System.out.println("1. NUMBER OF NODES OF LCC: "
        + largestComponent.getNodeIds().size());

    System.out.println("2. NUMBER OF ARCS OF LCC: "
        + largestComponent.getNumberOfArcs());

    Integer totalCost = 0;
    long totalExecutionTime = 0;
    int totalSettledNodes = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    ArcFlagsAlgorithm afg = new ArcFlagsAlgorithm(largestComponent);
    long startPre = System.currentTimeMillis();
    System.out.println("Start of precomputation....");
    afg.precomputeArcFlags(49.20, 49.25, 6.95, 7.05);
    System.out.println("End of precomputation....");
    long endPre = System.currentTimeMillis();
    long preComputationTime = endPre - startPre;

    for (int i = 0; i < numberOfExecutions; i++) {
      // System.out.println("------------------------------------------------");
      Integer sourceNodeId = largestComponent.getRandomNodeId();
      Integer targetNodeId = largestComponent.getRandomNodeIdWithinRegion(
          49.20, 49.25, 6.95, 7.05);
      while (sourceNodeId == targetNodeId) {
        targetNodeId = largestComponent.getRandomNodeId();
      }
      System.out.println("CALCULATING shortest path from Node " + sourceNodeId
          + " TO Node " + targetNodeId);
      long start = System.currentTimeMillis();
      Integer cost = afg.computeShortestPath(sourceNodeId, targetNodeId);
      long end = System.currentTimeMillis();

      Map<Integer, Integer> settledNodeCosts = afg.getVisitedNodes();
      totalSettledNodes = totalSettledNodes + settledNodeCosts.size();
      totalCost = totalCost + cost;

      totalExecutionTime = totalExecutionTime + (end - start);

      System.out.println("SHORTEST PATH FROM NODE: " + sourceNodeId
          + " TO NODE: " + targetNodeId + " :::: " + cost + " seconds");

      // System.out.println("------------------------------------------------");
    }
    System.out
        .println("RUNNING TIME FOR PRECOMPUTATION OF ARC FLAGS: "
            + Double.valueOf(twoDForm.format(preComputationTime))
            + " milliseconds");
    System.out.println("ARC FLAGS");
    System.out.println("AVERAGE RUNNING TIME: "
        + Double.valueOf(twoDForm.format((totalExecutionTime)
            / numberOfExecutions)) + " milliseconds");
    System.out.println("AVERAGE SETTLED NODES: " + totalSettledNodes
        / numberOfExecutions);
    System.out.println("AVERAGE SP. COST: " + totalCost / numberOfExecutions);

  }

  /**
   * Main method.
   * 
   * @param args
   */

  public static void main(String[] args) {
    // If one wants to reduce the file size.
    // ReduceFileSize rfs = new ReduceFileSize(
    // "E:/Documents/UNI/SS12/Efficient Route Planning/groupRepository/"
    // + "src/routeplanning/resources/saarland.osm",
    // "E:/Documents/UNI/SS12/Efficient Route Planning/groupRepository/"
    // + "src/routeplanning/resources/saarland_reduced.osm");
    // rfs.process();
    RoadNetwork roadNet = new RoadNetwork();
    roadNet.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
        + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
    // roadNet.readFromOsmFile("D:/workspace/routeplanning/src"
    // + "/routeplanning/resources/saarland_reduced.osm");

    // System.out.println("****************************"
    // + "*****************************");
    // //MainClass.tryDijkstrasWithSimpleHeuristic(roadNet, 100);
    // System.out.println("****************************"
    // + "*****************************");
    // MainClass.tryDijkstrasWithLandmarks(roadNet, 100);
//    System.out.println("****************************"
//        + "*****************************");
    // MainClass.tryArcFlags(roadNet, 100);
//    System.out.println("****************************"
//        + "*****************************");

    /*
     * DijkstraAlgorithm dij = new DijkstraAlgorithm(roadNet);
     * System.out.println("COST:" + dij.computeShortestPath(385925420,
     * 262172939)); //MainClass.toTextFile(roadNet, dij.parents,
     * //"D:/workspace/routeplanning/src/routeplanning/resources/path.txt",
     * //385925420, 259000790); ArcFlagsAlgorithm afg = new
     * ArcFlagsAlgorithm(roadNet); afg.precomputeArcFlags(49.20, 49.25, 6.95,
     * 7.05); System.out.println("COST:" + afg.computeShortestPath(385925420,
     * 262172939)); pathToTextFile(roadNet, afg.getParents(),
     * "E:/Documents/UNI/SS12/Efficient Route Planning/" +
     * "groupRepository/src/routeplanning/resources/path.txt", 385925420,
     * 262172939);
     */
    // RoadNetwork rn;
    // TestGraphEx6 testgraph = new TestGraphEx6();
    // rn = testgraph.createSampleGraphEx6();
    //
    // System.out.println(rn.asString());
    // DijkstraAlgorithm dj = new DijkstraAlgorithm(rn);
    // System.out.println("SP:" + dj.computeShortestPath(0, 8));
    // dj.printShortestPath(0, 8);
    //
    // ContractionHiearchies2 ch = new ContractionHiearchies2(rn);
    // ch.computeRandomNodeOrdering();
    // ch.contractNode();
    System.out.println("****************************"
        + "*****************************");
    MainClass.contractFirstNodes(roadNet, 1000);
    System.out.println("****************************"
        + "*****************************");    
  }
  
  
  /**
   * As requested in Exercise Sheet 6 - ex. 2.
   * 
   * @param network
   *          the original road network extracted from the osm file.
   */
  public static void contractFirstNodes(RoadNetwork network,
      int numberOfExecutions) {
    int zeroShortcuts = 0;
    int oneShortcut = 0;
    int twoShortcuts = 0;
    int threeShortcuts = 0;
    int fourOrMoreShortcuts = 0;
    int edSmallerEqualMinus3 = 0;
    int edEqualsMinus2 = 0;
    int edEqualsMinus1 = 0;
    int edEqualsZero = 0;
    int edEqualsOne = 0;
    int edEqualsTwo = 0;
    int edGreaterEqualsThree = 0;

    System.out.println("Start from Largest Connected Component...");
    RoadNetwork largestComponent = network.reduceToLargestConnectedComponent();
    System.out.println("End from Largest Connected Component...");

    System.out.println("1. NUMBER OF NODES OF LCC: "
        + largestComponent.getNodeIds().size());

    System.out.println("2. NUMBER OF ARCS OF LCC: "
        + largestComponent.getNumberOfArcs());

    ContractionHierarchies ch = new ContractionHierarchies(largestComponent);
    ch.computeRandomNodeOrdering();

    long totalExecutionTime = 0;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    for (int i = 0; i < numberOfExecutions; i++) {
      // Compute Edge difference values, without modifying the graph.
      long start = System.currentTimeMillis();
      List<Integer> info = ch.contractNode(i, true);
      long end = System.currentTimeMillis();
      
      int ed = info.get(1);
      if (ed <= -3) {
        edSmallerEqualMinus3++;
      } else if (ed == -2) {
        edEqualsMinus2++;
      } else if (ed == -1) {
        edEqualsMinus1++;
      } else if (ed == 0) {
        edEqualsZero++;
      } else if (ed == 1) {
        edEqualsOne++;
      } else if (ed == 2) {
        edEqualsTwo++;
      } else if (ed >= 3) {
        edGreaterEqualsThree++;
      } else {
        System.out.println("???");
      }
      
      //System.out.println("------------------------------------------------");
      int shortcutsAdded = info.get(0);

      if (shortcutsAdded == 0) {
        zeroShortcuts++;
      } else if (shortcutsAdded == 1) {
        oneShortcut++;
      } else if (shortcutsAdded == 2) {
        twoShortcuts++;
      } else if (shortcutsAdded == 3) {
        threeShortcuts++;
      } else if (shortcutsAdded >= 4) {
        fourOrMoreShortcuts++;
      } else {
        System.out.println("???shortcutsAdded" + shortcutsAdded);
      }
      totalExecutionTime = totalExecutionTime + (end - start);
      //System.out.println("------------------------------------------------");
    }

    System.out.println("AVERAGE CONTRACTION TIME: "
      + ((totalExecutionTime * 1000) / numberOfExecutions) + " microseconds");

//    System.out.println("TOTAL EXECUTION TIME: " + totalExecutionTime
//        + " milliseconds");
    System.out.println("NUMBER OF CONTRACTION WHERE SHORTCUTS ADDED ARE: ");
    System.out.println("0 -> " + zeroShortcuts + "| 1 -> " + oneShortcut
        + "| 2 -> " + twoShortcuts + "| 3 -> " + threeShortcuts + "| 4 -> "
        + fourOrMoreShortcuts);
    System.out.println("EDGE DIFFERENCES: ");
    System.out.println("<=3 -> " + edSmallerEqualMinus3 + "| -2 -> "
        + edEqualsMinus2 + "| -1 -> " + edEqualsMinus1 + "| 0 -> "
        + edEqualsZero + "| 1 -> " + edEqualsOne + "| 2 -> " + edEqualsTwo
        + "| >=3 -> " + edGreaterEqualsThree);
  }

  /**
   * Export settled nodes to text file, used for tests.
   * 
   * @param afg
   * @param roadNet
   */
  public static void settledNodesToFile(ArcFlagsAlgorithm afg,
      RoadNetwork roadNet) {
    Node node;
    String pathOut = "D:/workspace/routeplanning/src/routeplanning/"
        + "resources/afg_settled.txt";
    try {
      BufferedWriter outWriter = new BufferedWriter(new FileWriter(pathOut));
      for (Object key : afg.getVisitedNodes().keySet()) {
        node = roadNet.getMapNodeId().get((Integer) key);
        System.out.println(node.latitude + "," + node.longitude);
        outWriter.newLine();
        outWriter.write(node.latitude + ";" + node.longitude);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Method to export a path to a text file, used for tests.
   */
  public static void pathToTextFile(RoadNetwork rn,
      Map<Integer, Integer> parent, String pathOut, int sourceNodeId,
      int targetNodeId) {
    try {
      BufferedWriter outWriter = new BufferedWriter(new FileWriter(pathOut));
      Node currentNode, prevNode;
      int currentNodeId, prevNodeId;

      currentNode = rn.getMapNodeId().get(targetNodeId);
      currentNodeId = targetNodeId;
      outWriter.newLine();
      outWriter.write(currentNode.id + ";" + currentNode.latitude + ";"
          + currentNode.longitude);

      while (currentNode.id != sourceNodeId) {
        System.out.println(currentNodeId);
        currentNodeId = parent.get(currentNodeId);
        currentNode = rn.getMapNodeId().get(currentNodeId);
        outWriter.newLine();
        outWriter.write(currentNode.id + ";" + currentNode.latitude + ";"
            + currentNode.longitude);
      }

      parent.get(targetNodeId);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to export nodes to a file, used for tests.
   */
  public void exportListNodesToFile(List<Node> nodes, String pathOut) {
    // String pathOut = "D:/workspace/routeplanning/src/
    // routeplanning/resources/boundaryNodes.txt";
    try {
      BufferedWriter outWriter = new BufferedWriter(new FileWriter(pathOut));
      for (int i = 0; i < nodes.size(); i++) {
        outWriter.newLine();
        outWriter.write(nodes.get(i).latitude + ";" + nodes.get(i).longitude);
        // System.out.println(nodes.get(i).latitude + ";"
        // + nodes.get(i).longitude);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
