package mc;

import java.util.*;

public class Hilo extends Thread {	
	Vector visited;
	int initialNodeTravelling;
	
	double totalTime;
	String route = "";
	
	
	
	public void travel() {
		int initialNode = 0;
		int destinyNode;
		double time;
		Vector candidates;
		
		visited = new Vector();
		totalTime = 0.0;
		
		while (visited.size() < Parallel.nNodes + 1) {
			route += String.valueOf(Parallel.nodes[initialNode] + " ");
			candidates = new Vector();
			visited.add(initialNode);
			
			for(int i = 0; i < Parallel.time_matrix.length; i++) {
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
			
			time = Parallel.time_matrix[initialNode][destinyNode];
			totalTime += time;

			if (initialNode != destinyNode) {
				Parallel.edgesTraveled[initialNode][destinyNode]++;
				Parallel.edgesTraveled[destinyNode][initialNode]++;
			}
			
			initialNode = destinyNode;
			
		}
	}


	public int selectEdge(int initialNode, Vector candidates) {
		int candidate;
		double aptitud, totalAptitud, probabilitySelection, random, range;
		Hashtable aptitudes = new Hashtable();
		
		totalAptitud = 0.0;
		
		for (int i = 0; i < candidates.size(); i++) {
			candidate = ((Integer) candidates.get(i)).intValue();
			aptitud = Parallel.pheromone[initialNode][candidate] * (1.0 / Parallel.time_matrix[initialNode][candidate]);
			
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
	
	public String getRoute() {
		return route;
	}
	
	public double getTime() {
		return totalTime;
	}
	
	public void run() {
		travel();

		synchronized (Parallel.monitor) {
			if (totalTime < Parallel.bestTime) {
				Parallel.bestTime = totalTime;
				Parallel.bestRoute = route;
			}
            Parallel.hilosActivos--;
            Parallel.monitor.notifyAll();
        }
	}
}
