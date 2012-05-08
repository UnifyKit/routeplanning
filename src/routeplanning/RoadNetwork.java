package routeplanning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


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
   * Map nodeId->Node. Contains all nodes
   */
  public Map<Integer,Node> mapNodeId;
  /**
   * Map nodeid -> position of node as tail node in adjacentArcs
   * used to avoid search by id of the node in adjacentArcs, when adding a new arc.
   */
  //public Map<Integer,Integer> nodeIdPosAdjArc;
  /**
   * List of all NodesID
   */
  public List<Integer> nodes;
  /**
   * Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
    mapNodeId = new HashMap<Integer, Node>();
    nodes = new ArrayList<Integer>();
    //nodeIdPosAdjArc = new HashMap<Integer, Integer>();
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
			addNodeToGraph(newNode.id);
			createdNodes.add(newNode);
		}
		System.out.println("createdNodes: "+createdNodes.size());	
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
  public void readFromOsmFile2(String pathIn){
	  //List <String> nodesIDs = new ArrayList<String>();
	  //String strFile = new String();
	  int tmpNodeID=0;
	  Double tmpLatitude=0.0;
	  Double tmpLongitude=0.0;
	  Node currentNode;
	  Node prevNode;
	  String lineIn = "";
	  Node newNode;
	  Boolean readingWay=false;
	  int numNodes=0;
	  List<String> tmpWay = new ArrayList<String>();
	  String roadType = new String();
	  Integer position;
	  int countArcs=0;
	  try {
		  //Read data from osm file
		  System.out.println("Start: " + Calendar.getInstance().getTime());
		  
		  File in = new File(pathIn);
		  BufferedReader inBuff  = new BufferedReader(new FileReader(in));
		  while ((lineIn = inBuff.readLine())!=null){
			  //System.out.println(lineIn);
			  if(lineIn.contains("<node")){
				  numNodes++;
				  tmpNodeID = Integer.valueOf(lineIn.substring(lineIn.indexOf("id=")+4, lineIn.indexOf("\" lat")));
				  tmpLatitude = Double.valueOf(lineIn.substring(lineIn.indexOf("lat=")+5, lineIn.indexOf("\" lon")));
				  tmpLongitude = Double.valueOf(lineIn.substring(lineIn.indexOf("lon=")+5, lineIn.indexOf("\">")));
				  
				  newNode = new Node(tmpNodeID, tmpLatitude, tmpLongitude);
				  mapNodeId.put(tmpNodeID, newNode);
				  addNodeToGraph(newNode.id);
				  //nodeIdPosAdjArc.put(newNode.id, adjacentArcs.size() - 1); //probar y quitar! tailnode
			  } else if (lineIn.contains("<way")) {
				  readingWay = true;
				  tmpWay = new ArrayList<String>();
			  } else if(lineIn.contains("</way")) {
				  readingWay = false;
			  } else if(lineIn.contains("<tag k=\"highway\"") && readingWay) {
				  //roadType, we use it to compute travel time (cost)
				  //from pos 2 nodes that are in the way
				  roadType = String.valueOf(lineIn.substring(lineIn.indexOf("v=")+3, lineIn.indexOf("\" />")));
				  //tmpWay.add(roadType);
				  prevNode = null;
				  if (tmpWay.size() > 2){ //To avoid arcs without head nodes.
					  //System.out.println(tmpWay.toString());
					  for (int i=0; i<tmpWay.size()-1; i++){
						  	currentNode = mapNodeId.get(Integer.valueOf(tmpWay.get(i)));
						  	position =  nodes.indexOf(currentNode.id);// nodeIdPosAdjArc.get(currentNode.id);
						  	//If node doesn't exist in adjacentArcs, then add it
						  	if(position == null){
						  		//addNodeToGraph(currentNode); regresar!!!
						  		position = adjacentArcs.size()-1;
						  		//nodeIdPosAdjArc.put(currentNode.id, position);
						  	}
						  	
							if(i == 0){ //first element doesn't have previous node
								
							} else {
								//add arcs to adjacentArcs
								// prev -> current and current -> prev because is an undirected graph
								//Compute cost and create new arcs
								Arc arc1 = new Arc(prevNode,1.0);
								Arc arc2 = new Arc(currentNode,1.0);
								
								//currentNode -> prevNode
								//Check if the arc already exist.
								if(!arcAlreadyInserted(position,arc1.headNode.id)){
									this.adjacentArcs.get(position).add(arc1);
									countArcs++;
								}
								//prevNode -> currentNode
								position = nodes.indexOf(prevNode.id); //nodeIdPosAdjArc.get(prevNode.id);
								if(!arcAlreadyInserted(position,arc2.headNode.id)){
									this.adjacentArcs.get(position).add(arc2);
									countArcs++;
								}
							}
							prevNode = currentNode;							  
					  }
					  
				} 
			  } else if(lineIn.contains("<nd ref=") && readingWay){
				  //current node id in way
				  
				  tmpWay.add(String.valueOf(lineIn.substring(lineIn.indexOf("<nd ref=")+9, lineIn.indexOf("\"/>"))));
				  //System.out.println(tmpWay.toString());
//				  
				  
			  } else {
				  
			  }
			  
		  }
		  
		  System.out.println("Num nodes: " + numNodes);
		  //System.out.println(nodes.size());
		  System.out.println("Number of arcs: " + countArcs/2);
		  //System.out.println("adjacentArcs Size: " + this.adjacentArcs.size());
		  System.out.println("Finish: " + Calendar.getInstance().getTime());
		  
		  //System.out.println(printArcsFromNode(10467200));
		  checkArcs();
		  
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
public Boolean arcAlreadyInserted(int tailNodePos, int nodeid){
	
	for(int k=0; k<adjacentArcs.get(tailNodePos).size(); k++){
		if(nodeid == adjacentArcs.get(tailNodePos).get(k).headNode.id){
			return true;
		}
	}
	return false;
}
public void checkArcs(){
	List<Integer> listid;
	int count=0;
	for(int i=0; i<adjacentArcs.size(); i++){
		listid = new ArrayList<Integer>();
		for(int j=0; j<adjacentArcs.get(i).size(); j++){
			if(listid.contains(adjacentArcs.get(i).get(j).headNode.id)){
				count++;
				System.out.println("Error en nodeid: " +nodes.get(i) +" nodeid repeated:"+adjacentArcs.get(i).get(j).headNode.id);
			}else{
				listid.add(adjacentArcs.get(i).get(j).headNode.id);
			}	
		}
	}
	System.out.println("Num errores: " + count);
}
  /**
   * Add an ArrayList<Arc> for the arcs of tail node
   * @param tailNode
   */
  public void addNodeToGraph(int tailNodeID) {
    adjacentArcs.add(new ArrayList<Arc>());
    nodes.add(tailNodeID);
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
      list.add(String.valueOf(nodes.get(i)));
      for (int j = 0; j < adjacentArcs.get(i).size(); j++) {
        list.add(adjacentArcs.get(i).get(j).asString());
      }
      res.add(list);
    }
    return res;
  }  
  public List<String> printArcsFromNode(int nodeid){
	  List<String> list = new ArrayList<String>();
	  list.add(String.valueOf(nodeid));
	  int pos;
	  pos = nodes.indexOf(nodeid);
	  for(int i=0; i<adjacentArcs.get(pos).size(); i++){
		  list.add(String.valueOf(adjacentArcs.get(pos).get(i).headNode.id));
	  }
	  return list;
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
	  double lat1 = node1.latitude;
	  double lat2 = node2.latitude;
	  double lon1 = node1.longitude;
	  double lon2 = node1.longitude;
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
    	if(firstNode.id==id){
    		matchedNode = firstNode;
    	}	
    }
    return matchedNode;
  }
}
