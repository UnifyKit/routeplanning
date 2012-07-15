package routeplanning;

/**
 * Class representing an active node.
 * @author AAA
 */
public class ActiveNode {
  /**
   *Id of node.
   */
  public long id;
  /**
   * travel time.
   */
  public int dist;
  /**
   * heuristic value for node.
   */
  public int heuristic;
  
  /**
   * Parent.
   */
  public long parent;
  /**
   *Constructor.
  */
  public ActiveNode(long id, int dist, int heuristic, long parent) {
    this.id = id;
    this.dist = dist;
    this.heuristic = heuristic;
    this.parent = parent;
  }
}
