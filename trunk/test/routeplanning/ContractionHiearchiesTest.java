package routeplanning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing class of class ContractionHiearchies.
 * 
 * @author AAA
 */
public class ContractionHiearchiesTest {

  /**
   * Creates a road network representing the example slide 16 lecture 6.
   */
  public RoadNetwork createSampleGraph() {
    RoadNetwork rn = new RoadNetwork();
    Node nodeA = new Node(1, 1.0f, 1.0f);
    Node nodeB = new Node(2, 1.0f, 1.0f);
    Node nodeC = new Node(3, 1.0f, 1.0f);
    Node nodeD = new Node(4, 1.0f, 1.0f);
    Node nodeE = new Node(5, 1.0f, 1.0f);
    Node nodeF = new Node(6, 1.0f, 1.0f);
    Node nodeG = new Node(7, 1.0f, 1.0f);
    Node nodeH = new Node(8, 1.0f, 1.0f);
    Node nodeI = new Node(9, 1.0f, 1.0f);
    Node nodeJ = new Node(10, 1.0f, 1.0f);
    Node nodeK = new Node(11, 1.0f, 1.0f);
    Node nodeL = new Node(12, 1.0f, 1.0f);
    Node nodeM = new Node(13, 1.0f, 1.0f);

    rn.addNodeToGraph(nodeA);
    rn.addNodeToGraph(nodeB);
    rn.addNodeToGraph(nodeC);
    rn.addNodeToGraph(nodeD);
    rn.addNodeToGraph(nodeE);
    rn.addNodeToGraph(nodeF);
    rn.addNodeToGraph(nodeG);
    rn.addNodeToGraph(nodeH);
    rn.addNodeToGraph(nodeI);
    rn.addNodeToGraph(nodeJ);
    rn.addNodeToGraph(nodeK);
    rn.addNodeToGraph(nodeL);
    rn.addNodeToGraph(nodeM);

    Arc newArcAB = new Arc(nodeB, 3);
    rn.addAdjacentArc(nodeA, newArcAB);
    Arc newArcBA = new Arc(nodeA, 3);
    rn.addAdjacentArc(nodeB, newArcBA);
    Arc newArcAD = new Arc(nodeD, 4);
    rn.addAdjacentArc(nodeA, newArcAD);
    Arc newArcDA = new Arc(nodeA, 4);
    rn.addAdjacentArc(nodeD, newArcDA);
    Arc newArcBD = new Arc(nodeD, 5);
    rn.addAdjacentArc(nodeB, newArcBD);
    Arc newArcDB = new Arc(nodeB, 5);
    rn.addAdjacentArc(nodeD, newArcDB);
    Arc newArcBC = new Arc(nodeC, 2);
    rn.addAdjacentArc(nodeB, newArcBC);
    Arc newArcCB = new Arc(nodeB, 2);
    rn.addAdjacentArc(nodeC, newArcCB);
    Arc newArcCD = new Arc(nodeD, 2);
    rn.addAdjacentArc(nodeC, newArcCD);
    Arc newArcDC = new Arc(nodeC, 2);
    rn.addAdjacentArc(nodeD, newArcDC);
    Arc newArcAG = new Arc(nodeG, 7);
    rn.addAdjacentArc(nodeA, newArcAG);
    Arc newArcGA = new Arc(nodeA, 7);
    rn.addAdjacentArc(nodeG, newArcGA);
    Arc newArcDF = new Arc(nodeF, 1);
    rn.addAdjacentArc(nodeD, newArcDF);
    Arc newArcFD = new Arc(nodeD, 1);
    rn.addAdjacentArc(nodeF, newArcFD);
    Arc newArcFG = new Arc(nodeG, 4);
    rn.addAdjacentArc(nodeF, newArcFG);
    Arc newArcGF = new Arc(nodeF, 4);
    rn.addAdjacentArc(nodeG, newArcGF);
    Arc newArcCE = new Arc(nodeE, 5);
    rn.addAdjacentArc(nodeC, newArcCE);
    Arc newArcEC = new Arc(nodeC, 5);
    rn.addAdjacentArc(nodeE, newArcEC);
    Arc newArcEF = new Arc(nodeF, 3);
    rn.addAdjacentArc(nodeE, newArcEF);
    Arc newArcFE = new Arc(nodeE, 3);
    rn.addAdjacentArc(nodeF, newArcFE);
    Arc newArcEH = new Arc(nodeH, 7);
    rn.addAdjacentArc(nodeE, newArcEH);
    Arc newArcHE = new Arc(nodeE, 7);
    rn.addAdjacentArc(nodeH, newArcHE);
    Arc newArcFI = new Arc(nodeI, 1);
    rn.addAdjacentArc(nodeF, newArcFI);
    Arc newArcIF = new Arc(nodeF, 1);
    rn.addAdjacentArc(nodeI, newArcIF);
    Arc newArcGJ = new Arc(nodeJ, 6);
    rn.addAdjacentArc(nodeG, newArcGJ);
    Arc newArcJG = new Arc(nodeG, 6);
    rn.addAdjacentArc(nodeJ, newArcJG);
    Arc newArcIJ = new Arc(nodeJ, 3);
    rn.addAdjacentArc(nodeI, newArcIJ);
    Arc newArcJI = new Arc(nodeI, 3);
    rn.addAdjacentArc(nodeJ, newArcJI);
    Arc newArcHI = new Arc(nodeI, 3);
    rn.addAdjacentArc(nodeH, newArcHI);
    Arc newArcIH = new Arc(nodeH, 3);
    rn.addAdjacentArc(nodeI, newArcIH);
    Arc newArcHK = new Arc(nodeK, 4);
    rn.addAdjacentArc(nodeH, newArcHK);
    Arc newArcKH = new Arc(nodeH, 4);
    rn.addAdjacentArc(nodeK, newArcKH);
    Arc newArcIL = new Arc(nodeL, 1);
    rn.addAdjacentArc(nodeI, newArcIL);
    Arc newArcLI = new Arc(nodeI, 1);
    rn.addAdjacentArc(nodeL, newArcLI);
    Arc newArcJM = new Arc(nodeM, 5);
    rn.addAdjacentArc(nodeJ, newArcJM);
    Arc newArcMJ = new Arc(nodeJ, 5);
    rn.addAdjacentArc(nodeM, newArcMJ);
    Arc newArcLM = new Arc(nodeM, 2);
    rn.addAdjacentArc(nodeL, newArcLM);
    Arc newArcML = new Arc(nodeL, 2);
    rn.addAdjacentArc(nodeM, newArcML);
    Arc newArcKM = new Arc(nodeM, 4);
    rn.addAdjacentArc(nodeK, newArcKM);
    Arc newArcMK = new Arc(nodeK, 4);
    rn.addAdjacentArc(nodeM, newArcMK);
    Arc newArcKL = new Arc(nodeL, 3);
    rn.addAdjacentArc(nodeK, newArcKL);
    Arc newArcLK = new Arc(nodeK, 3);
    rn.addAdjacentArc(nodeL, newArcLK);

    return rn;
  }
  
  
  /**
   * Creates a road network representing the example slide 16 lecture 6.
   */
  public RoadNetwork createSampleGraph2() {
    RoadNetwork rn = new RoadNetwork();
    Node node0 = new Node(0, 1.0f, 1.0f);
    Node node1 = new Node(1, 1.0f, 1.0f);
    Node node2 = new Node(2, 1.0f, 1.0f);
    Node node3 = new Node(3, 1.0f, 1.0f);
    Node node4 = new Node(4, 1.0f, 1.0f);
    Node node5 = new Node(5, 1.0f, 1.0f);
    Node node6 = new Node(6, 1.0f, 1.0f);
    Node node7 = new Node(7, 1.0f, 1.0f);
    Node node8 = new Node(8, 1.0f, 1.0f);
    Node node9 = new Node(9, 1.0f, 1.0f);
    Node node10 = new Node(10, 1.0f, 1.0f);
    Node node11 = new Node(11, 1.0f, 1.0f);
    Node node12 = new Node(12, 1.0f, 1.0f);
    
    Arc newArc01 = new Arc(node1, 2);
    Arc newArc05 = new Arc(node5, 5);
    Arc newArc09 = new Arc(node9, 3);
    
    Arc newArc10 = new Arc(node0, 2);
    Arc newArc15 = new Arc(node5, 2);
    Arc newArc12 = new Arc(node2, 5);

    Arc newArc21 = new Arc(node1, 5);
    Arc newArc26 = new Arc(node6, 3);
    Arc newArc23 = new Arc(node3, 7);
    
    Arc newArc32 = new Arc(node2, 7);
    Arc newArc37 = new Arc(node7, 3);
    Arc newArc34 = new Arc(node4, 4);
    
    Arc newArc43 = new Arc(node3, 4);
    Arc newArc48 = new Arc(node8, 3);
    Arc newArc412 = new Arc(node12, 4);
    
    Arc newArc50 = new Arc(node0, 5);
    Arc newArc51 = new Arc(node1, 2);
    Arc newArc59 = new Arc(node9, 4);
    Arc newArc56 = new Arc(node6, 1);
    
    Arc newArc65 = new Arc(node5, 1);
    Arc newArc62 = new Arc(node2, 3);
    Arc newArc67 = new Arc(node7, 1);
    Arc newArc610 = new Arc(node10, 4);
    
    Arc newArc76 = new Arc(node6, 1);
    Arc newArc73 = new Arc(node3, 3);
    Arc newArc78 = new Arc(node8, 1);
    Arc newArc711 = new Arc(node11, 3);
    
    Arc newArc87 = new Arc(node7, 1);
    Arc newArc84 = new Arc(node4, 3);
    Arc newArc812 = new Arc(node12, 2);
    
    Arc newArc90 = new Arc(node0, 3);
    Arc newArc95 = new Arc(node5, 4);
    Arc newArc910 = new Arc(node10, 7);
    
    Arc newArc109 = new Arc(node9, 7);
    Arc newArc106 = new Arc(node6, 4);
    Arc newArc1011 = new Arc(node11, 6);
    
    Arc newArc1110 = new Arc(node10, 6);
    Arc newArc117 = new Arc(node7, 3);
    Arc newArc1112 = new Arc(node12, 5);
    
    Arc newArc1211 = new Arc(node11, 5);
    Arc newArc128 = new Arc(node8, 2);
    Arc newArc124 = new Arc(node4, 4);
    
    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(node2);
    rn.addNodeToGraph(node3);
    rn.addNodeToGraph(node4);
    rn.addNodeToGraph(node5);
    rn.addNodeToGraph(node6);
    rn.addNodeToGraph(node7);
    rn.addNodeToGraph(node8);
    rn.addNodeToGraph(node9);
    rn.addNodeToGraph(node10);
    rn.addNodeToGraph(node11);
    rn.addNodeToGraph(node12);

    rn.addAdjacentArc(node0, newArc01);
    rn.addAdjacentArc(node0, newArc05);
    rn.addAdjacentArc(node0, newArc09);
    
    rn.addAdjacentArc(node1, newArc10);
    rn.addAdjacentArc(node1, newArc15);
    rn.addAdjacentArc(node1, newArc12);

    rn.addAdjacentArc(node2, newArc21);
    rn.addAdjacentArc(node2, newArc26);
    rn.addAdjacentArc(node2, newArc23);
    
    rn.addAdjacentArc(node3, newArc32);
    rn.addAdjacentArc(node3, newArc37);
    rn.addAdjacentArc(node3, newArc34);
    
    rn.addAdjacentArc(node4, newArc43);
    rn.addAdjacentArc(node4, newArc48);
    rn.addAdjacentArc(node4, newArc412);
    
    rn.addAdjacentArc(node5, newArc50);
    rn.addAdjacentArc(node5, newArc51);
    rn.addAdjacentArc(node5, newArc59);
    rn.addAdjacentArc(node5, newArc56);
    
    rn.addAdjacentArc(node6, newArc65);
    rn.addAdjacentArc(node6, newArc62);
    rn.addAdjacentArc(node6, newArc67);
    rn.addAdjacentArc(node6, newArc610);
    
    rn.addAdjacentArc(node7, newArc76);
    rn.addAdjacentArc(node7, newArc73);
    rn.addAdjacentArc(node7, newArc78);
    rn.addAdjacentArc(node7, newArc711);
    
    rn.addAdjacentArc(node8, newArc87);
    rn.addAdjacentArc(node8, newArc84);
    rn.addAdjacentArc(node8, newArc812);

    rn.addAdjacentArc(node9, newArc90);
    rn.addAdjacentArc(node9, newArc95);
    rn.addAdjacentArc(node9, newArc910);

    rn.addAdjacentArc(node10, newArc109);
    rn.addAdjacentArc(node10, newArc106);
    rn.addAdjacentArc(node10, newArc1011);
    
    rn.addAdjacentArc(node11, newArc1110);
    rn.addAdjacentArc(node11, newArc117);
    rn.addAdjacentArc(node11, newArc1112);
    
    rn.addAdjacentArc(node12, newArc1211);
    rn.addAdjacentArc(node12, newArc128);
    rn.addAdjacentArc(node12, newArc124);
    
    return rn;
  }

