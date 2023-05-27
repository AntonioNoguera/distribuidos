package mc;

public class Sequential extends Ant {
	
	public Sequential(String[] nodes, int[][] time_matrix) {
		super(nodes, time_matrix);
		// TODO Auto-generated constructor stub
	}

	void execute() {
		// TODO printMatrix
		
		long init = System.nanoTime();
		for (int i = 0; i < Main.ITERATIONS; i++) {
			for (int j = 0; j < Main.ANTS; j++) {
				travel();
			}
			
			updatePheromone();
			cleanEdges();
		}
		long fin = System.nanoTime();
		System.out.println("Secuencial: tarda " + (fin - init) + " nano.");
	}
}
