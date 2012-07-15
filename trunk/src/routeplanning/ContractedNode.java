package routeplanning;

/**
 * Class representing an contracted node.
 * @author AAA
 */
public class ContractedNode {
  /**
   *Id of node.
   */
  public long id;
  
  /**
   * edge difference.
   */
  public int edgeDiff;
  
  /**
   *Constructor.
  */
  public ContractedNode(long id, int edgeDiff) {
    this.id = id;
    this.edgeDiff = edgeDiff;
  }
  
  /**
   *For debugging purposes.
  */
  public String toString() {
    return new String("ID: " + id + " ED: " + edgeDiff);
  }
}
