package routeplanning;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing class of class Node.
 * @author AAA
 */
public class RoadNetworkTest {
  /**
   * Generic method which reads from file and builds a road network.
   */
  public RoadNetwork buildGraphFromOsm() {
    RoadNetwork rn = new RoadNetwork();
    URL url = this.getClass().getClassLoader().getResource(
      "routeplanning/resources/osmTest_reduced.osm");
    rn.readFromOsmFile(url.getPath().replace("%20", " "));
    return rn;
  }
  
  /**
   * Tests constructor and string representation of object.
   */
  @Test
  public void testRoadNetwork() {
    RoadNetwork rn = new RoadNetwork();
    List<Long> nodes =  rn.getNodeIds();
    List<List<Arc>> adjacentArcs = rn.getAdjacentArcs();
    Assert.assertTrue("Node's structure was created", nodes.size() >= 0);
    Assert.assertTrue("Arc's structure was created", adjacentArcs.size() >= 0);
  }

  /**
   * Tests setNodes() and getNodes().
   */
  @Test
  public void testSetGetNodes() {
    List<Long> testNodes = new ArrayList<Long>();
    //Adds 5 elements to the list
    for (int i = 0; i < 5; i++) {
      testNodes.add(new Long(i));
    }
    RoadNetwork rn = new RoadNetwork();
    rn.setNodes(testNodes);
    Assert.assertTrue("", (rn.getNodeIds()).size() == 5);
  }
  
  /**
   * Tests setAdjacentArcs() and getAdjacentArcs().
   */
  @Test
  public void testSetGetAdjacentArcs() {
    List<List<Arc>> testAdjArcs = new ArrayList<List<Arc>>();
    //Adds 5 lists to the list and for each list 3 arcs
    for (Integer i = 0; i < 5; i++) {
      List<Arc> arcs = new ArrayList<Arc>();
      for (Integer k = 0; k < 3; k++) {
        Integer nodeId = Integer.parseInt(
          new String(i.toString() + k.toString()));
        Arc newArc = new Arc(new Node(nodeId, 1.0f, 1.0f), 1);
        arcs.add(newArc);
      }
      testAdjArcs.add(arcs);
    }
    
    RoadNetwork rn = new RoadNetwork();
    rn.setAdjacentArcs(testAdjArcs);
    
    int numberOfArcs = 0;
    for (int i = 0; i < rn.getAdjacentArcs().size(); i++) { 
      List<Arc> arcs = rn.getAdjacentArcs().get(i);
      for (int k = 0; k < arcs.size(); k++) {
        numberOfArcs++;
      } 
    }
    Assert.assertTrue("ALL ARCS are inserted", numberOfArcs == 15);
  }

  /**
   * This method will test the graph shown in the lecture.
   */
  @Test
  public void testLectureGraph() {
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
    
    //Adding irrelevant nodes:
    Node nodex = new Node(100, 1.0f, 1.0f);
    Node nodey = new Node(101, 1.0f, 1.0f);
    Node nodez = new Node(105, 1.0f, 1.0f);
    
    rn.addNodeToGraph(nodex);
    rn.addNodeToGraph(nodey);
    rn.addNodeToGraph(nodez);
        
    String stringRep = rn.asString();
    String expectedRep = new String("0|0-1-2-3-4-\n1|1-0-3-"
      + "\n2|2-0-3-5-\n3|3-0-1-2-4-\n4|4-0-3-5-\n5|5-2-4-\n");
    Assert.assertEquals(stringRep, expectedRep);
  }

  
  /**
   * This method tests if nodes added are saved in the structure allNodesIds.
   */
  @Test
  public void testAddNodeToGraph() {
    RoadNetwork rn = new RoadNetwork();
    
    Node node0 = new Node(0, 1.0f, 1.0f);
    Node node1 = new Node(1, 1.0f, 1.0f);
    Node node2 = new Node(2, 1.0f, 1.0f);
    Node node3 = new Node(3, 1.0f, 1.0f);
    Node node4 = new Node(4, 1.0f, 1.0f);
    Node node5 = new Node(5, 1.0f, 1.0f);
    
    Node nullNode = null;
    
    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(node2);
    rn.addNodeToGraph(node3);
    rn.addNodeToGraph(node4);
    rn.addNodeToGraph(node5);
    
    Map<Long, Node> expectedNodeMap = new HashMap<Long, Node>();
    expectedNodeMap.put(0L, node0);
    expectedNodeMap.put(1L, node1);
    expectedNodeMap.put(2L, node2);
    expectedNodeMap.put(3L, node3);
    expectedNodeMap.put(4L, node4);
    expectedNodeMap.put(5L, node5);    
    
    Assert.assertEquals(expectedNodeMap, rn.getMapNodeId());
    
    rn.addNodeToGraph(nullNode);
    Assert.assertEquals(expectedNodeMap, rn.getMapNodeId());
  }

