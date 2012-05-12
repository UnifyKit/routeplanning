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
   *TODO.
   */
  public int dist;
  
  /**
   *Constructor.
  */
  public ActiveNode(int id, int dist) {
    this.id = id;
    this.dist = dist;
  }
}
