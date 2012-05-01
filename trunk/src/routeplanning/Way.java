/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeplanning;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CJC
 */
public class Way {
    List<Arc> arcs;
    
    public Way(){
        arcs = new ArrayList<Arc>();
    }
    public void addArc(Arc arc){
        arcs.add(arc);
    }
    public List<String> asString(){
        List<String> way = new ArrayList<String>();
        for(int i=0; i<arcs.size(); i++){
            way.add(arcs.get(i).asString());
        }
        return way; 
    }
}