  /**
   * This method tests addAdjacentArc().
   */
  @Test
  public void testAddAdjacentArc() {
    RoadNetwork rn = new RoadNetwork();
    
    Node node0 = new Node(0, 1.0f, 1.0f);
    Node node1 = new Node(1, 1.0f, 1.0f);
    Node ghostNode = new Node(5, 1.0f, 1.0f);
    Node nullNode = null;
    
    Arc arc00 = new Arc(node0, 0);
    Arc arc01 = new Arc(node1, 10);
    Arc arc10 = new Arc(node0, 10);
    Arc arc11 = new Arc(node1, 11);
    Arc arc0null = new Arc(nullNode, 5);
    Arc arcnull0 = new Arc(node0, 5);
    Arc arcGhost0 = new Arc(node0, 4);
    
    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(nullNode);
    rn.addAdjacentArc(node0, arc00);
    rn.addAdjacentArc(node0, arc01);
    rn.addAdjacentArc(node1, arc10);
    rn.addAdjacentArc(node1, arc11);
    rn.addAdjacentArc(node0, arc0null);
    rn.addAdjacentArc(nullNode, arcnull0);
    rn.addAdjacentArc(ghostNode, arcGhost0);
    
    //Adding irrelevant nodes:
    Node nodex = new Node(100, 1.0f, 1.0f);
    Node nodey = new Node(101, 1.0f, 1.0f);
    Node nodez = new Node(1055, 1.0f, 1.0f);
    
    rn.addNodeToGraph(nodex);
    rn.addNodeToGraph(nodey);
    rn.addNodeToGraph(nodez);
    
    String expectedString = new String("0|0-1-\n1|0-1-\n5|0-\n");
    Assert.assertEquals(expectedString, rn.asString());
  }
  
  /**
   * This method tests reduceToLargestConnectedComponent().
   */
  @Test
  public void testReduceToLargestConnectedComponent() {
    //To test this feature I'll create three disconnected subgraphs 
    //and I'll test if it selects the largest component
    
    //The first subgraph is the lecture graph:
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
    
    //The second component consists of only connected nodes:
    Node node6 = new Node(6, 1.0f, 1.0f);
    Node node7 = new Node(7, 1.0f, 1.0f);
    
    Arc newArc66 = new Arc(node6, 0);
    Arc newArc77 = new Arc(node7, 0);

    Arc newArc67 = new Arc(node7, 1);
    Arc newArc76 = new Arc(node6, 1);
    
    rn.addAdjacentArc(node6, newArc66);
    rn.addAdjacentArc(node7, newArc77);
    rn.addAdjacentArc(node6, newArc67);
    rn.addAdjacentArc(node7, newArc76);

    //Third component
    Node node8 = new Node(8, 1.0f, 1.0f);
    Node node9 = new Node(9, 1.0f, 1.0f);
    
    Arc arc88 = new Arc(node8, 0);
    Arc arc89 = new Arc(node9, 10);
    Arc arc98 = new Arc(node8, 10);
    Arc arc99 = new Arc(node9, 0);
    
    rn.addNodeToGraph(node8);
    rn.addNodeToGraph(node9);
    rn.addAdjacentArc(node8, arc88);
    rn.addAdjacentArc(node8, arc89);
    rn.addAdjacentArc(node9, arc98);
    rn.addAdjacentArc(node9, arc99);
    
    //Adding irrelevant nodes:
    Node nodex = new Node(100, 1.0f, 1.0f);
    Node nodey = new Node(101, 1.0f, 1.0f);
    Node nodez = new Node(105, 1.0f, 1.0f);
    
    rn.addNodeToGraph(nodex);
    rn.addNodeToGraph(nodey);
    rn.addNodeToGraph(nodez);
    
    RoadNetwork lcc = rn.reduceToLargestConnectedComponent();
    String stringRep = lcc.asString();
    String expectedRep = new String("0|0-1-2-3-4-\n1|1-0-3-"
      + "\n2|2-0-3-5-\n3|3-0-1-2-4-\n4|4-0-3-5-\n5|5-2-4-\n");
    Assert.assertEquals(stringRep, expectedRep);
  }
  
//We test the same methods using an OSM File:
  /**
   * Tests if the correct graph is build from OSM.
   */
  @Test
  public void testGraphFromOsm() {
    RoadNetwork rn = buildGraphFromOsm();
        
    String expectedRep = new String("3|0-4-2-1-\n0|3-2-1-4-\n4|3-5-0-\n5"
      + "|4-2-\n2|5-0-3-\n1|0-3-\n7|6-\n6|7-\n");
    Assert.assertEquals(rn.asString(), expectedRep);
    System.out.println();
    Assert.assertTrue("Number of overall nodes failed", 
        rn.getMapNodeId().size() == 11);
    Assert.assertTrue("Number of nodes with arcs failed", 
        rn.getNodeIds().size() == 8);
    Assert.assertTrue("Number of arcs failed", rn.getNumberOfArcs() == 10);
  }
  
