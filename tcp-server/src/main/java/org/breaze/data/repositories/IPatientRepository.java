package org.breaze.data.repositories;

import org.breaze.data.entities.Patient;
import org.breaze.common.exceptions.DuplicatePatientException;
import java.io.IOException;
import java.util.List;

public interface IPatientRepository {
    /**
     * Guarda un nuevo paciente
     * @throws DuplicatePatientException Si ID ya existe
     */
    void save(Patient p) throws DuplicatePatientException, IOException;

    /**
     * Busca un paciente por su documento de identidad
     */
    Patient findById(String id) throws IOException;

    /**
     * Retorna la lista todos los pacientes
     */
    List<Patient> findAll() throws IOException;
}