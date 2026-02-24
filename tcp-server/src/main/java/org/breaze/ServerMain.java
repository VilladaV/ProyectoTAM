package org.breaze;

import org.breaze.common.PropertiesManager;
import org.breaze.network.TCPConfig;
import org.breaze.network.SSLTCPServer;
import org.breaze.business.GenomicProcessor;
import org.breaze.business.SimpleMessageProcessor;
import org.breaze.data.repositories.*;

public class ServerMain {
    public static void main(String[] args) {
        try {
            //  Carga Configuración
            PropertiesManager props = new PropertiesManager("application.properties");
            TCPConfig config = new TCPConfig(props);

            // Inicializar Persistencia rchivos
            IPatientRepository patientRepo = new CSVPatientRepository("pacientes.csv");
            IDiseaseRepository diseaseRepo = new FastaDiseaseRepository("data/diseases");
            IDiagnosticRepository diagnosticRepo = new CSVDiagnosticRepository("data/diagnostics");

            // Inicializar Lógica de Negocio
            GenomicProcessor genomicProcessor = new GenomicProcessor(patientRepo, diseaseRepo, diagnosticRepo);
            SimpleMessageProcessor messageProcessor = new SimpleMessageProcessor(genomicProcessor, patientRepo);

            // Arrancar Servidor SSL
            SSLTCPServer server = new SSLTCPServer(config, messageProcessor);
            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}