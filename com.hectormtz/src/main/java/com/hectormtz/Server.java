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

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;



public class Server extends JFrame {
	public static boolean eternity = true;
    
	private JTable tabla;
    private DefaultTableModel modelo;
    
    static String ServerIp = "";

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
    
    static int nCliente = 0;
    
    static String newMember = "";
    
    static Object[][] datos = new Object[1][11];
    
    static ArrayList<String> DevicesQueue = new ArrayList<String>();
    
    static ArrayList<String> servedDevices = new ArrayList<String>();
    
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
    		
    		Gen = (int)cadena[0].charAt(cadena[0].length()-1)- '0';
    		
    	} 
    	double MemDisp = Double.parseDouble(propiedades[5]) ;
    	double freqP = Double.parseDouble(propiedades[3]);
    	double mFreq = Double.parseDouble(propiedades[4]);;
    	int nNucleos = Integer.parseInt(propiedades[2]);
    	
    	
    	int rank= (int) ((150* MemDisp) + (nNucleos*(mFreq-freqP)*100)) + Gen*200;
    	
		return rank;
    	
    }
     
    private void actualizarTabla() { 
        modelo.setRowCount(0); 
        System.out.println("IP DEL SERVIDOR "+ ServerIp);

        // Agregamos los nuevos datos al modelo
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }
    
    static void arrancaEstres() {
    	System.out.println("-------- SE ARRANCA PRUEBAS DE ESTRES --------");
    }
   
    static ArrayList<String> activeDevices(){
    	ArrayList<String> onlineDevices = new ArrayList<String>();
    	
    	for(int i=0;i<datos.length;i++) {
    		if(!(datos[i][10].equals("Desconectado"))) {
    			onlineDevices.add(datos[i][1].toString());
    		}
    		
    	}
    	
    	System.out.println("Active Devices: "+ onlineDevices);
    	
    	return onlineDevices;
    }
    
    
    static void actualizarObjeto(String Buffer,InetAddress ip) {
    	String propiedades[] = Buffer.split("_");   
    	
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
			//Codigo del nuevo miembro
			/*
			CSVRead csv = new CSVRead();
			String netMember = ip.toString()+"_"+Buffer;
			csv.newNetMember(netMember);
			*/
			
			//newMember = ip.toString()+"_"+Buffer;
			
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
        	
        	msgBuilder();
        	activeDevices();
    	}else {
    		// Existe el cliente 
    		//Actualizar los elementos RanK-0 Frec Prom-6  FrecMax-7 - RamDIs Ram-8 - Ciente10 
    		
    		datos[flagIp][0] = rankGenerator(propiedades);
    		
    		datos[flagIp][3] = propiedades[0];
    		datos[flagIp][4] = propiedades[1]; 
    		datos[flagIp][5] = propiedades[2];
    		
    		datos[flagIp][9] = propiedades[6];
    		
    		//Idealmente estos son los que se mantienen
        	datos[flagIp][6] = propiedades[3];
        	datos[flagIp][7] = propiedades[4];  
        	datos[flagIp][8] = propiedades[5];
        	
        	//Funcion que determina si es servidor o no
        	if(datos[flagIp][1].equals("/"+ServerIp)) {
        		datos[flagIp][10] ="Servidor"; 
        	}else {
        		datos[flagIp][10] ="Cliente"; 
        	}
        			
    	}
    }
    
    static String msgBuilder() {
		String fnlStr = "";
		
		String separador = "_";
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < datos.length; i++) {
		    for (int j = 0; j < datos[i].length; j++) {
		        stringBuilder.append(datos[i][j]);
		        if (j < datos[i].length - 1) {
		            stringBuilder.append(separador);
		        }
		    }
		    if (i < datos.length - 1) {
		        stringBuilder.append(System.lineSeparator());
		    }
		}

		fnlStr = stringBuilder.toString();
		
		
		  
		DevicesQueue.add(fnlStr);
		return fnlStr;
	}
    
    static void startValues() { 
    	CSVRead csv = new CSVRead();
        
        ArrayList<String[]> DATA = csv.getValues(); 
        //Leemos los datos almacenados en el txt
        
        for(int w=0;w<DATA.size();w++) {
        	nCliente++;
        	String[] array = DATA.get(w);
        	//Añadimos al objeto de datos
        	Object[][] nuevoArreglo;
        	if(nCliente==1) {
        		nuevoArreglo = new Object[datos.length][datos[0].length]; 
        	}else {
        		nuevoArreglo = new Object[datos.length+1][datos[0].length]; 
        	}
        	
    		//Necesitamos Castear el tipo correcto 
    		Object[] nuevoObjeto = {"NA", array[1], nCliente, 
    				array[2],array[3],array[4],array[5],
    				array[6],array[7],array[8],"Desconectado"}; 
    		
    		for (int i = 0; i < datos.length; i++) {
        	    for (int j = 0; j < datos[i].length; j++) {
        	        nuevoArreglo[i][j] = datos[i][j];
        	    }
        	}
        	nuevoArreglo[nCliente-1] = nuevoObjeto;
        	datos = nuevoArreglo;	
        	
        } 
    }
    
    public static void start(String ip)  throws Exception{
        //Se arranca la tabla
    	
    	ServerIp = ip;
    	 
    	startValues();
    	
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
				System.out.println();
				
				InetAddress ipRecibida = s.getInetAddress();
				
                System.out.println("Se ingresaron datos desde la IP: " +ipRecibida);
                
                
                
                // enmascaro la entrada y salida de bytes
                ois = new ObjectInputStream( s.getInputStream() );
                
                oos = new ObjectOutputStream( s.getOutputStream() );
                
                //Se recibe la cadena objeto
                
                String paquete = (String) ois.readObject(); 
                
                //Actualizacion del objeto
                actualizarObjeto(paquete,ipRecibida);

                // envio el saludo al cliente
                //Acá es donde se va a enviar la nueva cadena
                
                String serverOutPut = "";
                
                if(DevicesQueue.size()>0) { 
                	ArrayList<String> devices = activeDevices();
                	
                	String ipR = ipRecibida.toString();
                	
                	if(devices.contains(ipR) && !(servedDevices.contains(ipR))) {
                		//se manda la instrucción 
                		//DevicesQueue.getIndex(0)
                		serverOutPut = DevicesQueue.get(0);
                		
                		//se agrega a dispositivos atendidos
                		servedDevices.add(ipRecibida.toString());
                	}
                	
                	if(devices.size() == servedDevices.size()) {
                		servedDevices.clear();
                		DevicesQueue.remove(0);
                	}
                	 
                } 
                
                oos.writeObject(serverOutPut);
                

            }catch(Exception ex){
            	// Disables error 
            	
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
