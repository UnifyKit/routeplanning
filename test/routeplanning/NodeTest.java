package routeplanning;

import static org.junit.Assert.assertNotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing class of class Node.
 * @author AAA
 */
public class NodeTest {
  /**
   * Tests constructor and string representation of object.
   */
  @Test
  public void testNode() {
    Node testNode = new Node(1, 1.0, 1.0);
    assertNotNull(testNode);
    
    String representation = new String("(1|1.0|1.0)");
    Assert.assertEquals(representation, testNode.asString());
    
    testNode = null;
    Assert.assertNull(testNode);
  }

  /**
   * Tests getId Method.
   */
  @Test
  public void testGetId() {
    Node testNode1 = new Node(1, 1.0, 1.0);
    Integer nodeId = new Integer(1);
    Assert.assertEquals(nodeId, testNode1.getId());
  }
  
  /**
   * Tests getLongitude Method.
   */
  @Test
  public void testGetLongitude() {
    Node testNode = new Node(1, 1.0, 1.0);
    Double longitude = new Double(1.0);
    Assert.assertEquals(longitude, testNode.getLongitude());
  }
  
  /**
   * Tests getLatitude Method.
   */
  @Test
  public void testGetLatitude() {
    Node testNode = new Node(1, 1.0, 2.0);
    Double latitude = new Double(2.0);
    Assert.assertEquals(latitude, testNode.getLongitude());
  }
}
