package com.hectormtz;
 
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.JFrame; 
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;



public class Server extends JFrame {
	public static boolean eternity = true;
    
	private JTable tabla;
    private DefaultTableModel modelo;

    public Server() {
    	setTitle("Rankeo de Unidades - E2023 - Sistemas Distribuidos - Server Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 100);
        setLocationRelativeTo(null);
        setSize(1000, 250);
        String[] columnas = {"Rank", "Ip del Cliente", "# de Cliente", "Fabricante CPU",
                "Procesador", "# de Núcleos", "Frec. Promedio", "Frec. Máx", "Ram Disponible",
                "Ram Total", "Cliente/Servidor"};

        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        // Crear renderizador personalizado para centrar el contenido en las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Establecer el renderizador personalizado en cada columna
        int columnCount = tabla.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tabla);
        getContentPane().add(scrollPane);

        setVisible(true);

        // Creamos un Timer para actualizar la tabla cada 1s
        Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarTabla(); 
			}
		});
        
        timer.start(); 
    }
    
    static int nCliente=0;
    
    static Object[][] datos = new Object[1][11];
    
    /*
    private static void detectBetterDevice() {
    	int maximumRank = Integer.MIN_VALUE;
    	String newIP = "";
    	for (int i = 0; i < datos.length; i++) {
    		if (datos[i][0] != null) {
				int presentRank = (int) datos[i][0];
				if (presentRank > maximumRank) {
					maximumRank = presentRank;

					newIP = (String) datos[i][1].toString().substring(1);
				}
    		}
    	}
    	if (newIP.equals("")) {
    		return;
    	}
		
	
    	if (!newIP.equals("127.0.0.1")) {
			for (int i = 0; i < datos.length; i++) {
				if (datos[i][0] != null) {
					System.out.println(datos[i][1]);

					//networkPost( (String) datos[i][1], newIP);
				}
			}
    		Main.ipAddress = newIP;
    		eternity = false;
    		
    		
    	}
    }
    /*
    public static void networkPost(String ip, String newIP) {
    	ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		Socket s = null;
		
		try {
			// instantiate socket with ip and port
			s = new Socket(ip, 5555);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			// send name
			oos.writeObject(newIP);
			
			// receive greeting
			String res = (String)ois.readObject();
			
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				try {
					s.close();

					if (ois != null) ois.close();
					if (oos != null) oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    }
    */
    
    
    private static int rankGenerator(String[] propiedades) { 
    	int Gen=0;
    	if(propiedades[0].equals("AuthenticAMD")) {
			String[] cadena = propiedades[1].split(" ");
			Gen = Integer.parseInt(cadena[2]); 
			
    	}else if(propiedades[0].equals("GenuineIntel")) {
    		String[] cadena = propiedades[1].split("-"); 
    		
    		Gen = (int)cadena[0].charAt(cadena[0].length()-1)- '0';; 
    		System.out.println("GEN: "+Gen);
    		
    	} 
    	double MemDisp = Double.parseDouble(propiedades[5]) ;
    	double freqP = Double.parseDouble(propiedades[3]);
    	double mFreq = Double.parseDouble(propiedades[4]);;
    	int nNucleos = Integer.parseInt(propiedades[2]);
    	
    	
    	int rank= (int) ((150* MemDisp) + (nNucleos*(mFreq-freqP)*100)) + Gen*200;
    	
		return rank;
    	
    }
    
    private void actualizarTabla() {
    	
        // Creamos los datos para la tabla
    	
        // Eliminamos todos los datos anteriores del modelo
        modelo.setRowCount(0);
        
        // Agregamos los nuevos datos al modelo
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }
    
    static void arrancaEstres() {
    	System.out.println("-------- SE ARRANCA PRUEBAS DE ESTRES --------");
    }
    
    static void actualizarObjeto(String Buffer,InetAddress ip) {
    	String propiedades[] = Buffer.split("/");  
    	System.out.println("BANDERA DE ARRANQUE: "+propiedades[7]);
    	if(propiedades[7].equals("true")) {
    		arrancaEstres();
    	}
    	
    	int flagIp= -1;

    	//For que vereficará si existe la ip
    	for(int i=0;i<datos.length;i++) {
    		if(ip.toString().equals((String) datos[i][1])) {
    			// Cliente actualizando datos
    			flagIp = i;
    		}
    	}
    	
    	// Cliente Nuevo
    	if(flagIp == -1) {
			nCliente++; 
			Object[][] nuevoArreglo;
			if(nCliente==1) {
				nuevoArreglo = new Object[datos.length][datos[0].length];
			}else {
				nuevoArreglo = new Object[datos.length+1][datos[0].length];
			}
    		
    		Object[] nuevoObjeto = {rankGenerator(propiedades), ip.toString(), nCliente, 
    				propiedades[0],propiedades[1],propiedades[2],propiedades[3],
    				propiedades[4],propiedades[5],propiedades[6],"a"}; 
    		
    		for (int i = 0; i < datos.length; i++) {
        	    for (int j = 0; j < datos[i].length; j++) {
        	        nuevoArreglo[i][j] = datos[i][j];
        	    }
        	}
        	nuevoArreglo[nCliente - 1] = nuevoObjeto;
        	datos = nuevoArreglo;
    	}else {
    		// Existe el cliente
    		//
    		//Actualizar los elementos RanK-0 Frec Prom-6  FrecMax-7 - RamDIs Ram-8 - Ciente10 
    		
    		datos[flagIp][0] = rankGenerator(propiedades);
        	datos[flagIp][6] = propiedades[3];
        	datos[flagIp][7] = propiedades[4];  
        	datos[flagIp][8] = propiedades[5];
        	
        	//Funcion que determina si es servidor o no
			datos[flagIp][10] ="Nada"; 
    	}
    	
    	//String[] columnas = {"Rank","Ip del Cliente"," # de Cliente ", " Procesador ", " # de Núcleos "," F. Promedio "," Ram Disponible "," Ram Total "};

    	 
    }
    
    public static void start()  throws Exception{
        //Se arranca la tabla
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Server();
			}
		});
        
        //Se arranca el servidor
    	ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        Socket s = null;
        ServerSocket ss = new ServerSocket(5432);

        while( eternity ){


            try {

                // el ServerSocket me da el Socket
                s = ss.accept();
                // informacion en la consola
				System.out.println("------Server");
				System.out.println("Esperando Mensaje ");
				System.out.println();
                System.out.println("Se ingresaron datos desde la IP: " +s.getInetAddress());
                InetAddress ipRecibida = s.getInetAddress();
                

                // enmascaro la entrada y salida de bytes
                ois = new ObjectInputStream( s.getInputStream() );
                oos = new ObjectOutputStream( s.getOutputStream() );
                
                //Se recibe la cadena objeto
                String paquete = (String) ois.readObject(); 
                
                //Actualizacion del objeto
                actualizarObjeto(paquete,ipRecibida);

                // envio el saludo al cliente
                oos.writeObject("Conexion Establecida");
//                System.out.println("Recepcion Declarada");

            }catch(Exception ex){
            	// Disables error

//                ex.printStackTrace();

            }finally{

                if( oos !=null ) oos.close(); 
                if( ois !=null ) ois.close(); 
                if( s != null ) s.close();

                //detectBetterDevice();
                System.out.println("Recepcion Exitosa!");
            }
        }
        
        
    }
}