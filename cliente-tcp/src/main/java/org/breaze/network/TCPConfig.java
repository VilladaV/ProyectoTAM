package org.breaze.network;

import org.breaze.common.IConfigReader;

public class TCPConfig implements ITCPConfig{
    private final IConfigReader configReader;

    public TCPConfig(IConfigReader configReader){
        this.configReader = configReader;
    }
    @Override
    public String getHost() {
        return configReader.getString("server.address");
    }

    @Override
    public int getPort() {
        return configReader.getInt("server.port");
    }
}
