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
  // private Integer numberOfArcs = 0;

  /**
   * Default Constructor.
   */
  public RoadNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
    mapNodeId = new HashMap<Integer, Node>();
    nodeIds = new ArrayList<Integer>();
    nodeIdPosAdjArc = new HashMap<Integer, Integer>();
  }

  /**
   * Setter method for nodes.
   */
  // TODO
  public void setNodes(List<Integer> nodeIds) {
    this.nodeIds = nodeIds;
  }

  /**
   * Setter method for adjacentArcs.
   */
  // TODO
  public void setAdjacentArcs(List<List<Arc>> adjacentArcs) {
    this.adjacentArcs = adjacentArcs;
  }

  /**
   * Setter method for mapNodeId.
   */
  public void setMapNodeId(Map<Integer, Node> mapNodeId) {
    this.mapNodeId = mapNodeId;
  }

  /**
   * Setter method for nodeIdPosAdjArc.
   */
  public void setNodeIdPosAdjArc(Map<Integer, Integer> nodeIdPosAdjArc) {
    this.nodeIdPosAdjArc = nodeIdPosAdjArc;
  }

  /**
   * Getter method for nodes.
   */
  public List<Integer> getNodeIds() {
    return nodeIds;
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
  public Map<Integer, Node> getMapNodeId() {
    return mapNodeId;
  }

  /**
   * Getter method for nodeIdPosAdjArc.
   */
  public Map<Integer, Integer> getNodeIdPosAdjArc() {
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
   * 
   * @param tailNode
   *          ID of the node added to the road network
   */
  public void addNodeToGraph(Node tailNode) {
    if (tailNode != null) {
      Integer nodeId = tailNode.getId();
      // Is the node already there?
      boolean alreadyInNetwork = mapNodeId.containsKey(nodeId);
      if (!alreadyInNetwork) {
        mapNodeId.put(nodeId, tailNode);
      }
    }
  }

  /**
   * Add adjacent arc to for the given node.
   * 
   * @param tailNode
   *          Node's ID.
   * @param arc
   *          New Arc
   */
  public void addAdjacentArc(Node tailNode, Arc arc) {
    if (tailNode != null && arc != null) {
      if (arc.getHeadNode() != null) {
        Integer tailNodeId = tailNode.getId();
        if (!mapNodeId.containsKey(tailNodeId)) {
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
          boolean alreadyInserted = arcAlreadyInserted(position, arc
              .getHeadNode().getId());
          if (!alreadyInserted) {
            adjacentArcs.get(position).add(arc);
          }
        }
      }
    }
  }

  /**
   * Given a node ID it finds the list containing its adjacent arcs. If the ID
   * cannot be find in the road network. It simply returns NULL
   * 
   * @param nodeId
   *          The ID of the node
   * @return The list of adjacent arcs corresponding to the node
   */
  public List<Arc> getNodeAdjacentArcs(Integer nodeId) {
    List<Arc> arcs = null;
    if (nodeIdPosAdjArc.containsKey(nodeId)) {
      arcs = adjacentArcs.get(nodeIdPosAdjArc.get(nodeId));
    }
    return arcs;
  }

  /**
   * Checks if the Arc is already present to avoid duplicates.
   * 
   * @param tailNodePos
   *          ID of tail node
   * @param nodeid
   *          ID of head node
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
              // position = nodeIdPosAdjArc.get(currentNode.getId());
              // // If node doesn't exist in adjacentArcs, then add it
              // if (position == null) {
              // // addNodeToGraph(currentNode); regresar!!!
              // if (adjacentArcs.size() > 1) {
              // position = adjacentArcs.size() - 1;
              // } else {
              // position = 0;
              // }
              // // nodeIdPosAdjArc.put(currentNode.id, position);
              // }
              // First element doesn't have previous node
              if (i != 0) {
                // add arcs to adjacentArcs
                // prev -> current and current -> prev because is an undirected
                // graph
                // Compute cost and create new arcs
                // System.out.println("prevNode: " + prevNode.id
                // + " currentNode: " + currentNode.id);
                // System.out.println("dist: "
                // + getDistance2(prevNode,currentNode));
                int cost = computeCost(roadType,
                    getDistance2(prevNode, currentNode));

                if (cost != -1) {
                  // System.out.println("timeTravel in min: " + cost);
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

      System.out.println("Num of nodes with arcs: " + getNodeIds().size());
      // System.out.println(nodes.size());
      System.out.println("Number of arcs: " + getNumberOfArcs());
      // System.out.println("adjacentArcs Size: " + this.adjacentArcs.size());
      System.out.println("Finish: " + Calendar.getInstance().getTime());

      // System.out.println(printArcsFromNode(10467200));
      // checkArcs();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to reduce the graph (already read from an OSM file) to its largest
   * connected component.
   * 
   * @return A subgraph of the original road network representing its biggest
   *         component
   */
  public RoadNetwork reduceToLargestConnectedComponent() {
    RoadNetwork biggestConnectedComponent = new RoadNetwork();
    List<Integer> bConnectedCompNodes = new ArrayList<Integer>();
    List<Integer> remainingNodes = new ArrayList<Integer>();
    for (int i = 0; i < nodeIds.size(); i++) {
      remainingNodes.add(nodeIds.get(i));
    }
    Integer nextNodeId = nodeIds.get(0);

    while (remainingNodes.size() > 0) {
      remainingNodes.remove(new Integer(nextNodeId));

      List<Integer> connectedNodes = new ArrayList<Integer>();
      // List<List<Arc>> arcsOfComp = new ArrayList<List<Arc>>();

      DijkstraAlgorithm dij = new DijkstraAlgorithm(this);
      dij.computeShortestPath(nextNodeId, -1);
      Map<Integer, Integer> costs = dij.getVisitedNodes();

      Iterator<Integer> it = costs.keySet().iterator();
      while (it.hasNext()) {
        Integer nodeId = (Integer) it.next();
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

    List<List<Arc>> listOfArcs = new ArrayList<List<Arc>>();
    Map<Integer, Node> lccMapIdNode = new HashMap<Integer, Node>();
    Map<Integer, Integer> lccMapIdList = new HashMap<Integer, Integer>();

    for (int i = 0; i < bConnectedCompNodes.size(); i++) {
      Integer nodeId = bConnectedCompNodes.get(i);
      lccMapIdNode.put(nodeId, (Node) mapNodeId.get(nodeId));
      List<Arc> arcs = getNodeAdjacentArcs(nodeId);
      listOfArcs.add(arcs);
      lccMapIdList.put(nodeId, listOfArcs.size() - 1);
    }
    biggestConnectedComponent.setNodes(bConnectedCompNodes);
    biggestConnectedComponent.setAdjacentArcs(listOfArcs);
    biggestConnectedComponent.setMapNodeId(lccMapIdNode);
    biggestConnectedComponent.setNodeIdPosAdjArc(lccMapIdList);

    return biggestConnectedComponent;
  }

  /**
   * Compute cost (travel time). If the type of road is not valid, the method
   * returns -1 to indicate that we should ignore this road.
   * 
   * @param roadType
   * @param distance
   *          Distance in Kilometers.
   * @return Time needed for the given distance
   */
  public int computeCost(String roadType, double distance) {
    int costMin = 0;
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
    } else if (roadType.equals("tertiary") || roadType.equals("motorway_link")
        || roadType.equals("trunk_link") || roadType.equals("primary_link")
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
    cost = cost * 60 * 60;
    costMin = (int) Math.round(cost);
    return costMin;
  }

  /**
   * Compute and return distance in Kilometers from node1 to node2.
   * 
   * @param node1
   *          Node at position one
   * @param node2
   *          Node at position two
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
   * 
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
   * 
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
          outputString = outputString + ((arcs.get(k)).getHeadNode()).getId()
              + "-";
        }
      }
      outputString = outputString + "\n";
    }
    return outputString;
  }
  /**
   * Get RoadNetwork as a String with arcFlags.
   * 
   * @return
   */
  public String asStringWithArcFlags() {
    String outputString = new String();
    for (int i = 0; i < nodeIds.size(); i++) {
      Integer nodeId = nodeIds.get(i);
      outputString = outputString + nodeId + "|";
      List<Arc> arcs = getAdjacentArcs().get(i);
      if (!arcs.isEmpty()) {
        for (int k = 0; k < arcs.size(); k++) {
          if (arcs.get(k).arcFlag) {
            outputString = outputString + ((arcs.get(k)).getHeadNode()).getId()
                + "(" + arcs.get(k).cost + ")" + "-";
          }
        }
      }
      outputString = outputString + "\n";
    }
    return outputString;
  }
  /**
   * Generated a random integers in range from 0 to the size of the nodes' list.
   * It returns the node ID saved at that position in the list.
   * 
   * @return
   */
  public int getRandomNodeId() {
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(nodeIds.size());
    return nodeIds.get(randomInt);
  }

  /**
   * It returns a random node ID within a region.
   * 
   * @return
   */
  public int getRandomNodeIdWithinRegion(double latMin, double latMax,
      double longMin, double longMax) {
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(nodeIds.size());
    Integer nodeId = nodeIds.get(randomInt);
    boolean inRegion = false;
    while (!inRegion) {
      Node currentNode = mapNodeId.get(nodeId);
      if (currentNode.getLatitude() > latMin
          && currentNode.getLatitude() < latMax
          && currentNode.getLongitude() > longMin
          && currentNode.getLongitude() < longMax) {
        inRegion = true;
      } else {
        randomInt = randomGenerator.nextInt(nodeIds.size());
        nodeId = nodeIds.get(randomInt);
      }
    }
    return nodeId;
  }

  /**
   * Compute heuristic using Euclidean Distance / max speed Values in minutes.
   * 
   * @param targetNodeId
   * @return
   */
  public List<Integer> computeStraightLineHeuristic(int targetNodeId) {
    List<Integer> heuristic = new ArrayList<Integer>();
    Node targetNode = mapNodeId.get(targetNodeId);
    /**
     * Euclidean distance in meters.
     */
    double dist;
    /**
     * Max speed according to roadType = motorway 110Km/hr
     */
    int maxSpeed = 110;
    Double travelTime;
    for (int i = 0; i < nodeIds.size(); i++) {
      dist = getDistance2(mapNodeId.get(nodeIds.get(i)), targetNode);
      travelTime = dist / maxSpeed; // hours
      travelTime = travelTime * 3600;
      // if (nodeIds.get(i) == 385925420) {
      // System.out.println("distance: " + dist);
      // System.out.println("travelTime: " + travelTime);
      // }
      heuristic.add((int) Math.round(travelTime));
    }
    return heuristic;
  }

  /**
   * Returns the id of the closest node from the given coordinates.
   */
  public List<Integer> getNodeIdsFromCoordinates(double sourceLat,
      double sourceLng, double targetLat, double targetLng) {
    List<Integer> nodeIdsSourceAndTarget = new ArrayList<Integer>();
    int sourceNodeId = 0;
    int targetNodeId = 0;
    boolean sourceFound = false;
    boolean targetFound = false;
    Node currentNode;

    Node sourceNode = new Node(0, sourceLat, sourceLng);
    Node targetNode = new Node(1, targetLat, targetLng);

    double bestDistanceToSourceNode = 50000.0;
    double bestDistanceToTargetNode = 50000.0;

    Node bestSourceNodeCandidate = null;
    Node bestTargetNodeCandidate = null;

    for (int i = 0; i < this.getNodeIds().size(); i++) {
      currentNode = mapNodeId.get(this.getNodeIds().get(i));
      double distanceToSourceNode = 
        getDistance2(sourceNode, currentNode) * 1000;
      double distanceToTargetNode = 
        getDistance2(targetNode, currentNode) * 1000;

      if (currentNode.latitude == sourceLat
          && currentNode.longitude == sourceLng) {
        sourceNodeId = currentNode.id;
        sourceFound = true;
      } else if (bestDistanceToSourceNode > distanceToSourceNode) {
        bestDistanceToSourceNode = distanceToSourceNode;
        bestSourceNodeCandidate = currentNode;
      }

      if (currentNode.latitude == targetLat
          && currentNode.longitude == targetLng) {
        targetNodeId = currentNode.id;
        targetFound = true;
      } else if (bestDistanceToTargetNode > distanceToTargetNode) {
        bestDistanceToTargetNode = distanceToTargetNode;
        bestTargetNodeCandidate = currentNode;
      }
    }

    if (sourceFound) {
      nodeIdsSourceAndTarget.add(sourceNodeId);
    } else {
      nodeIdsSourceAndTarget.add(bestSourceNodeCandidate.getId());
    }

    if (targetFound) {
      nodeIdsSourceAndTarget.add(targetNodeId);
    } else {
      nodeIdsSourceAndTarget.add(bestTargetNodeCandidate.getId());
    }

    System.out.println("sourceLat: " + sourceLat + " sourceLng: " + sourceLng
        + " targetLat: " + targetLat + " targetLng: " + targetLng);
    Node returnedSourceNode = mapNodeId.get(nodeIdsSourceAndTarget.get(0));
    Node returnedTargetNode = mapNodeId.get(nodeIdsSourceAndTarget.get(1));

    // System.out.println("RETURNED NODE:");
    // System.out.println(
    // "sourceLat: " + returnedSourceNode.getLatitude()
    // + " sourceLng: " + returnedSourceNode.getLongitude()
    // + " targetLat: " + returnedTargetNode.getLatitude()
    // + " targetLng: " + returnedTargetNode.getLongitude());

    return nodeIdsSourceAndTarget;
  }
  // //DEBUGGING PURPOSES
  // public List<String> printArcsFromNode(int nodeid) {
  // List<String> list = new ArrayList<String>();
  // list.add(String.valueOf(nodeid));
  // int pos;
  // pos = nodeIdPosAdjArc.get(nodeid);
  // for (int i = 0; i < adjacentArcs.get(pos).size(); i++) {
  // list.add(String.valueOf(adjacentArcs.get(pos).get(i).headNode.id));
  // }
  // return list;
  // }
  //
  // public void checkArcs() {
  // List<Integer> listid;
  // int count = 0;
  // for (int i = 0; i < adjacentArcs.size(); i++) {
  // listid = new ArrayList<Integer>();
  // for (int j = 0; j < adjacentArcs.get(i).size(); j++) {
  // if (listid.contains(adjacentArcs.get(i).get(j).headNode.id)) {
  // count++;
  // System.out.println("Error en nodeid: " + nodes.get(i)
  // + " nodeid repeated:" + adjacentArcs.get(i).get(j).headNode.id);
  // } else {
  // listid.add(adjacentArcs.get(i).get(j).headNode.id);
  // }
  // }
  // }
  // System.out.println("Num errores: " + count);
  // }
}
