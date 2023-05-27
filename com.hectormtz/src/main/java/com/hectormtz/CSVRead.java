package com.hectormtz;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class CSVRead {
	public static int datos = 0;
	
	public  int getLength() {
		return datos;
	}

	public  String[] read() {
	        String csvFilePath = "pcs.csv";

	        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
	            List<String[]> dataRows = reader.readAll();
	            String[] res = new String[5];

	            for (String[] row : dataRows) {
	                // Process each row of data
	                for (String cell : row) {
	                	res[datos] = cell;
	                	datos++;
	                }
	            }
	            return res;
	        } catch (IOException | CsvException e) {
	            e.printStackTrace();
	        }
			return null;
	    }	

}
