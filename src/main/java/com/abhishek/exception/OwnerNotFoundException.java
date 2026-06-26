package com.abhishek.exception;

import java.io.Serial;

public class OwnerNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OwnerNotFoundException(String message) {
        super(message);
    }
}
