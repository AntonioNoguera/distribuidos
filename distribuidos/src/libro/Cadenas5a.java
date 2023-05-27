package libro;

public class Cadenas5a {
	public static void main(String[] args) {
		StringBuffer cadenaFinal = new StringBuffer();
		
		int n = 1_000_000;
		
		long inicio = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			char c = (char) ('A' + (i % ('Z' - 'A' + 1)));
			cadenaFinal.append(c);
		}
		long fin = System.currentTimeMillis();
		
		
		System.out.println(cadenaFinal.toString());
		System.out.println("Se tardó " + (fin - inicio) + " miliseg.");
		
	}
	
}
