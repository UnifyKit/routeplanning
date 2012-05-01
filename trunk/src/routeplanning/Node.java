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
public class Node {
    int id;
    Double latitude;
    Double longitude;
    
    public Node(int id, Double latitude, Double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public Double getLongitude(){
        return longitude;
    }
    
    public Double getLatitude(){
        return latitude;
    }
    
    public String asString(){
        return "("+this.id+"|"+this.latitude+"|"+this.longitude+")";
                
    }
}
