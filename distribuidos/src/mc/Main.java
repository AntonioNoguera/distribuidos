package mc;

public class Main {
	static int ANTS = 5;
	static int ITERATIONS = 3;

	public static void main(String[] args) {
		String nodes[] = {"Base", "Balboa", "Esterline", "Arrow", "CMI SRL", "EX-Otay", "ENOVI", "F2", "F3"};
		// Time matrix in minutes
		int time_matrix[][] = {
			{0, 4, 7, 9, 14, 30, 24, 32, 38}, 
			{4, 0, 3, 5, 16, 29, 30, 40, 42},
			{7, 3, 0, 2, 18, 30, 31, 33, 35},
			{9, 5, 2, 0, 24, 31, 32, 36, 37},
			{14, 16, 18, 24, 0, 26, 40, 46, 49},
			{30, 29, 30, 31, 26, 0, 80, 60, 62},
			{24, 30, 31, 32, 40, 80, 0, 14, 16},
			{32, 40, 33, 36, 46, 60, 14, 0, 2},
			{38, 42, 35, 37, 49, 62, 16, 2, 0}
		};
		// TODO Run Sequential Program
		Sequential secuencial = new Sequential(nodes, time_matrix);
		secuencial.execute();
		
		System.out.println("Mejor recorrido: " + secuencial.getBestRoute());
		System.out.println("Mejor tiempo estimado: " + secuencial.getBestTime() + " min.\n");

		// TODO Run Parallel Program
		Parallel paralelo = new Parallel(nodes, time_matrix);
		paralelo.execute();

		System.out.println("Mejor recorrido: " + paralelo.getBestRoute());
		System.out.println("Mejor tiempo estimado: " + paralelo.getBestTime() + " min.");
	}

}
