package mc;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class Ant {
	String nodes[];
	int time_matrix[][];
	int nNodes;
	
	double evaporation;
	double [][]pheromone;
	int [][] edgesTraveled; 

	Vector<Integer> visited;
	int initialNodeTravelling;
	
	String bestRoute;
	double bestTime;
	
	public Ant(String nodes[], int time_matrix[][]) {
		this.nodes = nodes;
		this.time_matrix = time_matrix;
		
		nNodes = nodes.length;
		initializePheromone();
		initializeEdgesTraveled();
		
		bestRoute = "";
		bestTime = Double.MAX_VALUE;
	}
	
	public void initializePheromone() {
		pheromone = new double[nNodes][nNodes];
		for (int i = 0; i < pheromone.length; i++) {
			for (int j = 0; j < pheromone.length; j++) {
				pheromone[i][j] = 1.0;
			}
		}
	}
	
	public void initializeEdgesTraveled() {
		edgesTraveled = new int[nNodes][nNodes];
		cleanEdges();
	}
	
	public void cleanEdges() {
		for (int i = 0; i < edgesTraveled.length; i++) {
			for (int j = 0; j < edgesTraveled.length; j++) {
				edgesTraveled[i][j] = 0;
			}
		}

	}
	
	public void updatePheromone() {
		for (int i = 0; i < pheromone.length; i++) {
			for (int j = 0; j < pheromone.length; j++) {
				pheromone[i][j] = pheromone[i][j] * (1 - evaporation) + edgesTraveled[i][j] * (1.0 / nNodes);
			}
		}
	}
	
	public void travel() {
		int initialNode = 0;
		int destinyNode;
		double time, totalTime;
		Vector<Integer> candidates;
		String route = "";
		
		visited = new Vector<Integer>();
		totalTime = 0.0;
		
		while (visited.size() < nNodes + 1) {
			route += String.valueOf(nodes[initialNode] + " ");
			candidates = new Vector<Integer>();
			visited.add(initialNode);
			
			for(int i = 0; i < time_matrix.length; i++) {
				if (!visited.contains(i)) {
					candidates.add(i);
				}
			}
			
			if (candidates.size() == 0) {
				destinyNode = initialNodeTravelling;
			} else if (candidates.size() == 1) {
				destinyNode = ((Integer) candidates.get(0)).intValue();
			} else {
				destinyNode = selectEdge(initialNode, candidates);
			}
			
			time = time_matrix[initialNode][destinyNode];
			totalTime += time;

			if (initialNode != destinyNode) {
				edgesTraveled[initialNode][destinyNode]++;
				edgesTraveled[destinyNode][initialNode]++;
			}
			
			initialNode = destinyNode;
		}

		if (totalTime < bestTime) {
			bestTime = totalTime;
			bestRoute = route;
		}
	}
	
	public int selectEdge(int initialNode, Vector<Integer> candidates) {
		int candidate;
		double aptitud, totalAptitud, probabilitySelection, random, range;
		Hashtable aptitudes = new Hashtable();
		
		totalAptitud = 0.0;
		
		for (int i = 0; i < candidates.size(); i++) {
			candidate = ((Integer) candidates.get(i)).intValue();
			aptitud = pheromone[initialNode][candidate] * (1.0 / time_matrix[initialNode][candidate]);
			
			totalAptitud += aptitud;
			aptitudes.put(candidate, aptitud);
		}
		
		Enumeration keys = aptitudes.keys();
		
		while (keys.hasMoreElements()) {
			candidate = ((Integer) keys.nextElement()).intValue();
			aptitud = ((Double) aptitudes.get(candidate)).doubleValue();
			aptitud = aptitud / totalAptitud;
			
			aptitudes.put(candidate, aptitud);
		}
		
		random = Math.random();
		range = 0;
		
		// Ruleta
		for (int i = 0; i < candidates.size(); i++) {
			candidate = ((Integer) candidates.get(i)).intValue();
			aptitud = ((Double) aptitudes.get(candidate)).doubleValue();
			
			range += aptitud;
			
			if (random <= range) {
				return candidate;
			}
		}
		
		return 0;
	}
	
	public String getBestRoute() {
		return bestRoute;
	}
	
	public double getBestTime() {
		return bestTime;
	}

	
}
