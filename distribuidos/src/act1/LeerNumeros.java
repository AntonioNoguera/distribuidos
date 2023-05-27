package act1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeerNumeros {
	int BUFFER = 100_000_000;
	String path = "numeros.txt";
	String line = "";
	int[] numeros = new int[BUFFER];
	int index = 0;
	
	public int[] getNumeros() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while((line = br.readLine()) != null) {
				String[] values = line.split(",");
				for (int i = 0; i < values.length; i++) {
					numeros[index++] = Integer.parseInt(values[i]);
				}
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numeros;
	}
	
	public int getLength() {
		return index;
	}
}
