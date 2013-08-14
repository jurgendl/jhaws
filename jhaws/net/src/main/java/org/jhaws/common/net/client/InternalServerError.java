package org.jhaws.common.net.client;

/**
 * InternalServerError
 */
public class InternalServerError extends RuntimeException {
    private static final long serialVersionUID = 8516457376143083141L;

    public InternalServerError(String message) {
        super(message);
    }
}
