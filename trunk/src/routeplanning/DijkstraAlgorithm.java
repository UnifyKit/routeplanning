package routeplanning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
/**
 * 
 * @author CJC, AAA
 *
 */
public class DijkstraAlgorithm {

  // PRIVATE members.
  /**
   * Indicator which node was visited by a particular run of Dijkstra. Useful
   * for computing the connected components; 
   */
  private List<Double> visitedNodeMarks;
  /**
   * Reference to graph on which this object is supposed to work.
   */
  private RoadNetwork graph;
  List<Integer> distancesFromSource;
  
  
  /**
   * Create instance of this class for a given (road) graph.
   * @param graph
   */
  public DijkstraAlgorithm(RoadNetwork graph) {
    this.graph = graph;
    visitedNodeMarks = new ArrayList<Double>();
    distancesFromSource = new ArrayList<Integer>(); //shortest distances from source node
    for (int i = 0; i < this.graph.nodes.size(); i++) {
      visitedNodeMarks.add(null);
      distancesFromSource.add(Integer.MAX_VALUE);
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
  public double computeShortestPath(int sourceNodeId, int targetNodeId) {
    double shortestPathCost = 0;
    int pos = 0;
    int testNode = 0;
    
    List<List<Arc>> adjacentsArcs = graph.getAdjacentArcs();
    //Active nodes nodeId 
    List<Integer> activeNodesId = new ArrayList<Integer>();
    //Active nodes travelTime
    List<Double> activeNodesCost = new ArrayList<Double>();
     
    
    List<Arc> adjArcsTestNode;
    Double minDist=0.0;
    Integer minDistNode=0;
    Double distToAdjNode=0.0;
    int minValue = Integer.MAX_VALUE;
    int minValueKey = 0;
    int previous=0;
    List<Integer> path = new ArrayList<Integer>();
    PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
    Object next;
    testNode = sourceNodeId;
    activeNodesId.add(testNode);
    activeNodesCost.add(0.0);
    distancesFromSource.get(posInGraph(testNode));
    pq.add(sourceNodeId);
    Boolean noAdjacentNodes=false;
    
    //for(int i=0; i < this.graph.nodes.size(); i++) {
    //  pq.add(this.graph.nodes.get(i));
    //}
    
    pos = this.graph.nodes.indexOf(sourceNodeId);
    //Set sourceNodeId as visited

    visitedNodeMarks.set(pos, 0.0);
       
    path.add(testNode);
        
    System.out.println(visitedNodeMarks.toString());
    
    while (!activeNodesId.isEmpty()) {
//      if(path.contains(testNode) && testNode != sourceNodeId) {
//        activeNodesId.remove(activeNodesId.indexOf(testNode));
//        continue;
//      }
      System.out.println("------------------------------------------------------------");
      pos = this.graph.nodes.indexOf(testNode);
      noAdjacentNodes = false;
      //Add adjacent nodes of source -> activeNodes
      for (int i = 0; i < adjacentsArcs.get(pos).size(); i++) {
        if(visitedNodeMarks.get(posInGraph(adjacentsArcs.get(pos).get(i).headNode.id)) == null){
          activeNodesId.add(adjacentsArcs.get(pos).get(i).headNode.id);
          activeNodesCost.add(shortestPathCost + adjacentsArcs.get(pos).get(i).cost);
          //distancesFromSource.set(posInGraph(adjacentsArcs.get(pos).get(i).headNode.id),adjacentsArcs.get(pos).get(i).cost);
          pq.add(adjacentsArcs.get(pos).get(i).headNode.id);
          noAdjacentNodes = true;
        }
      }
      if(!noAdjacentNodes) {
        if(!path.contains(testNode)){
          visitedNodeMarks.set(this.graph.nodes.indexOf(testNode), activeNodesCost.get(activeNodesId.indexOf(testNode))); //settled
        }
        activeNodesCost.remove(activeNodesId.indexOf(testNode));
        activeNodesId.remove(activeNodesId.indexOf(testNode));
        
        if(!activeNodesId.isEmpty()){
          testNode = activeNodesId.get(0);
        }
        System.out.println("activeNodes antes continue" + activeNodesId.toString());
        continue;
        
      }
      System.out.println("activeNodes: " + activeNodesId.toString());
      System.out.println("activeNodesCost: " + activeNodesCost.toString());
      
      //calcular distancias y escoger la mejor despues poner testNode como settled
      adjArcsTestNode = adjacentsArcs.get(this.graph.nodes.indexOf(testNode));
      System.out.println("adjacentArcsTestNode: " + adjArcsTestNode.toString());
      minDist = Double.MAX_VALUE;
      for (int i = 0; i < adjArcsTestNode.size(); i++) {
        if(visitedNodeMarks.get(posInGraph(adjArcsTestNode.get(i).headNode.id)) == null ) {
          distToAdjNode = shortestPathCost + adjArcsTestNode.get(i).cost;
          if(minDist > distToAdjNode) {
            minDist = distToAdjNode;
            minDistNode = adjArcsTestNode.get(i).headNode.id;
            minValueKey = i; 
          }  
        }
      }
      System.out.println("Node adj con costo mas chico: " + minDistNode);
      //System.out.println("Node: " + testNode + " closer node: " + getAdjNodeWithShortestDist(testNode));
      
      shortestPathCost = shortestPathCost + adjArcsTestNode.get(minValueKey).cost;
      visitedNodeMarks.set(this.graph.nodes.indexOf(minDistNode), shortestPathCost); //settled
      path.add(minDistNode);
      System.out.println("Path: " + path.toString());
      
      //eliminar de activeNodes porque ya lo recorrimos
      //if(activeNodesId.contains(testNode)){
        activeNodesCost.remove(activeNodesId.indexOf(testNode));
        activeNodesId.remove(activeNodesId.indexOf(testNode));
      //}
      
      //nuevo testNode
      testNode = activeNodesId.get(activeNodesId.indexOf(minDistNode));
      
      
      System.out.println("visited: " + visitedNodeMarks.toString()); 
      System.out.println("activeNodesId: " + activeNodesId.toString());
      System.out.println("shortestPathCost: " + shortestPathCost);
     
    }
    System.out.println("visited: " + visitedNodeMarks.toString());
    return shortestPathCost;
  }
  private int posInGraph(int nodeId){
    return this.graph.nodes.indexOf(nodeId);
  }
  /**
   * Search in the adjacent nodes the one with the shortest distance.
   * return nodeId with the shortest distance.
   * @param nodeId
   * @param actualDistance
   */
  private int getAdjNodeWithShortestDist(int nodeId){
    List<Arc> adjArcsTestNode;
    Double minDist=Double.MAX_VALUE;
    Double distToAdjNode;
    int closerNode=-1; //manejar cuando sea -1
    
    adjArcsTestNode = this.graph.getAdjacentArcs().get(this.graph.nodes.indexOf(nodeId));
    
    for(int i = 0; i < adjArcsTestNode.size(); i++) {
      if(visitedNodeMarks.get(posInGraph(adjArcsTestNode.get(i).headNode.id)) == null ){
        distToAdjNode = adjArcsTestNode.get(i).cost;
        if(minDist > distToAdjNode) {
          minDist = distToAdjNode;
          closerNode = adjArcsTestNode.get(i).headNode.id;
        }    
      }
    }
    return closerNode;
  }
  /**
   * Mark all nodes visited by the next call to computeShortestPath with this
   * number.
   * @param mark
   */
  private void setVisitedNodeMark(int mark) {

  }
  
  
}
