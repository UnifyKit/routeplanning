package routeplanning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.net.URL;


import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;
import org.dom4j.Attribute;


/**
 *
 * @author CJC | AAA
 */
public class RoadNetwork {
  /**
   * List of adjacent Arcs. First element of a each list is the head node.
   */
  private List<List<Arc>> adjacentArcs;
  
  public List<List<Arc>> getAdjacentArcs() {
	return adjacentArcs;
  }

/**
   * Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
  }
  /**
   * Read OSM file (in XML format) and construct the corresponding road network.
   */
  public void readFromOsmFile(){
	URL osmUrl = this.getClass().getClassLoader().getResource("routeplanning/resources/osm-sample.osm");
    SAXReader  reader = new SAXReader ();
    try {
    	Document document = reader.read(osmUrl);
			
		//CREATION OF NODES
		List<Node> createdNodes = new ArrayList();
		List nodes = document.selectNodes("//osm/node");
		//for each of the nodes in the XML file
		for (int i= 0; i<nodes.size(); i++){
			Node newNode = null;
			Element node = (Element) nodes.get(i);
			List nodeAttributes = node.attributes();
			//for each of the attributes of the node
			Integer id = new Integer(0);
			Double lat = new Double(0.0);
			Double lon = new Double(0.0);
			for (int k= 0; k<nodeAttributes.size(); k++){
				Attribute att = (Attribute) nodeAttributes.get(k);
				if(att.getName().equals("id"))
					id = Integer.parseInt(att.getValue());
				if(att.getName().equals("lat"))
					lat = Double.parseDouble(att.getValue());
				if(att.getName().equals("lon"))
					lon = Double.parseDouble(att.getValue());
			}
			newNode = new Node(id, lat, lon);
			addNodeToGraph(newNode);
			createdNodes.add(newNode);
		}
			
		//CREATION OF ARCS IN WAYS	
		List ways = document.selectNodes("//way[tag/@k='highway']");
		for (int k= 0; k<ways.size(); k++){
			Element way  = (Element) ways.get(k);
			String wayId = way.attribute("id").getValue();
			
			//Getting the type of road
			List roads = document.selectNodes("//way[@id='"+wayId+"']/tag[@k='highway']");
			String roadType = ((Element)roads.get(0)).attribute("v").getValue();
							
			List nodesInWay = document.selectNodes("//way[@id='"+wayId+"']/nd");
			for (int j=0; j<nodesInWay.size(); j++){
				Element nodeInWay = (Element)nodesInWay.get(j);
				String nodeId = ((Attribute)nodeInWay.attribute("ref")).getValue();
				Node node = findNodebyId(Integer.parseInt(nodeId));
				
				if(j==0){
					Element adjacentNode =  (Element) nodesInWay.get(j+1);
					String adjacentNodeId = ((Attribute)adjacentNode.attribute("ref")).getValue();				
						
					Node adjNode = findNodebyId(Integer.parseInt(adjacentNodeId));
											
					if(node!=null && adjNode!=null){
						double distance = getDistance(node, adjNode);
						double cost = computeCost(roadType, distance);
						Arc newArc = new Arc(adjNode, cost);
						addAdjacentArc(node, newArc);
					}
				}
				else if(j==nodesInWay.size()-1){
					Element adjacentNode =  (Element) nodesInWay.get(j-1);
					String adjacentNodeId = ((Attribute)adjacentNode.attribute("ref")).getValue();
					
					Node adjNode = findNodebyId(Integer.parseInt(adjacentNodeId));
					
					if(node!=null && adjNode!=null){
						double distance = getDistance(node, adjNode);
						double cost = computeCost(roadType, distance);
						Arc newArc = new Arc(adjNode, cost);
						addAdjacentArc(node, newArc);
					}
				}
				else{
					Element nextAdjacentNode =  (Element) nodesInWay.get(j+1);
					String nextAdjacentNodeId = ((Attribute)nextAdjacentNode.attribute("ref")).getValue();
					Element prevAdjacentNode =  (Element) nodesInWay.get(j-1);
					String prevAdjacentNodeId = ((Attribute)prevAdjacentNode.attribute("ref")).getValue();
					
					Node nextAdjNode = findNodebyId(Integer.parseInt(nextAdjacentNodeId));
					Node prevAdjNode = findNodebyId(Integer.parseInt(prevAdjacentNodeId));
					
					if(node!=null && nextAdjNode!=null){
						double distance = getDistance(node, nextAdjNode);
						double cost = computeCost(roadType, distance);
						Arc newArc = new Arc(nextAdjNode, cost);
						addAdjacentArc(node, newArc);
					}
					if(node!=null && prevAdjNode!=null){
						double distance = getDistance(node, prevAdjNode);
						double cost = computeCost(roadType, distance);
						Arc newArc = new Arc(prevAdjNode, cost);
						addAdjacentArc(node, newArc);
					}	
				}
			}
		}
    }
    catch (DocumentException e) {
    	e.printStackTrace();
	}
  }	
  /**
   * First element of the ArrayList<Arc> is the tail node,
   * the other lists contain the headnodes.
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
   * @return cost
   */
  public double computeCost(String roadType, double distance) {
    /**
     * Speed in km/h.
     */
    double speed = 1;
    /**
     * Travel time.
     */
    double cost;
    if (roadType.equals("motorway") || roadType.equals("trunk")) {
      speed = 110;
    } else if (roadType.equals("primary")) {
      speed = 70;
    } else if (roadType.equals("secondary")) {
      speed = 60;
    } else if (roadType.equals("tertiary") || roadType.equals("motorway_link")
            || roadType.equals("trunk_link") || roadType.equals("primary_link")
            || roadType.equals("secondary_link")) {
      speed = 50;
    } else if (roadType.equals("road") || roadType.equals("unclassified")) {
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
  /**
   * Compute and return distance from node1 to node2.
   * @param node1
   * @param node2
   * @return distance
   */
/*  public double getDistance2(Node node1, Node node2) {
    double distance;
    distance = Math.sqrt(Math.pow(node1.latitude - node2.latitude, 2) + Math.pow(node1.longitude - node2.longitude, 2));
    return distance;
  }*/
  
  /**  
  * Compute and return distance from node1 to node2.
  * @param node1
  * @param node2
  * @return distance
  */
/*  public double getDistance(Node node1, Node node2) {
	  double lat1 = node1.getLatitude();
	  double lat2 = node2.getLatitude();
	  double lon1 = node1.getLongitude();
	  double lon2 = node1.getLongitude();
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
       return (dist);
    }
    
    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }
   private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
    */
  
  /**  
  * Compute and return distance in mts from node1 to node2.
  * @param node1
  * @param node2
  * @return distance
  */
  public double getDistance(Node node1, Node node2) {
	  double lat1 = node1.getLatitude();
	  double lat2 = node2.getLatitude();
	  double lon1 = node1.getLongitude();
	  double lon2 = node1.getLongitude();
	  double earthRadius = 3958.75;
	  double dLat = Math.toRadians(lat2-lat1);
	  double dLng = Math.toRadians(lon2-lon1);
	  double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			  Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	          Math.sin(dLng/2) * Math.sin(dLng/2);
	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	  double dist = earthRadius * c;

	  int meterConversion = 1609;

	  return new Float(dist * meterConversion).floatValue();
  }
  
  private Node findNodebyId(int id){
    Node matchedNode = null;
    for(int i=0; i<adjacentArcs.size(); i++){
    	List<Arc> arcList = adjacentArcs.get(i);
    	Arc firstArc = arcList.get(0);
    	Node firstNode = firstArc.getHeadNode();
    	if(firstNode.getId()==id){
    		matchedNode = firstNode;
    	}	
    }
    return matchedNode;
  }
}