  /**
   * Creates a road network representing the example slide 14 lecture 6.
   */
  public RoadNetwork createContractionSampleGraph() {
    RoadNetwork rn = new RoadNetwork();
    Node nodeU1 = new Node(1, 1.0f, 1.0f);
    Node nodeU2 = new Node(2, 1.0f, 1.0f);
    Node nodeV = new Node(3, 1.0f, 1.0f);
    Node nodeW1 = new Node(4, 1.0f, 1.0f);
    Node nodeW2 = new Node(5, 1.0f, 1.0f);
    Node nodeW3 = new Node(6, 1.0f, 1.0f);

    rn.addNodeToGraph(nodeU1);
    rn.addNodeToGraph(nodeU2);
    rn.addNodeToGraph(nodeV);
    rn.addNodeToGraph(nodeW1);
    rn.addNodeToGraph(nodeW2);
    rn.addNodeToGraph(nodeW3);

    // in both directions because it's unidirectional

    Arc newArcU1V = new Arc(nodeV, 1);
    rn.addAdjacentArc(nodeU1, newArcU1V);
    Arc newArcVU1 = new Arc(nodeU1, 1);
    rn.addAdjacentArc(nodeV, newArcVU1);

    Arc newArcU2V = new Arc(nodeV, 1);
    rn.addAdjacentArc(nodeU2, newArcU2V);
    Arc newArcVU2 = new Arc(nodeU2, 1);
    rn.addAdjacentArc(nodeV, newArcVU2);

    Arc newArcU1W1 = new Arc(nodeW1, 3);
    rn.addAdjacentArc(nodeU1, newArcU1W1);
    Arc newArcW1U1 = new Arc(nodeU1, 3);
    rn.addAdjacentArc(nodeW1, newArcW1U1);

    Arc newArcVW1 = new Arc(nodeW1, 2);
    rn.addAdjacentArc(nodeV, newArcVW1);
    Arc newArcW1V = new Arc(nodeV, 2);
    rn.addAdjacentArc(nodeW1, newArcW1V);

    Arc newArcVW2 = new Arc(nodeW2, 3);
    rn.addAdjacentArc(nodeV, newArcVW2);
    Arc newArcW2V = new Arc(nodeV, 3);
    rn.addAdjacentArc(nodeW2, newArcW2V);

    Arc newArcVW3 = new Arc(nodeW3, 4);
    rn.addAdjacentArc(nodeV, newArcVW3);
    Arc newArcW3V = new Arc(nodeV, 4);
    rn.addAdjacentArc(nodeW3, newArcW3V);

    Arc newArcW1W2 = new Arc(nodeW2, 1);
    rn.addAdjacentArc(nodeW1, newArcW1W2);
    Arc newArcW2W1 = new Arc(nodeW1, 1);
    rn.addAdjacentArc(nodeW2, newArcW2W1);

    Arc newArcW2W3 = new Arc(nodeW3, 1);
    rn.addAdjacentArc(nodeW2, newArcW2W3);
    Arc newArcW3W2 = new Arc(nodeW2, 1);
    rn.addAdjacentArc(nodeW3, newArcW3W2);

    return rn;
  }

