package routeplanning;

/**
 * Class Node.
 * @author CJC
 */
public class Node {
  /**
   * id Node.
   */
  int id;
  /**
   * Latitude of the node.
   */
  Double latitude;
  /**
   * Longitude of the node.
   */
  Double longitude;
  /**
   * Constructor.
   * @param id
   * @param latitude
   * @param longitude
   */
  public Node(int id, Double latitude, Double longitude) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
  }
 
  /**
  * Get node as String.
  * @return
  */
  public String asString() {
    return "(" + this.id + "|" + this.latitude + "|" + this.longitude + ")";
  }
}
