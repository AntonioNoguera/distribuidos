package libro;

import java.util.Scanner;

public class BinOctHexRepresentation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		
		System.out.print("Ingresa un numero: ");
		int n = input.nextInt();
		
		System.out.println("Numero en bin: " + Integer.toBinaryString(n));
		System.out.println("Numero en oct: " + Integer.toOctalString(n));
		System.out.println("Numero en hex: " + Integer.toHexString(n));
		
		System.out.print("Ingresa la base: ");
		int base = input.nextInt();
		
		System.out.println("El numero en la base " + base + " es: " + Integer.toString(n, base));
		
		
		input.close();
	}
}
