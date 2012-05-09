package routeplanning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
  public List<Integer> nodes;
  /**
   * List of adjacent Arcs. First element of a each list is the head node.
   */
  private List<List<Arc>> adjacentArcs;

  /**
   * Map nodeId->Node. Contains all nodes
   */
  public Map<Integer, Node> mapNodeId;
  /**
   * Map nodeid -> position of node as tail node in adjacentArcs used to avoid
   * search by id of the node in adjacentArcs, when adding a new arc.
   */
  Map<Integer, Integer> nodeIdPosAdjArc;


  /**
   * Default Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
    mapNodeId = new HashMap<Integer, Node>();
    nodes = new ArrayList<Integer>();
    nodeIdPosAdjArc = new HashMap<Integer, Integer>();
  }
  
  /**
   *Setter method for nodes.
   */
  public void setNodes(List<Integer> nodes) {
    this.nodes = nodes;
  }

  /**
   * Getter method for nodes.
   */  
  public List<Integer> getNodes() {
    return nodes;
  }
  
  /**
   * Setter method for adjacentArcs.
   */
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
   * Add an ArrayList<Arc> for the arcs of tail node.
   * @param tailNode ID of the node added to the road network
   */
  public void addNodeToGraph(Node tailNode) {
    // Arc arc0 = new Arc(tailNode, 0);
    adjacentArcs.add(new ArrayList<Arc>());
    // adjacentArcs.get(adjacentArcs.size() - 1).add(arc0);
    nodeIdPosAdjArc.put(tailNode.id, adjacentArcs.size() - 1);
  }

  /**
   * Add adjacent arc to for the given node.
   * @param tailNode Node's ID.
   * @param arc New Arc
   */
  public void addAdjacentArc(Node tailNode, Arc arc) {
    for (int i = 0; i < adjacentArcs.size(); i++) {
      // First list, first element
      if (adjacentArcs.get(i).get(0).getHeadNode().equals(tailNode)) {
        adjacentArcs.get(i).add(arc);
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
    int index = nodes.indexOf(nodeId);
    if (index != -1) {
      arcs = adjacentArcs.get(index);
    }
    return arcs;
  }
  
   /**
    * Checks if the Arc is already present to avoid duplicates.
    * @param tailNodePos ID of tail node
    * @param nodeid ID of head node
    */   
  public Boolean arcAlreadyInserted(int tailNodePos, int nodeid) {
    for (int k = 0; k < adjacentArcs.get(tailNodePos).size(); k++) {
      if (nodeid == adjacentArcs.get(tailNodePos).get(k).headNode.id) {
        return true;
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
    int numNodes = 0;
    List<String> tmpWay = new ArrayList<String>();
    String roadType = new String();
    Integer position;
    int countArcs = 0;
    try {
      // Read data from osm file
      System.out.println("Start: " + Calendar.getInstance().getTime());

      File in = new File(pathIn);
      BufferedReader inBuff = new BufferedReader(new FileReader(in));
      while ((lineIn = inBuff.readLine()) != null) {
        // System.out.println(lineIn);
        if (lineIn.contains("<node")) {
          numNodes++;
          tmpNodeID = Integer.valueOf(lineIn.substring(
              lineIn.indexOf("id=") + 4, lineIn.indexOf("\" lat")));
          tmpLatitude = Double.valueOf(lineIn.substring(
              lineIn.indexOf("lat=") + 5, lineIn.indexOf("\" lon")));
          tmpLongitude = Double.valueOf(lineIn.substring(
              lineIn.indexOf("lon=") + 5, lineIn.indexOf("\">")));

          newNode = new Node(tmpNodeID, tmpLatitude, tmpLongitude);
          mapNodeId.put(tmpNodeID, newNode);
          nodes.add(tmpNodeID);
          adjacentArcs.add(new ArrayList<Arc>()); // probar y luego quitar!
          nodeIdPosAdjArc.put(newNode.id, adjacentArcs.size() - 1); // probar y
                                                                    // quitar!
                                                                    // tailnode
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
              position = nodeIdPosAdjArc.get(currentNode.id);
              // If node doesn't exist in adjacentArcs, then add it
              if (position == null) {
                // addNodeToGraph(currentNode); regresar!!!
                position = adjacentArcs.size() - 1;
                // nodeIdPosAdjArc.put(currentNode.id, position);
              }
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
                Double cost = computeCost(roadType, getDistance2(
                    prevNode, currentNode));
                //System.out.println("timeTravel in min: " + cost);
                Arc arc1 = new Arc(prevNode, cost);
                Arc arc2 = new Arc(currentNode, cost);

                // currentNode -> prevNode
                // Check if the arc already exist.
                if (!arcAlreadyInserted(position, arc1.headNode.id)) {
                  this.adjacentArcs.get(position).add(arc1);
                  countArcs++;
                }
                // prevNode -> currentNode
                position = nodeIdPosAdjArc.get(prevNode.id);
                if (!arcAlreadyInserted(position, arc2.headNode.id)) {
                  this.adjacentArcs.get(position).add(arc2);
                  countArcs++;
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

      System.out.println("Num nodes: " + numNodes);
      // System.out.println(nodes.size());
      System.out.println("Number of arcs: " + countArcs / 2);
      // System.out.println("adjacentArcs Size: " + this.adjacentArcs.size());
      System.out.println("Finish: " + Calendar.getInstance().getTime());

      // System.out.println(printArcsFromNode(10467200));
      //checkArcs();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }  

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
    List<Integer> remainingNodes = nodes;
    Integer nextNodeId = nodes.get(0);
    
    while (remainingNodes.size() > 0) {
      remainingNodes.remove(new Integer(nextNodeId));
      
      List<Integer> connectedNodes = new ArrayList<Integer>();
      List<List<Arc>> arcsOfComp = new ArrayList<List<Arc>>();

      connectedNodes.add(nextNodeId);
      arcsOfComp.add(getNodeAdjacentArcs(nextNodeId));
      
      DijkstraAlgorithm dij = new DijkstraAlgorithm(this);
      dij.computeShortestPath(nextNodeId, -1);
      List<Double> costs = dij.getVisitedNodes();
      
      for (int i = 0; i < costs.size(); i++) {
        Double costOfCurrentNode = costs.get(i);
        if (costOfCurrentNode > 0) {
          connectedNodes.add(nodes.get(i));
          arcsOfComp.add(adjacentArcs.get(i));
          remainingNodes.remove(new Integer(nodes.get(i)));
        }
      }
      if (connectedNodes.size() > bConnectedCompNodes.size()) {
        bConnectedCompNodes = connectedNodes;
        arcsOfConnectedComp = arcsOfComp;
      }
      nextNodeId = remainingNodes.get(0);
    }
    biggestConnectedComponent.setNodes(bConnectedCompNodes);
    biggestConnectedComponent.setAdjacentArcs(arcsOfConnectedComp);
    
    return biggestConnectedComponent;
  }
  
  
  /**
   * Compute cost (travel time). If the type of road is not valid, 
   * the method returns -1 to indicate that we should ignore this road.
   * @param roadType 
   * @param distance Distance in Meters.
   * @return Time needed for the given distance  
   */ 
  //TODO time needed unit might be wrong
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
    cost = (distance / 1000) / (speed * 60);
    return cost;
  }
  
  
  /**
   * Compute and return distance in Meters from node1 to node2.
   * 
   * @param node1 Node at position one
   * @param node2 Node at position two
   * @return Distance in KMs
   */
  public double getDistance(Node node1, Node node2) {
    double lat1 = node1.latitude;
    double lat2 = node2.latitude;
    double lon1 = node1.longitude;
    double lon2 = node1.longitude;
    double earthRadius = 3958.75;
    double dLat = Math.toRadians(lat2 - lat1);
    double dLng = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double dist = earthRadius * c;

    int meterConversion = 1609;

    return new Float(dist * meterConversion).floatValue();
  }
  
  /**
   * Compute and return distance in Meters from node1 to node2.
   * 
   * @param node1 Node at position one
   * @param node2 Node at position two
   * @return Distance in KMs
   */
  //TODO Check distance
  public int getDistance2(Node node1, Node node2) { 
    int distance;
    Double tmpdist;
    Integer diffLat, diffLon;
    tmpdist = (node1.latitude * 111229) - (node2.latitude * 111229);
    diffLat = tmpdist.intValue();
    tmpdist = (node1.longitude * 71695) - (node2.longitude * 71695);
    diffLon = tmpdist.intValue();
    
    tmpdist = Math.sqrt(Math.pow(diffLat, 2) + Math.pow(diffLon, 2));
    distance = tmpdist.intValue();
    return distance; 
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
  
  
  /**
   * Generated a random integers in range from 0 to the size of the nodes' list.
   * It returns the node ID saved at that position in the list.
   * @return 
   */
  public int getRandomNodeId() {
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(nodes.size());
    return nodes.get(randomInt);
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