  /**
   * Tests that the random generator for the swapping of positions work as
   * expected (within the limits of the list).
   */
  @Test
  public void testRoadNetwork() {
    System.out.println("------------------------testRoadNetwork"
        + "------------------------");
    RoadNetwork rn = createSampleGraph();
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    int min = 0;
    int max = rn.getNodeIds().size();
    int randomNumber = ch.generateRandomNodePosition(max);
    Assert.assertTrue(randomNumber >= min && randomNumber < max);
  }

  /**
   * Tests that the computeRandomNodeOrdering method.
   */
  @Test
  public void testComputeRandomNodeOrdering() {
    System.out.println("------------------------testComputeRandomNodeOrdering"
        + "------------------------");
    RoadNetwork rn = createSampleGraph();
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    ch.computeRandomNodeOrdering();
    List<Long> randomPosNodeIds = ch.getNodeOrdering();
    List<Long> realOrdering = rn.getNodeIds();
    Map<Long, Integer> orderingMap = ch.getNodeOrderingMap();

    System.out.println(realOrdering);
    System.out.println(randomPosNodeIds);
    System.out.println(orderingMap);

    // Checks if both lists have the same size in the end.
    Assert.assertTrue(randomPosNodeIds.size() == realOrdering.size());

    for (int i = 0; i < randomPosNodeIds.size(); i++) {
      Long nodeId = randomPosNodeIds.get(i);
      Assert.assertEquals(orderingMap.get(nodeId), new Integer(i));
      realOrdering.remove(nodeId);
    }
    // Checks if the elements in the reordered list are the same
    // as in the old list.
    Assert.assertTrue(realOrdering.size() == 0);
  }

