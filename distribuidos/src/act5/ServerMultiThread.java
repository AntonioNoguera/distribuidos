package act5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMultiThread {
	public static void main(String[] args) throws Exception {
		Socket s = null;
		ServerSocket ss = new ServerSocket(5432);
		
		int threads = 0;
		
		// Always listening
		while (true) {
			try {
				// When we have a new connection
				s = ss.accept();
				
				// Instantiate a thread
				new Tarea(s).start();
				threads++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static class Tarea extends Thread {
		private Socket s = null;
		private ObjectInputStream ois = null;
		private ObjectOutputStream oos = null;
		
		// constructor: we receive the socket
		public Tarea(Socket socket) {
			this.s = socket;
		}
		
		int sum = 0;
		
		public void run() {
			try {
				System.out.println("Connected from: " + s.getInetAddress());
				
				// mask in and out bytes
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				
				long from = System.currentTimeMillis();
				int length = (int)ois.readObject();
				int[] numbers = (int[])ois.readObject();
				
				for (int i = 0; i < length; i++) {
					sum += numbers[i];
				}
				double prom = sum / length;

				oos.writeObject((double)prom);
				long to = System.currentTimeMillis();

				System.out.println("Average sent! = " + prom);
				System.out.println("Time spend: " + (to - from) + " ms.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (oos != null) oos.close();
					if (ois != null) ois.close();
					if (s != null) s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}