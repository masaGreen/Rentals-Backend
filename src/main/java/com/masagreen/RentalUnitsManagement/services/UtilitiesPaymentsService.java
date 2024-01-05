package com.masagreen.RentalUnitsManagement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.utils.UtilsReqDto;
import com.masagreen.RentalUnitsManagement.models.entities.UtilitiesPayments;
import com.masagreen.RentalUnitsManagement.repositories.UtilitiesPaymentsRepository;
import com.masagreen.RentalUnitsManagement.utils.ProcessDownloadResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UtilitiesPaymentsService {
    private final UtilitiesPaymentsRepository utilitiesPaymentsRepository;

    public List<UtilitiesPayments> findAllByUnitNumber(String unitNumber) {
        return utilitiesPaymentsRepository.findAllByUnitNumber(unitNumber);
    }

    public List<UtilitiesPayments> findAllUtilitiesPayments() {

        return utilitiesPaymentsRepository.findAll();

    }

    public List<UtilitiesPayments> findByStatus(String status) {

        return utilitiesPaymentsRepository.findAllByStatus(status);

    }

    public CommonResponseMessageDto saveUtilitiesPayments(UtilsReqDto utilsReqDto) {
        // does the unit have some pending payments
        Optional<UtilitiesPayments> lastUtilPayment = utilitiesPaymentsRepository
                .findByUnitNumber(utilsReqDto.unitNumber());

        if (lastUtilPayment.isPresent()) {
            // pending bal /overpayment
            double bill = Double.parseDouble(lastUtilPayment.get().getCarriedForward());
            double newCarriedForward = Double.parseDouble(utilsReqDto.amountPaid()) + bill;
            double carriedForward = newCarriedForward -
                    (Double.parseDouble(utilsReqDto.garbage()) + Double.parseDouble(utilsReqDto.waterBill()));
            String status = carriedForward >= 0 ? "paid" : "unpaid";

            // update carriedForward, and/or status if it changes for lastutil
            lastUtilPayment.get().setCarriedForward(String.valueOf(newCarriedForward));
            lastUtilPayment.get().setStatus(newCarriedForward >= 0 ? "paid" : "unpaid");
            utilitiesPaymentsRepository.save(lastUtilPayment.get());
            // new util entry
            UtilitiesPayments utilitiesPayments = UtilitiesPayments.builder()
                    .garbage(utilsReqDto.garbage())
                    .waterBill(utilsReqDto.waterBill())
                    .amountPaid(utilsReqDto.amountPaid())
                    .carriedForward(String.valueOf(carriedForward))
                    .date(LocalDateTime.now())
                    .unitNumber(utilsReqDto.unitNumber())
                    .status(status)
                    .build();
            utilitiesPaymentsRepository.save(utilitiesPayments);
            return CommonResponseMessageDto.builder().message("successfully saved").build();

        } else {

            Double carriedForward = Double.parseDouble(utilsReqDto.amountPaid()) - (Double.parseDouble(
                    utilsReqDto.garbage()) + Double.parseDouble(utilsReqDto.waterBill()));

            System.out.println(-20);
            String status = carriedForward >= 0 ? "paid" : "unpaid";

            UtilitiesPayments utilitiesPayments = UtilitiesPayments.builder()
                    .garbage(utilsReqDto.garbage())
                    .waterBill(utilsReqDto.waterBill())
                    .amountPaid(utilsReqDto.amountPaid())
                    .carriedForward(String.valueOf(carriedForward))
                    .date(LocalDateTime.now())
                    .unitNumber(utilsReqDto.unitNumber())
                    .status(status)
                    .build();
            utilitiesPaymentsRepository.save(utilitiesPayments);

            return CommonResponseMessageDto.builder().message("successfully saved").build();
        }

    }

    public String deleteUtility(String id) {

        utilitiesPaymentsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("not found"));
        utilitiesPaymentsRepository.deleteById(id);
        return "success";

    }

    public byte[] handleAllUtilsDownloads(HttpServletResponse response) {

        List<UtilitiesPayments> allUtils = findAllUtilitiesPayments();
        // add headers
        ProcessDownloadResponse.processResponse(response);

        try {
            return generate("AllUtilitiesPayments", allUtils);

        } catch (DocumentException | IOException e) {
            log.error("error processing utilities download {}", e.getMessage());
            return null;
        }
    }

    public byte[] handleUtilsWithPendingBills(HttpServletResponse response) {

        List<UtilitiesPayments> allUtilsWithPendingBills = utilitiesPaymentsRepository.findAllByStatus("unpaid");

        ProcessDownloadResponse.processResponse(response);
        try {
            return generate("AllUtilities with Pending Payments", allUtilsWithPendingBills);

        } catch (Exception e) {
            log.info("error processing utils-with-pending-bills downloads {}", e.getCause());
            return null;
        }

    }

    public byte[] handleAllUtilsForSingleUnit(HttpServletResponse response, String unitNumber) {

        List<UtilitiesPayments> allUtilsByUnitNumber = utilitiesPaymentsRepository.findAllByUnitNumber(unitNumber);

        ProcessDownloadResponse.processResponse(response);
        try {
            return generate("All UtilitiesPayments for " + unitNumber, allUtilsByUnitNumber);

        } catch (Exception e) {
            log.info("error processing utils-payments-for single unit {}", e.getCause());
            return null;
        }

    }

    private byte[] generate(String title, List<UtilitiesPayments> utilitiesPaymentsList)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create a PdfWriter to write the document to the ByteArrayOutputStream

        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(20);

        Paragraph paragraph = new Paragraph("List Of " + title, fontTitle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(7);

        table.setWidthPercentage(100f);
        table.setWidths(new int[]{2, 2, 2, 2, 2, 2, 2});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.gray);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(CMYKColor.green);
        font.setSize(12);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("AmountPaid", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Garbage", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("UnitNumber", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("WaterBill", font));
        table.addCell(cell);

        // Iterating over the list of utils
        for (UtilitiesPayments utilitiesPayment : utilitiesPaymentsList) {

            table.addCell(String.valueOf(utilitiesPayment.getId()));
            table.addCell(utilitiesPayment.getAmountPaid());
            table.addCell(String.valueOf(utilitiesPayment.getDate()));
            table.addCell(utilitiesPayment.getGarbage());
            table.addCell(utilitiesPayment.getStatus());
            table.addCell(utilitiesPayment.getUnitNumber());
            table.addCell(utilitiesPayment.getWaterBill());

        }

        document.add(table);
        document.close();
        writer.close();
        return byteArrayOutputStream.toByteArray();
    }

}
