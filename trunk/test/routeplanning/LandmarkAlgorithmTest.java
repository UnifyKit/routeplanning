package routeplanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import org.junit.Test;


/**
 * Testing class of class DijkstraAlgorithm.
 * @author AAA
 */
public class LandmarkAlgorithmTest {
  
  /**
   * Builds the example from lecture 3 for A*.
   */
  public RoadNetwork createSampleGraph() {

    RoadNetwork rn = new RoadNetwork();
    Node node0 = new Node(0, 49.3413853, 7.3014897);
    Node node1 = new Node(1, 49.3407084, 7.3006280);
    Node node2 = new Node(2, 49.3406105, 7.3004165);
    Node node3 = new Node(3, 49.3407516, 7.2998333);
    Node node4 = new Node(4, 49.3401466, 7.3997222);
    Node node5 = new Node(5, 49.3401942, 7.2998333);
    Node node6 = new Node(6, 49.3401942, 7.2998333);
    Node node7 = new Node(7, 49.3401942, 7.2998333);
    Node node8 = new Node(8, 49.3401942, 7.2998333);

    Arc arc01 = new Arc(node1, 2);
    Arc arc05 = new Arc(node5, 1);

    Arc arc10 = new Arc(node0, 2);
    Arc arc12 = new Arc(node2, 2);

    Arc arc21 = new Arc(node1, 2);
    Arc arc23 = new Arc(node3, 2);

    Arc arc32 = new Arc(node2, 2);
    Arc arc34 = new Arc(node4, 2);

    Arc arc43 = new Arc(node3, 2);
    Arc arc46 = new Arc(node6, 10);
    Arc arc47 = new Arc(node7, 10);
    Arc arc48 = new Arc(node8, 10);

    Arc arc50 = new Arc(node0, 1);
    Arc arc56 = new Arc(node6, 1);
    Arc arc57 = new Arc(node7, 1);
    Arc arc58 = new Arc(node8, 1);

    Arc arc65 = new Arc(node5, 1);
    Arc arc64 = new Arc(node4, 10);

    Arc arc75 = new Arc(node5, 1);
    Arc arc74 = new Arc(node4, 10);

    Arc arc85 = new Arc(node5, 1);
    Arc arc84 = new Arc(node4, 10);

    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(node2);
    rn.addNodeToGraph(node3);
    rn.addNodeToGraph(node4);
    rn.addNodeToGraph(node5);
    rn.addNodeToGraph(node6);
    rn.addNodeToGraph(node7);
    rn.addNodeToGraph(node8);

    rn.addAdjacentArc(node0, arc01);
    rn.addAdjacentArc(node0, arc05);

    rn.addAdjacentArc(node1, arc10);
    rn.addAdjacentArc(node1, arc12);

    rn.addAdjacentArc(node2, arc21);
    rn.addAdjacentArc(node2, arc23);

    rn.addAdjacentArc(node3, arc32);
    rn.addAdjacentArc(node3, arc34);

    rn.addAdjacentArc(node4, arc43);
    rn.addAdjacentArc(node4, arc46);
    rn.addAdjacentArc(node4, arc47);
    rn.addAdjacentArc(node4, arc48);

    rn.addAdjacentArc(node5, arc50);
    rn.addAdjacentArc(node5, arc56);
    rn.addAdjacentArc(node5, arc57);
    rn.addAdjacentArc(node5, arc58);

    rn.addAdjacentArc(node6, arc65);
    rn.addAdjacentArc(node6, arc64);

    rn.addAdjacentArc(node7, arc75);
    rn.addAdjacentArc(node7, arc74);

    rn.addAdjacentArc(node8, arc85);
    rn.addAdjacentArc(node8, arc84);
    
    return rn;
  }
  
//  /**
//   * TODO.
//   * @param graph
//   */
//  @Test
//  public void testVisitedNodes() {
//    // Source node0, Target node4
//    RoadNetwork rn = createSampleGraph();
//    LandmarkAlgorithm landAlg = new LandmarkAlgorithm(rn);
//    landAlg.selectLandmarks(2);
//    int cost = landAlg.computeShortestPath(0, 4);
//    int expectedCost = 8;
//    System.out.println("VisitedNodes:" + landAlg.getVisitedNodes().toString());
//    String visitedNodes = landAlg.getVisitedNodes().toString();
//    String expectedVisitedNodes = "{0=0, 1=2, 2=4, 3=6, 4=8}";
//    System.out.println("Selected nodes: " + landAlg.getLandmarkIds());
//    Assert.assertEquals(expectedCost, cost);
//    Assert.assertEquals(expectedVisitedNodes, visitedNodes);
//  }
  
  /**
   * Tests the behavior of the selection of landmarks.
   */
  @Test
  public void testNumberOfLandmarks() {
    // Source node0, Target node4
    RoadNetwork rn = createSampleGraph();
    LandmarkAlgorithm landAlg = new LandmarkAlgorithm(rn);
    landAlg.selectLandmarks(4);
    
    List<Integer> landmarkIds = landAlg.getLandmarkIds();
    boolean allAreValidNodes = true;
    for (int i = 0; i < landmarkIds.size(); i++) {
      Integer lmId = landmarkIds.get(i);
      if (!rn.getNodeIds().contains(lmId)) {
        allAreValidNodes = false;
      }
    }
    
    Assert.assertEquals(new Integer(4), 
        new Integer(landAlg.getLandmarkIds().size()));
    Assert.assertTrue("Some landmarks are not valid", allAreValidNodes);
  }
  
