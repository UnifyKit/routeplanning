package routeplanning;

import java.util.List;
import java.util.Map;

/**
 * Generic class of a network.
 */
public interface Network {

  /**
   * Map node id -> position of node as tail node in adjacentArcs used to avoid
   * search by id of the node in adjacentArcs, when adding a new arc.
   */
  Map<Long, Integer> getNodeIdPosAdjArc();

  /**
   * Given a node ID it finds the list containing its adjacent arcs. If the ID
   * cannot be find in the road network. It simply returns NULL
   */ 
  List<Arc> getNodeAdjacentArcs(Long id);

  /**
   * Map nodeId->Node. Contains all nodes
   */
  Map<Long, Node> getMapNodeId();

  /**
   * Return a random Id from the set of nodes.
   */
  long getRandomNodeId();

  /**
   * Return list of nodes.
   */
  List<Long> getNodeIds();

}
