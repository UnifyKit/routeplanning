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
   * List of lists of adjacent Arcs. Each position of this list
   * corresponds to the position of nodes 
   */
  private List<List<Arc>> adjacentArcs;
  
  /**
   * Map nodeId->Node. Contains all nodes
   */
  public Map<Integer, Node> mapNodeId;

  /**
   * Default Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
    mapNodeId = new HashMap<Integer, Node>();
    nodes = new ArrayList<Integer>();
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
   * @param nodeId ID of the node added to the road network
   */
  public void addNodeToGraph(int nodeId) {
    adjacentArcs.add(new ArrayList<Arc>());
    nodes.add(nodeId);
  }

  /**
   * Add adjacent arc to for the given node.
   * @param nodeId Node's ID.
   * @param arc New Arc
   */
  public void addAdjacentArc(Node nodeId, Arc arc) {
    for (int i = 0; i < adjacentArcs.size(); i++) {
      // First list, first element
      if (adjacentArcs.get(i).get(0).getHeadNode().equals(nodeId)) {
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
   * @param tailNodeId ID of tail node
   * @param headNodeId ID of head node
   */
  private Boolean arcAlreadyInserted(int tailNodeId, int headNodeId) {
    List<Arc> nodeAdjArcs = getNodeAdjacentArcs(tailNodeId);
    
    if (nodeAdjArcs != null) {
      for (int i = 0; i < nodeAdjArcs.size(); i++) {
        if (headNodeId == nodeAdjArcs.get(i).getHeadNode().getId()) {
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
          addNodeToGraph(newNode.id);
        } else if (lineIn.contains("<way")) {
          readingWay = true;
          tmpWay = new ArrayList<String>();
        } else if (lineIn.contains("</way")) {
          readingWay = false;
        } else if (lineIn.contains("<tag k=\"highway\"") && readingWay) {
          // roadType, we use it to compute travel time (cost)
          roadType = String.valueOf(lineIn.substring(lineIn.indexOf("v=") + 3,
              lineIn.indexOf("\" />")));
          // tmpWay.add(roadType);
          prevNode = null;
          if (tmpWay.size() > 1) { // To avoid arcs without head nodes.
            // System.out.println(tmpWay.toString());
            for (int i = 0; i < tmpWay.size(); i++) {
              currentNode = mapNodeId.get(Integer.valueOf(tmpWay.get(i)));
           // nodeIdPosAdjArc.get(currentNode.id);
              position = nodes.indexOf(currentNode.id);
              // If node doesn't exist in adjacentArcs, then add it
              if (position == null) {
                // addNodeToGraph(currentNode); regresar!!!
                position = adjacentArcs.size() - 1;
                // nodeIdPosAdjArc.put(currentNode.id, position);
              }
              // first element doesn't have previous node
              if (i != 0) {
                // add arcs to adjacentArcs
                // prev -> current and current -> prev because is an undirected
                // graph
                // Compute cost and create new arcs
                double distance = getDistance(prevNode, currentNode);
                Double cost = computeCost(roadType, distance);
                Arc arc1 = new Arc(prevNode, cost);
                Arc arc2 = new Arc(currentNode, cost);

                // currentNode -> prevNode
                // Check if the arc already exist.
                if (!arcAlreadyInserted(position, arc1.headNode.id)) {
                  this.adjacentArcs.get(position).add(arc1);
                  countArcs++;
                }
                // prevNode -> currentNode
                position = nodes.indexOf(prevNode.id);
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
      checkArcs();

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
//  public RoadNetwork reduceToLargestConnectedComponent() {
//    RoadNetwork biggestConnectedComponent = new RoadNetwork();
//    List<Integer> bConnectedCompNodes = new ArrayList<Integer>();
//    List<List<Arc>> arcsOfConnectedComp = new ArrayList<List<Arc>>();
//    List<Integer> remainingNodes = nodes;
//    Integer nextNodeId = nodes.get(0);
//    
//    while (remainingNodes.size() > 0) {
//      remainingNodes.remove(new Integer(nextNodeId));
//      
//      List<Integer> connectedNodes = new ArrayList<Integer>();
//      List<List<Arc>> arcsOfComp = new ArrayList<List<Arc>>();
//
//      connectedNodes.add(nextNodeId);
//      arcsOfComp.add(getAdjacentArcs(nextNodeId));
//      
//      DijkstraAlgorithm dij = new DijkstraAlgorithm(this);
//      dij.computeShortestPath(nextNodeId, -1);
//      List<Integer> costs = dij.getVisitedNodes();
//      
//      for (int i = 0; i < costs.size(); i++) {
//        Integer costOfCurrentNode = costs.get(i);
//        if (costOfCurrentNode > 0) {
//          connectedNodes.add(nodes.get(i));
//          arcsOfComp.add(adjacentArcs.get(i));
//          remainingNodes.remove(new Integer(nodes.get(i)));
//        }
//      }
//      if (connectedNodes.size() > bConnectedCompNodes.size()) {
//        bConnectedCompNodes = connectedNodes;
//        arcsOfConnectedComp = arcsOfComp;
//      }
//      nextNodeId = remainingNodes.get(0);
//    }
//    biggestConnectedComponent.setNodes(bConnectedCompNodes);
//    biggestConnectedComponent.setAdjacentArcs(arcsOfConnectedComp);
//    
//    return biggestConnectedComponent;
//  }
  
  /**
   * Compute cost (travel time). If the type of road is not valid, 
   * the method returns -1 to indicate that we should ignore this road.
   * @param roadType 
   * @param distance Distance in KMs.
   * @return Time needed to    
   */
  public double computeCost(String roadType, double distance) {
    /**
     * Speed in km/h.
     */
    double speed = 1;
    /**
     * Travel time.
     */
    Double cost;
    if (roadType.equals("motorway") 
        || roadType.equals("trunk")) {
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
    cost = (distance / speed);
    return cost;
  }

  /**
   * Compute and return distance in Kilometers from node1 to node2.
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

    return new Float(dist * meterConversion).floatValue() / 1000;
  }
  
  /**
   * Get RoadNetwork as String.
   * 
   * @return A list of lists of strings
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
  
  //DEBUGGING PURPOSES
  public List<String> printArcsFromNode(int nodeid) {
    List<String> list = new ArrayList<String>();
    list.add(String.valueOf(nodeid));
    int pos;
    pos = nodes.indexOf(nodeid);
    for (int i = 0; i < adjacentArcs.get(pos).size(); i++) {
      list.add(String.valueOf(adjacentArcs.get(pos).get(i).headNode.id));
    }
    return list;
  }

  private void checkArcs() {
    List<Integer> listid;
    int count = 0;
    for (int i = 0; i < adjacentArcs.size(); i++) {
      listid = new ArrayList<Integer>();
      for (int j = 0; j < adjacentArcs.get(i).size(); j++) {
        if (listid.contains(adjacentArcs.get(i).get(j).headNode.id)) {
          count++;
          System.out.println("Error en nodeid: " + nodes.get(i)
              + " nodeid repeated:" + adjacentArcs.get(i).get(j).headNode.id);
        } else {
          listid.add(adjacentArcs.get(i).get(j).headNode.id);
        }
      }
    }
    System.out.println("Num errores: " + count);
  }
  
}
