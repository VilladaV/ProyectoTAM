package org.breaze.network;

import org.breaze.business.IMessageProcessor;
import java.io.*;
import java.net.Socket;

/**
 * Gestiona la comunicación individual con un cliente en un hilo separado.
 * Cumple con el requisito de hilos nativos sin librerías externas.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final IMessageProcessor processor;

    /**
     * @param socket El socket (SSL) ya aceptado por el servidor.
     * @param processor El procesador de mensajes para delegar la lógica de negocio.
     */
    public ClientHandler(Socket socket, IMessageProcessor processor) {
        this.clientSocket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        // Uso de try-with-resources para asegurar el cierre de flujos
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            // El servidor escucha peticiones del cliente hasta que este cierre la conexión
            while ((inputLine = in.readLine()) != null) {
                // Inversión de Dependencias: ClientHandler no sabe QUÉ hace el proceso, solo delega
                String response = processor.process(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            // Aquí podrías usar tus excepciones personalizadas si el error es de lógica
            System.err.println("Error de comunicación en hilo: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("No se pudo cerrar el socket del cliente: " + e.getMessage());
        }
    }
}
