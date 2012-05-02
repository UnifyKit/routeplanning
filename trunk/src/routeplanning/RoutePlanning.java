package routeplanning;
/**
 * RoutePlanning.
 * 
 */
public class RoutePlanning {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RoadNetwork rn = new RoadNetwork();
        /*Node n1 = new Node(1, 49.3432663, 7.3032654);
        Node n2 = new Node(2, 49.3432663, 7.3032654);
        Node n3 = new Node(3, 49.3432663, 7.3032654);
        Node n4 = new Node(4, 49.3432663, 7.3032654);
        Node n5 = new Node(5, 49.3432663, 7.3032654);
        rn.addNodeToGraph(n1);
        rn.addNodeToGraph(n2);
        rn.addNodeToGraph(n3);
        rn.addNodeToGraph(n4);
        rn.addNodeToGraph(n5);
        
        Arc arc1_2 = new Arc(n2, 3);
        rn.addAdjacentArc(n1, arc1_2);
        
        Arc arc1_3 = new Arc(n3, 1);
        rn.addAdjacentArc(n1, arc1_3);
        
        Arc arc2_1 = new Arc(n1, 3);
        rn.addAdjacentArc(n2, arc2_1);
        
        Arc arc2_3 = new Arc(n3, 1);
        rn.addAdjacentArc(n2, arc2_3);
        
        Arc arc2_5 = new Arc(n5, 3);
        rn.addAdjacentArc(n2, arc2_5);
        
        Arc arc3_1 = new Arc(n1, 1);
        rn.addAdjacentArc(n3, arc3_1);
        
        Arc arc3_2 = new Arc(n2, 1);
        rn.addAdjacentArc(n3, arc3_2);
        
        Arc arc4_5 = new Arc(n5, 5);
        rn.addAdjacentArc(n4, arc4_5);
        
        Arc arc5_2 = new Arc(n2, 3);
        rn.addAdjacentArc(n5, arc5_2);
        
        Arc arc5_4 = new Arc(n4, 5);
        rn.addAdjacentArc(n5, arc5_4);
        
        System.out.println("ROAD NETWORK: " + rn.asString());
        Way way = new Way();
        way.addArc(arc1_2);
        way.addArc(arc1_3);
        way.addArc(arc5_4);
        System.out.println("WAY:" + way.asString());*/
        
        rn.readFromOsmFile();
        System.out.println("ROAD NETWORK: " + rn.asString());
    }
}
