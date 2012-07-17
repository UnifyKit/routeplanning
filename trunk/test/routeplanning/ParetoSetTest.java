package routeplanning;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing class PertoSet.
 * As required in Exercise 10.2.
 */
public class ParetoSetTest {
  /**
   * Tests construction of Pareto set and other methods to
   * check if everything works as established.
   */
  @Test
  public void methodsTest() {
    System.out.println("---------------ParetoSet : methodsTest"
        + "---------------");
    CriteriaSet cSet = new CriteriaSet();
    CriteriaCost c1 = new CriteriaCost(3, 0);
    CriteriaCost c2 = new CriteriaCost(3, 5);
    CriteriaCost c3 = new CriteriaCost(2, 1);
    CriteriaCost c4 = new CriteriaCost(4, 0);
    cSet.addCriteriaCost(c1);
    cSet.addCriteriaCost(c2);
    cSet.addCriteriaCost(c3);
    cSet.addCriteriaCost(c4);
    
    List<CriteriaCost> expectedList = new ArrayList();
    expectedList.add(c1);
    expectedList.add(c3);
    
    ParetoSet pSet = new ParetoSet(cSet);
    System.out.println(pSet.getPset());
    System.out.println(pSet.numberOfComparisons());
    
    Assert.assertEquals(expectedList, pSet.getPset());
    
    Assert.assertTrue(pSet.numberOfComparisons() == 10);
    
    System.out.println("---------------ParetoSet : methodsTest"
        + "---------------");
  }
  
  /**
   * Tests case 1 of 3
   * Based on the example of the exercise sheet.
   */
  @Test
  public void testCase1() {
    System.out.println("---------------ParetoSet : testCase1"
        + "---------------");
    //First we create a criteria set in arbitrary order
    CriteriaSet cSet = new CriteriaSet();
    CriteriaCost c1 = new CriteriaCost(4, 3);
    CriteriaCost c2 = new CriteriaCost(2, 4);
    CriteriaCost c3 = new CriteriaCost(1, 5);
    CriteriaCost c4 = new CriteriaCost(6, 5);
    CriteriaCost c5 = new CriteriaCost(7, 1);
    CriteriaCost c6 = new CriteriaCost(3, 5);
    CriteriaCost c7 = new CriteriaCost(5, 4);
    cSet.addCriteriaCost(c1);
    cSet.addCriteriaCost(c2);
    cSet.addCriteriaCost(c3);
    cSet.addCriteriaCost(c4);
    cSet.addCriteriaCost(c5);
    cSet.addCriteriaCost(c6);
    cSet.addCriteriaCost(c7);
    
    //The pareto set is built from this set where non optimal
    //criteria costs are removed
    ParetoSet pSet1 = new ParetoSet(cSet);
    ParetoSet pSet2 = new ParetoSet(cSet);
    
    List<CriteriaCost> expectedList = new ArrayList<>();
    expectedList.add(c3);
    expectedList.add(c2);
    expectedList.add(c5);
    expectedList.add(c1);
    
    List<CriteriaCost> copyOfPSet = new ArrayList<>();
    copyOfPSet.addAll(pSet1.getPset());
    
    for (int i = 0; i < expectedList.size(); i++) {
      copyOfPSet.remove(expectedList.get(i));
    }
    //We assert that the pareto set contains the same
    //criteria costs as expectedList
    Assert.assertTrue(copyOfPSet.isEmpty());

    System.out.println("PARETO SET OF TEST CASE 1");
    System.out.println(pSet1.getPset());
    
    //Now we add a new criteria cost to this set
    //without ordering and check the number of steps it need to
    //add it / reject
    CriteriaCost otherC = new CriteriaCost(0, 5);
    
    pSet1.addCriteriaCostWithoutOrdering(otherC);
    int numberOfStepsWihoutOrdering = pSet1.numberOfComparisons();
    
    System.out.println("PARETO SET OF TEST CASE 1 after adding (0, 5)" 
      + " without ordering");
    System.out.println(pSet1.getPset());
    
    pSet2.addCriteriaCostWithOrdering(otherC);
    int numberOfStepsWithOrdering = pSet2.numberOfComparisons();
    
    System.out.println("PARETO SET OF TEST CASE 1 after adding (0, 5)"
        + " with ordering");
    System.out.println(pSet2.getPset());
    
    System.out.println("numberOfStepsWihoutOrdering = " 
      + numberOfStepsWihoutOrdering);
    System.out.println("numberOfStepsWithOrdering = "
      + numberOfStepsWithOrdering);
    
    Assert.assertTrue(numberOfStepsWithOrdering < numberOfStepsWihoutOrdering);
        
    System.out.println("---------------ParetoSet : testCase1"
        + "---------------");
  }
  
