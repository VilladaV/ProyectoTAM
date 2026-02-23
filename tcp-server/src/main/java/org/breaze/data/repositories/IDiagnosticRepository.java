package org.breaze.data.repositories;

import java.io.IOException;
import java.util.List;

public interface IDiagnosticRepository {
    /**
     * Guarda el resultado de diagnóstico en la carpeta del paciente
     * @param id Identificador de paciente
     * @param results Lista de hallazgos (virus detectado y posicion).
     */
    void saveResult(String id, List<String> results) throws IOException;

    /**
     * Obtiene el historial de diagnósticos previo
     */
    List<String> getHistory(String id) throws IOException;
}