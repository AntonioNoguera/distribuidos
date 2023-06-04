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
			System.out.println("Verificando: " + direccionIP);
			InetAddress ipAddress = InetAddress.getByName(direccionIP);
            if (!ipAddress.isReachable(puerto)) {
            	socket.close();
            	return false;
            }

            // Establecer la dirección IP y el puerto en el socket
			socket.setSoTimeout(2000);
            socket.connect(new InetSocketAddress(ipAddress, puerto));
			socket.close();
			return true; // La conexión fue exitosa, el servidor está activo
		} catch (SocketTimeoutException e) {
			System.out.println("Tiempo de espera agotado");
			return false;
		} catch (UnknownHostException e) {
            System.out.println("La dirección IP es desconocida: " + direccionIP);
            return false;
		} catch (Exception e) {
			return false; // No se pudo establecer la conexión, el servidor no está disponible
		}
	}

	public static void main(String[] args) throws Exception {
		CSVRead csv = new CSVRead();
		ArrayList<String> pcs = csv.read();
		int datos = csv.getLength();
		
		//Nuevo Metodo BroadCast 
		for (int i = 0; i < datos; i++) {
			boolean isActive = verificarServidorActivo(pcs.get(i), 5432);
			if (isActive) {
				ipAddress = pcs.get(i);
				break;
			}
		} 
		
		if (ipAddress.equals("")) {
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
			Client client = new Client(ipAddress);
			client.start();
			Listener listener = new Listener();
			listener.start();
		}
		
	} 		
}