  /**
   * Tests if the reduction to the largest component works from OSM.
   */
  @Test
  public void testReduceGraphFromOsm() {
    RoadNetwork rn = buildGraphFromOsm();
    RoadNetwork lc = rn.reduceToLargestConnectedComponent();
    String expectedRep = new String("0|3-2-1-4-\n1|0-3-\n2|5-0-3-\n3|0-4-2-1-\n"
      + "4|3-5-0-\n5|4-2-\n");
    
    Assert.assertEquals(lc.asString(), expectedRep);
  }
  
  /**
   * Tests the distance between two nodes.
   */
  @Test
  public void testDistance() {
    
    RoadNetwork rn = new RoadNetwork();
    
    Node node0 = new Node(0, 49.3705278f, 7.3613889f);
    Node node1 = new Node(1, 49.3705278f, 7.3611944f);
    
    double distanceInKMeters = rn.getDistance2(node0, node1);
    
    int cost = rn.computeCost("motorway_link", distanceInKMeters);
    System.out.println(cost);
    
  }
  
  /**
   * Tests the method which gives you approximated source and target nodes
   * given lat and long coordinates.
   */
 
/*@Test
  public void testGetNodeIdsFromCoordinates() {
   
    RoadNetwork rn = new RoadNetwork();
    rn.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
      + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
    
    RoadNetwork largestComponent = rn.reduceToLargestConnectedComponent();
   
    int randomSourceId = largestComponent.getRandomNodeId();
    int randomTargetId = largestComponent.getRandomNodeId();
    
    Node randomSourceNode = largestComponent.getMapNodeId().get(randomSourceId);
    Node randomTargetNode = largestComponent.getMapNodeId().get(randomTargetId);
    
    double sourceLat = randomSourceNode.getLatitude();
    double sourceLong = randomSourceNode.getLongitude();
    
    double targetLat = randomTargetNode.getLatitude();
    double targetLong = randomTargetNode.getLongitude();
    
    List<Integer> nodes = 
        largestComponent.getNodeIdsFromCoordinates(
            sourceLat, sourceLong, targetLat, targetLong);
    
    Assert.assertEquals(new Integer(randomSourceId), nodes.get(0));
    Assert.assertEquals(new Integer(randomTargetId), nodes.get(1));
    
    sourceLat = randomSourceNode.getLatitude() + 0.00000075;
    sourceLong = randomSourceNode.getLongitude() + 0.00000075;
    
    targetLat = randomTargetNode.getLatitude() + 0.00000075;
    targetLong = randomTargetNode.getLongitude() + 0.00000075;
    
    nodes = largestComponent.getNodeIdsFromCoordinates(
            sourceLat, sourceLong, targetLat, targetLong);
    
    Assert.assertEquals(new Integer(randomSourceId), nodes.get(0));
    Assert.assertEquals(new Integer(randomTargetId), nodes.get(1));    
  }
*/
}
