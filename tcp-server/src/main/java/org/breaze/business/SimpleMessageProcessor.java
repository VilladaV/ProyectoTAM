package org.breaze.business;

import org.breaze.data.entities.Patient;
import org.breaze.data.entities.DNASample;
import org.breaze.data.repositories.IPatientRepository;
import org.breaze.common.exceptions.DuplicatePatientException;
import java.io.IOException;

public class SimpleMessageProcessor implements IMessageProcessor {

    private final GenomicService genomicService;
    private final IPatientRepository patientRepo;

    // Inyección de dependencias por constructor (SOLID)
    public SimpleMessageProcessor(GenomicService genomicService, IPatientRepository patientRepo) {
        this.genomicService = genomicService;
        this.patientRepo = patientRepo;
    }

    @Override
    public String process(String message) {
        if (message == null || message.isEmpty()) {
            return "ERROR: Mensaje vacío";
        }

        String[] parts = message.split(";");
        String command = parts[0].toUpperCase();

        try {
            switch (command) {
                case "REG": // Formato: REG;id;nombre;apellido;edad;email;genero;ciudad;pais
                    return registerPatient(parts);

                case "FIND_PATIENT": // Formato: FIND_PATIENT;id
                    Patient p = patientRepo.findById(parts[1]);
                    return (p != null) ? "SUCCESS;" + p.toString() : "ERROR: No encontrado";

                case "DNA": // Formato: DNA;id;fecha;secuencia
                    DNASample sample = new DNASample(parts[1], parts[2], parts[3]);
                    genomicService.analyzeDNA(sample);
                    return "SUCCESS: Análisis completado y guardado.";

                case "REPORT": // Formato: REPORT
                    return "SUCCESS;" + genomicService.getHighRiskReport();

                default:
                    return "ERROR: Comando no reconocido";
            }
        } catch (DuplicatePatientException e) {
            return "ERROR: " + e.getMessage();
        } catch (Exception e) {
            return "ERROR: Error interno del servidor: " + e.getMessage();
        }
    }

    private String registerPatient(String[] p) throws DuplicatePatientException, IOException {
        // Validación mínima de campos
        if (p.length < 9) return "ERROR: Datos insuficientes para registro";

        Patient newPatient = new Patient(p[1], p[2], p[3], Integer.parseInt(p[4]), p[5], p[6], p[7], p[8]);
        patientRepo.save(newPatient);
        return "SUCCESS: Paciente registrado correctamente";
    }
}