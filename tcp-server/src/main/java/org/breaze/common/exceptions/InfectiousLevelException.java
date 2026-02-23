package org.breaze.common.exceptions;

public class InfectiousLevelException extends RuntimeException {
    public InfectiousLevelException(String level) {
        super("Nivel de infecciosidad no válido: " + level);
    }
}
