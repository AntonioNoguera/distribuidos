package mc;

import java.util.*;

public class Parallel {
	static String nodes[];
	static int time_matrix[][];
	static int nNodes;
	
	double evaporation;
	static double [][]pheromone;
	static int [][] edgesTraveled; 

	Vector visited;
	int initialNodeTravelling;
	
	static String bestRoute;
	static double bestTime;
    static final Object monitor = new Object();
    static int hilosActivos = Main.ANTS;
	Hilo hilos[];

	public Parallel(String nodes[], int time_matrix[][]) {
		Parallel.nodes = nodes;
		Parallel.time_matrix = time_matrix;
		
		nNodes = nodes.length;
		hilos = new Hilo[Main.ANTS];
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

	public String getBestRoute() {
		return bestRoute;
	}
	
	public double getBestTime() {
		return bestTime;
	}
	
	void execute() {
		// TODO printMatrix
		long init = System.nanoTime();
		for (int i = 0; i < Main.ITERATIONS; i++) {
			for (int j = 0; j < Main.ANTS; j++) {
				hilos[j] = new Hilo();
				hilos[j].start();
			}
			
			// Espera a que terminen los hilos
	        synchronized (monitor) {
	            while (hilosActivos > 0) {
	                    try {
							monitor.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	            }
	        }
			updatePheromone();
			cleanEdges();
		}
		long fin = System.nanoTime();
		System.out.println("Paralelo: tarda " + (fin - init) + " nano.");
	}
}
