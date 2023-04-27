import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AppAyudante {

    public static int leerConfig(String[] filamentos, int[] precios) {
        int n = 0;
        try {
            Scanner scan = new Scanner(new File("config.txt"));
            n = Integer.parseInt(scan.nextLine());
            int cantFilamentos = 0;

            while (scan.hasNextLine()) {
                String[] partes = scan.nextLine().split(",");
                String filamento = partes[0];
                int precio = Integer.parseInt(partes[1]);

                filamentos[cantFilamentos] = filamento;
                precios[cantFilamentos++] = precio;
            }
            scan.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return n;
    }

    public static void leerPeriodos(int n, String[] filamentos,
        float[] metrosUsados, int[] precios) {
        try {
            int impresionesTotales = 0;
            float impresionesDetalle = 0;
            int costoTotal = 0;

            for (int i = 1; i <= n; i++) {
                File file = new File("P" + i + ".txt");
                Scanner scan = new Scanner(file);

                int tiempoTotal = 0;
                int tiempoMenor = 9999999;
                String idMenor = "";
                int tiempoMayor = -1;
                String idMayor = "";

                while (scan.hasNextLine()) {
                    String[] partes = scan.nextLine().split(",");
                    String idPedido = partes[0];
                    float metros = Float.parseFloat(partes[1]);
                    String detalleStr = partes[2];
                    String material = partes[3];
                    int indiceMaterial = buscarFilamento(filamentos, material);

                    if (i != -1) {
                        metrosUsados[indiceMaterial] += metros;
                    }

                    int tiempo;
                    if (detalleStr.equals("Verdadero")) {
                        tiempo = (int)(metros * 46);
                        impresionesDetalle++;
                    }
                    else {
                        tiempo = (int)(metros * 30);
                    }
                    tiempoTotal += tiempo;

                    if (tiempo > tiempoMayor) {
                        tiempoMayor = tiempo;
                        idMayor = idPedido;
                    }
                    if (tiempo < tiempoMenor) {
                        tiempoMenor = tiempo;
                        idMenor = idPedido;
                    }
                    impresionesTotales++;
                }
                System.out.println("\tInformacion Periodo " + i);
                System.out.println("Tiempo total: " + transformarMinutos(tiempoTotal));
                System.out.println("Impresion mas rapida: " 
                    + transformarMinutos(tiempoMenor));
                System.out.println("Impresion mas extensa: "
                    + transformarMinutos(tiempoMayor));
            }
            System.out.println("\nImpresiones totales: " + impresionesTotales);
            System.out.println("Porcentaje a detalle: " + 
                (impresionesDetalle / impresionesTotales) * 100 + " %");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void obtenerInfoFilamentos(String[] filamentos,
        float[] metrosUsados, int[] precios) {
            int costoTotal = 0;
            for (int i = 0; i < filamentos.length; i++) {
                if (filamentos[i] == null) {
                    break;
                }
                int costo = (int)(metrosUsados[i] * precios[i]);
                costoTotal += costo;

                System.out.println(filamentos[i] + ":");
                System.out.println("Metros usados: " + metrosUsados[i]);
                System.out.println("Costo: $ " + costo + "\n");
            }
            System.out.println("Costo total: $ " + costoTotal);
        }

    public static int buscarFilamento(String[] filamentos, String filamento) {
        for (int i = 0; i < filamentos.length; i++) {
            if (filamentos[i] == null) {
                break;
            }
            if (filamentos[i].equals(filamento)) {
                return i;
            }
        }
        return -1;
    }

    public static String transformarMinutos(int minutos) {
        String salida = "";
        if (minutos > 1440) {
            int dias = minutos / 1440;
            minutos -= (dias * 1440);
            salida += dias + " dias ";
        }
        if (minutos > 60) {
            int horas = minutos / 60;
            minutos -= (horas * 60);
            salida += horas + " horas ";
        }
        return salida + minutos + " minutos";
    }

    public static void main(String[] args) throws Exception {
        String[] filamentos = new String[100];
        int[] precios = new int[100];
        float[] metrosUsados = new float[100];

        int n = leerConfig(filamentos, precios);
        leerPeriodos(n, filamentos, metrosUsados, precios);
        obtenerInfoFilamentos(filamentos, metrosUsados, precios);
    }
}