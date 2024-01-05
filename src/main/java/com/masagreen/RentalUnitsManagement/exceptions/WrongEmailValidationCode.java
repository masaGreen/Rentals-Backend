package com.masagreen.RentalUnitsManagement.exceptions;

public class WrongEmailValidationCode extends RuntimeException {
    public WrongEmailValidationCode(String message) {
        super(message);
    }
}
