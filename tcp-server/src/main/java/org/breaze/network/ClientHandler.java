package org.breaze.network;

import org.breaze.business.IMessageProcessor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final IMessageProcessor processor;

    public ClientHandler(Socket socket, IMessageProcessor processor) {
        this.clientSocket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        // Us DataInput/DataOutput
        try (
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            // Lee mensaje del cliente
            String clientMessage = in.readUTF();

            //  procesa
            String response = processor.process(clientMessage);

            // Envia la respuesta
            out.writeUTF(response);
            out.flush();

        } catch (IOException e) {
            System.err.println("[Handler] Error de comunicación con cliente: " + e.getMessage());
        } finally {
            try {
                if (!clientSocket.isClosed()) clientSocket.close();
            } catch (IOException e) {
                System.err.println("[Handler] Error cerrando socket: " + e.getMessage());
            }
        }
    }
}