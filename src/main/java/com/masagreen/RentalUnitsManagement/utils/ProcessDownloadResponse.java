package com.masagreen.RentalUnitsManagement.utils;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.UUID;


import jakarta.servlet.http.HttpServletResponse;

public class ProcessDownloadResponse {

    public static HttpServletResponse  processResponse(HttpServletResponse response){
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" +
                currentDateTime+ UUID.randomUUID().toString().substring(5) + ".pdf";
        response.setHeader(headerKey, headerValue);
        return response;
    }

}
