package com.hectormtz;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
	public void start() throws Exception {
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		Socket s = null;
		ServerSocket ss = new ServerSocket(5555);
		
		// This is for always listening
		while (true) {
			try {
				System.out.println("--------Listener");
				System.out.println("Listening on port 5555");
				s = ss.accept(); 
			
				System.out.println("Connection from: " + s.getInetAddress());
				
				// mask in and out bytes
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				
				// read name from client
				// and cast it to a string
				String newIp = (String)ois.readObject();
				
				// make the output
				String greet = newIp;
				// send greet
				oos.writeObject(greet); 
				throw new Exception("Hola");
			} catch (Exception e) {
				e.getMessage();
//				e.printStackTrace();
			} finally {
				if (ois != null) ois.close();
				if (oos != null) oos.close();
				if (s != null) s.close();
				System.out.println("Connection closed");
			}
		
		}	
		
	}
}