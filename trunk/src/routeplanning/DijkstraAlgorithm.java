package routeplanning;

import java.util.ArrayList;
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
   * Class needed to compare the current values of two nodes. This is required
   * to implement the priority queue.
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
   * Parent pointers computed by the last call to computeShortestPath.
   */
  protected Map<Integer, Integer> parents;

  /**
   * Whether computeShortestPath should consider the arcFlag from each Arc or
   * not. It is FALSE by default.
   */
  protected boolean considerArcFlags = false;

  /**
   * Stop Dijkstra when a node with cost greater than this cost is settled.
   */
  protected int costUpperBound;

  /**
   * Stop Dijkstra after this many nodes are settled, to make sure that it never
   * takes too long.
   */
  protected int maxNumSettledNodes;

  /**
   * Create instance of this class for a given (road) graph.
   * 
   * @param graph
   */
  public DijkstraAlgorithm(RoadNetwork graph) {
    this.graph = graph;
    this.costUpperBound = Integer.MAX_VALUE;
    this.maxNumSettledNodes = Integer.MAX_VALUE;
  }

  /**
   * Sets the boolean considerArcFlags to true or false.
   */
  public void setConsiderArcFlags(boolean considerArcFlags) {
    this.considerArcFlags = considerArcFlags;
  }

  /**
   * Set cost upper bound, see costUpperBound. Default (set in constructor)
   * INT_MAX, then no effect.
   * 
   * @param costUpperBound
   */
  public void setCostUpperBound(int costUpperBound) {
    this.costUpperBound = costUpperBound;
  }

  /**
   * Set maxNumSettledNodes, see below. Default (set in constructor) INT_MAX,
   * then no effect.
   * 
   * @param maxNumSettledNodes
   */
  public void setMaxNumSettledNodes(int maxNumSettledNodes) {
    this.maxNumSettledNodes = maxNumSettledNodes;
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
    parents = new HashMap<Integer, Integer>();
    int shortestPathCost = 0;
    List<Arc> adjArcsCurrentNode;
    // int pos;
    int distToAdjNode = 0;
    ActiveNode activeNode;
    int numSettledNodes = 0;
    // System.out.println("Compute Shortest Path Start: "
    // + Calendar.getInstance().getTime());

    // System.out.println("From Node: " + sourceNodeId + " to Node "
    // + targetNodeId);
    ActiveNode sourceNode;
    if (heuristic == null) {
      sourceNode = new ActiveNode(sourceNodeId, 0, 0, -1);
    } else {
      sourceNode = new ActiveNode(sourceNodeId, 0, heuristic.get(this.graph
          .getNodeIdPosAdjArc().get(sourceNodeId)), -1);
    }
    PriorityQueue<ActiveNode> pq = new PriorityQueue<ActiveNode>(1,
        travelTimeComparator);
    pq.add(sourceNode);

    while (!pq.isEmpty()) {
      ActiveNode currentNode = pq.poll();

      if (isVisited(currentNode.id)) {
        continue;
      }

      // settle node
      visitedNodeMarks.put(currentNode.id, currentNode.dist);
      parents.put((Integer) currentNode.id, (Integer) currentNode.parent);
      numSettledNodes++;
      /*
       * System.out.println("ADDED:" + (Integer) currentNode.id + " -> " +
       * (Integer) currentNode.parent);
       */

      if (currentNode.id == targetNodeId) {
        shortestPathCost = currentNode.dist;
        break;
      }
      // Stop dijkstra when a node with cost greater
      // than costUpperBound is settled.
      // or when the number of settled nodes is greater than maxNumSettledNodes.
      // Used for ContractionHierarchies algorithm.
      if (currentNode.dist > costUpperBound
          || numSettledNodes > maxNumSettledNodes) {
        // System.out.println("costUpperBound: " + costUpperBound);
        // System.out.println("numSettledNodes: " + numSettledNodes);
        shortestPathCost = currentNode.dist; // Revisar, si esta bien...!!!
        break;
      }

      // search adjacent node with shortest distance
      adjArcsCurrentNode = this.graph.getNodeAdjacentArcs(currentNode.id);
      for (int i = 0; i < adjArcsCurrentNode.size(); i++) {
        Arc arc;
        arc = adjArcsCurrentNode.get(i);
        if (this.considerArcFlags && !arc.arcFlag) {
          continue;
        }
        if (!isVisited(arc.headNode.id)) {
          distToAdjNode = currentNode.dist + arc.cost;
          if (heuristic == null) {
            activeNode = new ActiveNode(arc.headNode.id, distToAdjNode, 0,
                currentNode.id);
          } else {
            // activeNode = new ActiveNode(arc.headNode.id, distToAdjNode,
            // heuristic.get(this.graph.getNodeIds().indexOf(arc.headNode.id)));
            activeNode = new ActiveNode(arc.headNode.id, distToAdjNode,
                heuristic.get(this.graph.getNodeIdPosAdjArc().get(
                    arc.headNode.id)), currentNode.id);
          }
          pq.add(activeNode);
        }
      }
    }
    // System.out.println("shortestPathCost: " + shortestPathCost);
    // // System.out.println("visitedNodeMarks: " +
    // visitedNodeMarks.toString());
    // System.out.println("Compute Shortest Path End: "
    // + Calendar.getInstance().getTime());
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
   * Returns visitedNodeMarks.
   * 
   * @return visitedNodeMarks
   */
  public Map<Integer, Integer> getVisitedNodes() {
    return visitedNodeMarks;
  }

  /**
   * Returns visitedNodeMarks.
   * 
   * @return visitedNodeMarks
   */
  public Map<Integer, Integer> getParents() {
    return parents;
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
   * Returns the path from target node to source node.
   * 
   * @param sourceNodeId
   * @param targetNodeId
   * @return
   */
  public List<Float> getPathForGoogle(int sourceNodeId, int targetNodeId) {
    List<Float> pathCoordinates = new ArrayList();

    Node currentNode, prevNode;
    int currentNodeId, prevNodeId;

    currentNode = graph.getMapNodeId().get(targetNodeId);
    currentNodeId = targetNodeId;

    pathCoordinates.add((currentNode.latitude).floatValue());
    pathCoordinates.add((currentNode.longitude).floatValue());

    while (currentNode.id != sourceNodeId) {
      currentNodeId = parents.get(currentNodeId);
      currentNode = graph.getMapNodeId().get(currentNodeId);
      pathCoordinates.add((currentNode.latitude).floatValue());
      pathCoordinates.add((currentNode.longitude).floatValue());
    }
    // System.out.println(pathCoordinates);
    return pathCoordinates;
  }

  /**
   * Prints the priority queue (for debugging purposes).
   * 
   * @param pq
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

  /**
   * Prints the shortest path (for debugging purposes).
   */
  protected void printShortestPath(int sourceNodeId, int targetNodeId) {
    String path = "";
    Node currentNode;
    int currentNodeId;

    currentNode = graph.getMapNodeId().get(targetNodeId);
    currentNodeId = targetNodeId;

    path = path + currentNodeId + "->";

    while (currentNode.id != sourceNodeId) {
      currentNodeId = parents.get(currentNodeId);
      currentNode = graph.getMapNodeId().get(currentNodeId);
      path = path + currentNodeId + "->";
    }
    System.out.println(path);
  }
}