  /**
   * Tests that the computeNodeOrderingByEdgeDifference method.
   */
  @Test
  public void testComputeNodeOrderingByEdgeDifference() {
    System.out.println("------------testComputeNodeOrderingByEdgeDifference"
        + "--------------");
    RoadNetwork rn = createSampleGraph();
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    ch.computeNodeOrderingByEdgeDifference();
    List<Long> randomPosNodeIds = ch.getNodeOrdering();
    List<Long> realOrdering = rn.getNodeIds();
    Map<Long, Integer> orderingMap = ch.getNodeOrderingMap();

    System.out.println(realOrdering);
    System.out.println(randomPosNodeIds);
    System.out.println(orderingMap);

    // Checks if both lists have the same size in the end.
    Assert.assertTrue(randomPosNodeIds.size() == realOrdering.size());

    for (int i = 0; i < randomPosNodeIds.size(); i++) {
      Long nodeId = randomPosNodeIds.get(i);
      Assert.assertEquals(orderingMap.get(nodeId), new Integer(i));
      realOrdering.remove(nodeId);
    }
    // Checks if the elements in the reordered list are the same
    // as in the old list.
    Assert.assertTrue(realOrdering.size() == 0);
    
    PriorityQueue<ContractedNode> pq = ch.getPriorityQueue();
    System.out.println("PRIORITY QUEUE");
    System.out.println(pq);
    
    String expectedQueue = "[ID: 1 ED: -3, ID: 2 ED: -2, ID: 4 ED: -1,"
      + " ID: 8 ED: -2, ID: 7 ED: -2, ID: 6 ED: -1, ID: 5 ED: -1,"
      + " ID: 3 ED: -1, ID: 9 ED: -1, ID: 10 ED: -1, ID: 11 ED: -2,"
      + " ID: 12 ED: -1, ID: 13 ED: 0]";
    Assert.assertEquals(expectedQueue, pq.toString());
  }

