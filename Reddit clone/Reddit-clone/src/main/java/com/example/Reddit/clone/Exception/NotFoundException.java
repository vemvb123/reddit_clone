package com.example.Reddit.clone.Exception;

import java.util.Locale;

public class NotFoundException extends RuntimeException {

    public NotFoundException(NotFound notFound) {
        super(notFound.toString().toLowerCase(Locale.ROOT).replace("_", " ") + " not found");
    }

}