  /**
   * Tests case 2 of 3
   * Similar to the example before, but the new criteria cost
   * won't be added because it doesn't improve any value in
   * the set.
   * So one can see the difference with ordering and without.
   */
  @Test
  public void testCase2() {
    System.out.println("---------------ParetoSet : testCase2"
        + "---------------");
    //First we create a criteria set in arbitrary order
    CriteriaSet cSet = new CriteriaSet();
    CriteriaCost c1 = new CriteriaCost(4, 3);
    CriteriaCost c2 = new CriteriaCost(2, 4);
    CriteriaCost c3 = new CriteriaCost(1, 5);
    CriteriaCost c4 = new CriteriaCost(7, 2);
    CriteriaCost c5 = new CriteriaCost(7, 1);
    CriteriaCost c6 = new CriteriaCost(0, 6);
    CriteriaCost c7 = new CriteriaCost(6, 2);
    cSet.addCriteriaCost(c1);
    cSet.addCriteriaCost(c2);
    cSet.addCriteriaCost(c3);
    cSet.addCriteriaCost(c4);
    cSet.addCriteriaCost(c5);
    cSet.addCriteriaCost(c6);
    cSet.addCriteriaCost(c7);
    
    //The pareto set is built from this set where non optimal
    //criteria costs are removed
    ParetoSet pSet1 = new ParetoSet(cSet);
    ParetoSet pSet2 = new ParetoSet(cSet);
    
    //System.out.println(pSet1.getPset());

    List<CriteriaCost> expectedList = new ArrayList<>();
    expectedList.add(c3);
    expectedList.add(c2);
    expectedList.add(c5);
    expectedList.add(c1);
    expectedList.add(c6);
    expectedList.add(c7);
    
    List<CriteriaCost> copyOfPSet = new ArrayList<>();
    copyOfPSet.addAll(pSet1.getPset());
    
    for (int i = 0; i < expectedList.size(); i++) {
      copyOfPSet.remove(expectedList.get(i));
    }
    //We assert that the pareto set contains the same
    //criteria costs as expectedList
    Assert.assertTrue(copyOfPSet.isEmpty());

    System.out.println("PARETO SET OF TEST CASE 2");
    System.out.println(pSet1.getPset());
    
    //Now we add a new criteria cost to this set
    //without ordering and check the number of steps it need to
    //add it / reject
    CriteriaCost otherC = new CriteriaCost(7, 2);
    
    pSet1.addCriteriaCostWithoutOrdering(otherC);
    int numberOfStepsWihoutOrdering = pSet1.numberOfComparisons();
    
    System.out.println("PARETO SET OF TEST CASE 2 after adding (7, 2)" 
      + " without ordering");
    System.out.println(pSet1.getPset());
    
    pSet2.addCriteriaCostWithOrdering(otherC);
    int numberOfStepsWithOrdering = pSet2.numberOfComparisons();
    
    System.out.println("PARETO SET OF TEST CASE 2 after adding (7, 2)"
        + " with ordering");
    System.out.println(pSet2.getPset());
    
    System.out.println("numberOfStepsWihoutOrdering = " 
      + numberOfStepsWihoutOrdering);
    System.out.println("numberOfStepsWithOrdering = "
      + numberOfStepsWithOrdering);
    
    Assert.assertTrue(numberOfStepsWithOrdering < numberOfStepsWihoutOrdering);
        
    System.out.println("---------------ParetoSet : testCase2"
        + "---------------");
  }
  
