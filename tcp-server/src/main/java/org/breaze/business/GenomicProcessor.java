package org.breaze.business;

import org.breaze.data.entities.DNASample;
import org.breaze.data.entities.Patient;
import org.breaze.data.entities.Virus;
import org.breaze.data.repositories.IDiagnosticRepository;
import org.breaze.data.repositories.IDiseaseRepository;
import org.breaze.data.repositories.IPatientRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenomicProcessor implements GenomicService {
    private final IPatientRepository patientRepo;
    private final IDiseaseRepository diseaseRepo;
    private final IDiagnosticRepository diagnosticRepo;

    public GenomicProcessor(IPatientRepository patientRepo, IDiseaseRepository diseaseRepo, IDiagnosticRepository diagnosticRepo) {
        this.patientRepo = patientRepo;
        this.diseaseRepo = diseaseRepo;
        this.diagnosticRepo = diagnosticRepo;
    }

    @Override
    public void analyzeDNA(DNASample sample) throws IOException {
        List<Virus> allVirus = diseaseRepo.findAllVirus();
        List<String> findings = new ArrayList<>();
        String dna = sample.getSequence();

        for (Virus v : allVirus) {
            int index = dna.indexOf(v.getSequence());
            while (index != -1) {
                // virus, posicion_inicio, posicion_fin
                findings.add(v.getName() + "," + index + "," + (index + v.getSequence().length()));
                index = dna.indexOf(v.getSequence(), index + 1);
            }
        }
        diagnosticRepo.saveResult(sample.getPatientId(), findings);
    }

    @Override
    public void checkMutations(DNASample sample) throws IOException {
        // Obtiene diagnósticos previos para comparar cambios en patrones
        List<String> history = diagnosticRepo.getHistory(sample.getPatientId());
        if (history.isEmpty()) return;

        // Compara si los virus detectados antes siguen en las mismas posiciones
        // o si hay nuevos rangos de cambios.
        System.out.println("Comparando muestra de fecha " + sample.getDate() + " con historial...");
    }

    @Override
    public String getHighRiskReport() throws IOException {
        StringBuilder report = new StringBuilder("Documento,Cant_Virus,Cant_Altamente_Infecciosos,Normales,Altos\n");
        List<Patient> patients = patientRepo.findAll();
        List<Virus> catalog = diseaseRepo.findAllVirus();

        for (Patient p : patients) {
            List<String> history = diagnosticRepo.getHistory(p.getId());
            int totalVirus = history.size();
            int highRiskCount = 0;
            List<String> lowNames = new ArrayList<>();
            List<String> highNames = new ArrayList<>();

            for (String entry : history) {
                String virusName = entry.split(",")[0];
                Virus v = catalog.stream().filter(vir -> vir.getName().equals(virusName)).findFirst().orElse(null);

                if (v != null && v.isHighlyInfectious()) {
                    highRiskCount++;
                    highNames.add(virusName);
                } else {
                    lowNames.add(virusName);
                }
            }

            // Condición: más de 3 virus "Altamente Infeccioso"
            if (highRiskCount > 3) {
                report.append(String.format("%s,%d,%d,\"%s\",\"%s\"\n",
                        p.getId(), totalVirus, highRiskCount,
                        String.join(";", lowNames), String.join(";", highNames)));
            }
        }
        return report.toString();
    }

    // Método auxiliar
    private Match findVirusInSequence(String dna, String virusDna) {
        int start = dna.indexOf(virusDna);
        if (start != -1) {
            return new Match(start, start + virusDna.length());
        }
        return null;
    }

    // Clase interna para representar una coincidencia genómica
    private static class Match {
        int start;
        int end;
        Match(int s, int e) { this.start = s; this.end = e; }
    }
}