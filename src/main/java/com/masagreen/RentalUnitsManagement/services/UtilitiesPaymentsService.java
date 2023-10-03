package com.masagreen.RentalUnitsManagement.services;


import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.masagreen.RentalUnitsManagement.models.UtilitiesPayments;
import com.masagreen.RentalUnitsManagement.repositories.UtilitiesPaymentsRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UtilitiesPaymentsService {
    @Autowired
    private UtilitiesPaymentsRepository utilitiesPaymentsRepository;
    @Autowired
    private UnitService unitService;

    public void generate(HttpServletResponse res, String title, List<UtilitiesPayments> utilitiesPaymentsList) throws DocumentException, IOException {


        Document document = new Document(PageSize.A4);


        PdfWriter.getInstance(document, res.getOutputStream());

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
    }
        public List<UtilitiesPayments> getAllUtils() {
        return utilitiesPaymentsRepository.findAll();

    }

    public Optional< UtilitiesPayments> findUtilityById(String id){
        return  utilitiesPaymentsRepository.findById(Long.parseLong(id));
    }

    public List<UtilitiesPayments> findByUnitNumber(String unitNumber) {
        return  utilitiesPaymentsRepository.findAllByUnitNumber(unitNumber);
    }

    public List<UtilitiesPayments> findByStatus(String status) {
        return utilitiesPaymentsRepository.findAllByStatus(status);
    }
    public UtilitiesPayments saveUtilitiesPayments(UtilitiesPayments utilitiesPayments){

       return utilitiesPaymentsRepository.save(utilitiesPayments);
    }

    public String deleteUtility(String id) {
        Optional<UtilitiesPayments> utilitiesPayment = utilitiesPaymentsRepository.findById(Long.parseLong(id));

        if (utilitiesPayment.isPresent()){
            utilitiesPayment.get().getUnit().setStatus(false);
            unitService.saveUnit(utilitiesPayment.get().getUnit());
            utilitiesPaymentsRepository.deleteById(Long.parseLong(id));
            return null;
        }
        return "id doesn't exist";
    }
}
