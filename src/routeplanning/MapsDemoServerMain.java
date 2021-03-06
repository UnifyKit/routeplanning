package routeplanning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample Server class.
 * */
public class MapsDemoServerMain {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    int port = 8888;
    ServerSocket server = new ServerSocket(port);
    BufferedReader in = null;
    PrintWriter out = null;

    RoadNetwork rn = new RoadNetwork();
    rn.readFromOsmFile("D:/workspace/routeplanning/"
        + "src/routeplanning/resources/saarland_reduced.osm");
    rn = rn.reduceToLargestConnectedComponent();

    LandmarkAlgorithm lm = new LandmarkAlgorithm(rn);
    lm.selectLandmarks(16);

    int i = 0;

    while (true) {
      System.out.println("\u001b[1m\u001b[34m[" + (++i) + "] "
          + "Waiting for query on port " + port + " ...\u001b[0m");
      Socket client = server.accept();
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));

      // Get the request string.
      String request = in.readLine();
      System.out
          .println("Request string is \""
              + (request.length() < 99 ? request : request.substring(0, 97)
                  + "...") + "\"");

      // Extract coordinates from GET request string.
      int pos = request.indexOf("&");
      if (pos != -1) {
        request = request.substring(6, pos);
      }
      System.out.println(request);
      String[] parts = request.split(",");
      float sourceLat = Float.parseFloat(parts[0]);
      float sourceLng = Float.parseFloat(parts[1]);
      float targetLat = Float.parseFloat(parts[2]);
      float targetLng = Float.parseFloat(parts[3]);

      // nodeIds = rn.getNodeIdsFromCoordinates(49.3139855, 6.8137583,
      // 49.3135735, 6.8105003);
      // 385925420,962989650,962989607
      // id="385925420" lat="49.3139855" lon="6.8137583">
      // id="962989650" lat="49.3136879" lon="6.8140206
      // id="962989607" lat="49.3132449" lon="6.8143430
//      List<Integer> nodeIds = new ArrayList<Integer>();
//      nodeIds = rn.getNodeIdsFromCoordinates(truncCoordinate(sourceLat, 7),
//          truncCoordinate(sourceLng, 7), truncCoordinate(targetLat, 7),
//          truncCoordinate(targetLng, 7));
      
      List<Long> nodeIds = new ArrayList<Long>();
      nodeIds = rn.getNodeIdsFromCoordinates(sourceLat, sourceLng, targetLat,
          targetLng);

      System.out.println("A* Landmark SP:"
          + lm.computeShortestPath(nodeIds.get(0), nodeIds.get(1)));
      List<Float> shortestPath = new ArrayList<Float>();
      shortestPath.add(targetLat);
      shortestPath.add(targetLng);
      shortestPath.addAll(lm.getPathForGoogle(nodeIds.get(0), nodeIds.get(1)));
      shortestPath.add(sourceLat);
      shortestPath.add(sourceLng);
      String jsonp = "redrawLineServerCallback({\n" + "  path: [";

      // shortestPath.add(targetLat);
      // shortestPath.add(targetLng);
      for (int m = 0; m < shortestPath.size(); m++) {
        if (m == shortestPath.size()) {
          jsonp = jsonp + shortestPath.get(m);
        } else {
          jsonp = jsonp + shortestPath.get(m) + ",";
        }

      }
      jsonp = jsonp + "]\n" + "})\n";
      // Send JSONP results string back to client.
      // String jsonp = "redrawLineServerCallback({\n" +
      // "  path: [" + sourceLat + "," + sourceLng + "," +
      // + targetLat + "," + targetLng + "]\n" + "})\n";

      String answer = "HTTP/1.0 200 OK\r\n" + "Content-Length: "
          + jsonp.length() + "\r\n" + "Content-Type: application/javascript"
          + "\r\n" + "Connection: close\r\n" + "\r\n" + jsonp;

      out = new PrintWriter(client.getOutputStream(), true);
      out.println(answer);
      out.close();
      in.close();
      client.close();
    }
  }
  /**
   * Truncate decimals of a coordinate.
   * @param coordinate
   * @param decimalPlace
   * @return
   */
  public static double truncCoordinate(double coordinate, int decimalPlace) {
    String coord = (new Double(coordinate).toString()).substring(0,
        decimalPlace + 3);
    return Double.parseDouble(coord);
  }
}
