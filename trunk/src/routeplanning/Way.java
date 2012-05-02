package routeplanning;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CJC
 */
public class Way {
  /**
   * List of arcs in a way.
   */
  List<Arc> arcs;
  /**
   * Constructor.
   */
  public Way() {
    arcs = new ArrayList<Arc>();
  }
  /**
   * Add arc to way.
   * @param arc
   */
  public void addArc(Arc arc) {
    arcs.add(arc);
  }
  /**
   * Get arc as a String.
   * @return
   */
  public List<String> asString() {
    List<String> way = new ArrayList<String>();
    for (int i = 0; i < arcs.size(); i++) {
      way.add(arcs.get(i).asString());
    }
    return way; 
  }
}
