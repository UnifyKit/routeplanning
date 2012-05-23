package routeplanning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class ArcFlagsAlgorithm.
 * @author CJC
 */
public class ArcFlagsAlgorithm {
  /**
   * Inner RoadNetwork object.
   */
  RoadNetwork graph;
  
  /**
   * Inner DijkstraAlgorithm object.
   */
  DijkstraAlgorithm dijkstra;
  
  /**
   * Latitude (upper border).
   */
  int latMin;
  
  /**
   * Latitude (lower border).
   */
  int latMax;
  
  /**
   * Longitude (left border).
   */
  int lngMin;
  
  /**
   * Longitude (right border).
   */
  int lngMax;
  
  /**
   * Constructor.
   */
  public ArcFlagsAlgorithm(RoadNetwork rn) {
    this.graph = rn;
    dijkstra = new DijkstraAlgorithm(graph);
    dijkstra.setConsiderArcFlags(true);
  }
  
  /**
   * Precomputation.
   */  
  public void precomputeArcFlags(double latMin, double latMax, 
      double lngMin, double lngMax) {
    List<Arc> arcs;
    List<Integer> boundaryNodes = new ArrayList<Integer>();
    Arc arc0;
    Node headNode;
    Node tailNode;
    
   //compute boundary nodes
    for (int i = 0; i < graph.getAdjacentArcs().size(); i++) {
      arcs = graph.getAdjacentArcs().get(i);
      tailNode = graph.getMapNodeId().get(graph.getNodeIds().get(i));
      //if tailNode is in region, check if it is a boundary node
      if (isInRegion(latMin, latMax, lngMin, lngMax, tailNode)) {
        for (int j = 0; j < arcs.size(); j++) {
          arc0 = arcs.get(j);
          headNode = arc0.headNode;
          if (!isInRegion(latMin, latMax, lngMin, lngMax, headNode)) {
            if (!boundaryNodes.contains(tailNode.id)) {
              boundaryNodes.add(tailNode.id);
            }
            //break;
          } else {
            //set arcFlag to true for arcs inside the region
            arc0.arcFlag = true;
          }
        }
      }
    }
    
    System.out.println("---All boundary nodes found---");
    System.out.println("#Of Boundary nodes: " + boundaryNodes.size());
    
    //compute Dijkstra for each of the boundary nodes
    //saves the map parents into a list
    for (int i = 0; i < boundaryNodes.size(); i++) {
      int boundaryNodeId  = boundaryNodes.get(i);
      DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(graph);
      dijAlg.computeShortestPath(boundaryNodeId, -1);
      
      //we set flags
      Map<Integer, Integer> parents = dijAlg.getParents();
      Iterator<Integer> it = parents.keySet().iterator();
      while (it.hasNext()) {
        Integer currentNodeId = (Integer) it.next();
        Integer parentNodeId = parents.get(currentNodeId);
        
        //System.out.println(currentNodeId + "-" +  parentNodeId);
        
        if (parentNodeId != -1) {
          List<Arc> allArcs = graph.getNodeAdjacentArcs(currentNodeId);
        
          for (int k = 0; k < allArcs.size(); k++) {
            Arc arc = allArcs.get(k);
            if (arc.getHeadNode().getId().equals(parentNodeId)) {
              arc.arcFlag = true;
            }
          }
        }
      }
      System.out.print(i + " - ");
    }
    System.out.println("---Dijkstra for all boundary node completed---");
    
/*    for (int i = 0; i < parentsOfBoundaryNodes.size(); i++) {
      Map<Integer, Integer> parents = parentsOfBoundaryNodes.get(i);
      Iterator<Integer> it = parents.keySet().iterator();
      while (it.hasNext()) {
        Integer currentNode = (Integer) it.next();
        Integer parentNode = parents.get(currentNode);
        List<Arc> allArcs = graph.getNodeAdjacentArcs(parentNode);
        
        for (int k = 0; k < allArcs.size(); k++) {
          Arc arc = allArcs.get(k);
          if (arc.getHeadNode().getId() == currentNode) {
            arc.arcFlag = true;
          }
        }
      }
    }*/
    
    //exportToFile(boundaryNodes);
    System.out.println("Finish boundary nodes!");
    
   //TODO: mark arcFlags to arcs in shortest paths to boundary nodes. 
  }
  
  /**
   * Returns true if a given node is in the region described by the 
   * first four parameters.
   */ 
  public boolean isInRegion(double latMin, double latMax, double lngMin,
      double lngMax, Node node) {
    boolean isInRegion = false;
    if (node.latitude > latMin && node.latitude < latMax 
        && node.longitude > lngMin && node.longitude < lngMax) {
      isInRegion = true;
    }
    return isInRegion;
  }

  
  /**
   * Invokes Dijkstra algorithm.
   */  
  public int computeShortestPath(int sourceNodeId, int targetNodeId) {
    return dijkstra.computeShortestPath(sourceNodeId, targetNodeId);
  }
  
  /**
   * Returns the settled nodes from the inner Dijkstra Algorithm.
   */ 
  public Map<Integer, Integer> getVisitedNodes() {
    return dijkstra.getVisitedNodes();
  }
  
  public Map<Integer, Integer> getParents() {
    return dijkstra.parents;
  }
  
  
//  public void exportToFile(List<Node> nodes, String pathOut) {
//    //String pathOut = "D:/workspace/routeplanning/src/routeplanning/
//    //resources/boundaryNodes.txt";
//    try {
//      BufferedWriter outWriter = new BufferedWriter(new FileWriter(pathOut));
//      for (int i = 0; i < nodes.size(); i++) {
//        outWriter.newLine();
//        outWriter.write(nodes.get(i).latitude + ";" + nodes.get(i).longitude);
//        //System.out.println(nodes.get(i).latitude + ";" 
//        //+ nodes.get(i).longitude);
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    
//  }
}
