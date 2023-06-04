package com.hectormtz;

import java.net.ConnectException;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Main {
	static String ipAddress = "";
	
	public static boolean verificarServidorActivo(String direccionIP, int puerto) { 

        try {
        	Socket socket = new Socket();
            socket.connect(new InetSocketAddress(direccionIP, puerto),10);
            System.out.println("Se inicializa el servidor: "+direccionIP);
            
            socket.close();
            return true;  
        } catch (Exception e) {
            System.out.println("No se pudo establecer la conexión a la dirección: " + direccionIP);
            return false;
        }
	}
	

	public static void main(String[] args) throws Exception {
		CSVRead csv = new CSVRead();
		ArrayList<String> pcs = csv.read();
		int datos = csv.getLength();
		
		
		for (int i = 0; i < datos; i++) {
			boolean isActive = verificarServidorActivo(pcs.get(i), 5432);
			if (isActive) {
				ipAddress = pcs.get(i);
				break;
			}
		} 
		
		if (ipAddress.equals("")) {
		//if (false) {
			try {
	            InetAddress localhost = InetAddress.getLocalHost();
	            ipAddress = localhost.getHostAddress();  
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
			Client client = new Client(ipAddress);
			client.start();
			Server.start(ipAddress.toString());
		} else {
			Client client = new Client("192.168.100.129");
			client.start();
			Listener listener = new Listener();
			listener.start();
		}
		
	} 		
}