  /**
   * Tests if the dist(l, u) calculated for the landmarks are ok.
   */
  @Test
  public void testPrecomputation() {
    // Source node0, Target node4
    RoadNetwork rn = createSampleGraph();
    LandmarkAlgorithm landAlg = new LandmarkAlgorithm(rn);
    landAlg.selectLandmarks(5);
    
    List<Integer> landmarkIds = landAlg.getLandmarkIds();
    boolean allOk = true;
    for (int i = 0; i < landmarkIds.size(); i++) {
      Integer lmId = landmarkIds.get(i);
      Map<Integer, Integer> costsMap = landAlg.getCostMaps().get(i);
      
      DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(rn);
      dijAlg.computeShortestPath(lmId, -1);
      
      if (!costsMap.equals(dijAlg.getVisitedNodes())) {
        allOk = false;
      }
    }
    
    Assert.assertTrue("The precomputed values of landmarks are wrong", allOk);
  }
  
  /**
   * Tests if the heuristics are correctly calculated.
   */
  @Test
  public void checkHeuristics() {
    RoadNetwork rn = createSampleGraph();
    LandmarkAlgorithm landAlg = new LandmarkAlgorithm(rn);
    List<Integer> nodeIds = new ArrayList<Integer>();
    nodeIds.add(5);
    nodeIds.add(2);
    nodeIds.add(8);
    landAlg.setLandmarkIds(nodeIds);
    landAlg.precomputeLandmarkDistances();
    //dist(l,u) for landmark 5
    Map<Integer, Integer> costsOf5 = new HashMap<Integer, Integer>();
    costsOf5.put(5, 0);
    costsOf5.put(6, 1);
    costsOf5.put(7, 1);
    costsOf5.put(8, 1);
    costsOf5.put(0, 1);
    costsOf5.put(1, 3);
    costsOf5.put(2, 5);
    costsOf5.put(3, 7);
    costsOf5.put(4, 9);
    System.out.println(costsOf5);
    
    Map<Integer, Integer> generatedCostsOf5 = landAlg.getCostMaps().get(0);
    System.out.println(generatedCostsOf5);
    
    Assert.assertEquals(costsOf5, generatedCostsOf5);
    
    Map<Integer, Integer> costsOf2 = new HashMap<Integer, Integer>();
    costsOf2.put(5, 5);
    costsOf2.put(6, 6);
    costsOf2.put(7, 6);
    costsOf2.put(8, 6);
    costsOf2.put(0, 4);
    costsOf2.put(1, 2);
    costsOf2.put(2, 0);
    costsOf2.put(3, 2);
    costsOf2.put(4, 4);
    System.out.println(costsOf2);
    Map<Integer, Integer> generatedCostsOf2 = landAlg.getCostMaps().get(1);
    System.out.println(generatedCostsOf2);
    
    Assert.assertEquals(costsOf2, generatedCostsOf2);
    
    Map<Integer, Integer> costsOf8 = new HashMap<Integer, Integer>();
    costsOf8.put(5, 1);
    costsOf8.put(6, 2);
    costsOf8.put(7, 2);
    costsOf8.put(8, 0);
    costsOf8.put(0, 2);
    costsOf8.put(1, 4);
    costsOf8.put(2, 6);
    costsOf8.put(3, 8);
    costsOf8.put(4, 10);
    System.out.println(costsOf8);
    
    Map<Integer, Integer> generatedCostsOf8 = landAlg.getCostMaps().get(2);
    System.out.println(generatedCostsOf8);
    
    Assert.assertEquals(costsOf8, generatedCostsOf8);
    
    Assert.assertTrue("The Precomputation works", 
        landAlg.getCostMaps().size() == 3);
    
    List<Integer> expectedHeuristics = new ArrayList<Integer>();
    expectedHeuristics.add(8);
    expectedHeuristics.add(6);
    expectedHeuristics.add(4);
    expectedHeuristics.add(2);
    expectedHeuristics.add(0);
    expectedHeuristics.add(9);
    expectedHeuristics.add(8);
    expectedHeuristics.add(8);
    expectedHeuristics.add(10);
    
    List<Integer> calculatedHeuristics = landAlg.calculateHeuristicList(4);
    
    System.out.println(expectedHeuristics);
    System.out.println(calculatedHeuristics);
    
    Assert.assertEquals(calculatedHeuristics, expectedHeuristics);
    
    landAlg.computeShortestPath(0, 4);
    System.out.println(landAlg.getVisitedNodes());
    
  }
  
  /**
   * Tests if all the versions of Dijkstra return the same result.
   */
  
//  @Test
//  public void testAllDijkstras() {
//    RoadNetwork rn = new RoadNetwork();
//    rn.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
//      + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
//  
//    int sourceNodeId = 451965304;
//    int targetNodeId = 1598221820;
//    int expectedValue = 1492;
//    
//    DijkstraAlgorithm dij = new DijkstraAlgorithm(rn);
//    int cost1 = dij.computeShortestPath(sourceNodeId, targetNodeId);
//    
//    System.out.println("Normal Dijkstra");
//    System.out.println(cost1);
//    
//    DijkstraAlgorithm slDij = new DijkstraAlgorithm(rn);
//    List<Integer> heuristic;
//    heuristic = rn.computeStraightLineHeuristic(targetNodeId);
//    slDij.setHeuristic(heuristic);
//    int cost2 = slDij.computeShortestPath(sourceNodeId, targetNodeId);
//    
//    System.out.println("Dijkstra with Straightline heuristic");
//    System.out.println(cost2);
//    
//    LandmarkAlgorithm landAlg = new LandmarkAlgorithm(rn);
//    landAlg.selectLandmarks(16);
//    int cost3 = landAlg.computeShortestPath(sourceNodeId, targetNodeId);
//    
//    System.out.println("Dijkstra with landmarks");
//    System.out.println(cost3);
//  }
}
