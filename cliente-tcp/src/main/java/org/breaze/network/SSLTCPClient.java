package org.breaze.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SSLTCPClient implements IMessageService {
    private final ISSLConfig sslConfig;

    public SSLTCPClient(ISSLConfig sslConfig) {
        this.sslConfig = sslConfig;
    }

    @Override
    public String sendMessage(String message) {
        // Delegamos al SocketFactory
        try (Socket socket = SocketFactory.createSSLClientSocket(sslConfig, sslConfig)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            System.out.println("[TCP] Conectados a servidor %s en puerto %s".formatted(sslConfig.getHost(), sslConfig.getPort()));

            out.writeUTF(message);
            out.flush();
            System.out.println("[TCP] Mensaje enviado: %s".formatted(message));

            String response = in.readUTF();
            System.out.println("[TCP] Respuesta: %s".formatted(response));
            return response;

        } catch (UnknownHostException e) { // Quitamos el '_' para compatibilidad con versiones anteriores de Java
            System.out.println("[TCP] Error de host: no se encuentra el host: %s".formatted(sslConfig.getHost()));
            return "ERROR_HOST_DESCONOCIDO";
        } catch (IOException e) {
            System.out.println("[TCP] Error critico %s".formatted(e.getMessage()));
            return "ERROR_COMUNICACION";
        } catch (Exception e) {
            System.err.println("[TCP] Error critico del sistema: " + e.getMessage());
            return "ERROR_CRITICO";
        }
    }
}