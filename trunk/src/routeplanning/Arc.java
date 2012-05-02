package routeplanning;

/**
 * Class Arc.
 * @author CJC
 */
public class Arc {
  /**
   * Variable for headNode of Arc.
   */
  Node headNode;
  /**
   * Variable for cost of Arc.
   */
  double cost;
  /**
   * Constructor Arc.  
   * @param headNode
   * @param cost
   */
  public Arc(Node headNode, double cost) {
    this.headNode = headNode;
    this.cost = cost;
  }
  /**
   * Method to output Arc as a String.
   * @return
   */
  public String asString() {
    String res = new String();
    if (this.cost == 0) {
      res = "{" + this.headNode.id + "}";
    } else {
      res = "{" + this.headNode.id + "|" + this.cost + "}";
    }
    return res;
  }
  /**
   * Get headNode of Arc.
   * @return
   */
  public Node getHeadNode() {
    return headNode;
  }
}
