package routeplanning.deprecated;

import java.util.ArrayList;
import java.util.List;

import routeplanning.Arc;
import routeplanning.RoadNetwork;
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
  private List<Integer> visitedNodeMarks;
  /**
   * Reference to graph on which this object is supposed to work.
   */
  private RoadNetwork graph;
  
  
  
  /**
   * Create instance of this class for a given (road) graph.
   * @param graph
   */
  public DijkstraAlgorithm(RoadNetwork graph) {
    this.graph = graph;
    visitedNodeMarks = new ArrayList<Integer>();
    
    for (int i = 0; i < this.graph.getNodeIds().size(); i++) {
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
  public double computeShortestPath(int sourceNodeId, int targetNodeId) {
    int shortestPathCost = 0;
    int pos = 0;
    int testNode = 0;
    System.out.println("nodesId" + this.graph.getNodeIds().toString());
    List<List<Arc>> adjacentsArcs = graph.getAdjacentArcs();
    //Active nodes nodeId 
    List<Integer> activeNodesId = new ArrayList<Integer>();
    //Active nodes travelTime
    List<Integer> activeNodesCost = new ArrayList<Integer>();
     
    
    List<Arc> adjArcsTestNode;
    int minDist = 0;
    Integer minDistNode = 0;
    int distToAdjNode = 0;
    //int minValue = Integer.MAX_VALUE;
    int minValueKey = 0;
    
    List<Integer> path = new ArrayList<Integer>();
    
    
    testNode = sourceNodeId;
    activeNodesId.add(testNode);
    activeNodesCost.add(0);
    Boolean noAdjacentNodes = false;
    
    pos = this.graph.getNodeIds().indexOf(sourceNodeId);
    //Set sourceNodeId as visited

    visitedNodeMarks.set(pos, 0);
       
    path.add(testNode);
        
    //System.out.println(visitedNodeMarks.toString());
    
    while (!activeNodesId.isEmpty()) {

      //System.out.println("--------------------
        //+ ----------------------------------------");
      pos = this.graph.getNodeIds().indexOf(testNode);
      noAdjacentNodes = false;
      //Add adjacent nodes of source -> activeNodes
      for (int i = 0; i < adjacentsArcs.get(pos).size(); i++) {
        if (visitedNodeMarks.get(posInGraph(
          (int) adjacentsArcs.get(pos).get(i).headNode.getId())) == null) {
          activeNodesId.add((int) adjacentsArcs.get(pos).get(i).
              headNode.getId());
          activeNodesCost.add(shortestPathCost 
              + adjacentsArcs.get(pos).get(i).cost);    
          noAdjacentNodes = true;
        }
      }
      if (!noAdjacentNodes) {
        if (!path.contains(testNode)) {
          visitedNodeMarks.set(this.graph.getNodeIds().indexOf(testNode), 
              activeNodesCost.get(activeNodesId.indexOf(testNode))); //settled
        }
        activeNodesCost.remove(activeNodesId.indexOf(testNode));
        activeNodesId.remove(activeNodesId.indexOf(testNode));
        
        if (!activeNodesId.isEmpty()) {
          testNode = activeNodesId.get(0);
        }
        //System.out.println("activeNodes antes continue" 
          //+ activeNodesId.toString());
        continue;
        
      }
      System.out.println("activeNodes: " + activeNodesId.toString());
      //System.out.println("activeNodesCost: " + activeNodesCost.toString());
      
      //calcular distancias y escoger la mejor 
      //despues poner testNode como settled
      adjArcsTestNode = adjacentsArcs.get(this.graph.
          getNodeIds().indexOf(testNode));
      //System.out.println("adjacentArcsTestNode: " 
      //+ adjArcsTestNode.toString());
      minDist = Integer.MAX_VALUE;
      for (int i = 0; i < adjArcsTestNode.size(); i++) {
        if (visitedNodeMarks.get(posInGraph(
            (int) adjArcsTestNode.get(i).headNode.getId())) == null) {
          distToAdjNode = shortestPathCost + adjArcsTestNode.get(i).cost;
          if (minDist > distToAdjNode) {
            minDist = distToAdjNode;
            minDistNode = (int) adjArcsTestNode.get(i).headNode.getId();
            minValueKey = i; 
          }  
        }
      }
      //System.out.println("Node adj con costo mas chico: " + minDistNode);
      //System.out.println("Node: " + testNode + " closer node: " 
      //+ getAdjNodeWithShortestDist(testNode));
      
      shortestPathCost = shortestPathCost
        + adjArcsTestNode.get(minValueKey).cost;
      visitedNodeMarks.set(this.graph.getNodeIds().indexOf(minDistNode), 
          shortestPathCost); //settled
      path.add(minDistNode);
      //System.out.println("Path: " + path.toString());
      
      if (minDistNode == targetNodeId) {
        break;
      }
      
      //eliminar de activeNodes porque ya lo recorrimos

      activeNodesCost.remove(activeNodesId.indexOf(testNode));
      activeNodesId.remove(activeNodesId.indexOf(testNode));

      
      //nuevo testNode
      testNode = activeNodesId.get(activeNodesId.indexOf(minDistNode));
      
      
      //System.out.println("visited: " + visitedNodeMarks.toString()); 
      //System.out.println("activeNodesId: " + activeNodesId.toString());
      //System.out.println("shortestPathCost: " + shortestPathCost);
     
    }
    System.out.println("visited: " + visitedNodeMarks.toString());
    System.out.println("Path: " + path.toString());
    System.out.println("shortestPathCost: " + shortestPathCost);
    return shortestPathCost;
  }
  
  
  /**
   * Pos in Graph.
   */
  private int posInGraph(int nodeId) {
    return this.graph.getNodeIds().indexOf(nodeId);
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
}
