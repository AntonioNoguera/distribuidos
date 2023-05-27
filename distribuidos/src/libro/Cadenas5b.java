package libro;

public class Cadenas5b {
	public static void main(String[] args) {
		String cadenaFinal = "";
		
		int n = 1_000_000;
		
		long inicio = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			char c = (char) ('A' + (i % ('Z' - 'A' + 1)));
			cadenaFinal += c;
		}
		long fin = System.currentTimeMillis();
		
		System.out.println(cadenaFinal);
		System.out.println("Se tardo " + (fin - inicio) + " miliseg.");		
	}
}
