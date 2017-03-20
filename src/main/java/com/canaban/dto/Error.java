package com.canaban.dto;

import lombok.Data;

/**
 * Created by antongusev on 18.03.17.
 */
@Data
public class Error {

    private String message;

    public Error(String message) {
        this.message = message;
    }

}
