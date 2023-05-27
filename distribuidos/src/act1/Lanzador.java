package act1;

public class Lanzador {
	double finalRes = 0;

	// Number of threads
	int numeroThreads;
	int[] numeros;
	int length;
	
	public Lanzador(int numeroThreads, int[] numeros, int length) {
		this.numeroThreads = numeroThreads;
		this.numeros = numeros;
		this.length = length;
	}
	
	public long getTime() {
		int pasos = Math.round(length / numeroThreads);		
		Promediador[] prom = new Promediador[numeroThreads];
		
		long inicio = System.nanoTime();
		for (int i = 0; i < numeroThreads; i++) {
			int from = i * pasos;
			int to = from + pasos;
			if ((i + 1) == numeroThreads) to = length;
			prom[i] = new Promediador(i, numeros, from, to); 
			prom[i].start();
		}
		// Wait all threads
		int index = 0;
		int threadsCompletados = 0;
		while (true) {
			if (!prom[index].isAlive()) threadsCompletados++;
			if (threadsCompletados == numeroThreads) break;
			index++;
			if (index >= numeroThreads) {
				index = 0;
				threadsCompletados = 0;
			}
		}
		// When all threads finish
		for (int i = 0; i < numeroThreads; i++) {
			finalRes += prom[i].getSuma();
		}
		finalRes /= length;
		long fin = System.nanoTime();
		
		System.out.println("Hilos usados: " + numeroThreads);
		System.out.println("La cantidad de números es: " + length);
		System.out.println("El promedio total es: " + finalRes);		
		System.out.println("Lo que se tardo fue: " + (fin - inicio) + " nanoseg.\n");
		
		return fin - inicio;
	}
}
