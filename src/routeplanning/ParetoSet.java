package routeplanning;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Implements a Pareto set.
 */
public class ParetoSet {
  /**
   * Structure that keeps all criteria costs which are part of the set of
   * optimal solutions.
   */
  private List<CriteriaCost> optimalSet = new ArrayList();

  /**
   * The number of iterations when the last Pareto set was calculated.
   */
  private int numberOfComparisons = 0;

  /**
   * Getter of cSet.
   */
  public List<CriteriaCost> getPset() {
    return optimalSet;
  }

  /**
   * Getter of numberOfComparisons. The number of iterations when the last
   * Pareto set was calculated.
   */
  public int numberOfComparisons() {
    return numberOfComparisons;
  }

  /**
   * Constructor. Gets a criteria set as argument and calculates the set of
   * optimal solutions. After calculating this set, it applies the ordering
   * described in exercise 1.
   */
  public ParetoSet(CriteriaSet cSet) {
    // Calculating the optimal solutions
    List<CriteriaCost> cList = cSet.getCset();
    calculateParetoSetWithoutOrdering(cList);
  }

  /**
   * Calculates the Pareto set given a list of criteria cost
   * without ordering.
   */
  public void calculateParetoSetWithoutOrdering(List<CriteriaCost> cList) {
    numberOfComparisons = 0;
    optimalSet = new ArrayList();
    List<CriteriaCost> toBeRemoved = new ArrayList();

    // Quadratic behaviour
    for (int index = 0; index < cList.size(); index++) {
      CriteriaCost currentCriteria = cList.get(index);
      for (int subindex = 0; subindex < cList.size(); subindex++) {
        CriteriaCost criteria = cList.get(subindex);
        // System.out.println("COMPARING: " + criteria + " WITH "
        // + currentCriteria);
        if (CriteriaCost.isEqual(criteria, currentCriteria)) {
          numberOfComparisons++;
          continue;
        }
        numberOfComparisons++;
        if (CriteriaCost.isLessEqualThan(criteria, currentCriteria)) {

          toBeRemoved.add(currentCriteria);
          // System.out.println("REMOVED: " + currentCriteria);
          break;
        }
      }
    }
    // at the end of the process we add only the optimal CriteriaCost
    // objects to our Pareto set
    for (int index = 0; index < cList.size(); index++) {
      if (!toBeRemoved.contains(cList.get(index))) {
        optimalSet.add(cList.get(index));
      }
    }
  }

  /**
   * Given the Pareto set, first the ordering "function" is applied. Then it
   * recalculates this set.
   */
  public void addCriteriaCostWithOrdering(CriteriaCost cc) {
    List<CriteriaCost> toBeRemoved = new ArrayList();
    numberOfComparisons = 0;
    boolean mustAdd = false;
    if (!optimalSet.contains(cc)) {
      // Here we apply the ordering function
      Collections.sort(optimalSet, new CriteriaComparator());
      //Now that the criteria is ordered we can add the criteria
      //or decide that is not optimal in LINEAR TIME
      int cost1 = cc.getCost1();
      for (int i = 0; i < optimalSet.size(); i++) {
        CriteriaCost currentCc = optimalSet.get(i);
        numberOfComparisons++;
        if (cc.getCost1() > currentCc.getCost1()) {
          continue;
        } else {
          if (CriteriaCost.isLessEqualThan(cc, currentCc)) {
            mustAdd = true;
            toBeRemoved.add(currentCc);
            // System.out.println("REMOVED: " + currentCriteria);
          }
        }        
      }
      // at the end of the process we add only the optimal CriteriaCost
      // objects to our Pareto set
      for (int index = 0; index < toBeRemoved.size(); index++) {
        if (optimalSet.contains(toBeRemoved.get(index))) {
          optimalSet.remove(toBeRemoved.get(index));
        }
      }
      if (mustAdd) {
        optimalSet.add(cc);
      }
      Collections.sort(optimalSet, new CriteriaComparator());
    }
  }

  /**
   * Given an existing Pareto set, it is recalculated with a new riteria cost.
   */
  public void addCriteriaCostWithoutOrdering(CriteriaCost cc) {
    if (!optimalSet.contains(cc)) {
      List<CriteriaCost> cList = new ArrayList();
      // first we add all the elements which are already present
      // in this set
      cList.addAll(optimalSet);
      // Then we add the new criteria cost
      cList.add(cc);
      calculateParetoSetWithoutOrdering(cList);
    }
  }

  /**
   * Comparator for a two-cost criteria cost.
   */
  static class CriteriaComparator implements Comparator<CriteriaCost> {
    @Override
    public int compare(CriteriaCost criteria1, CriteriaCost criteria2) {

      int cri1Cost1 = criteria1.getCost1();
      // int cri1Cost2 = criteria1.getCost2();
      int cri2Cost1 = criteria2.getCost1();
      // int cri2Cost2 = criteria2.getCost2();

      if (cri1Cost1 > cri2Cost1) {
        return +1;
      } else if (cri1Cost1 < cri2Cost1) {
        return -1;
      } else {
        return 0;
      }
    }
  }
}
