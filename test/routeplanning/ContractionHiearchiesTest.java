package routeplanning;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing class of class ContractionHiearchies.
 * @author AAA
 */
public class ContractionHiearchiesTest {
  
  /**
   * Creates a road network representing the example slide 16 lecture 6.
   */
  public RoadNetwork createSampleGraph() {
    RoadNetwork rn = new RoadNetwork();
    Node nodeA = new Node(1, 1.0, 1.0);
    Node nodeB = new Node(2, 1.0, 1.0);
    Node nodeC = new Node(3, 1.0, 1.0);
    Node nodeD = new Node(4, 1.0, 1.0);
    Node nodeE = new Node(5, 1.0, 1.0);
    Node nodeF = new Node(6, 1.0, 1.0);
    Node nodeG = new Node(7, 1.0, 1.0);
    Node nodeH = new Node(8, 1.0, 1.0);
    Node nodeI = new Node(9, 1.0, 1.0);
    Node nodeJ = new Node(10, 1.0, 1.0);
    Node nodeK = new Node(11, 1.0, 1.0);
    Node nodeL = new Node(12, 1.0, 1.0);
    Node nodeM = new Node(13, 1.0, 1.0);
    
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
    
    Arc newArcAB = new Arc(nodeB, 3);  rn.addAdjacentArc(nodeA, newArcAB);
    Arc newArcAD = new Arc(nodeD, 4);  rn.addAdjacentArc(nodeA, newArcAD);
    Arc newArcBD = new Arc(nodeD, 5);  rn.addAdjacentArc(nodeB, newArcBD);
    Arc newArcBC = new Arc(nodeC, 2);  rn.addAdjacentArc(nodeB, newArcBC);
    Arc newArcCD = new Arc(nodeD, 2);  rn.addAdjacentArc(nodeC, newArcCD);
    Arc newArcAG = new Arc(nodeG, 7);  rn.addAdjacentArc(nodeA, newArcAG);
    Arc newArcDF = new Arc(nodeF, 1);  rn.addAdjacentArc(nodeD, newArcDF);
    Arc newArcFG = new Arc(nodeG, 4);  rn.addAdjacentArc(nodeF, newArcFG);
    Arc newArcCE = new Arc(nodeE, 5);  rn.addAdjacentArc(nodeC, newArcCE);
    Arc newArcEF = new Arc(nodeF, 3);  rn.addAdjacentArc(nodeE, newArcEF);
    Arc newArcEH = new Arc(nodeH, 7);  rn.addAdjacentArc(nodeE, newArcEH);
    Arc newArcFI = new Arc(nodeI, 1);  rn.addAdjacentArc(nodeF, newArcFI);
    Arc newArcGJ = new Arc(nodeJ, 6);  rn.addAdjacentArc(nodeG, newArcGJ);
    Arc newArcIJ = new Arc(nodeJ, 3);  rn.addAdjacentArc(nodeI, newArcIJ);
    Arc newArcHI = new Arc(nodeI, 3);  rn.addAdjacentArc(nodeH, newArcHI);
    Arc newArcHK = new Arc(nodeK, 4);  rn.addAdjacentArc(nodeH, newArcHK);
    Arc newArcIL = new Arc(nodeL, 1);  rn.addAdjacentArc(nodeI, newArcIL);
    Arc newArcJM = new Arc(nodeM, 5);  rn.addAdjacentArc(nodeJ, newArcJM);
    Arc newArcLM = new Arc(nodeM, 2);  rn.addAdjacentArc(nodeL, newArcLM);
    Arc newArcKM = new Arc(nodeM, 4);  rn.addAdjacentArc(nodeK, newArcKM);
    Arc newArcKL = new Arc(nodeL, 3);  rn.addAdjacentArc(nodeK, newArcKL);    
    
    return rn;
  }
  
