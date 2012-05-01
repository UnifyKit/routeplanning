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
public class RoadNetwork {
    List<List<Arc>> adjacentArcs;
    
    public RoadNetwork(){
        adjacentArcs = new ArrayList<List<Arc>>();
    }
    /*
     * Read OSM file (in XML format) and construct the corresponding road network
     */
    public void readFromOsmFile(){
        
    }
    /**
     * First element of the ArrayList<Arc> is the tail node, the other
     * lists contain the headnodes
     * because node1->node1 then cost=0 
     * @param tailNode 
     */
    public void addNodeToGraph(Node tailNode){
        Arc arc0 = new Arc(tailNode,0);
        adjacentArcs.add(new ArrayList<Arc>());
        adjacentArcs.get(adjacentArcs.size()-1).add(arc0);
    }
    
    public void addAdjacentArc(Node tailNode,Arc arc){
        for(int i=0; i<adjacentArcs.size(); i++){
            if(adjacentArcs.get(i).get(0).getHeadNode().equals(tailNode)){ //primera lista, primer elemento
                adjacentArcs.get(i).add(arc);
            }
        }
    }
    
    public ArrayList<List<String>> asString(){
        ArrayList<List<String>> res = new ArrayList<>();
        List<String> list;
        
        for(int i=0; i<adjacentArcs.size(); i++){
            list = new ArrayList<>();
            for(int j=0; j<adjacentArcs.get(i).size(); j++){
                list.add(adjacentArcs.get(i).get(j).asString());
            }
            res.add(list);
        }
        
        return res;
    }
}