  /**
   * Tests that all arcs are flags after creating a ContractionHierarchies
   * object.
   */
  @Test
  public void testArcFlags() {
    System.out.println("------------------------testArcFlags"
        + "------------------------");
    RoadNetwork rn = createSampleGraph();
    List<List<Arc>> adjacentArcs = rn.getAdjacentArcs();

    ContractionHierarchies ch = new ContractionHierarchies(rn);
    ch.setAllArcsToTrue();

    boolean allArcsAreFlags = true;

    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        if (!currentArc.arcFlag) {
          allArcsAreFlags = false;
        }
      }
    }
    Assert.assertTrue(allArcsAreFlags);
  }

  /**
   * Tests if the contraction works testing the same example of the lecture.
   */
  @Test
  public void testContraction() {
    System.out.println("------------------------testContraction"
        + "------------------------");
    RoadNetwork rn = createContractionSampleGraph();
    System.out.println(rn.asString());
    // We have to contract the same node as in the lecture, which is V
    ContractionHierarchies ch = new ContractionHierarchies(rn);

    // We are not invoking precompute(), therefore
    // we have to set all arcs as flags manually
    ch.setAllArcsToTrue();

    System.out.println(rn.getNodeIds());

    // we have to set manually the nodeOrdering list
    List<Long> ordering = new ArrayList();
    ordering.add(rn.getNodeIds().get(1));
    ordering.add(rn.getNodeIds().get(0));
    ordering.add(rn.getNodeIds().get(2));
    ordering.add(rn.getNodeIds().get(3));
    ordering.add(rn.getNodeIds().get(4));
    ordering.add(rn.getNodeIds().get(5));

    ch.setNodeOrdering(ordering);

    // Now we contract the node V with ID = 3
    List<Integer> info = ch.contractNode(0, false);

    System.out.println(rn.asString());
    String expectedValue = "1|3-4-2-\n3|1-2-4-5-6-\n2|3-1-4-5-6-"
        + "\n4|1-3-5-2-\n5|3-4-6-2-\n6|3-5-2-\n";
    Assert.assertEquals(expectedValue, rn.asString());
    //Checks the numer of 
    Assert.assertEquals(new Integer(4), info.get(0));
    Assert.assertEquals(new Integer(-1), info.get(1));
    
  }

  /**
   * Tests if the contraction works testing the same example of the lecture.
   */
  @Test
  public void testContractionInGraph() {
    System.out.println("----------------------testContractionGRAPH"
        + "----------------------");
    RoadNetwork rn = createSampleGraph();
    System.out.println(rn.asString());
    // We have to contract the same node as in the lecture, which is V
    ContractionHierarchies ch = new ContractionHierarchies(rn);

    // We are not invoking precompute(), therefore
    // we have to set all arcs as flags manually
    List<List<Arc>> adjacentArcs = rn.getAdjacentArcs();
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        currentArc.arcFlag = true;
      }
    }

    // we have to set manually the nodeOrdering list
    // as in the lecture.
    List<Long> ordering = new ArrayList();
    ordering.add(1L);
    ordering.add(2L);
    ordering.add(13L);
    ordering.add(11L);
    ordering.add(10L);
    ordering.add(7L);
    ordering.add(8L);
    ordering.add(3L);
    ordering.add(5L);
    ordering.add(6L);
    ordering.add(9L);
    ordering.add(12L);
    ordering.add(4L);

    ch.setNodeOrdering(ordering);

    for (int i = 0; i < ordering.size(); i++) {
      ch.contractNode(i, false);
    }

    System.out.println(rn.asString());
    String expectedValue = "1|2-4-7-\n2|1-4-3-\n4|1-2-3-6-9-12-\n"
        + "3|2-4-5-\n7|1-6-10-\n6|4-7-5-9-\n5|3-6-8-\n8|5-9-11-\n"
        + "9|6-10-8-12-4-\n10|7-9-13-\n11|8-13-12-\n12|9-13-11-4-\n"
        + "13|10-12-11-\n";

    Assert.assertEquals(expectedValue, rn.asString());
  }

  /**
   * Tests if the contraction works testing the same example of the lecture.
   */
  @Test
  public void testContractionWithBooleanInGraph() {
    System.out.println("----------------------testContractionWithBooleanGRAPH"
        + "----------------------");
    RoadNetwork rn = createSampleGraph();
    String graphBeforeContractions = rn.asString();
    System.out.println(rn.asString());
    // We have to contract the same node as in the lecture, which is V
    ContractionHierarchies ch = new ContractionHierarchies(rn);

    // We are not invoking precompute(), therefore
    // we have to set all arcs as flags manually
    List<List<Arc>> adjacentArcs = rn.getAdjacentArcs();
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        currentArc.arcFlag = true;
      }
    }

    // we have to set manually the nodeOrdering list
    // as in the lecture.
    List<Long> ordering = new ArrayList();
    ordering.add(1L);
    ordering.add(2L);
    ordering.add(13L);
    ordering.add(11L);
    ordering.add(10L);
    ordering.add(7L);
    ordering.add(8L);
    ordering.add(3L);
    ordering.add(5L);
    ordering.add(6L);
    ordering.add(9L);
    ordering.add(12L);
    ordering.add(4L);

    ch.setNodeOrdering(ordering);

    int totalEdgeDifference = 0;
    int totalAddedShortcuts = 0;
    for (int i = 0; i < ordering.size(); i++) {
      List<Integer> info = ch.contractNode(i, true);
      int currentAddedshortcuts = info.get(0);
      int currentDifference = info.get(1);
      System.out.println("EDGE DIFFERENCE CONTRACTING " + (i + 1));
      System.out.println(currentDifference);
      System.out.println("ADDED SHORTCUTS in " + (i + 1));
      System.out.println(currentAddedshortcuts);
      totalEdgeDifference = totalEdgeDifference + currentDifference;
      totalAddedShortcuts = totalAddedShortcuts + currentAddedshortcuts;
    }

    System.out.println(rn.asString());
    String graphAfterContractions = rn.asString();
    System.out.println("TOTAL EDGE DIFFERENCE: " + totalEdgeDifference);
    Assert.assertEquals(graphBeforeContractions, graphAfterContractions);

    // test that all arcs are set to true after invoking this method
    adjacentArcs = rn.getAdjacentArcs();
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        if (!currentArc.arcFlag) {
          System.out.println("AYYYYYYYYYYYYYYYYYYY::::::::"
              + currentArc.getHeadNode().getId());
          System.out.println(k);
        }
        Assert.assertTrue(currentArc.arcFlag);
      }
    }
    Assert.assertEquals(new Integer(-20), new Integer(totalEdgeDifference));
    Assert.assertEquals(new Integer(1), new Integer(totalAddedShortcuts));
  }
  
  /**
   * Tests the precomputation method with lazy heuristic.
   */
  @Test
  public void testPrecomputationLazy() {
    System.out.println("----------------------testPrecomputationLazy"
        + "----------------------");
    RoadNetwork rn = createSampleGraph();
    System.out.println(rn.asString());
    
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    ch.precomputationLazy();
    
    List<Long> nodeOrdering = ch.getNodeOrdering();
    PriorityQueue<ContractedNode> pq = ch.getPriorityQueue();    
    Map<Long, Integer> orderingMap = ch.getNodeOrderingMap();
    System.out.println(nodeOrdering);
    System.out.println(orderingMap);
    
    Assert.assertTrue(pq.isEmpty());
    Assert.assertEquals(new Integer(nodeOrdering.size()), 
        new Integer(rn.getNodeIds().size()));
    
    for (int i = 0; i < nodeOrdering.size(); i++) {
      Long nodeId =  nodeOrdering.get(i);
      Assert.assertEquals(orderingMap.get(nodeId), new Integer(i));
    }       
  }
  
  /**
   * Tests the precomputation method with lazy heuristic on the second 
   * sample graph.
   */
  @Test
  public void testPrecomputationLazy2() {
    System.out.println("----------------------testPrecomputationLazy2"
        + "----------------------");
    RoadNetwork rn = createSampleGraph2();
    System.out.println(rn.asString());
    
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    ch.precomputationLazy();
    
    List<Long> nodeOrdering = ch.getNodeOrdering();
    PriorityQueue<ContractedNode> pq = ch.getPriorityQueue();    
    Map<Long, Integer> orderingMap = ch.getNodeOrderingMap();
    System.out.println(nodeOrdering);
    System.out.println(orderingMap);
    
    Assert.assertTrue(pq.isEmpty());
    Assert.assertEquals(new Integer(nodeOrdering.size()), 
        new Integer(rn.getNodeIds().size()));
    
    for (int i = 0; i < nodeOrdering.size(); i++) {
      Long nodeId =  nodeOrdering.get(i);
      Assert.assertEquals(orderingMap.get(nodeId), new Integer(i));
    }       
  }

  /**
   * 
   * Test for computeShortestPath in ContractionHierarchies.
   */
  @Test
  public void computeShortestPathTest() {
    System.out
      .println("--------------------computeShortestPath "
      + "with 2 dijkstras--------------");
    RoadNetwork rn = createSampleGraph();
    // System.out.println(rn.asString());
    DijkstraAlgorithm dij = new DijkstraAlgorithm(rn);
    ContractionHierarchies ch = new ContractionHierarchies(rn);

    long node1 = rn.getRandomNodeId();
    long node2 = rn.getRandomNodeId();
    Integer spdij = dij.computeShortestPath(node1, node2);
    Integer spch = ch.computeShortestPath(node1, node2);

    String spReported = spch.toString();
    String spExpected = spdij.toString();

    // Special case to, in which we could see that we are using
    // the dist relaxed to compute min {distUs + distUt}
    Integer spdij2 = dij.computeShortestPath(11, 13);
    Integer spch2 = ch.computeShortestPath(11, 13);

    String spReported2 = spch.toString();
    String spExpected2 = spdij.toString();

    System.out.println("Dijkstra from node: " + node1 + " to: " + node2
        + " SP: " + spdij);
    System.out.println("2DijkstraCH from node: " + node1 + " to: " + node2
        + " SP: " + spch);

    System.out.println("Dijkstra from node: 11 to: 13 SP: " + spdij2);
    System.out.println("2DijkstraCH from node: 11 to: 13 SP: " + spch2);

    Assert.assertEquals(spReported, spExpected);
    Assert.assertEquals(spReported2, spExpected2);
    System.out
      .println("--------------------computeShortestPath" 
      + " with 2 dijkstras--------------");
  }
}
