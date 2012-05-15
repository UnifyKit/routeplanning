package routeplanning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class implementing A*-Landmarks.
 */
public class LandmarkAlgorithm extends DijkstraAlgorithm {
  /**
   * Keeps the number of landmarks implementing A*-Landmarks.
   * Default is 16 as requested in the exercise sheet.
   */  
  private int numberOfLandmarks = 0;
  
  /**
   * List of the nodes used as landmarks.
   * If we see we don't need this, we can remove this information.
   */
  // The set of landmarks. Each entry in the array is a node id.
  private List<Integer> landmarkIds =  new ArrayList<Integer>();
  
  /**
   * Each entry of the list represent the values dist(l,u) calculated
   * for each landmark.
   * We simply save the map returned by the getVisitedNodes() method
   * in constant time.
   */
  // Precomputed distances (shorted path costs in seconds) to and from these
  // landmarks. This is one array of size #nodes per landmark.
  // NOTE: since our graphs are undirected (or rather, for each arc u,v we also
  // have an arc v,u with the same cost) we have dist(u, l) = dist(l, u) and it
  // suffices to store one distance array per landmark. For arbitrary directed
  // graphs we would need one array for the distances *to* the landmark and one
  // array for the distances *from* the landmark.
  private List<Map<Integer, Integer>> costMaps 
    = new ArrayList<Map<Integer, Integer>>();

  
  /**
   * Constructor. Assigns the inner road network.
   */  
  public LandmarkAlgorithm(RoadNetwork graph) {
    super(graph);
  }
  
  /**
   * Getter for landmarkIds.
   */
  public List<Integer> getLandmarkIds() {
    return landmarkIds;
  }
  
  /**
   * Getter for costMaps.
   */
  public List<Map<Integer, Integer>> getCostMaps() {
    return costMaps;
  }

  /**
   * Set the number of landmarks.
   */  
  public void selectLandmarks(int numberOfLandmarks) {
    if (numberOfLandmarks < graph.getNodeIds().size()) {
      this.numberOfLandmarks = numberOfLandmarks;
      for (int i = 0; i < numberOfLandmarks; i++) {
        Integer landmarkId = graph.getRandomNodeId();
        while (landmarkIds.contains(landmarkId)) {
          landmarkId = graph.getRandomNodeId();
        }
        landmarkIds.add(landmarkId);
      }
    }
    precomputeLandmarkDistances();
  }

  
  /**
   * Pre-computes the distances from the selected landmarks with a single
   * Dijkstra execution.
   */ 
  public void precomputeLandmarkDistances() {
    System.out.println("==================================");
    System.out.println("PRECOMPUTING DISTANCES FROM LANDMARKS");
    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(graph);
    
    for (int i = 0; i < landmarkIds.size(); i++) {
      Integer currentLandMarkId = landmarkIds.get(i);
      //DijkstraAlgorithm with target -1
      dijAlg.computeShortestPath(currentLandMarkId, -1);
      costMaps.add(dijAlg.getVisitedNodes());
    } 
    System.out.println("END OF PRECOMPUTING DISTANCES");
    System.out.println("==================================");

  }
  
  /**
   * Compute the shortest paths from the given source to the given target node.
   * NOTE 1: If called with target node -1, Dijkstra is run until all nodes
   * reachable from the source are settled. NOTE 2: If member variable heuristic
   * is not null, simply add h(u) to the value of node u in the priority queue.
   * 
   * @param sourceNodeId
   * @param targetNodeId
   * @return
   */
  public int computeShortestPath(int sourceNodeId, int targetNodeId) {
    List<Integer> heuristic = calculateHeuristicList(targetNodeId);
    setHeuristic(heuristic);
    int spcost = super.computeShortestPath(sourceNodeId, targetNodeId);
    return spcost;
  }

  
  /**
   * Calculates h(u) for each node of the graph. 
   */ 
  private List<Integer> calculateHeuristicList(int targetNodeId) {
    List<Integer> heuristic = new ArrayList<Integer>();
    List<Integer> nodeIds = graph.getNodeIds();
    for (int i = 0; i < nodeIds.size(); i++) {
      Integer sourceNodeId = nodeIds.get(i);
      int nodeHeuristic = calculateHeuristic(sourceNodeId, targetNodeId);
      heuristic.add(nodeHeuristic);
    }
    return heuristic;
  }
 
  /**
   * Calculates max (h(u)) where each h(u) is given by one landmark.
   * h(u) = abs ( dist(l,u) - dist(l,t)). 
   */ 
  private int calculateHeuristic(int sourceNodeId, int targetNodeId) {
    int heuristic = 0;
    //we check the maximum value for all the landmarks
    for (int i = 0; i < costMaps.size(); i++) {
      Map<Integer, Integer > cost = costMaps.get(i);
      int distFromLtoU = cost.get(sourceNodeId);
      int distFromLtoT = cost.get(targetNodeId);
      int currentHeuristic = Math.abs(distFromLtoU - distFromLtoT);
      if (currentHeuristic > heuristic) {
        heuristic = currentHeuristic;
      }
    }
    return heuristic;
  }

}
