package routeplanning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 * @author CJC | AAA
 */
public class RoadNetwork {
  /**
   * List of all Nodes IDs.
   */
  private List<Integer> allNodeIds;
 
  /**
   * List of all Nodes that have adjacent arcs.
   */
  private List<Integer> nodeIds;
  /**
   * List of adjacent Arcs. First element of a each list is the head node.
   */
  private List<List<Arc>> adjacentArcs;

  /**
   * Map nodeId->Node. Contains all nodes
   */
  private Map<Integer, Node> mapNodeId;
  /**
   * Map nodeid -> position of node as tail node in adjacentArcs used to avoid
   * search by id of the node in adjacentArcs, when adding a new arc.
   */
  private Map<Integer, Integer> nodeIdPosAdjArc;

  /**
   * Keeps track of the number of arcs.
   */
  //private Integer numberOfArcs = 0;


  /**
   * Default Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
    mapNodeId = new HashMap<Integer, Node>();
    allNodeIds = new ArrayList<Integer>();
    nodeIds = new ArrayList<Integer>();
    nodeIdPosAdjArc = new HashMap<Integer, Integer>();
  }
  
  /**
   *Setter method for nodes.
   */
  //TODO
  public void setNodes(List<Integer> nodeIds) {
    this.nodeIds = nodeIds;
  }
  
  /**
   *Setter method for allNodes.
   */
  //TODO
  public void setAllNodes(List<Integer> allNodeIds) {
    this.allNodeIds = allNodeIds;
  }

  /**
   * Getter method for nodes.
   */
  public List<Integer> getNodeIds() {
    return nodeIds;
  }
  
  /**
   * Getter method for allNodesIds.
   */
  public List<Integer> getAllNodeIds() {
    return allNodeIds;
  }
  
  /**
   * Setter method for adjacentArcs.
   */
  //TODO
  public void setAdjacentArcs(List<List<Arc>> adjacentArcs) {
    this.adjacentArcs = adjacentArcs;
  }
  
  /**
   * Getter method for adjacentArcs.
   */
  public List<List<Arc>> getAdjacentArcs() {
    return adjacentArcs;
  }
  
  /**
   * Getter method for mapNodeId.
   */
  public Map<Integer, Node> getNodeIdPosAdjArc() {
    return mapNodeId;
  }
  
  /**
   * Getter method for nodeIdPosAdjArc.
   */
  public Map<Integer, Integer> getMapNodeId() {
    return nodeIdPosAdjArc;
  }
  
  /**
   * Getter for numberOfArcs.
   */
  public Integer getNumberOfArcs() {
    int numberOfArcs = 0;
    
    for (int i = 0; i < adjacentArcs.size(); i++) {
      List<Arc> arcs = adjacentArcs.get(i);
      numberOfArcs = numberOfArcs + arcs.size();
    }
    
    return numberOfArcs / 2;
  }

  /**
   * Add an ArrayList<Arc> for the arcs of tail node.
   * @param tailNode ID of the node added to the road network
   */
  public void addNodeToGraph(Node tailNode) {
    if (tailNode != null) {
      Integer nodeId = tailNode.getId();
      //Is the node already there?
      boolean alreadyInNetwork = mapNodeId.containsKey(nodeId);
      if (!alreadyInNetwork) {
        allNodeIds.add(nodeId);
        mapNodeId.put(nodeId, tailNode);
      }
    }
  }

  /**
   * Add adjacent arc to for the given node.
   * @param tailNode Node's ID.
   * @param arc New Arc
   */
  public void addAdjacentArc(Node tailNode, Arc arc) {
    if (tailNode != null && arc != null) {
      if (arc.getHeadNode() != null) {
        Integer tailNodeId = tailNode.getId();
        if (!mapNodeId.containsKey(tailNodeId)) {
          allNodeIds.add(tailNodeId);
          mapNodeId.put(tailNodeId, tailNode);
        }
        if (!nodeIdPosAdjArc.containsKey(tailNodeId)) {
          List<Arc> arcs = new ArrayList<Arc>();
          adjacentArcs.add(arcs);
          arcs.add(arc);
          nodeIdPosAdjArc.put(tailNodeId, adjacentArcs.size() - 1);
          nodeIds.add(tailNodeId);
        } else {
          Integer position = nodeIdPosAdjArc.get(tailNodeId);
          boolean alreadyInserted  = 
              arcAlreadyInserted(position, arc.getHeadNode().getId());
          if (!alreadyInserted) {
            adjacentArcs.get(position).add(arc);
          }
        }
      }
    }  
  }

