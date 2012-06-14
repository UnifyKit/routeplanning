package routeplanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class implementing the Contraction Hierarchies' approach from lecture 6.
 */
public class ContractionHierarchies {
  /**
   * Reference to graph on which this object is supposed to work.
   * NOTE: the contraction hierarchies algorithm *modifies* this graph by adding
   * shortcuts, so this must be a *non-const* reference or pointer, and you 
   * should be aware that by using this class, the original graph will be 
   * modified.
   */
  private RoadNetwork graph;
  
  /**
   * Object for the various execution of  Dijkstra's algorithm on 
   * the augmented graph.
   */
  private DijkstraAlgorithm dijkstra;
  
  // The ordering of the nodes. This is simply a permutation of {0, ..., n-1},
  // where n is the number of nodes; nodeOrdering[i] simply contains the index
  // of the i-th node in the ordering, for i = 0, ..., n-1.
  /**
   * ordering of the nodes.
   */
  List<Integer> nodeOrdering;

  /**
   * Creates instance of this class for a given (road) graph and
   * builds the DijkstraAlgorithm with setting setConsiderArcFlags(true).
   */
  public ContractionHierarchies(RoadNetwork graph) {
    this.graph = graph;
    //prepares DijkstraAlgorithm
    dijkstra = new DijkstraAlgorithm(graph);
    dijkstra.setConsiderArcFlags(true);
  }
  
  /**
   * Getter for nodeOrdering.
   */
  public List<Integer> getNodeOrdering() {
    return nodeOrdering;
  }
  
  /**
   * Setter for nodeOrdering.
   */
  public void setNodeOrdering(List<Integer> nodeOrdering) {
    this.nodeOrdering = nodeOrdering;
  }
  
