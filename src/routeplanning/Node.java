package routeplanning;

/**
 * Class Node.
 * 
 * @author CJC
 */
public class Node {

  /**
   * id Node.
   */
  Identifier id;

  /**
   * Latitude of the node.
   */
  Float latitude = new Float(0.0f);

  /**
   * Longitude of the node.
   */
  Float longitude = new Float(0.0f);

  /**
   * Constructor 1 for creating an node with OSM id.
   * 
   * @param id
   * @param latitude
   * @param longitude
   */
  public Node(long id, Float latitude, Float longitude) {
    this.id = new Identifier();
    this.id.setOsmId(id);
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Constructor 2 for creating an node with Station id. Station type 0 is
   * departure station Station type 1 is arrival station Station type 2 is
   * transit station
   * 
   * @param stationType
   * @param stationId
   * @param time
   */
  public Node(int stationType, int stationId, int time) {
    this.id = new Identifier();
    this.id.setStationType(stationType);
    this.id.setStationId(stationId);
    this.id.setTime(time);
  }

  /**
   * Get the ID of the node. Note: for transportation networks, the ID is made
   * of station type, station id and time (in seconds).
   * 
   * For example: Arrival Station: 1 Station id: 70 Time: 14615
   * 
   * ID: 17014615
   */
  public long getId() {
    if (id.getIdentifierType() == 0) {
      return id.getOsmId();
    }
    String stId = id.getStationType() + "" + id.getStationId() + ""
        + id.getTime();
    return Long.parseLong(stId);
  }

  /**
   * Get longitude of the node.
   * 
   * @return
   */
  public Float getLongitude() {
    return longitude;
  }

  /**
   * Get latitude of the node.
   * 
   * @return
   */
  public Float getLatitude() {
    return latitude;
  }

  /**
   * Get the time of the node.
   */
  public int getTime() {
    return id.getTime();
  }

  /**
   * Returns 0 if station is arrival, 1 if it's departure, 2 if it's transfer.
   * 
   * @return
   */
  public int stationType() {
    return id.getStationType();
  }

  /**
   * Returns the station ID if the identifier contains this information.
   * Otherwise return -1.
   * 
   * @return
   */
  public int getStationId() {
    if (id.getIdentifierType() == 1) {
      return id.getStationId();
    }
    return -1;
  }

  /**
   * Get node as String.
   * 
   * @return
   */
  public String asString() {
    return "(" + this.id + "|" + this.latitude + "|" + this.longitude + ")";
  }

  /**
   * Get node as String.
   * 
   * @return
   */
  public String toString() {
    return id.toString();
  }

  /**
   * Class which encapsulates a node identifier.
   */
  class Identifier {
    /**
     * OSM id.
     */
    private long osmId;

    /**
     * Station id.
     */
    private int stationId;

    /**
     * Time.
     */
    private int time;

    /**
     * Type of node.
     */
    private int nodeType;

    /**
     * Type of station.
     */
    private int stationType;

    /**
     * Getter of OSM id.
     */
    public long getOsmId() {
      return osmId;
    }

    /**
     * Setter of OSM id.
     */
    public void setOsmId(long osmId) {
      stationId = -1;
      nodeType = 0;
      this.osmId = osmId;
    }

    /**
     * Getter of Station id.
     */
    public int getStationId() {
      return stationId;
    }

    /**
     * Setter of Station id.
     */
    public void setStationId(int stationId) {
      osmId = -1;
      nodeType = 1;
      this.stationId = stationId;
    }

    /**
     * Returns 0 if type of identifier is OSM or returns 1 if it is a station
     * ID.
     */
    public int getIdentifierType() {
      return nodeType;
    }

    /**
     * Getter of Station type Returns 0 if station is arrival, 1 if it's
     * departure, 2 if it's transfer.
     */
    public int getStationType() {
      return stationType;
    }

    /**
     * Setter of station type.
     */
    public void setStationType(int stationType) {
      this.stationType = stationType;
    }

    /**
     * Getter of time.
     */
    public int getTime() {
      return time;
    }

    /**
     * Setter of time.
     */
    public void setTime(int time) {
      this.time = time;
    }

    /**
     * Returns a string representation of the identifier.
     * 
     * @return
     */
    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (osmId != -1) {
        sb.append(osmId);
      } else if (stationId != -1) {
        int secondsCount = time;
        int seconds = secondsCount % 60;
        secondsCount -= seconds;
        // Calculate the minutes:
        int minutesCount = secondsCount / 60;
        long minutes = minutesCount % 60;
        minutesCount -= minutes;
        // Calculate the hours:
        int hoursCount = minutesCount / 60;

        sb.append(stationId);
        sb.append("@");
        sb.append(hoursCount + ":" + minutes + ":" + seconds);
        sb.append("/");
        if (stationType == 0) {
          sb.append("ARR");
        } else if (stationType == 1) {
          sb.append("DEP");
        } else if (stationType == 2) {
          sb.append("TRA");
        }
      }
      return sb.toString();
    }
  }
}
