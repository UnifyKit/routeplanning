package routeplanning;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;

/**
 * Class implementing the Contraction Hierarchies' approach from lecture 6.
 */
public class ContractionHierarchies extends DijkstraAlgorithm {
  /**
   * Reference to graph on which this object is supposed to work. NOTE: the
   * contraction hierarchies algorithm *modifies* this graph by adding
   * shortcuts, so this must be a *non-const* reference or pointer, and you
   * should be aware that by using this class, the original graph will be
   * modified.
   */
  private RoadNetwork graph;

  /**
   * Object for the various execution of Dijkstra's algorithm on the augmented
   * graph.
   */
  private DijkstraAlgorithm dijkstra;

  // The ordering of the nodes. This is simply a permutation of {0, ..., n-1},
  // where n is the number of nodes; nodeOrdering[i] simply contains the index
  // of the i-th node in the ordering, for i = 0, ..., n-1.
  /**
   * ordering of the nodes.
   */
  private List<Integer> nodeOrdering = new ArrayList();
  
  /**
   * Class needed to compare the current values of two nodes. This is required
   * to implement the priority queue.
   */
  protected final Comparator<ContractedNode> nodesComparator 
    = new Comparator<ContractedNode>() {
      public int compare(ContractedNode c1, ContractedNode c2) {
        if (c1.edgeDiff < c2.edgeDiff) {
          return -1;
        } else {
          return 1;
        }
      }
    };
    
    /**
     * priority queue of the nodes.
     */    
  private PriorityQueue<ContractedNode> pq 
    = new PriorityQueue<ContractedNode>(1, nodesComparator);

  /**
   * The following structure maps a node to its position in the List
   * nodeordering. The key is the node id, the value is its position in the
   * nodeOrdering list.
   */
  private Map<Integer, Integer> orderOfNodeMap = new HashMap();
  
  /**
   * The map maps each edge difference with a list of nodes that have
   * this edge difference.
   */ 
  private TreeMap<Integer, List<Integer>> edgeDiffMap = new TreeMap();
  /**
   * VisitedNodeMarks map for second dijkstra used in computeShortestPath.
   */
  Map<Integer, Integer> visitedNodeMarks2;


  /**
   * Creates instance of this class for a given (road) graph and builds the
   * DijkstraAlgorithm with setting setConsiderArcFlags(true).
   */
  public ContractionHierarchies(RoadNetwork graph) {
    super(graph);
    this.graph = graph;
    // prepares DijkstraAlgorithm
    dijkstra = new DijkstraAlgorithm(graph);
    dijkstra.setConsiderArcFlags(true);
    dijkstra.setMaxNumSettledNodes(20);
  }

  /**
   * Getter for nodeOrdering.
   */
  public List<Integer> getNodeOrdering() {
    return nodeOrdering;
  }

  /**
   * Getter for nodeOrderingMap.
   */
  public Map<Integer, Integer> getNodeOrderingMap() {
    return orderOfNodeMap;
  }
  

  /**
   * Getter for priority queue.
   */
  public PriorityQueue<ContractedNode> getPriorityQueue() {
    return pq;
  }

  /**
   * Setter for nodeOrdering.
   */
  public void setNodeOrdering(List<Integer> nodeOrdering) {
    this.nodeOrdering = nodeOrdering;
    orderOfNodeMap = new HashMap();
    for (int i = 0; i < nodeOrdering.size(); i++) {
      Integer currentNodeId = nodeOrdering.get(i);
      orderOfNodeMap.put(currentNodeId, i);
    }
  } 

