package routeplanning;

/**
 * Criteria cost with two criteria.
 */
public class CriteriaCost {
  /**
   * First criteria.
   */
  int cost1 = 0;
  
  /**
   * Second criteria.
   */
  int cost2 = 0;
 
  /**
   * Constructor.
   */
  public CriteriaCost(int cost1, int cost2) {
    this.cost1 = cost1;
    this.cost2 = cost2;
  }

  /**
   * Getter of cost 1.
   */
  public int getCost1() {
    return cost1;
  }

  /**
   * Getter of cost 2.
   */
  public int getCost2() {
    return cost2;
  }
  
  /**
   * <= comparator.
   */  
  public static boolean isLessEqualThan(CriteriaCost c1, CriteriaCost c2) {
    return (c1.getCost1() <= c2.getCost1()) 
        && (c1.getCost2() <= c2.getCost2());
  }
  
  /**
   * == comparator.
   */  
  public static boolean isEqual(CriteriaCost c1, CriteriaCost c2) {
    return (c1.getCost1() == c2.getCost1()) 
        && (c1.getCost2() == c2.getCost2());
  }
  
  /**
   * < comparator.
   */  
  public static boolean isStrictlyLessThan(CriteriaCost c1, CriteriaCost c2) {
    return (c1.getCost1() < c2.getCost1()) 
        && (c1.getCost2() < c2.getCost2());
  }
  
  /**
   * incomparable comparator.
   */  
  public boolean areIncomparable(CriteriaCost c1, CriteriaCost c2) {
    return (!CriteriaCost.isLessEqualThan(c1, c2)) 
        && (!CriteriaCost.isLessEqualThan(c2, c1));
  } 
  
  /**
   * For debugging purposes.
   */  
  public String toString() {
    return "(" + cost1 + ", " + cost2 + ")"; 
  }
}
