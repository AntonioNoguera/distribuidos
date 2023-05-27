package act1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class PromedioSecuencial {
	public static void main(String[] args) {
		// Read all numbers and put it in an array
		LeerNumeros myInput = new LeerNumeros();
		int[] numeros = myInput.getNumeros();
		int length = myInput.getLength();

		long inicio = System.nanoTime();
		double suma = 0;
		double finalRes = 0;
		for (int i = 0; i < length; i++) {
			suma += numeros[i];
		}
		finalRes = suma / length;
		long fin = System.nanoTime();
		System.out.println("La cantidad de números es: " + length);
		System.out.println("El promedio total es: " + finalRes);			
		System.out.println("Lo que se tardo fue: " + (fin - inicio) + " nanoseg.");
	}
}