  /**
   * Main precomputation method. Initially, all arcs in the graph are initially
   * set as Flags. Precomputation by contracting all nodes in the order of their
   * edge differences, and adding shortcuts on the way. IMPLEMENTATION NOTE:
   * Maintain nodes in a priority queue with key = edge difference. When
   * contracting the i-th node, and it is node u, set orderOfNode[u] = i; see
   * below. After all nodes have been contracted (and various shortcuts added to
   * the original graph), reset the arc flags such that only the arc flags for
   * arcs u, v with orderOfNode[u] < orderOfNode[v] are true.
   * 
   * @return the number of added shortcuts.
   */
  public int precomputationLazy() {
    int numberOfAddedShortcuts = 0;
    computeNodeOrderingByEdgeDifference();
    System.out.println("computeNodeOrderingByEdgeDifference .... COMPLETED!");
    
//    System.out.println(orderOfNodeMap);
    
    nodeOrdering = new ArrayList();
    orderOfNodeMap = new HashMap();

    int positionCounter = 0;
    
    //The first can always go as the first in the new list
    ContractedNode currentNode = pq.poll();
    nodeOrdering.add(currentNode.id);
    contractNode(positionCounter, false);
    orderOfNodeMap.put(currentNode.id, positionCounter);
//    System.out.println("BY: " + currentNode.toString());
    
    while (!pq.isEmpty()) {
      positionCounter++;
      currentNode = pq.poll();
      
      if (!orderOfNodeMap.containsKey(currentNode.id)) {
        nodeOrdering.add(currentNode.id);
//        System.out.println(nodeOrdering);
//        System.out.println("BY: " + currentNode.toString());
        orderOfNodeMap.put(currentNode.id, positionCounter);
        //Just getting the info
        List<Integer> info = 
          contractNode(orderOfNodeMap.get(currentNode.id), true);
//        System.out.println("NEW ED: " + info.get(1));
//        System.out.println("ADDED SC: " + info.get(0));
        //nothing has changed
        if (info.get(1).intValue() <= currentNode.edgeDiff) {
//          System.out.println("NOTHING HAS CHANGED");
          //real contraction
          info = contractNode(orderOfNodeMap.get(currentNode.id), false);
          numberOfAddedShortcuts = numberOfAddedShortcuts + info.get(0);
        } else {
          orderOfNodeMap.remove(currentNode.id);
          nodeOrdering.remove(new Integer(currentNode.id));
          ContractedNode newContractedNode 
            = new ContractedNode(currentNode.id, info.get(1));
          positionCounter--;
        }
      } 
//      else {
//        System.out.println(currentNode.toString() + " ALREADY THERE");
//      }
    }
    //System.out.println(graph.asString());
    // Reset Arc Flags such that only the arc flags for arcs u,v
    // with orderOfNode[u] < orderOfNode[v]
    List<List<Arc>> adjArcs = graph.getAdjacentArcs();
    List<Integer> nodeIds = graph.getNodeIds();
    //System.out.println("orderOfNodeMap: " + orderOfNodeMap.toString());
    for (int i = 0; i < adjArcs.size(); i++) {
      int nodeId = nodeIds.get(i);
      int orderOfNodeU = orderOfNodeMap.get(nodeId);
      // System.out.println("nodeId: " + nodeId + " orderOfNode: " +
      // orderOfNodeU);
      List<Arc> adjArcsU = adjArcs.get(i);
      for(int j = 0; j < adjArcsU.size(); j++) {
        int adjNodeId = adjArcsU.get(j).headNode.getId();
        int orderOfAdjNode = orderOfNodeMap.get(adjNodeId);
        if(orderOfAdjNode < orderOfNodeU) {
          adjArcsU.get(j).arcFlag = false;
        } else {
          adjArcsU.get(j).arcFlag = true;
        }
      }
    }
    //System.out.println(graph.asStringWithArcFlags());
    return numberOfAddedShortcuts;
  }

