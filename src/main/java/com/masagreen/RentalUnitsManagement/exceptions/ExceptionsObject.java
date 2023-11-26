package com.masagreen.RentalUnitsManagement.exceptions;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public record ExceptionsObject(Date timeStamp, Map<String, String> errorsMessages, int code) {

    public static final ExceptionsObject withSingleMessage(Date timeStamp, String errorMessage, int code) {
        return new ExceptionsObject(timeStamp, Collections.singletonMap("message", errorMessage), code);
    }

    public static final ExceptionsObject withManyMessages(Date timeStamp, Map<String, String> errorMessages, int code) {
        return new ExceptionsObject(timeStamp, errorMessages, code);
    }
}