  /**
   * Given a node ID it finds the list containing its adjacent arcs.
   * If the ID cannot be find in the road network. It simply returns 
   * NULL
   * @param nodeId The ID of the node
   * @return The list of adjacent arcs corresponding to the node
   */
  public List<Arc> getNodeAdjacentArcs(Integer nodeId) {
    List<Arc> arcs = null;
    if (nodeIdPosAdjArc.containsKey(nodeId)) {
      arcs = adjacentArcs.get(nodeIdPosAdjArc.get(nodeId));
    }
//    int index = nodeIds.indexOf(nodeId);
//    if (index != -1) {
//      arcs = adjacentArcs.get(index);
//    }
    return arcs;
  }
  
   /**
    * Checks if the Arc is already present to avoid duplicates.
    * @param tailNodePos ID of tail node
    * @param nodeid ID of head node
    */   
  public Boolean arcAlreadyInserted(int tailNodePos, int nodeid) {
    if (adjacentArcs.size() > 0) {
      for (int k = 0; k < adjacentArcs.get(tailNodePos).size(); k++) {
        if (nodeid == adjacentArcs.get(tailNodePos).get(k).headNode.getId()) {
          return true;
        }
      }
    }
    return false;
  }   
   
  /**
   * Read OSM file (in XML format) and construct the corresponding road network.
   */
  public void readFromOsmFile(String pathIn) {
    // List <String> nodesIDs = new ArrayList<String>();
    // String strFile = new String();
    int tmpNodeID = 0;
    Double tmpLatitude = 0.0;
    Double tmpLongitude = 0.0;
    Node currentNode;
    Node prevNode;
    String lineIn = "";
    Node newNode;
    Boolean readingWay = false;
    List<String> tmpWay = new ArrayList<String>();
    String roadType = new String();
    Integer position;
    try {
      // Read data from osm file
      System.out.println("Start: " + Calendar.getInstance().getTime());

      File in = new File(pathIn);
      BufferedReader inBuff = new BufferedReader(new FileReader(in));
      while ((lineIn = inBuff.readLine()) != null) {
        // System.out.println(lineIn);
        if (lineIn.contains("<node")) {
          tmpNodeID = Integer.valueOf(lineIn.substring(
              lineIn.indexOf("id=") + 4, lineIn.indexOf("\" lat")));
          tmpLatitude = Double.valueOf(lineIn.substring(
              lineIn.indexOf("lat=") + 5, lineIn.indexOf("\" lon")));
          tmpLongitude = Double.valueOf(lineIn.substring(
              lineIn.indexOf("lon=") + 5, lineIn.indexOf("\">")));

          newNode = new Node(tmpNodeID, tmpLatitude, tmpLongitude);
          addNodeToGraph(newNode);
          
        } else if (lineIn.contains("<way")) {
          readingWay = true;
          tmpWay = new ArrayList<String>();
        } else if (lineIn.contains("</way")) {
          readingWay = false;
        } else if (lineIn.contains("<tag k=\"highway\"") && readingWay) {
          // last pos of each list contains roadType, we use it to compute
          // travel time (cost)
          // from pos 2 nodes that are in the way
          roadType = String.valueOf(lineIn.substring(lineIn.indexOf("v=") + 3,
              lineIn.indexOf("\" />")));
          // tmpWay.add(roadType);
          prevNode = null;
          if (tmpWay.size() > 1) { // To avoid arcs without head nodes.
            // System.out.println(tmpWay.toString());
            for (int i = 0; i < tmpWay.size(); i++) {
              currentNode = mapNodeId.get(Integer.valueOf(tmpWay.get(i)));
//              position = nodeIdPosAdjArc.get(currentNode.getId());
//              // If node doesn't exist in adjacentArcs, then add it
//              if (position == null) {
//                // addNodeToGraph(currentNode); regresar!!!
//                if (adjacentArcs.size() > 1) {
//                  position = adjacentArcs.size() - 1;
//                } else {
//                  position = 0;
//                }
//                // nodeIdPosAdjArc.put(currentNode.id, position);
//              }
              //First element doesn't have previous node
              if (i != 0) {
                // add arcs to adjacentArcs
                // prev -> current and current -> prev because is an undirected
                // graph
                // Compute cost and create new arcs
                //System.out.println("prevNode: " + prevNode.id 
                  //+ " currentNode: " + currentNode.id);
                //System.out.println("dist: " 
                  //+ getDistance2(prevNode,currentNode));                
                int cost = computeCost(roadType, getDistance2(
                    prevNode, currentNode));

                if (cost != -1) {
                  //System.out.println("timeTravel in min: " + cost);
                  Arc arc1 = new Arc(prevNode, cost);
                  Arc arc2 = new Arc(currentNode, cost);

                  // currentNode -> prevNode
                  addAdjacentArc(currentNode, arc1);
                  // prevNode -> currentNode
                  addAdjacentArc(prevNode, arc2);
                }
              }
              prevNode = currentNode;
            }

          }
        } else if (lineIn.contains("<nd ref=") && readingWay) {
          // current node id in way

          tmpWay.add(String.valueOf(lineIn.substring(
              lineIn.indexOf("<nd ref=") + 9, lineIn.indexOf("\"/>"))));
          // System.out.println(tmpWay.toString());
        }
      }

      System.out.println("TOTAL Num of nodes: " + getAllNodeIds().size());
      System.out.println("Num of nodes with arcs: " + getNodeIds().size());
      // System.out.println(nodes.size());
      System.out.println("Number of arcs: " + getNumberOfArcs());
      // System.out.println("adjacentArcs Size: " + this.adjacentArcs.size());
      System.out.println("Finish: " + Calendar.getInstance().getTime());

      // System.out.println(printArcsFromNode(10467200));
      //checkArcs();

    } catch (Exception e) {
      e.printStackTrace();
    }
  } 
  
  
  