  /**
   * Creates a road network representing the example slide 14 lecture 6.
   */
  public RoadNetwork createContractionSampleGraph() {
    RoadNetwork rn = new RoadNetwork();
    Node nodeU1 = new Node(1, 1.0, 1.0);
    Node nodeU2 = new Node(2, 1.0, 1.0);
    Node nodeV  = new Node(3, 1.0, 1.0);
    Node nodeW1 = new Node(4, 1.0, 1.0);
    Node nodeW2 = new Node(5, 1.0, 1.0);
    Node nodeW3 = new Node(6, 1.0, 1.0);
    
    rn.addNodeToGraph(nodeU1);
    rn.addNodeToGraph(nodeU2);
    rn.addNodeToGraph(nodeV);
    rn.addNodeToGraph(nodeW1);
    rn.addNodeToGraph(nodeW2);
    rn.addNodeToGraph(nodeW3);
    
    //in both directions because it's unidirectional
    
    Arc newArcU1V = new Arc(nodeV, 1);  rn.addAdjacentArc(nodeU1, newArcU1V);
    Arc newArcVU1 = new Arc(nodeU1, 1);  rn.addAdjacentArc(nodeV, newArcVU1);
    
    Arc newArcU2V = new Arc(nodeV, 1);  rn.addAdjacentArc(nodeU2, newArcU2V);
    Arc newArcVU2 = new Arc(nodeU2, 1);  rn.addAdjacentArc(nodeV, newArcVU2);
    
    Arc newArcU1W1 = new Arc(nodeW1, 3);  rn.addAdjacentArc(nodeU1, newArcU1W1);
    Arc newArcW1U1 = new Arc(nodeU1, 3);  rn.addAdjacentArc(nodeW1, newArcW1U1);
    
    Arc newArcVW1 = new Arc(nodeW1, 2);  rn.addAdjacentArc(nodeV, newArcVW1);
    Arc newArcW1V = new Arc(nodeV, 2);  rn.addAdjacentArc(nodeW1, newArcW1V);
    
    Arc newArcVW2 = new Arc(nodeW2, 3);  rn.addAdjacentArc(nodeV, newArcVW2);
    Arc newArcW2V = new Arc(nodeV, 3);  rn.addAdjacentArc(nodeW2, newArcW2V);
    
    Arc newArcVW3 = new Arc(nodeW3, 4);  rn.addAdjacentArc(nodeV, newArcVW3);
    Arc newArcW3V = new Arc(nodeV, 4);  rn.addAdjacentArc(nodeW3, newArcW3V);
    
    Arc newArcW1W2 = new Arc(nodeW2, 1);  rn.addAdjacentArc(nodeW1, newArcW1W2);
    Arc newArcW2W1 = new Arc(nodeW1, 1);  rn.addAdjacentArc(nodeW2, newArcW2W1);
    
    Arc newArcW2W3 = new Arc(nodeW3, 1);  rn.addAdjacentArc(nodeW2, newArcW2W3);
    Arc newArcW3W2 = new Arc(nodeW2, 1);  rn.addAdjacentArc(nodeW3, newArcW3W2);
    
    return rn;
  }
  
  /**
   * Tests that the random generator for the swapping of positions
   * work as expected (within the limits of the list).
   */
  @Test
  public void testRoadNetwork() {
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
    RoadNetwork rn = createSampleGraph();
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    ch.computeRandomNodeOrdering();
    List<Integer> randomPosNodeIds = ch.getNodeOrdering();
    List<Integer> realOrdering = rn.getNodeIds();
    
    System.out.println(realOrdering);
    System.out.println(randomPosNodeIds);
    
    //Checks if both lists have the same size in the end.
    Assert.assertTrue(randomPosNodeIds.size() == realOrdering.size());
    
    for (int i = 0; i < randomPosNodeIds.size(); i++) {
      Integer nodeId =  randomPosNodeIds.get(i);
      realOrdering.remove(nodeId);
    }
    //Checks if the elements in the reordered list are the same
    //as in the old list.
    Assert.assertTrue(realOrdering.size() == 0);
  }
  
//  /**
//   * Tests that all arcs are flags after creating a ContractionHierarchies
//   * object.
//   */
//  @Test
//  public void testArcFlags() {
//    RoadNetwork rn = createSampleGraph();
//    ContractionHierarchies ch = new ContractionHierarchies(rn);
//    ch.precomputation();
//    
//    boolean allArcsAreFlags = true;
//    
//    List<List<Arc>> adjacentArcs = rn.getAdjacentArcs();
//    for (int i = 0; i < adjacentArcs.size(); i++) {
//      List<Arc> arcList = adjacentArcs.get(i);
//      for (int k = 0; k < arcList.size(); k++) {
//        Arc currentArc = arcList.get(k);
//        if (!currentArc.arcFlag) {
//          allArcsAreFlags = false;
//        }
//      }
//    }
//    Assert.assertTrue(allArcsAreFlags);
//  }
  
  /**
   * Tests if the contraction works testing the same example as
   * in the lecture.
   */
  @Test
  public void testContraction() {
    RoadNetwork rn = createContractionSampleGraph();
    //We have to contract the same node as in the lecture, which is V
    ContractionHierarchies ch = new ContractionHierarchies(rn);
    
    //We are not invoking precompute(), therefore
    //we have to set all arcs as flags manually
    List<List<Arc>> adjacentArcs = rn.getAdjacentArcs();
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        currentArc.arcFlag = true;
      }
    }
    //we have to set manually the nodeOrdering list
    List<Integer> ordering = new ArrayList();
    ordering.addAll(rn.getNodeIds());
    ch.setNodeOrdering(ordering);
    
    //Now we contract node at position 2 which is V and has ID = 3
    ch.contractNode(2);
    //Now we verify the resulting graph
    boolean contractionOk = true;
    
    //Node U1 Id = 1
    List<Arc> arcsU1 = rn.getNodeAdjacentArcs(1);
    for (int i = 0; i < arcsU1.size(); i++) {
      Arc currentArc = arcsU1.get(i);
      Integer nodeId = currentArc.getHeadNode().getId();
      //Arc U1 W1
      if (nodeId == 4) {
        if (!currentArc.arcFlag) {
          contractionOk = false;
        }
      }
    }
    
    //Node U2 Id = 2
    List<Arc> arcsU2 = rn.getNodeAdjacentArcs(2);
    
    //Node W1 Id = 4
    List<Arc> arcsW1 = rn.getNodeAdjacentArcs(4);
    
    //Node W2 Id = 5
    List<Arc> arcsW2 = rn.getNodeAdjacentArcs(5);
    
    //Node W3 Id = 6
    List<Arc> arcsW3 = rn.getNodeAdjacentArcs(6);
    
    Assert.assertTrue(contractionOk);
  }
}





















