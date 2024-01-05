package com.masagreen.RentalUnitsManagement.utils;


import jakarta.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ProcessDownloadResponse {

    public static HttpServletResponse processResponse(HttpServletResponse response) {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" +
                currentDateTime + UUID.randomUUID().toString().substring(5) + ".pdf";
        response.setHeader(headerKey, headerValue);
        return response;
    }
    // public static byte[] convertPdfToByteArray(File pdfFile) throws IOException {
    //     try (FileInputStream fis = new FileInputStream(pdfFile);
    //          ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

    //         byte[] buffer = new byte[1024];
    //         int bytesRead;

    //         while ((bytesRead = fis.read(buffer)) != -1) {
    //             bos.write(buffer, 0, bytesRead);
    //         }

    //         return bos.toByteArray();
    //     }
    // }

}
