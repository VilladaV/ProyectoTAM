package org.breaze.network;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

        //getResourceAsStream para buscar en carpeta resources
        try (InputStream is = SocketFactory.class.getClassLoader().getResourceAsStream(config.getKeyStorePath())) {
            if (is == null) {
                throw new FileNotFoundException("No se encontró el certificado: " + config.getKeyStorePath());
            }
            keyStore.load(is, config.getKeyStorePassword().toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, config.getKeyStorePassword().toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new java.security.SecureRandom());
        return sslContext;
    }
}