  /**
   * It computes a random permutation of the set {0,..., n-1} as follows: It
   * initializes nodeOrdering with 0, 1, ..., n - 1. Swap nodeOrdering[0] with a
   * random entry to the right. Then swap nodeOrdering[1] with a random entry on
   * the right. And so on. This implementation takes linear time.
   */
  public void computeNodeOrderingByEdgeDifference() {
    setAllArcsToTrue();
    // Initialization of nodeOrdering
    setNodeOrdering(graph.getNodeIds());

    // We have already the list "nodes" which has all the ids of the nodes.
    // We will work on a copy of this list (it only copies the references).
    List<Integer> tempNodeOrdering = new ArrayList<Integer>();
    tempNodeOrdering.addAll(graph.getNodeIds());
    // The map maps each edge difference with a list of nodes that have
    // this edge difference
    TreeMap<Integer, List<Integer>> edgeDiffMap = new TreeMap();

    // linear time (on the number of nodes)
    for (int i = 0; i < tempNodeOrdering.size(); i++) {
      Integer currentNodeId = tempNodeOrdering.get(i);
      List<Integer> info = contractNode(i, true);
      Integer edgeDifference = info.get(1);
      //Inserting this values to the queue.
      ContractedNode chNode = new ContractedNode(currentNodeId, edgeDifference);
      pq.add(chNode);
      
      
      if (edgeDiffMap.containsKey(edgeDifference)) {
        List<Integer> nodes = edgeDiffMap.get(edgeDifference);
        nodes.add(currentNodeId);
      } else {
        List<Integer> nodes = new ArrayList();
        nodes.add(currentNodeId);
        edgeDiffMap.put(edgeDifference, nodes);
      }
    }
    nodeOrdering = new ArrayList<Integer>();
    orderOfNodeMap = new HashMap();
    int counter = 0;
    
    Iterator it = edgeDiffMap.keySet().iterator();
    while (it.hasNext()) {
      Integer currentEdgeDiff = (Integer) it.next();
      List<Integer> nodes = edgeDiffMap.get(currentEdgeDiff);
//      System.out.println("EDGE DIFF: " + currentEdgeDiff);
//      System.out.println(nodes);
      for (int i = 0; i < nodes.size(); i++) {
        orderOfNodeMap.put(nodes.get(i), counter);
        nodeOrdering.add(nodes.get(i));
        counter++;
      }
    }
  }

  /**
   * Does the precomputation by contracting all nodes in the order of their edge
   * differences, and adding shortcuts on the way.
   */
  public void computeRandomNodeOrdering() {
    setAllArcsToTrue();
    orderOfNodeMap = new HashMap();
    // We have already the list "nodes" which has all the ids of the nodes.
    // We will work on a copy of this list (it only copies the references).
    List<Integer> randomNodeOrdering = new ArrayList<Integer>();
    randomNodeOrdering.addAll(graph.getNodeIds());
    int numberOfNodes = randomNodeOrdering.size();

    // linear time (on the number of nodes)
    for (int i = 0; i < randomNodeOrdering.size(); i++) {
      Integer currentNodeId = randomNodeOrdering.get(i);
      // We have to swap this with a random position
      int newPos = generateRandomNodePosition(numberOfNodes);
      while (newPos == i) {
        newPos = generateRandomNodePosition(numberOfNodes);
      }
      Integer tempRef = currentNodeId;
      randomNodeOrdering.set(i, randomNodeOrdering.get(newPos));
      orderOfNodeMap.put(randomNodeOrdering.get(newPos), i);
      randomNodeOrdering.set(newPos, tempRef);
      orderOfNodeMap.put(tempRef, newPos);
    }
    nodeOrdering = randomNodeOrdering;
  }

  /**
   * Central contraction routine: contract the i-th node in the ordering,
   * ignoring nodes 1, ..., i - 1 in the ordering and their adjacent arcs.
   * IMPLEMENTATION NOTE: To ignore nodes (and their adjacent arcs), we use the
   * Arc class. As it goes along and contract nodes, simply set the flags of the
   * arcs adjacent to contracted nodes to 0. Additional argument says whether we
   * really want to contract the node or just compute the numbers of shortcuts
   * and the edge difference. Default is false. If true, it doesn't change
   * anything in the graph (don't add any arcs and don't set any arc flags to
   * false) and return a list.
   * 
   * @return A list containing two integers. The first integer is the number of
   *         shortcuts, the second integer is the edge difference.
   */

