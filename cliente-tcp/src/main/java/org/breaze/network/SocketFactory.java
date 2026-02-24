package org.breaze.network;

import javax.net.ssl.*;
import java.io.FileInputStream;
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
        // AQUÍ EL CAMBIO: Usamos los nombres exactos de tu interfaz de cliente (TrustStore)
        try (FileInputStream fis = new FileInputStream(config.getTrustStorePath())) {
            trustStore.load(fis, config.getTrustStorePassword().toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}