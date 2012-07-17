package routeplanning;

import java.util.HashMap;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test of class TransportationNetwork.
 */
public class TransportationNetworkTest {

  /**
   * Tests that the different HashSet has the correct ids
   * after invoking readFromGtfsFiles() method.
   * It tests also the map station Id with numbers
   */
  @Test
  public void testHashSets() {
    System.out.println("--------------testHashSets--------------");
    TransportationNetwork testNetwork = new TransportationNetwork();
    testNetwork.readFromGtfsFiles("E:/Documents/UNI/SS12/Efficient Route"
        + " Planning/groupRepository/src/routeplanning/resources", true);
    
    HashSet actualServiceHSet = testNetwork.getServiceHSet();
    System.out.println(actualServiceHSet);
    HashSet<String> expectedServiceHashSet = new HashSet();
    expectedServiceHashSet.add("20120701CC");
    Assert.assertEquals(expectedServiceHashSet, actualServiceHSet);
        
    HashSet actualTripHSet = testNetwork.getTripHSet();
    System.out.println(actualTripHSet);
    System.out.println(actualTripHSet.size());
    HashSet<String> expectedTripHashSet = new HashSet();
    expectedTripHashSet.add("trip1");
    expectedTripHashSet.add("trip2");
    expectedTripHashSet.add("trip3");
    Assert.assertEquals(expectedTripHashSet, actualTripHSet);
    Assert.assertTrue(actualTripHSet.size() == 3);
    
    HashMap stationIdMap = testNetwork.getStationIdMap();
    System.out.println(stationIdMap);
    System.out.println(stationIdMap.size());
    //I test only the first and the last
    Assert.assertTrue(stationIdMap.get("404827").equals(new Integer(1)));
    Assert.assertTrue(stationIdMap.get("401977").equals(new Integer(1831)));
    Assert.assertTrue(stationIdMap.size() == 1831);
    
    //System.out.println(testNetwork.asString());
    
    System.out.println("--------------testHashSets--------------");
  }
  
  /**
   * Tests conversion of time.
   */
  @Test
  public void testConvertTimeToSeconds() {
    String stringTime = "26:16:40";
    TransportationNetwork testNetwork = new TransportationNetwork();
    Integer expected = 94600;
    Integer actualTime = testNetwork.convertTimeToSeconds(stringTime);
    
    Assert.assertEquals(expected, actualTime);
  }
  
}
