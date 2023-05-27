package act5;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerarNumeros {
    public static void main(String[] args) {
        int limite = 9_999_999;
        File numeros = new File("numeros.txt");
        try {
            numeros.createNewFile();
            FileWriter archivo = new FileWriter(numeros);
            for (int i = 0; i < limite - 1; i++) {
                int numero = (int)(100 * Math.random());
                archivo.append(numero + ",");
            }
            Random ran = new Random();
            int numero = ran.nextInt(100) + 1;
            archivo.append(Integer.toString(numero));
            archivo.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

