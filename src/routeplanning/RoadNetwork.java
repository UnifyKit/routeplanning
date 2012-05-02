package routeplanning;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CJC
 */
public class RoadNetwork {
  /**
   * List of adjacent Arcs. First element of a each list is the head node.
   */
  List<List<Arc>> adjacentArcs;
  /**
   * Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
  }
  /**
   * Read OSM file (in XML format) and construct the corresponding road network.
   */
  public void readFromOsmFile() {
    
  }
  /**
   * First element of the ArrayList<Arc> is the tail node, the other lists contain the headnodes.
   * because node1->node1 then cost=0
   * @param tailNode 
   */
  public void addNodeToGraph(Node tailNode) {
    Arc arc0 = new Arc(tailNode, 0);
    adjacentArcs.add(new ArrayList<Arc>());
    adjacentArcs.get(adjacentArcs.size() - 1).add(arc0);
  }
  /**
   * Add adjacent arc to tail node.
   * @param tailNode
   * @param arc
   */
  public void addAdjacentArc(Node tailNode, Arc arc) {
    for (int i = 0; i < adjacentArcs.size(); i++) {
      //First list, first element
      if (adjacentArcs.get(i).get(0).getHeadNode().equals(tailNode)) {  
        adjacentArcs.get(i).add(arc);
      }
    }
  }
  /**
   * Get RoadNetwork as String.
   * @return
   */
  public ArrayList<List<String>> asString() {
    ArrayList<List<String>> res = new ArrayList<List<String>>();
    List<String> list;
    for (int i = 0; i < adjacentArcs.size(); i++) {
      list = new ArrayList<String>();
      for (int j = 0; j < adjacentArcs.get(i).size(); j++) {
        list.add(adjacentArcs.get(i).get(j).asString());
      }
      res.add(list);
    }
    return res;
  }
  /**
   * Compute cost (travel time).
   * If roadType is not valid, method return -1 to indicate that we should
   * ignore this road.
   * @param roadType
   * @param distance in km
   * @return
   */
  public double computeCost (String roadType, double distance){
	//speed in km/h  
	double speed=1;
	double cost;
	if (roadType.equals("motorway") || roadType.equals("trunk")) {
		speed = 110;
	} else if (roadType.equals("primary")) {
		speed = 70;
	} else if (roadType.equals("secondary")) {
		speed = 60;
	} else if (roadType.equals("tertiary") || roadType.equals("motorway_link") || roadType.equals("trunk_link") || roadType.equals("primary_link") || roadType.equals("secondary_link")) {
		speed = 50;
	} else if(roadType.equals("road") || roadType.equals("unclassified")) {
		speed = 40;
	} else if (roadType.equals("residential") || roadType.equals("unsurfaced")) {
		speed = 30;
	} else if (roadType.equals("living_street")) {
		speed = 10;
	} else if (roadType.equals("service")) {
		speed = 5;	
	} else {
		return -1;
	}
	  
	cost = distance / speed;  
    return cost; 
  }
}