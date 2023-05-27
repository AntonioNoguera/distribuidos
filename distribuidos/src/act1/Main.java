package act1;

public class Main {
	// Read all numbers and put it in an array
	static LeerNumeros myInput = new LeerNumeros();
	static int[] numeros = myInput.getNumeros();
	static int length = myInput.getLength();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int hilos = 50;
		long[] time = new long[hilos];
		int hiloPreferido = -1;
		long minTime = Long.MAX_VALUE;
		Lanzador[] lanzadores = new Lanzador[hilos];
		for (int i = 0; i < hilos; i++) {
			lanzadores[i] = new Lanzador(i + 1, numeros, length);
			time[i] = lanzadores[i].getTime();
			if (time[i] < minTime) {
				minTime = time[i];
				hiloPreferido = i + 1;
			}
		}
		
		System.out.println("La cantidad de hilos que tardó menos fue: " + hiloPreferido);
		System.out.println("con: " + minTime + " nanoseg.");
	}

}
