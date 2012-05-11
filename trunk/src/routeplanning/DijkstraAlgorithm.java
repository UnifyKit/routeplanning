package routeplanning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
  /**
   * Indicator which node was visited by a particular run of Dijkstra. Useful
   * for computing the connected components; 
   */
  private List<Integer> visitedNodeMarks;
  /**
   * Reference to graph on which this object is supposed to work.
   */
  private RoadNetwork graph;
  /**
   * 
   */
  private final Comparator<ActiveNode> travelTimeComparator = new Comparator<ActiveNode>() {
    public int compare(ActiveNode n1, ActiveNode n2) {
      double dist = n1.dist - n2.dist;
      if (dist < 0) {
        return -1;
      } else {
        return 1;
      }
    }
  };
  /**
   * Create instance of this class for a given (road) graph.
   * @param graph
   */
  public DijkstraAlgorithm(RoadNetwork graph) {
    this.graph = graph;
    visitedNodeMarks = new ArrayList<Integer>();
    for (int i = 0; i < this.graph.nodeIds.size(); i++) {
      visitedNodeMarks.add(null);
    }
  }
  /**
   * Compute the shortest paths from the given source to the given target node.
   * Returns the cost of the shortest path. If called with target node -1,
   * Dijkstra is run until all nodes reachable from the source are settled.
   * @param sourceNodeId
   * @param targetNodeId
   * @return
   */
  public int computeShortestPath(int sourceNodeId, int targetNodeId) {
    int shortestPathCost = 0;
    List<Arc> adjArcsCurrentNode;
    int pos;
    int minDist = Integer.MAX_VALUE;
    int distToAdjNode=0;
    ActiveNode activeNode;
    Boolean noAdjacentNodes=false;
    
    
    System.out.println("Compute Shortest Path Start: " + Calendar.getInstance().getTime());
    ActiveNode sourceNode = new ActiveNode(sourceNodeId, 0);
    
    PriorityQueue<ActiveNode> pq = new PriorityQueue<ActiveNode>(1, travelTimeComparator);
    pq.add(sourceNode);
    
    
    while(!pq.isEmpty()) {
      ActiveNode currentNode = pq.poll();
      
      if(isVisited(currentNode.id)) {
        continue;
      }
      pos = this.graph.nodeIds.indexOf(currentNode.id);
      //settled the node
      visitedNodeMarks.set(pos, currentNode.dist);
      
      
      if(currentNode.id == targetNodeId) {
        shortestPathCost = currentNode.dist;
        break;
      }
      
      //System.out.println("currentNode: " + currentNode.id + " dist: " + currentNode.dist);
      //pqAsString(pq);
       
      //search adjacent node with shortest distance
      adjArcsCurrentNode = this.graph.getAdjacentArcs().get(pos);
      
      minDist = Integer.MAX_VALUE;
      noAdjacentNodes = false;
      for (int i = 0; i < adjArcsCurrentNode.size(); i++) {
        Arc arc;  
        arc = adjArcsCurrentNode.get(i);
        if (!isVisited(arc.headNode.id)) {
          noAdjacentNodes = true;
          distToAdjNode = currentNode.dist + arc.cost;
          if (minDist > distToAdjNode) {
            minDist = distToAdjNode;
            //minDistNodeId = arc.headNode.id;
            //minDistNodeCost = currentNode.dist + arc.cost;
          }
          activeNode = new ActiveNode(arc.headNode.id, distToAdjNode);
          pq.add(activeNode);
        }
      }
      if(!noAdjacentNodes) {
        //System.out.println(currentNode.id);
      }
      
    }
    System.out.println("shortestPathCost: " + shortestPathCost);
    //System.out.println("visitedNodeMarks: " + visitedNodeMarks.toString());
    System.out.println("Compute Shortest Path End: " + Calendar.getInstance().getTime());
    return shortestPathCost;
  }
  private Boolean isVisited(int nodeId){
    if (visitedNodeMarks.get(this.graph.nodeIds.indexOf(nodeId)) == null) {
      return false;
    } else {
      return true;
    }
  }
  /**
   * Mark all nodes visited by the next call to computeShortestPath with this
   * number.
   * @param mark
   */
  private void setVisitedNodeMark(int mark) {

  }
  /**
   * Returns visitedNodeMarks.
   * @return visitedNodeMarks
   */  
  public List<Integer> getVisitedNodes() {
    return visitedNodeMarks;
  }
  private void pqAsString(PriorityQueue pq){
    String pqString;
    ActiveNode node;
    PriorityQueue<ActiveNode> queue = new PriorityQueue<ActiveNode>();
    System.out.println("-------------------------------------");
    queue = pq;     
    while(!pq.isEmpty()){
      node = queue.peek();
      System.out.println("id: " + node.id + "value: " + node.dist);
    }
    System.out.println("-------------------------------------");
  }
}
