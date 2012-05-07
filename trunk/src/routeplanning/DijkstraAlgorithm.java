package routeplanning;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm {
	
	// PRIVATE members.

	// Indicator which node was visited by a particular run of Dijkstra. Useful
	// for computing the connected components; see the advice given in Lecture 2.
	private List<Integer> visitedNodeMarks;
	//private Map<Node, Integer> 
	
	// Reference to graph on which this object is supposed to work. In C++ make
	// sure this is a reference or pointer.
	private RoadNetwork graph;
	
	// Create instance of this class for a given (road) graph.
	public DijkstraAlgorithm(RoadNetwork graph){
		this.graph =  graph;
	}
	  
	// Compute the shortest paths from the given source to the given target node.
	// Returns the cost of the shortest path. If called with target node -1,
	// Dijkstra is run until all nodes reachable from the source are settled.
	int computeShortestPath(int sourceNodeId, int targetNodeId){
		int shortestPathCost = -1;
		
		List<List<Arc>> adjacentsArcs = graph.getAdjacentArcs();
		List<Arc> sourceAdjArcs = new ArrayList();
		boolean found = false;
		int index = 0;
		while (found == false && index<adjacentsArcs.size()){
			sourceAdjArcs = adjacentsArcs.get(index);
			Node node = sourceAdjArcs.get(0).getHeadNode();
			if(node.id==sourceNodeId){
				found  = true;
			}
		}
		
		if(found==true){
			for (int i = 1; i< sourceAdjArcs.size(); i++){
				
			}
		}
		
		return shortestPathCost;
	}

	// Mark all nodes visited by the next call to computeShortestPath with this
	// number.
	void setVisitedNodeMark(int mark){
		
	}


}
