package com.masagreen.RentalUnitsManagement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.masagreen.RentalUnitsManagement.models.Unit;
import com.masagreen.RentalUnitsManagement.repositories.UnitRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Service
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;
    public Unit saveUnit(Unit unit) {

        Optional<Unit> foundUnit = unitRepository.findByUnitNumber(unit.getUnitNumber());
        if(foundUnit.isEmpty() ){
             return unitRepository.save(unit);
        }
        return null;

    }
    public void generate(HttpServletResponse res, String title, List<Unit> units) throws DocumentException, IOException {


        Document document = new Document(PageSize.A4);


        PdfWriter.getInstance(document, res.getOutputStream());

        document.open();


        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(20);

        Paragraph paragraph = new Paragraph("List Of " + title, fontTitle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(5);


        table.setWidthPercentage(100f);
        table.setWidths(new int[]{2, 2, 2, 2,2});
        table.setSpacingBefore(5);


        PdfPCell cell = new PdfPCell();


        cell.setBackgroundColor(CMYKColor.gray);
        cell.setPadding(5);


        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(CMYKColor.green);
        font.setSize(12);
        //first titles row
        cell.setPhrase(new Phrase("Plotname", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Unit No.", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Tag", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Rent(Ksh)", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("IsOccupied", font));
        table.addCell(cell);


        // Iterating over the list of utils
        for (Unit unit: units) {

            table.addCell(unit.getPlotName());
            table.addCell(unit.getUnitNumber());
            table.addCell(unit.getTag());
            table.addCell(String.valueOf(unit.getRent()));
            table.addCell(String.valueOf(unit.isStatus()));

        }

        document.add(table);
        document.close();
    }
    public Optional<Unit> findByUnitNumber(String unitNumber){
        return  unitRepository.findByUnitNumber(unitNumber);
    }

    public List<Unit> findAllUnits() {
       
        return unitRepository.findAll();
    }

    public String deleteUnit(String id) {
        System.out.println(id+">>>>>>>>");
       Optional<Unit> unit = unitRepository.findById(Long.parseLong(id));
       if (unit.isPresent()){
         unitRepository.deleteById(Long.parseLong(id));
         return null;
       }
       return "unit id doesn't exist";
    }

    public Optional<Unit> getSingleUnit(String id) throws NumberFormatException {
        return unitRepository.findById(Long.parseLong(id));
    }


}
