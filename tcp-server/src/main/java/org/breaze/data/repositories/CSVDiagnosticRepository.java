package org.breaze.data.repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDiagnosticRepository implements IDiagnosticRepository {
    private final String baseDir; // Directorio raíz donde estarán las carpetas de pacientes

    public CSVDiagnosticRepository(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void saveResult(String id, List<String> results) throws IOException {
        // 1. Crear la ruta de la carpeta del paciente si no existe
        File patientFolder = new File(baseDir, id);
        if (!patientFolder.exists()) {
            patientFolder.mkdirs();
        }

        // 2. Definir el archivo de diagnóstico dentro de esa carpeta
        // Se puede usar un nombre fijo o basado en la fecha para no sobrescribir
        File diagnosticFile = new File(patientFolder, "diagnostico.csv");

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(diagnosticFile, true)))) {
            for (String line : results) {
                // Formato requerido: virus, posicion_inicio, posicion_fin [cite: 220]
                out.println(line);
            }
        }
    }

    @Override
    public List<String> getHistory(String id) throws IOException {
        List<String> history = new ArrayList<>();
        File diagnosticFile = new File(baseDir + File.separator + id, "diagnostico.csv");

        if (!diagnosticFile.exists()) {
            return history;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(diagnosticFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                history.add(line);
            }
        }
        return history;
    }
}