  /**
   * Main precomputation method.
   * Initially, all arcs in the graph are initially set as Flags.  
   */  
  public void precomputation() {
    //set all arcs as flags
    List<List<Arc>> adjacentArcs = graph.getAdjacentArcs();
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcList = adjacentArcs.get(i);
      for (int k = 0; k < arcList.size(); k++) {
        Arc currentArc = arcList.get(k);
        currentArc.arcFlag = true;
      }
    }
    computeRandomNodeOrdering();
    for (int i = 0; i < nodeOrdering.size(); i++) {
      contractNode(i);
    }
  }
  
  /**
   * It computes a random permutation of the set {0,..., n-1} as follows:
   * It initializes nodeOrdering with 0, 1, ..., n - 1. Swap nodeOrdering[0]
   * with a random entry to the right. Then swap nodeOrdering[1] with
   * a random entry on the right. And so on.
   * This implementation takes linear time.
   */
  public void computeRandomNodeOrdering() {
    //We have already the list "nodes" which has all the ids of the nodes.
    //We will work on a copy of this list (it only copies the references).
    List<Integer> randomNodeOrdering = new ArrayList<Integer>();
    randomNodeOrdering.addAll(graph.getNodeIds());
    int numberOfNodes = randomNodeOrdering.size();
    
    //linear time (on the number of nodes)
    for (int i = 0; i < randomNodeOrdering.size(); i++) {
      Integer currentNodeId = randomNodeOrdering.get(i);
      //We have to swap this with a random position
      int newPos = generateRandomNodePosition(numberOfNodes);
      while (newPos == i) {
        newPos = generateRandomNodePosition(numberOfNodes);
      }
      Integer tempRef = currentNodeId;
      randomNodeOrdering.set(i, randomNodeOrdering.get(newPos));
      randomNodeOrdering.set(newPos, tempRef);
    }
    nodeOrdering = randomNodeOrdering;
  }

  /**
   * Central contraction routine: contract the i-th node in the ordering,
   * ignoring nodes 1, ..., i - 1 in the ordering and their adjacent arcs.
   * IMPLEMENTATION NOTE: To ignore nodes (and their adjacent arcs), you can
   * simply use the arcFlag member of the Arc class. As it goes along and 
   * contract nodes, simply set the flags of the arcs adjacent
   * to contracted nodes to 0.
   */
  public void contractNode(int i) {    //The following map will save the costs from all Ui to V
    Map<Integer, Integer> costsMap = new HashMap();
    //The following map keeps all the new arcs that will be added
    //in the end.
    Map<Node, List<Arc>> addedArcsMap = new HashMap();
    
    //nodeId is "v" the node I want to contract.
    Integer nodeVId = nodeOrdering.get(i);
    System.out.println("Contracting node " + nodeVId);
    //Node currentNode = graph.getMapNodeId().get(nodeId);
    //First we need all adjacent nodes of currentNode.
    //list of all "u_i"
    List<Arc> arcs = graph.getNodeAdjacentArcs(nodeVId);
    
    //We set all arcs from all u_i to v as not Flag, so that they
    //won't be considered in the calculation of SP.
    for (int k = 0; k < arcs.size(); k++) {
      Arc arcToV = arcs.get(k);
      arcToV.arcFlag = false;
      Integer nodeUi = arcs.get(k).getHeadNode().getId();
      Integer cost = arcs.get(k).cost;
      costsMap.put(nodeUi, cost);
      System.out.println("Cost between " +  nodeVId 
          +  " and " + nodeUi + " is " + cost); 
    }
    
    //TODO: check if this strategy is correct
    //For each pair of adjacent nodes:
    for (int k = 0; k < arcs.size(); k++) {
      
      Integer nodeIdUi = arcs.get(k).getHeadNode().getId();
      List<Arc> uiArcs = graph.getNodeAdjacentArcs(nodeIdUi);
      
      //Setting to set the arc from Ui to V as arcFlag False
      for (int q = 0; q < uiArcs.size(); q++) {
        Arc currentUiArc = uiArcs.get(q);
        Integer headNodeId = currentUiArc.getHeadNode().getId();
        if (headNodeId == nodeVId) {
          currentUiArc.arcFlag = false;
          System.out.println("Setting to set the arc " 
            + " from Ui to V as arcFlag False");
        }
      }
      
      for (int j = 0; j < arcs.size(); j++) {
        Integer nodeIdWj = arcs.get(j).getHeadNode().getId();
        //Checks if an adjacent node was deleted in previous contractions.
        boolean wjWasDeleted = true;
        List<Arc> wjArcs = graph.getNodeAdjacentArcs(nodeIdWj);
        for (int q = 0; q < wjArcs.size(); q++) {
          if (wjArcs.get(q).arcFlag) {
            wjWasDeleted = false;
            break;
          }
        }
        
        //checks that ui and wj are the same.
        if (k != j && !wjWasDeleted) {
          System.out.println("Checking nodes " + nodeIdUi + " - " + nodeIdWj);
          int cost = dijkstra.computeShortestPath(nodeIdUi, nodeIdWj);
          //I have to verify if the SP without v is better or
          //worse than Dij = cost(Ui,V)+ cost(V,Wj)
          int pathThroughV = costsMap.get(nodeIdUi) + costsMap.get(nodeIdWj);
          
          System.out.println("COST:" + cost + " - PATH-V: " + pathThroughV);
        
          if (cost > pathThroughV || cost == 0) {
            System.out.println("***ADDING new Arc: " 
              + nodeIdUi + " - " + nodeIdWj + " COST: " + pathThroughV);
          //then it is necessary to add the new arc
            Node nodeUi = graph.getMapNodeId().get(nodeIdUi);
            Node nodeWj = graph.getMapNodeId().get(nodeIdWj);
          
            //Arcs in two directions are needed
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
          }
        //else no extra arc is needed
        }
      }
    }
    
    //Time to add all accumulated arcs.
    Iterator it = addedArcsMap.keySet().iterator();
    while (it.hasNext()) {
      Node node = (Node) it.next();
      List<Arc> arcsToAdd = addedArcsMap.get(node);
      for (int k = 0; k < arcsToAdd.size(); k++) {
        graph.addAdjacentArc(node, arcsToAdd.get(k));
        System.out.println("***ADDED ARC: " + node.getId() + " - " 
          + arcsToAdd.get(k).getHeadNode().getId() + ":: "
          + arcsToAdd.get(k).cost);
      }
    }
  }
  
  /**
   * Generates a random number which  of precomputation.
   */
  public int generateRandomNodePosition(int numberOfNodes) {
  //We prepare a random number generator 
    Random rand = new Random();
    int min = 0; 
    //no problem the random generator won't include this value.
    int max = numberOfNodes;
    return rand.nextInt(max - min);
  }
}







