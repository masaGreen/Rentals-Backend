package com.masagreen.RentalUnitsManagement.exceptions;

public class DeletionNotAllowedCurrentlyException extends RuntimeException {
    public DeletionNotAllowedCurrentlyException(String messase) {
        super(messase);
    }
}
