package routeplanning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class implementing A*-Landmarks.
 */
public class LandmarkAlgorithm extends DijkstraAlgorithm {
  
  /**
   * Keeps the time needed to calculate the heuristic function
   * for all nodes.
   */  
  private long heuristicTimeExection = 0; 
  
  
  /**
   * Keeps the number of landmarks implementing A*-Landmarks.
   */  
  private int numberOfLandmarks = 0;
  
  /**
   * List of the nodes used as landmarks.
   */
  // The set of landmarks. Each entry in the array is a node id.
  private List<Long> landmarkIds =  new ArrayList<Long>();
  
  /**
   * Each entry of the list represent the values dist(l,u) calculated
   * for each landmark.
   * We simply save the map returned by the getVisitedNodes() method
   * for each landmark to allow access in constant time.
   */
  // Precomputed distances (shorted path costs in seconds) to and from these
  // landmarks. This is one array of size #nodes per landmark.
  // NOTE: since our graphs are undirected (or rather, for each arc u,v we also
  // have an arc v,u with the same cost) we have dist(u, l) = dist(l, u) and it
  // suffices to store one distance array per landmark. For arbitrary directed
  // graphs we would need one array for the distances *to* the landmark and one
  // array for the distances *from* the landmark.
  private List<Map<Long, Integer>> costMaps 
    = new ArrayList<Map<Long, Integer>>();

  
  /**
   * Constructor. Assigns the inner road network.
   */  
  public LandmarkAlgorithm(RoadNetwork graph) {
    super(graph);
  }
  
  /**
   * Getter for landmarkIds.
   */
  public List<Long> getLandmarkIds() {
    return landmarkIds;
  }
  
  /**
   * Getter for costMaps.
   */
  public List<Map<Long, Integer>> getCostMaps() {
    return costMaps;
  }
  
  /**
   * Getter for heuristicTimeExection.
   */
  public long getHeuristicTimeExection() {
    return heuristicTimeExection;
  }
  
  /**
   * Setter for landmarkIds.
   */
  public void setLandmarkIds(List<Long> landmarkIds) {
    this.landmarkIds = landmarkIds;
  }

  /**
   * Setter for costMaps.
   */
  public void setCostMaps(List<Map<Long, Integer>> costMaps) {
    this.costMaps = costMaps;
  }

  /**
   * Sets the number of landmarks and precomputes distances.
   */  
  public void selectLandmarks(int numberOfLandmarks) {
    if (numberOfLandmarks < graph.getNodeIds().size()) {
      this.numberOfLandmarks = numberOfLandmarks;
      for (int i = 0; i < numberOfLandmarks; i++) {
        Long landmarkId = graph.getRandomNodeId();
        while (landmarkIds.contains(landmarkId)) {
          landmarkId = (graph).getRandomNodeId();
        }
        landmarkIds.add(landmarkId);
      }
    }
    precomputeLandmarkDistances();
  }

  
  /**
   * Precomputes the distances from the selected landmarks with a single
   * Dijkstra execution.
   */ 
  public void precomputeLandmarkDistances() {
    System.out.println("==================================");
    System.out.println("PRECOMPUTING DISTANCES FROM LANDMARKS");
    DijkstraAlgorithm dijAlg = new DijkstraAlgorithm(graph);
    
    for (int i = 0; i < this.landmarkIds.size(); i++) {
      Long currentLandMarkId = this.landmarkIds.get(i);
      //System.out.println("currentLandMarkId: " + currentLandMarkId);
      //DijkstraAlgorithm with target -1
      dijAlg.computeShortestPath(currentLandMarkId, -1);
      this.costMaps.add(dijAlg.getVisitedNodes());
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
  public int computeShortestPath(long sourceNodeId, long targetNodeId) {
    long startHeuristic = System.currentTimeMillis();
    List<Integer> heuristic = calculateHeuristicList(targetNodeId);
    setHeuristic(heuristic);
    long endHeuristic = System.currentTimeMillis();
    heuristicTimeExection = endHeuristic - startHeuristic;
    int spcost = super.computeShortestPath(sourceNodeId, targetNodeId);
    return spcost;
  }

  
  /**
   * Calculates h(u) for each node of the graph. 
   */ 
  public List<Integer> calculateHeuristicList(long targetNodeId) {
    List<Integer> heuristic = new ArrayList<Integer>();
    List<Long> nodeIds = graph.getNodeIds();
    for (int i = 0; i < nodeIds.size(); i++) {
      Long sourceNodeId = nodeIds.get(i);
      int nodeHeuristic = calculateHeuristic(sourceNodeId, targetNodeId);
      heuristic.add(nodeHeuristic);
    }
    return heuristic;
  }
 
  /**
   * Calculates max (h(u)) where each h(u) is given by one landmark.
   * h(u) = abs ( dist(l,u) - dist(l,t)). 
   */ 
  private int calculateHeuristic(long sourceNodeId, long targetNodeId) {
    //System.out.println("calculateHeuristic sourceNodeId: " 
    //+ sourceNodeId + " targetNodeId: " + targetNodeId);
    int heuristic = 0;
    //we check the maximum value for all the landmarks
    for (int i = 0; i < this.costMaps.size(); i++) {
      Map<Long, Integer > cost = this.costMaps.get(i);
      if (cost.containsKey(sourceNodeId)) {
        int distFromLtoU = cost.get(sourceNodeId);
        int distFromLtoT = cost.get(targetNodeId);       
        int currentHeuristic = Math.abs(distFromLtoU - distFromLtoT);
        if (currentHeuristic > heuristic) {
          heuristic = currentHeuristic;
        }  
      }
    }
    return heuristic;
  }
}
