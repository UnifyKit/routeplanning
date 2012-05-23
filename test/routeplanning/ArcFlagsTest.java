package routeplanning;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing class of class ArcFlagsAlgorithm.
 * @author AAA
 */
public class ArcFlagsTest {

//  /**
//   * Tests if the target node belongs to the given region.
//   */  
//  @Test
//  public void testRandomNodeInRegion() {
//    RoadNetwork rn = new RoadNetwork();
//    rn.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
//      + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
//    ArcFlagsAlgorithm afg = new ArcFlagsAlgorithm(rn);
//    int targetNodeId = rn
//        .getRandomNodeIdWithinRegion(49.20, 49.25, 6.95, 7.05);
//    Node targetNode = rn.getMapNodeId().get(targetNodeId);
//    Assert.assertTrue(targetNode.getLatitude() > 49.20);
//    Assert.assertTrue(targetNode.getLatitude() < 49.25);
//    Assert.assertTrue(targetNode.getLongitude() > 6.95);
//    Assert.assertTrue(targetNode.getLongitude() < 7.05);
//    System.out.println(targetNode.getId());
//  }
//  
//  /**
//   * Tests if every boundary node is marking n-1 arcs as flags,
//   * where n is the number of nodes in the graph.
//   */  
//  @Test
//  public void testPrecomputation() {
//    RoadNetwork rn = new RoadNetwork();
//    rn.readFromOsmFile("E:/Documents/UNI/SS12/Efficient Route Planning/"
//      + "groupRepository/src/routeplanning/resources/saarland_reduced.osm");
//    ArcFlagsAlgorithm afg = new ArcFlagsAlgorithm(rn);
//    afg.precomputeArcFlags(49.20, 49.25, 6.95, 7.05);
//    List<Integer> boundaryNodes = afg.getBoundaryNodes();
//
//    for (int i = 0; i < boundaryNodes.size(); i++) {
//      DijkstraAlgorithm alg = new DijkstraAlgorithm(rn);
//      alg.computeShortestPath(boundaryNodes.get(i), -1);
//      
//      Map<Integer, Integer> parents = alg.getParents();
//      Iterator<Integer> it = parents.keySet().iterator();
//      while (it.hasNext()) {
//        Integer currentNodeId = (Integer) it.next();
//        Integer parentNodeId = parents.get(currentNodeId);
//
//        // System.out.println(currentNodeId + "-" + parentNodeId);
//
//        if (parentNodeId != -1) {
//          List<Arc> allArcs = rn.getNodeAdjacentArcs(currentNodeId);
//
//          for (int k = 0; k < allArcs.size(); k++) {
//            Arc arc = allArcs.get(k);
//            if (arc.getHeadNode().getId().equals(parentNodeId)) {
//              Assert.assertEquals(arc.arcFlag, true);
//            }
//          }
//        }
//      }
//    }
//  }
}
