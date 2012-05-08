package routeplanning;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    int pos = 0;
    Iterator iter;
    List<List<Arc>> adjacentsArcs = graph.getAdjacentArcs();
    //Active nodes nodeId -> travelTime
    TreeMap<Integer, Double> activeNodes = new TreeMap<Integer, Double>();
    Set<Integer> keySet = new HashSet<Integer>();
      
    pos = this.graph.nodes.indexOf(sourceNodeId);
    //Search adjacent nodes of source -> activeNodes
    for (int i = 0; i < adjacentsArcs.get(pos).size(); i++) { 
      activeNodes.put(adjacentsArcs.get(pos).get(i).headNode.id, adjacentsArcs.get(pos).get(i).cost);
    }
    
    System.out.println("activeNodes: " + activeNodes.toString());
    
    //activeNodes = adjacentsArcs.get(pos);
    while (!activeNodes.isEmpty()) {
      //Search the min travel time of active node
      int minValue = Integer.MAX_VALUE;
      keySet = activeNodes.keySet();
      iter = keySet.iterator();
      while(iter.hasNext()) {
        if (minValue > activeNodes.get(iter.next())) {
          //minValue = activeNodes.get(iter.next().intValue());
        }
        System.out.println("keys iterator: " + iter.next());
      }
      for(int i = 0; i <activeNodes.size(); i++) {
        System.out.println("minValue: " + minValue);
        if(minValue > activeNodes.get(i)) {
          minValue = activeNodes.get(i).intValue(); //quitar intvalue si lo dejamos como double
        }
        System.out.println("minValue: " + minValue);
      }
      
      //testNode =
    }
    
    return shortestPathCost;
  }
  /**
   * Mark all nodes visited by the next call to computeShortestPath with this
   * number.
   * @param mark
   */
  private void setVisitedNodeMark(int mark) {

  }
  
  
}
