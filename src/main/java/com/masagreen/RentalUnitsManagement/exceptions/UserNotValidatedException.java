package com.masagreen.RentalUnitsManagement.exceptions;

public class UserNotValidatedException extends RuntimeException {
    public UserNotValidatedException(String message) {
        super(message);
    }
}
