package mc;

public class Utils {
    public static int getRandom(int a,int b)
    {
        double valor;
        Double resultado;

        valor=Math.floor(Math.random()*(b-a+1)) + a;

        resultado=new Double(valor);

        return resultado.intValue();
    }
    
    public static double truncate(double datum, int places)
    {
        long idatum;
        double precision;
        
        precision=Math.pow(10, places);
        
        datum=datum*precision;
        idatum=Math.round(datum);
        datum=idatum/(precision*1.0);
        
        return datum;
    }
    
    public static void print(int[][] matriz)
    {
        int i, j;
        
        System.out.println();
        
        for(i=0;i<matriz.length;i++)
        {
            for(j=0;j<matriz.length;j++)
                System.out.print(Utils.truncate(matriz[i][j],2) + " ");
            
            System.out.println();
        }
        
        System.out.println();
    }
    
    public static void print(double[][] matriz)
    {
        int i, j;
        
        System.out.println();
        
        for(i=0;i<matriz.length;i++)
        {
            for(j=0;j<matriz.length;j++)
                System.out.print(Utils.truncate(matriz[i][j],2) + " ");
            
            System.out.println();
        }
        
        System.out.println();
    }

}