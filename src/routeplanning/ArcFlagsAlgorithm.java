package routeplanning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcFlagsAlgorithm {
  RoadNetwork graph;
  DijkstraAlgorithm dijkstra;
  int latMin;
  int latMax;
  int lngMin;
  int lngMax;
  public ArcFlagsAlgorithm(RoadNetwork rn) {
    this.graph = rn;
  }
  public void precomputeArcFlags(double latMin, double latMax, double lngMin, double lngMax) {
    List<Arc> arcs;
    List<Integer> boundaryNodes = new ArrayList<Integer>();
    Arc arc0;
    Node headNode;
    Node tailNode;
    List<Map<Integer, Integer>> parents = new ArrayList<Map<Integer, Integer>>();  
    
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
            boundaryNodes.add(tailNode.id);
            //break;
          } else {
            //set arcFlag to true for arcs inside the region
            arc0.arcFlag = true;
          }
        }
      }
    }
    //exportToFile(boundaryNodes);
    System.out.println("Finish boundary nodes!");
    
   //TODO:run dijkstra for each boundary node
   //TODO: mark arcFlags to arcs in shortest paths to boundary nodes. 
   
     
  }
  public boolean isInRegion(double latMin, double latMax, double lngMin, double lngMax, Node node) {
    boolean isInRegion = false;
    if (node.latitude > latMin && node.latitude < latMax 
        && node.longitude > lngMin && node.longitude < lngMax) {
      isInRegion = true;
    }
    return isInRegion;
  }
  public void computeShortestPath(int sourceNodeId, int targetNodeId) {
    
  }
//  public void exportToFile(List<Node> nodes, String pathOut) {
//    //String pathOut = "D:/workspace/routeplanning/src/routeplanning/resources/boundaryNodes.txt";
//    try {
//      BufferedWriter outWriter = new BufferedWriter(new FileWriter(pathOut));
//      for (int i = 0; i < nodes.size(); i++) {
//        outWriter.newLine();
//        outWriter.write(nodes.get(i).latitude + ";" + nodes.get(i).longitude);
//        //System.out.println(nodes.get(i).latitude + ";" + nodes.get(i).longitude);
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    
//  }
}
