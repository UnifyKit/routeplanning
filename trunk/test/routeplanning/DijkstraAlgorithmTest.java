package routeplanning;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing class of class DijkstraAlgorithm.
 * 
 * @author AAA
 */
public class DijkstraAlgorithmTest {
  /**
   * Generic method which reads from file and builds a road network.
   */
  public RoadNetwork buildGraphFromOsm() {
    RoadNetwork rn = new RoadNetwork();
    URL url = this.getClass().getClassLoader()
        .getResource("routeplanning/resources/osmTest_reduced.osm");
    rn.readFromOsmFile(url.getPath().replace("%20", " "));
    return rn;
  }

  /**
   * Creates a road network that can be used in other classes.
   * 
   * @param graph
   */
  public RoadNetwork createSampleGraph() {
    RoadNetwork rn = new RoadNetwork();
    Node node0 = new Node(0, 1.0f, 1.0f);
    Node node1 = new Node(1, 1.0f, 1.0f);
    Node node2 = new Node(2, 1.0f, 1.0f);
    Node node3 = new Node(3, 1.0f, 1.0f);
    Node node4 = new Node(4, 1.0f, 1.0f);
    Node node5 = new Node(5, 1.0f, 1.0f);

    Arc newArc00 = new Arc(node0, 0);
    Arc newArc01 = new Arc(node1, 1);
    Arc newArc02 = new Arc(node2, 4);
    Arc newArc03 = new Arc(node3, 3);
    Arc newArc04 = new Arc(node4, 10);

    Arc newArc11 = new Arc(node1, 0);
    Arc newArc10 = new Arc(node0, 1);
    Arc newArc13 = new Arc(node3, 1);

    Arc newArc22 = new Arc(node2, 0);
    Arc newArc20 = new Arc(node0, 4);
    Arc newArc23 = new Arc(node3, 5);
    Arc newArc25 = new Arc(node5, 3);

    Arc newArc33 = new Arc(node3, 0);
    Arc newArc30 = new Arc(node0, 3);
    Arc newArc31 = new Arc(node1, 1);
    Arc newArc32 = new Arc(node2, 5);
    Arc newArc34 = new Arc(node4, 1);

    Arc newArc44 = new Arc(node4, 0);
    Arc newArc40 = new Arc(node0, 10);
    Arc newArc43 = new Arc(node3, 1);
    Arc newArc45 = new Arc(node5, 1);

    Arc newArc55 = new Arc(node5, 0);
    Arc newArc52 = new Arc(node2, 3);
    Arc newArc54 = new Arc(node4, 1);

    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(node2);
    rn.addNodeToGraph(node3);
    rn.addNodeToGraph(node4);
    rn.addNodeToGraph(node5);

    rn.addAdjacentArc(node0, newArc00);
    rn.addAdjacentArc(node0, newArc01);
    rn.addAdjacentArc(node0, newArc02);
    rn.addAdjacentArc(node0, newArc03);
    rn.addAdjacentArc(node0, newArc04);

    rn.addAdjacentArc(node1, newArc11);
    rn.addAdjacentArc(node1, newArc10);
    rn.addAdjacentArc(node1, newArc13);

    rn.addAdjacentArc(node2, newArc22);
    rn.addAdjacentArc(node2, newArc20);
    rn.addAdjacentArc(node2, newArc23);
    rn.addAdjacentArc(node2, newArc25);

    rn.addAdjacentArc(node3, newArc33);
    rn.addAdjacentArc(node3, newArc30);
    rn.addAdjacentArc(node3, newArc31);
    rn.addAdjacentArc(node3, newArc32);
    rn.addAdjacentArc(node3, newArc34);

    rn.addAdjacentArc(node4, newArc44);
    rn.addAdjacentArc(node4, newArc40);
    rn.addAdjacentArc(node4, newArc43);
    rn.addAdjacentArc(node4, newArc45);

    rn.addAdjacentArc(node5, newArc55);
    rn.addAdjacentArc(node5, newArc52);
    rn.addAdjacentArc(node5, newArc54);

    // Adding irrelevant nodes:
    Node nodex = new Node(100, 1.0f, 1.0f);
    Node nodey = new Node(101, 1.0f, 1.0f);
    Node nodez = new Node(105, 1.0f, 1.0f);

    rn.addNodeToGraph(nodex);
    rn.addNodeToGraph(nodey);
    rn.addNodeToGraph(nodez);

    return rn;
  }

  /**
   * Tests ComputeShortestPath() between two nodes.
   */
  @Test
  public void testComputeShortestPath() {
    RoadNetwork rn = createSampleGraph();
    DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
    Integer cost = alg.computeShortestPath(0, 4);
    Integer expectedValue = 3;
    Assert.assertEquals(cost, expectedValue);
  }

