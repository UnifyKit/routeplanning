package routeplanning;

import java.util.List;
import java.util.ArrayList;

/**
 * Implements a set of criteria costs.
 */
public class CriteriaSet {
  /**
   * Structure that keeps all criteria costs.
   */
  private List<CriteriaCost> cSet = new ArrayList();
  
  /**
   * Getter of cSet.
   */
  public List<CriteriaCost> getCset() {
    return cSet;
  }
  
  /**
   * Adds one element to this set.
   */
  public boolean addCriteriaCost(CriteriaCost c) {
    return cSet.add(c);
  }  
}
