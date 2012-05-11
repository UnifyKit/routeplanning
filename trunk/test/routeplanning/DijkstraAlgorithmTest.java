package routeplanning;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing class of class DijkstraAlgorithm.
 * @author AAA
 */
public class DijkstraAlgorithmTest {
  
   /**
   * Creates a road network that can be used in other classes.
   * @param graph
   */
  public RoadNetwork createSampleGraph() {
    RoadNetwork rn = new RoadNetwork();
    Node node0 = new Node(0, 1.0, 1.0);
    Node node1 = new Node(1, 1.0, 1.0);
    Node node2 = new Node(2, 1.0, 1.0);
    Node node3 = new Node(3, 1.0, 1.0);
    Node node4 = new Node(4, 1.0, 1.0);
    Node node5 = new Node(5, 1.0, 1.0);
    
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
    Integer cost = alg.computeShortestPath(0, 4);
    Integer expectedValue = 3;
    Assert.assertEquals(cost, expectedValue);
  }

  /**
   * Tests if test visited Nodes returns the expected costs.
   */
  @Test
  public void testGetVisitedNodes() {
    RoadNetwork rn = createSampleGraph();
    DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
    Integer cost = alg.computeShortestPath(0, 4);
    List<Integer> visitedNodes = alg.getVisitedNodes();
    
    boolean conformsResult = true;
    
    for (int i = 0; i < visitedNodes.size(); i++) {
      if (visitedNodes.get(i) != null) {
        if (i == 1) {
          if (visitedNodes.get(i) != 1) {
            conformsResult = false;
          }
        }
        if (i == 3) {
          if (visitedNodes.get(i) != 2) {
            conformsResult = false;
          }
        }
        if (i == 4) {
          if (visitedNodes.get(i) != 3) {
            conformsResult = false;
          }
        }
      }
    }
    Assert.assertTrue(conformsResult);
  }
}