  /**
   * Tests ComputeShortestPath() with target node having -1 as parameter.
   */
  @Test
  public void testComputeShortestPath2() {
    RoadNetwork rn = createSampleGraph();
    DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
    alg.computeShortestPath(0, -1);
    Map<Long, Integer> visitedNodes = alg.getVisitedNodes();
    System.out.println("SIZE OF VISITED NODES" + visitedNodes.size());
    Assert.assertTrue("Visited nodes is not of the expected size",
        visitedNodes.size() == 6);

    boolean conformsResult = true;

    if (visitedNodes.get(1L) != 1) {
      conformsResult = false;
    }

    if (visitedNodes.get(2L) != 4) {
      conformsResult = false;
    }

    if (visitedNodes.get(3L) != 2) {
      conformsResult = false;
    }

    if (visitedNodes.get(4L) != 3) {
      conformsResult = false;
    }

    if (visitedNodes.get(5L) != 4) {
      conformsResult = false;
    }

    Assert.assertTrue(conformsResult);
  }

  /**
   * Tests if test visited Nodes returns the expected costs.
   */
  @Test
  public void testGetVisitedNodes() {
    RoadNetwork rn = createSampleGraph();
    DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
    alg.computeShortestPath(0, 4);
    Map<Long, Integer> visitedNodes = alg.getVisitedNodes();

    boolean conformsResult = true;

    if (visitedNodes.get(1L) != 1) {
      conformsResult = false;
    }

    if (visitedNodes.get(3L) != 2) {
      conformsResult = false;
    }

    if (visitedNodes.get(4L) != 3) {
      conformsResult = false;
    }

    Assert.assertTrue(conformsResult);
  }

  /**
   * Tests ComputeShortestPath() between two nodes reading from OSM.
   */
  @Test
  public void testComputeShortestPathFromOsm() {
    // reduced version of the graph
    RoadNetwork rn = buildGraphFromOsm().reduceToLargestConnectedComponent();
    DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
    Integer cost = alg.computeShortestPath(0, 4);

    // Calculate the expected value (used to verify result):
    // Node node0 = new Node(0, 49.3413853, 7.3014897);
    // Node node1 = new Node(1, 49.3407084, 7.3006280);
    // Node node2 = new Node(2, 49.3406105, 7.3004165);
    // Node node3 = new Node(3, 49.3407516, 7.2998333);
    // Node node4 = new Node(4, 49.3401466, 7.3997222);
    // Node node5 = new Node(5, 49.3401942, 7.2998333);
    //
    // int cost01 = rn.computeCost("motorway", rn.getDistance2(node0, node1));
    // int cost02 = rn.computeCost("motorway", rn.getDistance2(node0, node2));
    // int cost03 = rn.computeCost("motorway", rn.getDistance2(node0, node3));
    // int cost04 = rn.computeCost("motorway", rn.getDistance2(node0, node4));
    //
    // int cost34 = rn.computeCost("motorway", rn.getDistance2(node3, node4));
    // int cost13 = rn.computeCost("motorway", rn.getDistance2(node1, node3));
    // int cost25 = rn.computeCost("motorway", rn.getDistance2(node2, node5));
    // int cost23 = rn.computeCost("motorway", rn.getDistance2(node2, node3));
    // int cost54 = rn.computeCost("motorway", rn.getDistance2(node5, node4));
    //
    // System.out.println("==================================");
    // System.out.println("0-1::: " + cost01);
    // System.out.println("0-2::: " + cost02);
    // System.out.println("0-3::: " + cost03);
    // System.out.println("0-4::: " + cost04);
    // System.out.println("3-4::: " + cost34);
    // System.out.println("1-3::: " + cost13);
    // System.out.println("2-5::: " + cost25);
    // System.out.println("2-3::: " + cost23);
    // System.out.println("5-4::: " + cost54);

    Integer expectedValue = 230;
    Assert.assertEquals(cost, expectedValue);
  }