  /**
   * Read OSM file (in XML format) and construct the corresponding road network.
   */
//  public void readFromOsmFile2(String pathIn) {
//    // List <String> nodesIDs = new ArrayList<String>();
//    // String strFile = new String();
//    int tmpNodeID = 0;
//    Double tmpLatitude = 0.0;
//    Double tmpLongitude = 0.0;
//    Node currentNode;
//    Node prevNode;
//    String lineIn = "";
//    Node newNode;
//    Boolean readingWay = false;
//    int numNodes = 0;
//    List<String> tmpWay = new ArrayList<String>();
//    String roadType = new String();
//    Integer position;
//    int countArcs = 0;
//    try {
//      // Read data from osm file
//      System.out.println("Start: " + Calendar.getInstance().getTime());
//
//      File in = new File(pathIn);
//      BufferedReader inBuff = new BufferedReader(new FileReader(in));
//      while ((lineIn = inBuff.readLine()) != null) {
//        // System.out.println(lineIn);
//        if (lineIn.contains("<node")) {
//          numNodes++;
//          tmpNodeID = Integer.valueOf(lineIn.substring(
//              lineIn.indexOf("id=") + 4, lineIn.indexOf("\" lat")));
//          tmpLatitude = Double.valueOf(lineIn.substring(
//              lineIn.indexOf("lat=") + 5, lineIn.indexOf("\" lon")));
//          tmpLongitude = Double.valueOf(lineIn.substring(
//              lineIn.indexOf("lon=") + 5, lineIn.indexOf("\">")));
//
//          newNode = new Node(tmpNodeID, tmpLatitude, tmpLongitude);
//          mapNodeId.put(tmpNodeID, newNode);
//          nodeIds.add(tmpNodeID);
//          adjacentArcs.add(new ArrayList<Arc>()); // probar y luego quitar!
//          nodeIdPosAdjArc.put(newNode.id, adjacentArcs.size() - 1);
//        } else if (lineIn.contains("<way")) {
//          readingWay = true;
//          tmpWay = new ArrayList<String>();
//        } else if (lineIn.contains("</way")) {
//          readingWay = false;
//        } else if (lineIn.contains("<tag k=\"highway\"") && readingWay) {
//          // last pos of each list contains roadType, we use it to compute
//          // travel time (cost)
//          // from pos 2 nodes that are in the way
//          roadType = String.valueOf(lineIn.substring(lineIn.indexOf("v=") + 3,
//              lineIn.indexOf("\" />")));
//          // tmpWay.add(roadType);
//          prevNode = null;
//          if (tmpWay.size() > 1) { // To avoid arcs without head nodes.
//            // System.out.println(tmpWay.toString());
//            for (int i = 0; i < tmpWay.size(); i++) {
//              currentNode = mapNodeId.get(Integer.valueOf(tmpWay.get(i)));
//              position = nodeIdPosAdjArc.get(currentNode.id);
//              // If node doesn't exist in adjacentArcs, then add it
//              if (position == null) {
//                // addNodeToGraph(currentNode); regresar!!!
//                position = adjacentArcs.size() - 1;
//                // nodeIdPosAdjArc.put(currentNode.id, position);
//              }
//              //First element doesn't have previous node
//              if (i != 0) {
//                // add arcs to adjacentArcs
//                // prev -> current and current -> prev because is undirected
//                // graph
//                // Compute cost and create new arcs
//                //System.out.println("prevNode: " + prevNode.id 
//                  //+ " currentNode: " + currentNode.id);
//                //System.out.println("dist: " 
//                  //+ getDistance2(prevNode,currentNode));                
//                int cost = computeCost(roadType, getDistance(
//                    prevNode, currentNode));
//                if (cost > 0) {
//                  //System.out.println("timeTravel in min: " + cost);
//                  Arc arc1 = new Arc(prevNode, cost);
//                  Arc arc2 = new Arc(currentNode, cost);
//
//                  // currentNode -> prevNode
//                  // Check if the arc already exist.
//                  if (!arcAlreadyInserted(position, arc1.headNode.getId())) {
//                    this.adjacentArcs.get(position).add(arc1);
//                    countArcs++;
//                  }
//                  // prevNode -> currentNode
//                  position = nodeIdPosAdjArc.get(prevNode.getId());
//                  if (!arcAlreadyInserted(position, arc2.headNode.getId())) {
//                    this.adjacentArcs.get(position).add(arc2);
//                    countArcs++;
//                  }
//                }
//              }
//              prevNode = currentNode;
//            }
//
//          }
//        } else if (lineIn.contains("<nd ref=") && readingWay) {
//          // current node id in way
//
//          tmpWay.add(String.valueOf(lineIn.substring(
//              lineIn.indexOf("<nd ref=") + 9, lineIn.indexOf("\"/>"))));
//          // System.out.println(tmpWay.toString());
//        }
//      }
//
//      System.out.println("TOTAL Num of nodes: " + getAllNodeIds().size());
//      System.out.println("Num of nodes with arcs: " + getNodeIds().size());
//      // System.out.println(nodes.size());
//      System.out.println("Number of arcs: " + countArcs / 2);
//      // System.out.println("adjacentArcs Size: " + this.adjacentArcs.size());
//      System.out.println("Finish: " + Calendar.getInstance().getTime());
//
//      // System.out.println(printArcsFromNode(10467200));
//      //checkArcs();
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }  
//  

  /**
   * Method to reduce the graph (already read from an OSM file) 
   * to its largest connected component.
   * @return A subgraph of the original road network representing
   * its biggest component
   */
  public RoadNetwork reduceToLargestConnectedComponent() {
    RoadNetwork biggestConnectedComponent = new RoadNetwork();
    List<Integer> bConnectedCompNodes = new ArrayList<Integer>();
    List<List<Arc>> arcsOfConnectedComp = new ArrayList<List<Arc>>();
    List<Integer> remainingNodes = new ArrayList<Integer>();
    for (int i = 0; i < nodeIds.size(); i++) {
      remainingNodes.add(nodeIds.get(i));
    }
    Integer nextNodeId = nodeIds.get(0);
    
    while (remainingNodes.size() > 0) {
      remainingNodes.remove(new Integer(nextNodeId));
      
      List<Integer> connectedNodes = new ArrayList<Integer>();
      //List<List<Arc>> arcsOfComp = new ArrayList<List<Arc>>();

      connectedNodes.add(nextNodeId);
      
      DijkstraAlgorithm dij = new DijkstraAlgorithm(this);
      dij.computeShortestPath(nextNodeId, -1);
      Map<Integer, Integer> costs = dij.getVisitedNodes();
      
      Iterator<Integer> it = costs.keySet().iterator();
      while (it.hasNext()) {

          Integer nodeId = (Integer) it.next();
          System.out.println("?????????????????????????????????????");
          System.out.println(nodeId);
          connectedNodes.add(nodeId);
          remainingNodes.remove(new Integer(nodeId));
      }
      if (connectedNodes.size() > bConnectedCompNodes.size()) {
        bConnectedCompNodes = connectedNodes;
      }
      if (remainingNodes.size() > 0) {
        nextNodeId = remainingNodes.get(0);
      }
    }
    biggestConnectedComponent.setNodes(bConnectedCompNodes);
    biggestConnectedComponent.setAllNodes(bConnectedCompNodes);
        
    for (int i = 0; i < bConnectedCompNodes.size(); i++) {
      Integer nodeId = bConnectedCompNodes.get(i);
      List<Arc> arcs = getNodeAdjacentArcs(nodeId);
      biggestConnectedComponent.getAdjacentArcs().add(arcs);
      if (arcs == null) {
        System.out.println("DU BIST DOOF");
      }
    }
    
    return biggestConnectedComponent;
  }
  
  
  /**
   * Compute cost (travel time). If the type of road is not valid, 
   * the method returns -1 to indicate that we should ignore this road.
   * @param roadType 
   * @param distance Distance in Kilometers.
   * @return Time needed for the given distance  
   */ 
  //TODO time needed unit might be wrong
  public int computeCost(String roadType, double distance) {
    int costMin=0;
    /**
     * Speed in km/h.
     */
    double speed = 1;
    /**
     * Travel time.
     */
    Double cost;
    if (roadType.equals("motorway") || roadType.equals("trunk")) {
      speed = 110;
    } else if (roadType.equals("primary")) {
      speed = 70;
    } else if (roadType.equals("secondary")) {
      speed = 60;
    } else if (roadType.equals("tertiary") 
        || roadType.equals("motorway_link")
        || roadType.equals("trunk_link") 
        || roadType.equals("primary_link")
        || roadType.equals("secondary_link")) {
      speed = 50;
    } else if (roadType.equals("road") || roadType.equals("unclassified")) {
      speed = 40;
    } else if (roadType.equals("residential") 
        || roadType.equals("unsurfaced")) {
      speed = 30;
    } else if (roadType.equals("living_street")) {
      speed = 10;
    } else if (roadType.equals("service")) {
      speed = 5;
    } else {
      return -1;
    }
    cost = distance / speed;
    cost = cost * 60 *60;
    costMin = (int)Math.round(cost);
    return costMin;
  }
  
  
  /**
   * Compute and return distance in Meters from node1 to node2.
   * 
   * @param node1 Node at position one
   * @param node2 Node at position two
   * @return Distance in Meters
   */
