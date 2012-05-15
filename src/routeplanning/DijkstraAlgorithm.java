package routeplanning;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class implementing the Dijkstra algorithm.
 */
public class DijkstraAlgorithm {
  /**
   * Indicator which node was visited by a particular run of Dijkstra. Useful
   * for computing the connected components;
   */
  protected Map<Integer, Integer> visitedNodeMarks;
  /**
   * Reference to graph on which this object is supposed to work.
   */
  protected RoadNetwork graph;

  /**
   * Class needed to compare the current values of two nodes.
   * This is required to implement the priority queue.
   */
  protected final Comparator<ActiveNode> travelTimeComparator 
    = new Comparator<ActiveNode>() {
      public int compare(ActiveNode n1, ActiveNode n2) {
        double dist = (n1.dist + n1.heuristic) - (n2.dist + n2.heuristic);
        if (dist < 0) {
          return -1;
        } else {
          return 1;
        }
      }
    };
    
  /**
   * Heuristic function if running in A* mode.
   */
  protected List<Integer> heuristic;

  /**
   * Create instance of this class for a given (road) graph.
   * 
   * @param graph
   */
  public DijkstraAlgorithm(RoadNetwork graph) {
    this.graph = graph;
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
    visitedNodeMarks = new HashMap<Integer, Integer>();
    int shortestPathCost = 0;
    List<Arc> adjArcsCurrentNode;
    //int pos;
    int distToAdjNode = 0;
    ActiveNode activeNode;
    System.out.println("Compute Shortest Path Start: "
        + Calendar.getInstance().getTime());

    System.out.println("From Node: " + sourceNodeId + " to Node "
        + targetNodeId);
    ActiveNode sourceNode;
    if (heuristic == null) {
      sourceNode = new ActiveNode(sourceNodeId, 0, 0);
    } else {
      sourceNode = new ActiveNode(sourceNodeId, 0, heuristic.get(this.graph
          .getNodeIdPosAdjArc().get(sourceNodeId)));
    }
    PriorityQueue<ActiveNode> pq = new PriorityQueue<ActiveNode>(1,
        travelTimeComparator);
    pq.add(sourceNode);

    while (!pq.isEmpty()) {
      ActiveNode currentNode = pq.poll();

      if (isVisited(currentNode.id)) {
        continue;
      }
      //pos = this.graph.getNodeIds().indexOf(currentNode.id);
      // settled the node
      visitedNodeMarks.put(currentNode.id, currentNode.dist);

      if (currentNode.id == targetNodeId) {
        shortestPathCost = currentNode.dist;
        break;
      }

      // search adjacent node with shortest distance
      adjArcsCurrentNode = this.graph.getNodeAdjacentArcs(currentNode.id);

      for (int i = 0; i < adjArcsCurrentNode.size(); i++) {
        Arc arc;
        arc = adjArcsCurrentNode.get(i);
        if (!isVisited(arc.headNode.id)) {
          distToAdjNode = currentNode.dist + arc.cost;
          if (heuristic == null) {
            activeNode = new ActiveNode(arc.headNode.id, distToAdjNode, 0);
          } else {
            //activeNode = new ActiveNode(arc.headNode.id, distToAdjNode,
            //heuristic.get(this.graph.getNodeIds().indexOf(arc.headNode.id)));
            activeNode = new ActiveNode(arc.headNode.id, distToAdjNode,
              heuristic.get(this.graph.getNodeIdPosAdjArc().get(
                arc.headNode.id)));
          }
          pq.add(activeNode);
        }
      }
    }

    System.out.println("shortestPathCost: " + shortestPathCost);
    // System.out.println("visitedNodeMarks: " + visitedNodeMarks.toString());
    System.out.println("Compute Shortest Path End: "
        + Calendar.getInstance().getTime());
    return shortestPathCost;
  }

  /**
   * Says if the given node was already visited.
   * 
   * @param nodeId
   */
  public Boolean isVisited(int nodeId) {
    if (visitedNodeMarks.containsKey(nodeId)) {
      return true;
    }
    return false;
  }

  /**
   * Mark all nodes visited by the next call to computeShortestPath with this
   * number.
   * 
   * @param mark
   */
  // private void setVisitedNodeMark(int mark) {
  //
  // }

  /**
   * Returns visitedNodeMarks.
   * 
   * @return visitedNodeMarks
   */
  public Map<Integer, Integer> getVisitedNodes() {
    return visitedNodeMarks;
  }

  /**
   * Set heuristic function to given array.
   * 
   * @param heuristic
   */
  public void setHeuristic(List<Integer> heuristic) {
    this.heuristic = heuristic;
  }

  /**
   * TODO.
   * 
   * @param pq
   *          priority queue
   */
  private void pqAsString(PriorityQueue pq) {
    String pqString;
    ActiveNode node;
    PriorityQueue<ActiveNode> queue = new PriorityQueue<ActiveNode>();
    System.out.println("-------------------------------------");
    queue = pq;
    while (!pq.isEmpty()) {
      node = queue.peek();
      System.out.println("id: " + node.id + "value: " + node.dist);
    }
    System.out.println("-------------------------------------");
  }
}