  /**
   * Heuristic function test using graph sample from lecture 3.
   */
  @Test
  public void testHeuristic() {
    // Example in lecture 3 for A*
    // Source node0, Target node4

    RoadNetwork rn = new RoadNetwork();
    Node node0 = new Node(0, 49.3413853f, 7.3014897f);
    Node node1 = new Node(1, 49.3407084f, 7.3006280f);
    Node node2 = new Node(2, 49.3406105f, 7.3004165f);
    Node node3 = new Node(3, 49.3407516f, 7.2998333f);
    Node node4 = new Node(4, 49.3401466f, 7.3997222f);
    Node node5 = new Node(5, 49.3401942f, 7.2998333f);
    Node node6 = new Node(6, 49.3401942f, 7.2998333f);
    Node node7 = new Node(7, 49.3401942f, 7.2998333f);
    Node node8 = new Node(8, 49.3401942f, 7.2998333f);

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

    List<Integer> heuristic = new ArrayList<Integer>();
    heuristic.add(5);
    heuristic.add(4);
    heuristic.add(3);
    heuristic.add(1);
    heuristic.add(0);
    heuristic.add(9);
    heuristic.add(10);
    heuristic.add(10);
    heuristic.add(10);

    String stringRep = rn.asString();
    System.out.println("--------------HeuristicTest----------------");
    System.out.println(rn.asString());
    DijkstraAlgorithm dij = new DijkstraAlgorithm(rn);
    dij.setHeuristic(heuristic);
    int cost = dij.computeShortestPath(0, 4);
    int expectedCost = 8;
    System.out.println("VisitedNodes:" + dij.getVisitedNodes().toString());
    System.out.println("Paths: " + dij.parents.toString());
    String visitedNodes = dij.getVisitedNodes().toString();
    String expectedVisitedNodes = "{0=0, 1=2, 2=4, 3=6, 4=8}";
    Assert.assertEquals(expectedCost, cost);
    Assert.assertEquals(expectedVisitedNodes, visitedNodes);

    System.out.println("--------------HeuristicTest----------------");

  }

  /**
   * Tests if all the structures needed by DijkstraAlgorithm are correctly
   * reseted.
   */
  @Test
  public void testConsistencyOfTwoInvocations() {
    RoadNetwork rn = buildGraphFromOsm().reduceToLargestConnectedComponent();
    DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
    Integer cost = alg.computeShortestPath(0, 4);
    Integer expectedValue = 230;

    cost = alg.computeShortestPath(0, 4);
    Assert.assertEquals(cost, expectedValue);
  }

  // Commented because it uses saarland.osm as hardcoded test file

  /**
   * Test of heuristic to see if it is admissable using a random value h(u) <=
   * dist(u,v).
   */
  // @Test
  // public void heuristicAdmissableTest() {
  // RoadNetwork rn = new RoadNetwork();
  // RoadNetwork lc;
  // List<Integer> heuristic;
  // boolean admissableTest = true;
  // int sourceNodeId;
  // int targetNodeId;
  // int distUT;
  // int hu;
  // System.out.println("-----------------HeuristicAdmissableTest--"
  // + "-------------------");
  // rn.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
  // + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
  // //rn.readFromOsmFile("D:/workspace/routeplanning/src/
  // //+routeplanning/resources/saarland_reduced.osm");
  // //rn.computeStraightLineHeuristic(259000790);
  //
  // //lc = rn.reduceToLargestConnectedComponent();
  //
  // DijkstraAlgorithm dij = new DijkstraAlgorithm(rn);
  //
  // for (int i = 0; i < 10; i++) {
  // sourceNodeId = rn.getRandomNodeId();
  // targetNodeId = rn.getRandomNodeId();
  // distUT = dij.computeShortestPath(sourceNodeId, targetNodeId);
  // heuristic = rn.computeStraightLineHeuristic(targetNodeId);
  // hu = heuristic.get(rn.getNodeIdPosAdjArc().get(sourceNodeId));
  // if (hu > distUT) {
  // System.out.println("Error h(u) is not admissable from: "
  // + sourceNodeId + " to: " + targetNodeId);
  // admissableTest = false;
  // }
  // System.out.println("h(u): " + hu);
  // System.out.println("dist(u,t): " + distUT);
  // System.out.println("************************");
  // }
  // boolean expectedValue = true;
  // Assert.assertEquals(expectedValue, admissableTest);
  // System.out.println("-----------------HeuristicAdmissableTest--"
  // + "-------------------");
  // }

  /**
   * Tests visited node marks and parents list after computation of shortest
   * path.
   */
  @Test
  public void pathTest() {
    RoadNetwork rn;
    rn = createSampleGraph();
    DijkstraAlgorithm dij = new DijkstraAlgorithm(rn);
    dij.computeShortestPath(0, -1);
    System.out.println("----------------PathTest-----------------------");
    System.out.println("SPs: " + dij.visitedNodeMarks.toString());
    System.out.println("Paths: " + dij.parents.toString());
    String expectedValue = "{0=-1, 1=0, 2=0, 3=1, 4=3, 5=4}";
    Assert.assertEquals(expectedValue, dij.parents.toString());
    System.out.println("----------------PathTest-----------------------");
  }

}
