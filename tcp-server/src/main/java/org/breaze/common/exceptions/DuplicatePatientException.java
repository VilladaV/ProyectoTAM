package org.breaze.common.exceptions;

public class DuplicatePatientException extends RuntimeException {
    public DuplicatePatientException(String id) {
        super("Error: El paciente con ID " + id + " ya se encuentra registrado.");
    }
}
