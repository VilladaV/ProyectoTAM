package org.breaze.common.exceptions;

public class DNASourcingException extends RuntimeException {
    public DNASourcingException(String message) {
        super("Error en la fuente de ADN: " + message);
    }
}
