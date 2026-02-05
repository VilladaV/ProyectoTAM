package org.breaze;

import org.breaze.common.IConfigReader;
import org.breaze.common.PropertiesManager;
import org.breaze.network.IMessageService;
import org.breaze.network.ITCPConfig;
import org.breaze.network.TCPClient;
import org.breaze.network.TCPConfig;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        IConfigReader reader = new PropertiesManager("application.properties");
        ITCPConfig tcpConfig = new TCPConfig(reader);
        IMessageService client = new TCPClient(tcpConfig);
        String response = client.sendMessage("Simon:Gaviria");
        System.out.println("Respuesta: %s".formatted(response));

    }
}