//  public double getDistance(Node node1, Node node2) {
//    double lat1 = node1.latitude;
//    double lat2 = node2.latitude;
//    double lon1 = node1.longitude;
//    double lon2 = node1.longitude;
//    double earthRadius = 3958.75;
//    double dLat = Math.toRadians(lat2 - lat1);
//    double dLng = Math.toRadians(lon2 - lon1);
//    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//        * Math.sin(dLng / 2) * Math.sin(dLng / 2);
//    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//    double dist = earthRadius * c;
//
//    int meterConversion = 1609;
//
//    return new Float(dist * meterConversion).floatValue();
//  }
  
  /**
   * Compute and return distance in Meters from node1 to node2.
   * 
   * @param node1 Node at position one
   * @param node2 Node at position two
   * @return Distance in KMs
   */
  public double getDistance2(Node node1, Node node2) { 
    double distance;
    Double tmpdist;
    Integer diffLat, diffLon;
    tmpdist = (node1.latitude * 111229) - (node2.latitude * 111229);
    diffLat = tmpdist.intValue();
    tmpdist = (node1.longitude * 71695) - (node2.longitude * 71695);
    diffLon = tmpdist.intValue();
    
    tmpdist = Math.sqrt(Math.pow(diffLat, 2) + Math.pow(diffLon, 2));
    distance = tmpdist.intValue();
    return distance / 1000; 
  }
  
  /**
   * Get RoadNetwork as a list of lists.
   * @return
   */
  public List<List<String>> asLists() {
    ArrayList<List<String>> res = new ArrayList<List<String>>();
    List<String> list;
    for (int i = 0; i < adjacentArcs.size(); i++) {
      list = new ArrayList<String>();
      list.add(String.valueOf(nodeIds.get(i)));
      for (int j = 0; j < adjacentArcs.get(i).size(); j++) {
        list.add(adjacentArcs.get(i).get(j).asString());
      }
      res.add(list);
    }
    return res;
  }
  
  /**
   * Get RoadNetwork as a String.
   * @return
   */
  public String asString() {
    String outputString = new String();
    for (int i = 0; i < nodeIds.size(); i++) {
      Integer nodeId = nodeIds.get(i);
      outputString = outputString + nodeId + "|";
      List<Arc> arcs = getAdjacentArcs().get(i);
      if (!arcs.isEmpty()) {
        for (int k = 0; k < arcs.size(); k++) {
          outputString = outputString + ((arcs.get(k)).getHeadNode())
            .getId() + "-";
        }
      }
      outputString = outputString + "\n";
    }
    return outputString;
  }
  
  
  /**
   * Generated a random integers in range from 0 to the size of the nodes' list.
   * It returns the node ID saved at that position in the list.
   * @return 
   */
  public int getRandomNodeId() {
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(nodeIds.size());
    return nodeIds.get(randomInt);
  }
  
//  //DEBUGGING PURPOSES  
//  public List<String> printArcsFromNode(int nodeid) {
//    List<String> list = new ArrayList<String>();
//    list.add(String.valueOf(nodeid));
//    int pos;
//    pos = nodeIdPosAdjArc.get(nodeid);
//    for (int i = 0; i < adjacentArcs.get(pos).size(); i++) {
//      list.add(String.valueOf(adjacentArcs.get(pos).get(i).headNode.id));
//    }
//    return list;
//  }
//  
//  public void checkArcs() {
//    List<Integer> listid;
//    int count = 0;
//    for (int i = 0; i < adjacentArcs.size(); i++) {
//      listid = new ArrayList<Integer>();
//      for (int j = 0; j < adjacentArcs.get(i).size(); j++) {
//        if (listid.contains(adjacentArcs.get(i).get(j).headNode.id)) {
//          count++;
//          System.out.println("Error en nodeid: " + nodes.get(i)
//              + " nodeid repeated:" + adjacentArcs.get(i).get(j).headNode.id);
//        } else {
//          listid.add(adjacentArcs.get(i).get(j).headNode.id);
//        }
//      }
//    }
//    System.out.println("Num errores: " + count);
//  }
}
