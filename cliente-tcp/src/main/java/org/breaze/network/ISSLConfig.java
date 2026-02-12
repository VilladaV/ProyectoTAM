package org.breaze.network;

public interface ISSLConfig extends ITCPConfig {
    String getTrustStorePath();
    String getTrustStorePassword();
}