  public List<Integer> contractNode(int i, boolean computeEdgeDifferenceOnly) {
    int edgeDifference = 0;
    int removedArcs = 0;
    int createdShortcuts = 0;
    List<Integer> infoList = new ArrayList();

    // The following map will save the costs from all Ui to V
    Map<Integer, Integer> costsMap = new HashMap();
    // The following map keeps all the new arcs that will be added
    // in the end.
    Map<Node, List<Arc>> addedArcsMap = new HashMap();

    // We keep track of the arcs set to false to be set as true
    // in the end of the method in case computeEdgeDifferenceOnly
    // is set to true
    List<Arc> changedArcs = new ArrayList();

    // nodeId is "v" the node I want to contract.
    Integer nodeVId = nodeOrdering.get(i);
    // System.out.println("-----------(" + (i + 1) + ")"
    // + " :: Contracting node "
    // + nodeVId);
    // Node currentNode = graph.getMapNodeId().get(nodeId);
    // First we need all adjacent nodes of currentNode.
    // list of all "u_i"
    List<Arc> nodeVarcs = graph.getNodeAdjacentArcs(nodeVId);

    // We set all arcs from all u_i to v as not Flag, so that they
    // won't be considered in the calculation of SP.
    for (int k = 0; k < nodeVarcs.size(); k++) {
      Arc arcToV = nodeVarcs.get(k);
      // System.out.println("ADJACENT NODES: " + nodeVarcs.size());
      if (arcToV.arcFlag) {
        Integer nodeUi = nodeVarcs.get(k).getHeadNode().getId();
        Integer positionOfUi = orderOfNodeMap.get(nodeUi);
        // System.out.println("Analyzing edge " + nodeVId
        // + " - " + nodeUi + " :: ");

        if (positionOfUi == null || positionOfUi > i) {
          arcToV.arcFlag = false;
          Integer cost = nodeVarcs.get(k).cost;
          costsMap.put(nodeUi, cost);
          // System.out.println("Analyzing edge " + nodeVId
          // + " - " + nodeUi + " :: ");
          changedArcs.add(arcToV);
          removedArcs++;
        }
//        else {
//          System.out.println("MMMMMMM: " + nodeUi + " - " + positionOfUi);
//        }
      }
    }

    // For each pair of adjacent nodes:
    for (int k = 0; k < nodeVarcs.size(); k++) {

      Integer nodeIdUi = nodeVarcs.get(k).getHeadNode().getId();
      // We have to detect if the node Ui was processed.
      // We can use the nodeOrderingMap to do that:
      Integer positionOfUi = orderOfNodeMap.get(nodeIdUi);

      if (positionOfUi != null && positionOfUi < i) {
//         System.out.println("::SKIP Ui node " + nodeIdUi);
        continue;
      }

      List<Arc> uiArcs = graph.getNodeAdjacentArcs(nodeIdUi);

      // We set the arc from Ui to V as arcFlag False
      for (int q = 0; q < uiArcs.size(); q++) {
        Arc currentUiArc = uiArcs.get(q);
        Integer headNodeId = currentUiArc.getHeadNode().getId();
        if (headNodeId == nodeVId) {
          if (currentUiArc.arcFlag) {
            currentUiArc.arcFlag = false;
//             System.out.println("Setting to set the arc "
//             + " from Ui to V as arcFlag False");
            changedArcs.add(currentUiArc);
            removedArcs++;
          }
        }
      }

      for (int j = 0; j < nodeVarcs.size(); j++) {
        Integer nodeIdWj = nodeVarcs.get(j).getHeadNode().getId();

        // Checks if an adjacent node was deleted in previous contractions.
        Integer positionOfWj = orderOfNodeMap.get(nodeIdWj);

        if (positionOfWj != null && positionOfWj < i) {
          // System.out.println("::SKIP Wj node " + nodeIdWj);
          continue;
        }

        // checks that ui and wj are the same.
        if (k != j) {
//           System.out.println("Checking nodes " + nodeIdUi + " - " +
//           nodeIdWj);
          // I have to verify if the SP without v is better or
          // worse than Dij = cost(Ui,V)+ cost(V,Wj)
          int pathThroughV = costsMap.get(nodeIdUi) + costsMap.get(nodeIdWj);

          // to make Dijkstra faster:
          dijkstra.setCostUpperBound(pathThroughV);
          int cost = dijkstra.computeShortestPath(nodeIdUi, nodeIdWj);

          // System.out.println("COST:" + cost + " - PATH-V: " + pathThroughV);

          if (cost > pathThroughV || cost == 0) {
//             System.out.println("***ADDING new Arc: "
//             + nodeIdUi + " - " + nodeIdWj + " COST: " + pathThroughV);
            // then it is necessary to add the new arc
            Node nodeUi = graph.getMapNodeId().get(nodeIdUi);
            Node nodeWj = graph.getMapNodeId().get(nodeIdWj);

            // Arcs in two directions are needed
            Arc newArcDire1 = new Arc(nodeWj, pathThroughV);
            Arc newArcDire2 = new Arc(nodeUi, pathThroughV);
            newArcDire1.arcFlag = true;
            newArcDire2.arcFlag = true;

            if (addedArcsMap.containsKey(nodeUi)) {
              List<Arc> addedArcs = addedArcsMap.get(nodeUi);
              addedArcs.add(newArcDire1);
            } else {
              List<Arc> addedArcs = new ArrayList();
              addedArcs.add(newArcDire1);
              addedArcsMap.put(nodeUi, addedArcs);
            }

            if (addedArcsMap.containsKey(nodeWj)) {
              List<Arc> addedArcs = addedArcsMap.get(nodeWj);
              addedArcs.add(newArcDire2);
            } else {
              List<Arc> addedArcs = new ArrayList();
              addedArcs.add(newArcDire2);
              addedArcsMap.put(nodeWj, addedArcs);
            }
            createdShortcuts++;
          }
        }
      }
    }
    // We have to restablish the arcs set to false
    if (computeEdgeDifferenceOnly) {
      for (int k = 0; k < changedArcs.size(); k++) {
        Arc currentArc = changedArcs.get(k);
        currentArc.arcFlag = true;
      }
    } else {
      // Otherwise we add all the new arcs:
      // Time to add all accumulated arcs.
      Iterator it = addedArcsMap.keySet().iterator();
      while (it.hasNext()) {
        Node node = (Node) it.next();
        List<Arc> arcsToAdd = addedArcsMap.get(node);
        for (int k = 0; k < arcsToAdd.size(); k++) {
          graph.addAdjacentArc(node, arcsToAdd.get(k));
          // System.out.println("***ADDED ARC: " + node.getId() + " - "
          // + arcsToAdd.get(k).getHeadNode().getId() + ":: "
          // + arcsToAdd.get(k).cost);
        }
      }
    }
    //counting arcs, not edges
    createdShortcuts = createdShortcuts / 2;
    removedArcs = removedArcs / 2;

    edgeDifference = createdShortcuts - removedArcs;
    infoList.add(createdShortcuts);
    infoList.add(edgeDifference);
    return infoList;
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
    // For second dijkstra from target to source
    visitedNodeMarks2 = new HashMap<Integer, Integer>();

    parents = new HashMap<Integer, Integer>();
    // For second dijkstra from target to source
    Map<Integer, Integer> parents2 = new HashMap<Integer, Integer>();

    int shortestPathCost = 0;
    // For second dijkstra from target to source
    int shortestPathCost2 = 0;

    List<Arc> adjArcsCurrentNode;
    // int pos;
    int distToAdjNode = 0;
    ActiveNode activeNode;
    int numSettledNodes = 0;
    int numSettledNodes2 = 0;

    // For second dijkstra from target to source
    int sourceNodeId2 = targetNodeId;
    int targetNodeId2 = sourceNodeId;

    // System.out.println("Compute Shortest Path Start: "
    // + Calendar.getInstance().getTime());

    // System.out.println("From Node: " + sourceNodeId + " to Node "
    // + targetNodeId);
    ActiveNode sourceNode;
    ActiveNode sourceNode2;
    if (heuristic == null) {
      sourceNode = new ActiveNode(sourceNodeId, 0, 0, -1);
      // For second dijkstra from target to source
      sourceNode2 = new ActiveNode(sourceNodeId2, 0, 0, -1);
    } else {
      sourceNode = new ActiveNode(sourceNodeId, 0, heuristic.get(this.graph
          .getNodeIdPosAdjArc().get(sourceNodeId)), -1);
      // For second dijkstra from target to source
      sourceNode2 = new ActiveNode(sourceNodeId2, 0, heuristic.get(this.graph
          .getNodeIdPosAdjArc().get(sourceNodeId2)), -1);
    }

    PriorityQueue<ActiveNode> pq = new PriorityQueue<ActiveNode>(1,
        travelTimeComparator);
    // For second dijkstra from target to source
    PriorityQueue<ActiveNode> pq2 = new PriorityQueue<ActiveNode>(1,
        travelTimeComparator);

    pq.add(sourceNode);
    pq2.add(sourceNode2);

    while (!pq.isEmpty() || !pq2.isEmpty()) {
      // Check which of the two pq has the smallest value and then pick that.
      ActiveNode tmpCurrentNode1 = pq.peek();
      ActiveNode tmpCurrentNode2 = pq2.peek();
      ActiveNode currentNode;
      if (tmpCurrentNode1.dist <= tmpCurrentNode2.dist) {
        currentNode = pq.poll();
        // System.out.println("pick currentActiveNode from pq: " +
        // currentNode.id);
        if (isVisited(currentNode.id)) {
          continue;
        }

        // settle node
        visitedNodeMarks.put(currentNode.id, currentNode.dist);
        parents.put((Integer) currentNode.id, (Integer) currentNode.parent);
        numSettledNodes++;
        // Check if the node was settled in pq2, if it is stop and compute
        // shortest path
        // min {distUs + distUt} for all u visited in both dijikstras
        if (visitedNodeMarks2.containsKey(currentNode.id)) {

          int mindisUsUtrelax = getMinDisFromPq(pq, pq2, visitedNodeMarks,
              visitedNodeMarks2);
          // System.out.println("dij1 mindisUsUt de relaxed edges: " +
          // mindisUsUtrelax);

          shortestPathCost = mindisUsUtrelax;
          break;
        }

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
        // or when the number of settled nodes is greater than
        // maxNumSettledNodes.
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
              // heuristic.get(this.graph.getNodeIds()
//              .indexOf(arc.headNode.id)));
              activeNode = new ActiveNode(arc.headNode.id, distToAdjNode,
                  heuristic.get(this.graph.getNodeIdPosAdjArc().get(
                      arc.headNode.id)), currentNode.id);
            }
            pq.add(activeNode);
            // System.out.println("Add pq: " + activeNode.id + " dist: "
            // + activeNode.dist);
          }
        }
      } else { // ************Dijkstra2*********************
        currentNode = pq2.poll();
        // System.out
        // .println("pick currentActiveNode from pq2: " + currentNode.id);
        if (visitedNodeMarks2.containsKey(currentNode.id)) {
          continue;
        }

        // settle node
        visitedNodeMarks2.put(currentNode.id, currentNode.dist);
        parents2.put((Integer) currentNode.id, (Integer) currentNode.parent);
        numSettledNodes2++;

        if (visitedNodeMarks.containsKey(currentNode.id)) {
          int mindistUsUt = Integer.MAX_VALUE;
          Iterator it = visitedNodeMarks2.keySet().iterator();

          while (it.hasNext()) {
            Integer vnodeId = (Integer) it.next();
            int distUt = visitedNodeMarks2.get(vnodeId);
            if (visitedNodeMarks.containsKey(vnodeId)) {
              int distUs = visitedNodeMarks.get(vnodeId);
              if (mindistUsUt > (distUs + distUt)) {
                mindistUsUt = distUs + distUt;
              }
            }
          }
          int mindisUsUtrelax = getMinDisFromPq(pq, pq2, visitedNodeMarks,
              visitedNodeMarks2);
          // System.out.println("dij2 mindisUsUt de relaxed edges: " +
          // mindisUsUtrelax);

          shortestPathCost = mindisUsUtrelax;
          break;
        }
        /*
         * System.out.println("ADDED:" + (Integer) currentNode.id + " -> " +
         * (Integer) currentNode.parent);
         */

        if (currentNode.id == targetNodeId2) {
          shortestPathCost2 = currentNode.dist;
          break;
        }
        // Stop dijkstra when a node with cost greater
        // than costUpperBound is settled.
        // or when the number of settled nodes is greater than
        // maxNumSettledNodes.
        // Used for ContractionHierarchies algorithm.
        if (currentNode.dist > costUpperBound
            || numSettledNodes2 > maxNumSettledNodes) {
          // System.out.println("costUpperBound: " + costUpperBound);
          // System.out.println("numSettledNodes: " + numSettledNodes);
          shortestPathCost2 = currentNode.dist; // Revisar, si esta bien...!!!
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
          if (!visitedNodeMarks2.containsKey(arc.headNode.id)) {
            distToAdjNode = currentNode.dist + arc.cost;
            if (heuristic == null) {
              activeNode = new ActiveNode(arc.headNode.id, distToAdjNode, 0,
                  currentNode.id);
            } else {
              activeNode = new ActiveNode(arc.headNode.id, distToAdjNode,
                  heuristic.get(this.graph.getNodeIdPosAdjArc().get(
                      arc.headNode.id)), currentNode.id);
            }
            pq2.add(activeNode);
            // System.out.println("Add pq2: " + activeNode.id + " dist: "
            // + activeNode.dist);
          }
        }
      }
    } // while

    // System.out.println("shortestPathCost: " + shortestPathCost);
    // // System.out.println("visitedNodeMarks: " +
    // visitedNodeMarks.toString());
    // System.out.println("Compute Shortest Path End: "
    // + Calendar.getInstance().getTime());
    return shortestPathCost;
  }

  /**
   * Returns the minimum {distU in pq + distU in pq2}.
   * 
   * @return
   */
  public Integer getMinDisFromPq(PriorityQueue<ActiveNode> pq,
      PriorityQueue<ActiveNode> pq2, Map<Integer, Integer> visitedNodeMarks,
      Map<Integer, Integer> visitedNodeMarks2) {

    int mindistUsUt = Integer.MAX_VALUE;
    // relaxedPq<NodeId,relaxedDist>
    HashMap<Integer, Integer> relaxedPq = new HashMap<Integer, Integer>();
    while (!pq.isEmpty()) {
      ActiveNode vnode = pq.poll();
      // Check if it is already in map, because we could have repeated values
      // for same node
      if (!relaxedPq.containsKey(vnode.id)) {
        relaxedPq.put(vnode.id, vnode.dist);
      }
    }
    HashMap<Integer, Integer> relaxedPq2 = new HashMap<Integer, Integer>();
    while (!pq2.isEmpty()) {
      ActiveNode vnode = pq2.poll();
      // Check if it is already in map, because we could have repeated values
      // for same node
      if (!relaxedPq2.containsKey(vnode.id)) {
        relaxedPq2.put(vnode.id, vnode.dist);
      }
    }
    // Iterator it = relaxedPq.keySet().iterator();
    // while(it.hasNext()) {
    // Integer nodeId = (Integer)it.next();
    // int distUs = relaxedPq.get(nodeId);
    // if(relaxedPq2.containsKey(nodeId)) {
    // int distUt = relaxedPq2.get(nodeId);
    // if(mindistUsUt > (distUs + distUt)) {
    // mindistUsUt = distUs + distUt;
    // }
    // }
    // }
    Iterator it = visitedNodeMarks.keySet().iterator();
    while (it.hasNext()) {
      Integer vnodeId = (Integer) it.next();
      int distUs = visitedNodeMarks.get(vnodeId);
      if (relaxedPq2.containsKey(vnodeId)) {
        int distUt = relaxedPq2.get(vnodeId);
        if (mindistUsUt > (distUs + distUt)) {
          mindistUsUt = distUs + distUt;
        }
      }
    }
    int mindistUsUt2 = Integer.MAX_VALUE;
    Iterator it2 = visitedNodeMarks2.keySet().iterator();
    while (it2.hasNext()) {
      Integer vnodeId = (Integer) it2.next();
      int distUt2 = visitedNodeMarks2.get(vnodeId);
      if (relaxedPq.containsKey(vnodeId)) {
        int distUs2 = relaxedPq.get(vnodeId);
        if (mindistUsUt > (distUs2 + distUt2)) {
          mindistUsUt2 = distUs2 + distUt2;
        }
      }
    }
    // System.out.println("mindistUsUt: " + mindistUsUt);
    // System.out.println("mindistUsUt2: " + mindistUsUt2);
    if (mindistUsUt2 < mindistUsUt) {
      mindistUsUt = mindistUsUt2;
    }
    return mindistUsUt;
  }

  /**
   * Generates a random number which of precomputation.
   */
  public int generateRandomNodePosition(int numberOfNodes) {
    // We prepare a random number generator
    Random rand = new Random();
    int min = 0;
    // no problem the random generator won't include this value.
    int max = numberOfNodes;
    return rand.nextInt(max - min);
  }

  /**
   * Set all arcs to true (before precomputation).
   */
  public void setAllArcsToTrue() {
    List<List<Arc>> adjacentArcs = graph.getAdjacentArcs();
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        currentArc.arcFlag = true;
      }
    }
  }
}
