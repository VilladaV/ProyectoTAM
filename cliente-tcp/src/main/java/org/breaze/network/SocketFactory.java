package org.breaze.network;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class SocketFactory {
    public static SSLSocket createSSLClientSocket(ITCPConfig tcpConfig, ISSLConfig sslConfig) throws Exception {
        SSLContext context = createSSLContext(sslConfig);
        SSLSocketFactory factory = context.getSocketFactory();
        return (SSLSocket) factory.createSocket(tcpConfig.getHost(), tcpConfig.getPort());
    }

    private static SSLContext createSSLContext(ISSLConfig config) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        // AQUI CAMBIOOo! Usamos nombres exactos interfaz de cliente osea TrustStore
        // En el SocketFactory del CLIENTE
        try (InputStream is = SocketFactory.class.getClassLoader().getResourceAsStream(config.getTrustStorePath())) {
            if (is == null) {
                throw new FileNotFoundException("No se encontró el certificado: " + config.getTrustStorePath());
            }
            trustStore.load(is, config.getTrustStorePassword().toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}