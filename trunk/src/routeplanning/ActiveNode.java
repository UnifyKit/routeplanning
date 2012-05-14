package routeplanning;

/**
 * Class representing an active node.
 * @author AAA
 */
public class ActiveNode {
  /**
   *Id of node.
   */
  public int id;
  /**
   * travel time.
   */
  public int dist;
  /**
   * heuristic value for node.
   */
  public int heuristic;
  /**
   *Constructor.
  */
  public ActiveNode(int id, int dist, int heuristic) {
    this.id = id;
    this.dist = dist;
    this.heuristic = heuristic;
  }
}
