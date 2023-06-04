package com.hectormtz;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CSVRead {
	
	public static ArrayList<String> res= new ArrayList<String>();
	
	public static File archivo = new File("NetIPs.txt"); 
	
	public  int getLength() {
		return res.size();
	}

	public ArrayList<String> read() { 
        
        try {
			Scanner lector = new Scanner(archivo);
			res.clear();
			
			while(lector.hasNextLine()) {
				String linea = lector.nextLine();
				String[] lineaEncontrada = linea.split("_"); 
				lineaEncontrada[1]=lineaEncontrada[1].replace("/","");
				res.add(lineaEncontrada[1]);
			}
			
			lector.close();
		}catch(FileNotFoundException e) {
			System.out.println("Error a la hora de encontrar el archivo");
			e.printStackTrace();
		} 
        
		return res;
    }
	
	public void updateNetFile(String newValue) {
		
		try {
            // Lee el contenido del archivo existente
            StringBuilder contenido = new StringBuilder(newValue);  
            
            // Escribe el contenido actualizado en el archivo
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo));
            escritor.write(newValue.toString());
            escritor.close();
             
        } catch (IOException e) {
            	System.out.println("Ocurrió un error al insertar la nueva línea en el archivo: " + e.getMessage());
        	}
	}
	
	public ArrayList<String[]> getValues(){
		ArrayList<String[]> objects = new ArrayList<String[]>();
		
		try {
			Scanner lector = new Scanner(archivo); 
			
			while(lector.hasNextLine()) {
				String linea = lector.nextLine();
				String[] lineaEncontrada = linea.split("_"); 
				objects.add(lineaEncontrada); 
			}
			 
			lector.close();
		}catch(FileNotFoundException e) {
			System.out.println("Error a la hora de encontrar el archivo");
			e.printStackTrace();
		} 
		
		return objects;
	}
	
	
	//Funcion que actualiza unicamente el nuevo miembro a todos los txt
	public void newNetMember(String newMember) { 
		 
		try {
            // Lee el contenido del archivo existente
            StringBuilder contenido = new StringBuilder(); 
            
            
            Scanner lector = new Scanner(archivo);  
			while(lector.hasNextLine()) {
				String linea = lector.nextLine();
                contenido.append(linea);
                contenido.append(System.lineSeparator());
            }
            lector.close();

            // Agrega la nueva línea al contenido
            contenido.append(newMember); 
             
            // Escribe el contenido actualizado en el archivo
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo));
            escritor.write(contenido.toString());
            escritor.close();
             
        } catch (IOException e) {
            	System.out.println("Ocurrió un error al insertar la nueva línea en el archivo: " + e.getMessage());
        	}
		}
	}
