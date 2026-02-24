package org.breaze.main;

import org.breaze.common.PropertiesManager;
import org.breaze.network.TCPConfig;
import org.breaze.network.SSLTCPClient;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientMain {
    private static SSLTCPClient client;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            //Cargar configuración de cliente
            // En lugar de pasar la ruta como String
            PropertiesManager props = new PropertiesManager("application.properties");
            TCPConfig config = new TCPConfig(props);
            client = new SSLTCPClient(config);

            int option;
            do {
                printMenu();
                option = Integer.parseInt(sc.nextLine());
                executeOption(option);
            } while (option != 0);

        } catch (Exception e) {
            System.err.println("Error al arrancar el cliente: " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n--- BIOGUARD: VIGILANCIA GENÓMICA ---");
        System.out.println("1. Registrar nuevo paciente");
        System.out.println("2. Consultar paciente por ID");
        System.out.println("3. Analizar muestra de ADN Archivo FASTA");
        System.out.println("4. Generar reporte de alto riesgo");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void executeOption(int option) {
        try {
            switch (option) {
                case 1:
                    registrar();
                    break;
                case 2:
                    consultar();
                    break;
                case 3:
                    analizarADN();
                    break;
                case 4:
                    System.out.println(client.sendMessage("REPORT"));
                    break;
                case 0:
                    System.out.println("Cerrando sistema...");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error en la operación: " + e.getMessage());
        }
    }

    private static void registrar() {
        System.out.println("Ingrese datos (ID;Nombre;Apellido;Edad;Email;Género;Ciudad;País):");
        String datos = sc.nextLine();
        // Enviamos el comando REG seguido de los datos
        String response = client.sendMessage("REG;" + datos);
        System.out.println("Respuesta del servidor: " + response);
    }

    private static void consultar() {
        System.out.print("Ingrese el ID del paciente: ");
        String id = sc.nextLine();
        String response = client.sendMessage("FIND_PATIENT;" + id);
        System.out.println("Datos: " + response);
    }

    private static void analizarADN() throws IOException {
        System.out.print("Ingrese ID del paciente: ");
        String id = sc.nextLine();
        System.out.print("Ingrese fecha (AAAA-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("Ingrese la ruta del archivo FASTA del paciente: ");
        String path = sc.nextLine();

        // Leemos el archivo FASTA localment
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Error: El archivo no existe.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // altamos el encabezado del FASTA
            String secuencia = br.readLine(); // Leemos la secuencia ATCG

            // Enviamos  comando DNA;id;fecha;secuencia
            String response = client.sendMessage("DNA;" + id + ";" + fecha + ";" + secuencia);
            System.out.println("Respuesta: " + response);
        }
    }
}
