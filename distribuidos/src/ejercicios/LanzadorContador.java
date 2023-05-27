package ejercicios;

import java.util.Random;

public class LanzadorContador {
	public static void main(String[] args) {
		Random ran = new Random();
		int lim = ran.nextInt(100) + 1;
		Contador c1 = new Contador(1, lim);
		Contador c2 = new Contador(2, lim);
		c1.start();
		c2.start();
	}
}
