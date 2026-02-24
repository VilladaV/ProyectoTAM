package org.breaze.network;

import org.breaze.business.IMessageProcessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SSLTCPServer implements INetworkService {
    private final ISSLConfig sslConfig;
    private final IMessageProcessor processor;

    public SSLTCPServer(ISSLConfig sslConfig, IMessageProcessor processor) {
        this.sslConfig = sslConfig;
        this.processor = processor;
    }

    @Override
    public void start() {
        //
        try (ServerSocket serverSocket = SocketFactory.createSSLServerSocket(sslConfig, sslConfig)) {
            System.out.println("[Server] Escuchando de forma segura en el puerto: " + sslConfig.getPort());

            while (true) {
                // Acepta al cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] Nuevo cliente conectado.");

                //  Hilos nativos para concurrencia
                ClientHandler handler = new ClientHandler(clientSocket, processor);
                Thread clientThread = new Thread(handler);
                clientThread.start(); // El hilo atiende i el server vuelve a escuchar arriba
            }
        } catch (IOException e) {
            System.out.println("[Server] Error critico de E/S: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[Server] Error critico de seguridad SSL: " + e.getMessage());
        }
    }
}
