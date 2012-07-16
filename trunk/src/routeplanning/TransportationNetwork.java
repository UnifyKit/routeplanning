package routeplanning;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Class for transit network.
 */
public class TransportationNetwork implements Network {

  /**
   * Transfer buffer set to 5 minutes (300 seconds).
   */
  private int transferBuffer = 5 * 60;
  /**
   * List of all Nodes that have adjacent arcs.
   */
  private List<Long> nodeIds;
  /**
   * List of adjacent Arcs. First element of a each list is the head node.
   */
  private List<List<Arc>> adjacentArcs;

  /**
   * Map nodeId->Node. Contains all nodes
   */
  private Map<Long, Node> mapNodeId;
  /**
   * Map nodeid -> position of node as tail node in adjacentArcs used to avoid
   * search by id of the node in adjacentArcs, when adding a new arc.
   */
  private Map<Long, Integer> nodeIdPosAdjArc;

  /**
   * For each station id, all nodes from that station.
   */
  private HashMap<Integer, List<Node>> nodesPerStation = new HashMap();

  /**
   * HashSet which memorizes the service ids which contain the given weekday
   * (Wednesday).
   */
  private HashSet<String> serviceHSet = new HashSet();

  /**
   * HashSet which memorizes the trip ids which contain a service id from
   * serviceHSet.
   */
  private HashSet<String> tripHSet = new HashSet();

  /**
   * HashMap maps the String name of a station with its long id.
   */
  private HashMap<String, Integer> stationIdMap = new HashMap();

  /**
   * Default Constructor.
   */
  public TransportationNetwork() {
    adjacentArcs = new ArrayList<List<Arc>>();
    mapNodeId = new HashMap<Long, Node>();
    nodeIds = new ArrayList<Long>();
    nodeIdPosAdjArc = new HashMap<Long, Integer>();
  }

