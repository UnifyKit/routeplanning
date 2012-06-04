package routeplanning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
       
    int i = 0;
    while (true) {
      System.out.println("\u001b[1m\u001b[34m[" + (++i) + "] "
          + "Waiting for query on port " + port + " ...\u001b[0m");
      Socket client = server.accept();
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     
      // Get the request string.
      String request = in.readLine();
      System.out.println("Request string is \""
          + (request.length() < 99 ? request : request.substring(0, 97) + "...")
          + "\"");
           
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

      // Send JSONP results string back to client.
      String jsonp = "redrawLineServerCallback({\n" +
          "  path: [" + sourceLat + "," + sourceLng + "," +
                      + targetLat + "," + targetLng + "]\n" + "})\n";
      String answer = "HTTP/1.0 200 OK\r\n"
        + "Content-Length: " + jsonp.length() + "\r\n"
        + "Content-Type: application/javascript" + "\r\n"
        + "Connection: close\r\n"
        + "\r\n" + jsonp;
           
      out = new PrintWriter(client.getOutputStream(), true);
      out.println(answer);
      out.close();
      in.close();
      client.close();
    }
  }
}