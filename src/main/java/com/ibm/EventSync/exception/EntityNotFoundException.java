package com.ibm.EventSync.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends MainException {

    public EntityNotFoundException(String message) {
        super(message, "Entity Not Found Exception", HttpStatus.NOT_FOUND);
    }
}
