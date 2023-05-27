package act4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	static String IP = "127.0.0.1";
	static int PORT = 5432;
	public static void main(String[] args) throws Exception {
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		Socket s = null;
		
		LeerNumeros Clase = new LeerNumeros();
		int[] numeros = Clase.getNumeros();
		int length = Clase.getLength();
		
		try {
			// instantiate socket with ip and port
			s = new Socket(IP, PORT);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			long from = System.currentTimeMillis();
			// send length
			oos.writeObject(length);
			// send numbers 
			oos.writeObject(numeros);
			
			// receive average
			double res = (double)ois.readObject();
			long to = System.currentTimeMillis();
			
			System.out.println(res);
			System.out.println("Time spend: " + (to - from) + " ms.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null) s.close();
			if (ois != null) ois.close();
			if (oos != null) oos.close();
		}
	}
}
