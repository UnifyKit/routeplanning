package routeplanning;

/**
 * Class Arc.
 * @author CJC
 */
public class Arc {
  
  /**
   * Variable for headNode of Arc.
   */
  public Node headNode;
  
  /**
   * Variable for cost of Arc.
   */
  public int cost;
  
  public boolean arcFlag;

  /**
   * Constructor Arc.  
   * @param headNode
   * @param cost
   */
  public Arc(Node headNode, int cost) {
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
   * @return head node
   */
  public Node getHeadNode() {
    return headNode;
  }
}
