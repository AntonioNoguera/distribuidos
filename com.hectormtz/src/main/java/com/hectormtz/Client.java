package com.hectormtz;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.ProcessorIdentifier;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;


public class Client extends Thread {
	static String IP = "127.0.0.1";
	static int PORT = 5432;

	public Client(String ip) {
		Client.IP = ip;
	}

	public static double b_to_gb(long bytes) {
		long n = 1_000_000_000;
		return (0.0 + bytes) / n;
		
	}
	
	static boolean firstRun = true;
	static String stressFlag = "false";
	
	public static String[] getStaticInfo() {
		SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware(); 
        
        // RAM
        GlobalMemory memory = hal.getMemory();
        double totalMemory = b_to_gb(memory.getTotal());
        // CPU
        CentralProcessor processor = hal.getProcessor();
        
        int core_count = processor.getLogicalProcessorCount(); 
       
        // Info CPU
        ProcessorIdentifier cpu_info = processor.getProcessorIdentifier();
        
        String cpu_vendor = cpu_info.getVendor();
        String cpu_model = cpu_info.getName();        
        
		String info[] = {
			cpu_vendor,
			cpu_model,
			String.valueOf(core_count),
			String.valueOf(totalMemory), 
		};
	
		return info;
	}
	
	static double maxCPUFrec = 0;
	
	public static String[] getDynamicInfo() {
		SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware(); 
        
        // RAM
        GlobalMemory memory = hal.getMemory(); 
        double availableMemory = b_to_gb(memory.getAvailable());
        
        // CPU
        CentralProcessor processor = hal.getProcessor();
        
        //Max
        long [] frequency = processor.getCurrentFreq();
        
        double maxFreq = processor.getMaxFreq();
        double maxFrequency = maxFreq/(double)1000000000;
        
        if(maxFrequency>maxCPUFrec) {
        	maxCPUFrec = maxFrequency;
        }
        
        double promFreq=0;
        
        
        int core_count = processor.getLogicalProcessorCount();
        
        for(int i=0; i<frequency.length;i++){
        	promFreq += frequency[i]/core_count;
        }
        
        
        double f_ghz = promFreq/1000000000; 
        
		String info[] = { 
			String.valueOf(f_ghz),
			String.valueOf(maxCPUFrec),
			String.valueOf(availableMemory),
		};
	
		return info;
	}
	
	public static String[] joinInfo(String[] dInfo, String sInfo[]){
		
		String[] joinedInfo =  {sInfo[0], sInfo[1], sInfo[2],dInfo[0], 
								dInfo[1], dInfo[2], sInfo[3], stressFlag};
		
		return joinedInfo;
	}
	
	static String[] sInfo;
	static String[] dInfo;
	
    public void execute() {
    	if(firstRun) {
    		sInfo = getStaticInfo();
    		firstRun = false;
    	}
    	
    	dInfo = getDynamicInfo(); 
        
    	System.out.println("---------Client Side");
    	String[] info = joinInfo(dInfo,sInfo);
    	
        System.out.println(String.join("_", info)); 

		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		Socket s = null;
		
		try {
			// instantiate socket with ip and port
			s = new Socket(IP, PORT);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			long from = System.currentTimeMillis();
			// send info 
			oos.writeObject(String.join("_", info));
			
			
			String ret = (String)ois.readObject();
			
			if(ret.length()!=0) {
				CSVRead csv = new CSVRead(); 
				csv.updateNetFile(ret);
			}
			
			System.out.println("Mensaje Recibido: "+ ret);
			 
			
		} catch (Exception e) {
			System.err.println("Conexión Desde Cliente Fallida, intenta de nuevo...");
			//e.printStackTrace();
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
    
    private JFrame ventana; 
    private JPanel panel;
    
    public void MiVentana() {
    	ventana = new JFrame("Rankeo de Unidades - E2023 - Sistemas Distribuidos - Client Screen");
    	
    	panel = new JPanel(); 
        
        ventana.setSize(535, 350);
        ventana.setLocationRelativeTo(null);
        ventana.setSize(535, 40);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        JButton boton = new JButton("Haz clic para realizar prueba de estrés en el servidor");
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	stressFlag = "true";
            }
        });
        
        panel.add(boton);
         
        ventana.add(panel);
        
        ventana.setVisible(true);
    } 
    
    public void run() {
    	try {
    		MiVentana();
    		while (true) {
				execute();
				Thread.sleep(1000);
    		}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