  /**
   * Tests case 3 of 3
   * Another test case.
   */
  @Test
  public void testCase3() {
    System.out.println("---------------ParetoSet : testCase3"
        + "---------------");
    //First we create a criteria set in arbitrary order
    //Here one can notice that they are separated by a cost
    //of 2.
    CriteriaSet cSet = new CriteriaSet();
    CriteriaCost c1 = new CriteriaCost(0, 12);
    CriteriaCost c2 = new CriteriaCost(4, 8);
    CriteriaCost c3 = new CriteriaCost(8, 4);
    CriteriaCost c4 = new CriteriaCost(12, 0);
    CriteriaCost c5 = new CriteriaCost(10, 2);
    CriteriaCost c6 = new CriteriaCost(6, 6);
    CriteriaCost c7 = new CriteriaCost(2, 10);
    cSet.addCriteriaCost(c1);
    cSet.addCriteriaCost(c2);
    cSet.addCriteriaCost(c3);
    cSet.addCriteriaCost(c4);
    cSet.addCriteriaCost(c5);
    cSet.addCriteriaCost(c6);
    cSet.addCriteriaCost(c7);
    
    //The pareto set is built from this set where non optimal
    //criteria costs are removed
    ParetoSet pSet1 = new ParetoSet(cSet);
    ParetoSet pSet2 = new ParetoSet(cSet);
    
    //System.out.println(pSet1.getPset());

    List<CriteriaCost> expectedList = new ArrayList<>();
    expectedList.add(c1);
    expectedList.add(c2);
    expectedList.add(c3);
    expectedList.add(c4);
    expectedList.add(c5);
    expectedList.add(c6);
    expectedList.add(c7);
    
    
    List<CriteriaCost> copyOfPSet = new ArrayList<>();
    copyOfPSet.addAll(pSet1.getPset());
    
    for (int i = 0; i < expectedList.size(); i++) {
      copyOfPSet.remove(expectedList.get(i));
    }
    //We assert that the pareto set contains the same
    //criteria costs as expectedList
    Assert.assertTrue(copyOfPSet.isEmpty());

    System.out.println("PARETO SET OF TEST CASE 3");
    System.out.println(pSet1.getPset());
    
    //Now we add a new criteria cost to this set
    //without ordering and check the number of steps it need to
    //add it / reject
    CriteriaCost otherC2 = new CriteriaCost(5, 3);
    
    pSet1.addCriteriaCostWithoutOrdering(otherC2);
    int numberOfStepsWihoutOrdering = pSet1.numberOfComparisons();
    
    System.out.println("PARETO SET OF TEST CASE 3 after adding (5, 7)" 
      + " without ordering");
    System.out.println(pSet1.getPset());
    
    pSet2.addCriteriaCostWithOrdering(otherC2);
    int numberOfStepsWithOrdering = pSet2.numberOfComparisons();
    
    System.out.println("PARETO SET OF TEST CASE 1 after adding (7, 2)"
        + " with ordering");
    System.out.println(pSet2.getPset());
    
    System.out.println("numberOfStepsWihoutOrdering = " 
      + numberOfStepsWihoutOrdering);
    System.out.println("numberOfStepsWithOrdering = "
      + numberOfStepsWithOrdering);
    
    Assert.assertTrue(numberOfStepsWithOrdering < numberOfStepsWihoutOrdering);
        
    System.out.println("---------------ParetoSet : testCase3"
        + "---------------");
  }
}
