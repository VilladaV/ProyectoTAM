package org.breaze;

import org.breaze.common.IConfigReader;
import org.breaze.common.PropertiesManager;
import org.breaze.network.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        IConfigReader reader = new PropertiesManager("application.properties");
        ISSLConfig tcpConfig = new TCPConfig(reader);
        IMessageService client = new SSLTCPClient(tcpConfig);
        String response = client.sendMessage("Camila:Cortes");
        System.out.println("Respuesta: %s".formatted(response));

    }
}
