/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeplanning;

/**
 *
 * @author CJC
 */
public class Arc {
    Node headNode;
    double cost;
    
    public Arc(Node headNode,double cost){
        this.headNode = headNode;
        this.cost = cost;
    }
    public String asString(){
        String res = new String();
        if (this.cost==0){
            res = "{"+this.headNode.id+"}";
        }else{
            res = "{"+this.headNode.id+"|"+this.cost+"}";
        }
        
        return res;
    }
    public Node getHeadNode(){
        return headNode;
    }
}
