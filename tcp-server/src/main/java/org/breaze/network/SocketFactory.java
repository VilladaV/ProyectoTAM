package org.breaze.network;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class SocketFactory {
    public static SSLServerSocket createSSLServerSocket(ITCPConfig tcpConfig, ISSLConfig sslConfig) throws Exception {
        SSLContext context = createSSLContext(sslConfig);
        SSLServerSocketFactory factory = context.getServerSocketFactory();
        return (SSLServerSocket) factory.createServerSocket(tcpConfig.getPort());
    }

    private static SSLContext createSSLContext(ISSLConfig config) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // AQUÍ EL CAMBIO: Usamos los nombres exactos de tu interfaz de servidor
        try (FileInputStream fis = new FileInputStream(config.getKeyStorePath())) {
            keyStore.load(fis, config.getKeyStorePassword().toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, config.getKeyStorePassword().toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
        return sslContext;
    }
}