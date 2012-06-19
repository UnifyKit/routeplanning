package routeplanning;

public class TestGraphEx6 {
  /**
   * Creates a road network sample(Exercise Sheet6, slide 16).
   * TODO: Cambiar por un test.
   * @param graph
   */
  public RoadNetwork createSampleGraphEx6() {
    RoadNetwork rn = new RoadNetwork();
    Node node0 = new Node(0, 1.0, 1.0);
    Node node1 = new Node(1, 1.0, 1.0);
    Node node2 = new Node(2, 1.0, 1.0);
    Node node3 = new Node(3, 1.0, 1.0);
    Node node4 = new Node(4, 1.0, 1.0);
    Node node5 = new Node(5, 1.0, 1.0);
    Node node6 = new Node(6, 1.0, 1.0);
    Node node7 = new Node(7, 1.0, 1.0);
    Node node8 = new Node(8, 1.0, 1.0);
    Node node9 = new Node(9, 1.0, 1.0);
    Node node10 = new Node(10, 1.0, 1.0);
    Node node11 = new Node(11, 1.0, 1.0);
    Node node12 = new Node(12, 1.0, 1.0);
    
    Arc newArc01 = new Arc(node1, 2);
    Arc newArc05 = new Arc(node5, 5);
    Arc newArc09 = new Arc(node9, 3);
    
    Arc newArc10 = new Arc(node0, 2);
    Arc newArc15 = new Arc(node5, 2);
    Arc newArc12 = new Arc(node2, 5);

    Arc newArc21 = new Arc(node1, 5);
    Arc newArc26 = new Arc(node6, 3);
    Arc newArc23 = new Arc(node3, 7);
    
    Arc newArc32 = new Arc(node2, 7);
    Arc newArc37 = new Arc(node7, 3);
    Arc newArc34 = new Arc(node4, 4);
    
    Arc newArc43 = new Arc(node3, 4);
    Arc newArc48 = new Arc(node8, 3);
    Arc newArc412 = new Arc(node12, 4);
    
    Arc newArc50 = new Arc(node0, 5);
    Arc newArc51 = new Arc(node1, 2);
    Arc newArc59 = new Arc(node9, 4);
    Arc newArc56 = new Arc(node6, 1);
    
    Arc newArc65 = new Arc(node5, 1);
    Arc newArc62 = new Arc(node2, 3);
    Arc newArc67 = new Arc(node7, 1);
    Arc newArc610 = new Arc(node10, 4);
    
    Arc newArc76 = new Arc(node6, 1);
    Arc newArc73 = new Arc(node3, 3);
    Arc newArc78 = new Arc(node8, 1);
    Arc newArc711 = new Arc(node11, 3);
    
    Arc newArc87 = new Arc(node7, 1);
    Arc newArc84 = new Arc(node4, 3);
    Arc newArc812 = new Arc(node12, 2);
    
    Arc newArc90 = new Arc(node0, 3);
    Arc newArc95 = new Arc(node5, 4);
    Arc newArc910 = new Arc(node10, 7);
    
    Arc newArc109 = new Arc(node9, 7);
    Arc newArc106 = new Arc(node6, 4);
    Arc newArc1011 = new Arc(node11, 6);
    
    Arc newArc1110 = new Arc(node10, 6);
    Arc newArc117 = new Arc(node7, 3);
    Arc newArc1112 = new Arc(node12, 5);
    
    Arc newArc1211 = new Arc(node11, 5);
    Arc newArc128 = new Arc(node8, 2);
    Arc newArc124 = new Arc(node4, 4);
    
    rn.addNodeToGraph(node0);
    rn.addNodeToGraph(node1);
    rn.addNodeToGraph(node2);
    rn.addNodeToGraph(node3);
    rn.addNodeToGraph(node4);
    rn.addNodeToGraph(node5);
    rn.addNodeToGraph(node6);
    rn.addNodeToGraph(node7);
    rn.addNodeToGraph(node8);
    rn.addNodeToGraph(node9);
    rn.addNodeToGraph(node10);
    rn.addNodeToGraph(node11);
    rn.addNodeToGraph(node12);

    rn.addAdjacentArc(node0, newArc01);
    rn.addAdjacentArc(node0, newArc05);
    rn.addAdjacentArc(node0, newArc09);
    
    rn.addAdjacentArc(node1, newArc10);
    rn.addAdjacentArc(node1, newArc15);
    rn.addAdjacentArc(node1, newArc12);

    rn.addAdjacentArc(node2, newArc21);
    rn.addAdjacentArc(node2, newArc26);
    rn.addAdjacentArc(node2, newArc23);
    
    rn.addAdjacentArc(node3, newArc32);
    rn.addAdjacentArc(node3, newArc37);
    rn.addAdjacentArc(node3, newArc34);
    
    rn.addAdjacentArc(node4, newArc43);
    rn.addAdjacentArc(node4, newArc48);
    rn.addAdjacentArc(node4, newArc412);
    
    rn.addAdjacentArc(node5, newArc50);
    rn.addAdjacentArc(node5, newArc51);
    rn.addAdjacentArc(node5, newArc59);
    rn.addAdjacentArc(node5, newArc56);
    
    rn.addAdjacentArc(node6, newArc65);
    rn.addAdjacentArc(node6, newArc62);
    rn.addAdjacentArc(node6, newArc67);
    rn.addAdjacentArc(node6, newArc610);
    
    rn.addAdjacentArc(node7, newArc76);
    rn.addAdjacentArc(node7, newArc73);
    rn.addAdjacentArc(node7, newArc78);
    rn.addAdjacentArc(node7, newArc711);
    
    rn.addAdjacentArc(node8, newArc87);
    rn.addAdjacentArc(node8, newArc84);
    rn.addAdjacentArc(node8, newArc812);

    rn.addAdjacentArc(node9, newArc90);
    rn.addAdjacentArc(node9, newArc95);
    rn.addAdjacentArc(node9, newArc910);

    rn.addAdjacentArc(node10, newArc109);
    rn.addAdjacentArc(node10, newArc106);
    rn.addAdjacentArc(node10, newArc1011);
    
    rn.addAdjacentArc(node11, newArc1110);
    rn.addAdjacentArc(node11, newArc117);
    rn.addAdjacentArc(node11, newArc1112);
    
    rn.addAdjacentArc(node12, newArc1211);
    rn.addAdjacentArc(node12, newArc128);
    rn.addAdjacentArc(node12, newArc124);
    
    return rn;
  }
}
