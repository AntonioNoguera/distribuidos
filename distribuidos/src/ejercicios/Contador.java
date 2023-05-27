package ejercicios;

public class Contador extends Thread {
	int limite;
	int id;
	public Contador(int id, int limite) {
		this.id = id;
		this.limite = limite;
	}
	public void run() {
		for (int i = 0; i < limite; i++) {
			System.out.println("Soy el hilo " + id + " y mi conteo es: " + i);
		}
		System.out.println("El hilo " + id + " termino");
	}
}