  /**
   * Setter method for nodes.
   */
  // TODO
  public void setNodes(List<Long> nodeIds) {
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
  public void setMapNodeId(Map<Long, Node> mapNodeId) {
    this.mapNodeId = mapNodeId;
  }

  /**
   * Setter method for nodeIdPosAdjArc.
   */
  public void setNodeIdPosAdjArc(Map<Long, Integer> nodeIdPosAdjArc) {
    this.nodeIdPosAdjArc = nodeIdPosAdjArc;
  }

  /**
   * Getter method for nodes.
   */
  public List<Long> getNodeIds() {
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
  public Map<Long, Node> getMapNodeId() {
    return mapNodeId;
  }

  /**
   * Getter method for nodeIdPosAdjArc.
   */
  public Map<Long, Integer> getNodeIdPosAdjArc() {
    return nodeIdPosAdjArc;
  }

  /**
   * Getter of serviceHSet.
   */
  public HashSet<String> getServiceHSet() {
    return serviceHSet;
  }

  /**
   * Getter of tripHSet.
   */
  public HashSet<String> getTripHSet() {
    return tripHSet;
  }

  /**
   * Getter of nodesPerStation.
   */
  public HashMap<Integer, List<Node>> getNodesPerStation() {
    return nodesPerStation;
  }

  /**
   * Getter of stationIdMap.
   */
  public HashMap<String, Integer> getStationIdMap() {
    return stationIdMap;
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

    return numberOfArcs;
  }

  /**
   * Add an ArrayList<Arc> for the arcs of tail node.
   * 
   * @param tailNode
   *          ID of the node added to the road network
   */
  public void addNodeToGraph(Node tailNode) {
    if (tailNode != null) {
      Long nodeId = tailNode.getId();
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
        Long tailNodeId = tailNode.getId();
        if (!mapNodeId.containsKey(tailNodeId)) {
          mapNodeId.put(tailNodeId, tailNode);
        }
        if (!nodeIdPosAdjArc.containsKey(tailNodeId)) {
          List<Arc> arcs = new ArrayList<Arc>();
          adjacentArcs.add(arcs);
          arcs.add(arc);
          nodeIdPosAdjArc.put(tailNodeId, adjacentArcs.size() - 1);
          nodeIds.add(tailNodeId);
          addNodeToStationMap(tailNode.getStationId(), tailNode);
        } else {
          Integer position = nodeIdPosAdjArc.get(tailNodeId);
          boolean alreadyInserted = arcAlreadyInserted(position, arc
              .getHeadNode().getId());
          if (!alreadyInserted) {
            adjacentArcs.get(position).add(arc);
          }
        }
        if (!nodeIdPosAdjArc.containsKey((arc.getHeadNode()).getId())) {
          List<Arc> arcs = new ArrayList<Arc>();
          adjacentArcs.add(arcs);
          nodeIdPosAdjArc.put((arc.getHeadNode()).getId(),
              adjacentArcs.size() - 1);
          nodeIds.add((arc.getHeadNode()).getId());
          addNodeToStationMap((arc.getHeadNode()).getStationId(),
              arc.getHeadNode());
        }
      }
    }
  }

  /**
   * Adds a node to nodesPerStation.
   */
  public void addNodeToStationMap(Integer stationId, Node node) {
    if (nodesPerStation.containsKey(stationId)) {
      List<Node> nodeList = nodesPerStation.get(stationId);
      nodeList.add(node);
    } else {
      List<Node> nodeList = new ArrayList();
      nodeList.add(node);
      nodesPerStation.put(stationId, nodeList);
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
  public List<Arc> getNodeAdjacentArcs(Long nodeId) {
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
  public Boolean arcAlreadyInserted(int tailNodePos, long nodeid) {
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
   * Read graph from GTFS files in the given directory.
   */
  public void readFromGtfsFiles(String directoryName, boolean debug) {

    try {
      // Phase 1: it parses calendar.txt (in a hash set) to save
      // those service ids which contain the given weekday
      CsvParser calendarParser = new CsvParser(directoryName + "/calendar.txt");
      if (debug) {
        calendarParser = new CsvParser(directoryName + "/calendar_debug.txt");
      }
      while (calendarParser.readNextLine()) {
        // In this case we want WEDNESDAY as a day which corresponds to column 3
        String wednesday = calendarParser.getItem(3);
        if (wednesday.equals("1")) {
          // We memorize the service id
          serviceHSet.add(calendarParser.getItem(0));
        }
      }

      // Phase 2: it parses trips.txt (in a hash set) to save
      // those trip ids which contain a service id saved in serviceHSet
      CsvParser tripParser = new CsvParser(directoryName + "/trips.txt");
      if (debug) {
        tripParser = new CsvParser(directoryName + "/trips_debug.txt");
      }
      while (tripParser.readNextLine()) {
        // The service id corresponds to column 1
        String serviceId = tripParser.getItem(1);
        // We want to memorize trip id which corresponds to column 0
        if (serviceHSet.contains(serviceId)) {
          // We memorize the trip id
          tripHSet.add(tripParser.getItem(2));
        }
      }

      // Phase 3: it parses stops.txt (in a hash set) to create
      // a mapping from the GTFS stop id strings to consecutive numerical
      // stop ids
      CsvParser stopsParser = new CsvParser(directoryName + "/stops.txt");
      if (debug) {
        stopsParser = new CsvParser(directoryName + "/stops_debug.txt");
      }
      // We skip the first line
      stopsParser.readNextLine();
      int consecutiveId = 1;
      while (stopsParser.readNextLine()) {
        // The station unique id (string) corresponds to column 0
        String stopId = stopsParser.getItem(0);
        stationIdMap.put(stopId, consecutiveId);
        consecutiveId++;
      }

      // Phase 4: it parses stop_times.txt, the actual information
      // for each trip we create the nodes and the arcs between nodes
      // Assumes that the file is ordered by trip_id
      CsvParser stopTimesParser = new CsvParser(directoryName
          + "/stop_times.txt");
      if (debug) {
        stopTimesParser 
          = new CsvParser(directoryName + "/stop_times_debug.txt");
      }

      // Will save the current trip code
      String tripId = null;
      // Will save the trip code of the last iteration
      String previousTripId = null;
      // Saves the departure node of the last iteration
      Node outgoingNode = null;
      // Is true when a new trip begins
      boolean newTrip = true;
      // We skip the first line
      stopTimesParser.readNextLine();

      boolean firstIteration = true;

      while (stopTimesParser.readNextLine()) {
        if (newTrip) {
          tripId = stopTimesParser.getItem(0);
          previousTripId = tripId;
        } else {
          previousTripId = tripId;
          tripId = stopTimesParser.getItem(0);
        }

        // We get the information we need
        String realStationId = stopTimesParser.getItem(3);
        Integer stationId = stationIdMap.get(realStationId);
        int depTime = convertTimeToSeconds(stopTimesParser.getItem(2));
        int arrivalTime = convertTimeToSeconds(stopTimesParser.getItem(1));

        // we are in the same trip
        if (previousTripId.equals(tripId)) {
          newTrip = false;

          if (tripHSet.contains(tripId)) {
            // System.out.println("Working on TRIP: " + tripId);
            Node arrNode = new Node(1, stationId, arrivalTime);

            // The case outgoingNode == null happens only when
            // the file meets its first line
            if (!firstIteration) {
              // 0 if station is arrival, 1 if it's departure, 2 if it's
              // transfer.
              Node incomingNode = outgoingNode;
              int cost = arrivalTime - incomingNode.getTime();
              // With this arc we connect one departure with one arrival
              // (on two different lines)
              Arc newArc = new Arc(arrNode, cost);
              addNodeToGraph(arrNode);
              // addNodeToStationMap(stationId, arrNode);
              addAdjacentArc(incomingNode, newArc);
              // System.out.println(incomingNode + "-(" + cost + ")->" +
              // arrNode);
            }

            Node depNode = new Node(2, stationId, depTime);
            // This arc represents the waiting time at one station
            int waitingCost = depNode.getTime() - arrNode.getTime();
            Arc waitingArc = new Arc(depNode, waitingCost);
            addNodeToGraph(depNode);
            // addNodeToStationMap(stationId, depNode);
            addAdjacentArc(arrNode, waitingArc);
            // System.out.println(arrNode + "-(0)->" + depNode);
            // important for the next iteration
            outgoingNode = depNode;

            // Now we add the transfer node and the related arc
            Node transferNode 
              = new Node(3, stationId, depTime + transferBuffer);
            int transferCost = transferNode.getTime() - arrNode.getTime();
            Arc transferArc = new Arc(transferNode, transferCost);
            addNodeToGraph(transferNode);
            // addNodeToStationMap(stationId, transferNode);
            addAdjacentArc(arrNode, transferArc);
            // System.out.println(arrNode + "-(" + transferCost + ")->"
            // + transferNode);

          }
        } else {
          newTrip = true;
          // In the first iteration we only save the departure node for the
          // next iterations.
          Node depNode = new Node(2, stationId, depTime);
          addNodeToGraph(depNode);
          // addNodeToStationMap(stationId, depNode);
          Node arrNode = new Node(1, stationId, arrivalTime);
          addNodeToGraph(arrNode);

          int waitingCost = depNode.getTime() - arrNode.getTime();
          Arc waitingArc = new Arc(depNode, waitingCost);
          addAdjacentArc(arrNode, waitingArc);
          
          Node transferNode 
            = new Node(3, stationId, depTime + transferBuffer);
          int transferCost = transferNode.getTime() - arrNode.getTime();
          Arc transferArc = new Arc(transferNode, transferCost);
          addNodeToGraph(transferNode);
          // addNodeToStationMap(stationId, transferNode);
          addAdjacentArc(arrNode, transferArc);
          // System.out.println(arrNode + "-(" + transferCost + ")->"
          // + transferNode);

          outgoingNode = depNode;
        }
        firstIteration = false;
      }

      // Phase 5: for each station we order the nodes by time.
      // For equal times, sort the transfer nodes before the departure nodes
      // with ties between nodes of the same kind broken arbitrarily

      // First I have to loop on each station
      for (Integer stationKey : nodesPerStation.keySet()) {
        List<Node> stationNodes = nodesPerStation.get(stationKey);
        // The following comparator sorts the list with the given
        // criteria.
        Collections.sort(stationNodes, new NodeComparator());
        //System.out.println(stationNodes);

        Node lastTransferNode = null;

        // Now I have to connect the nodes as required
        for (Node stationNode : stationNodes) {
          // If the node is a transfer node
          if (stationNode.stationType() == 3) {
            // I have to save the last transfer node since
            // I have to connect this transfer node with
            // the previous one.
            if (lastTransferNode != null) {
              int cost = stationNode.getTime() - lastTransferNode.getTime();
              Arc transToTransArc = new Arc(stationNode, cost);
              addAdjacentArc(lastTransferNode, transToTransArc);
            }
            lastTransferNode = stationNode;
          } else if (stationNode.stationType() == 2) {
            // I found a departure node. Now I have to connect
            // the last transfer node found with it
            if (lastTransferNode != null) {
              int cost = stationNode.getTime() - lastTransferNode.getTime();
              Arc transToDepArc = new Arc(stationNode, cost);
              addAdjacentArc(lastTransferNode, transToDepArc);
            }
          }
        }
      }
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
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
  public TransportationNetwork reduceToLargestConnectedComponent() {
    TransportationNetwork biggestConnectedComponent 
      = new TransportationNetwork();
    List<Long> bConnectedCompNodes = new ArrayList<Long>();
    List<Long> remainingNodes = new ArrayList<Long>();
    for (int i = 0; i < nodeIds.size(); i++) {
      remainingNodes.add(nodeIds.get(i));
    }
    Long nextNodeId = nodeIds.get(0);

    while (remainingNodes.size() > 0) {
      remainingNodes.remove(new Long(nextNodeId));
      // System.out.println(remainingNodes);
      List<Long> connectedNodes = new ArrayList<Long>();
      // List<List<Arc>> arcsOfComp = new ArrayList<List<Arc>>();

      DijkstraAlgorithm dij = new DijkstraAlgorithm(this);
      dij.computeShortestPath(nextNodeId, -1L);
      Map<Long, Integer> costs = dij.getVisitedNodes();

      Iterator<Long> it = costs.keySet().iterator();
      while (it.hasNext()) {
        Long nodeId = (Long) it.next();
        connectedNodes.add(nodeId);
        remainingNodes.remove(new Long(nodeId));
      }
      if (connectedNodes.size() > bConnectedCompNodes.size()) {
        bConnectedCompNodes = connectedNodes;
      }
      if (remainingNodes.size() > 0) {
        nextNodeId = remainingNodes.get(0);
      }
    }

    List<List<Arc>> listOfArcs = new ArrayList<List<Arc>>();
    Map<Long, Node> lccMapIdNode = new HashMap<Long, Node>();
    Map<Long, Integer> lccMapIdList = new HashMap<Long, Integer>();

    for (int i = 0; i < bConnectedCompNodes.size(); i++) {
      Long nodeId = bConnectedCompNodes.get(i);
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
      Long nodeId = nodeIds.get(i);
      outputString = outputString + mapNodeId.get(nodeId).toString() + "  |  ";
      List<Arc> arcs = getAdjacentArcs().get(i);
      if (!arcs.isEmpty()) {
        for (int k = 0; k < arcs.size(); k++) {
          outputString = outputString
              + mapNodeId.get(((arcs.get(k)).getHeadNode()).getId()).toString()
              + "  -  ";
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
  public long getRandomNodeId() {
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(nodeIds.size());
    return nodeIds.get(randomInt);
  }

  /**
   * Converts the time as it is represented in the file stop_times.txt
   * (04:50:00) into a representation based on seconds.
   */
  public int convertTimeToSeconds(String time) {
    int newTime = 0;
    int iterationNumber = 0;
    StringTokenizer st = new StringTokenizer(time, ":");

    while (st.hasMoreElements()) {
      if (iterationNumber == 0) {
        newTime = newTime + (Integer.parseInt(st.nextElement().toString()))
            * 3600;
      } else if (iterationNumber == 1) {
        newTime = newTime + (Integer.parseInt(st.nextElement().toString()))
            * 60;
      } else if (iterationNumber == 2) {
        newTime = newTime + Integer.parseInt(st.nextElement().toString());
      }
      iterationNumber++;
    }
    return newTime;
  }

  /**
   * Comparator for nodes to sort them by time.
   */
  class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {

      int timeNode1 = node1.getTime();
      int timeNode2 = node2.getTime();

      int stationType1 = node1.stationType();
      int stationType2 = node2.stationType();

      if (timeNode1 > timeNode2) {
        return +1;
      } else if (timeNode1 < timeNode2) {
        return -1;
      } else {
        if (stationType1 == 3 && stationType2 == 2) {
          return -1;
        } else if (stationType2 == 3 && stationType1 == 2) {
          return +1;
        }
        return 0;
      }
    }
  }
}
