package org.breaze;

import org.breaze.business.IMessageProcessor;
import org.breaze.business.NameProcessor;
import org.breaze.business.SimpleMessageProcessor;
import org.breaze.common.IConfigReader;
import org.breaze.common.PropertiesManager;
import org.breaze.network.INetworkService;
import org.breaze.network.ITCPConfig;
import org.breaze.network.TCPConfig;
import org.breaze.network.TCPServer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        IConfigReader reader = new PropertiesManager("application.properties");
        ITCPConfig tcpConfig = new TCPConfig(reader);
        IMessageProcessor processor = new NameProcessor();
        INetworkService server = new TCPServer(tcpConfig, processor);
        server.start();
    }
}
