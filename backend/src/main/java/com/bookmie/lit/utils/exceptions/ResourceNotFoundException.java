package com.bookmie.lit.utils.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String resource, String id) {
        super(resource + " with id '" + id + "' not found", HttpStatus.NOT_FOUND);
    }
}
