package com.masagreen.RentalUnitsManagement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantReqDto;
import com.masagreen.RentalUnitsManagement.models.Tenant;
import com.masagreen.RentalUnitsManagement.models.Unit;
import com.masagreen.RentalUnitsManagement.repositories.TenantRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UnitService unitService;
    public List<Tenant> findAllTenants() {
        return tenantRepository.findAll();
    }

    public void generate(HttpServletResponse res, String title, List<Tenant> tenants) throws DocumentException, IOException {


        Document document = new Document(PageSize.A4);


        PdfWriter.getInstance(document, res.getOutputStream());

        document.open();


        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(20);

        Paragraph paragraph = new Paragraph("List Of " + title, fontTitle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);


        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1, 1, 1});
        table.setSpacingBefore(3);


        PdfPCell cell = new PdfPCell();


        cell.setBackgroundColor(CMYKColor.gray);
        cell.setPadding(5);


        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(CMYKColor.green);
        font.setSize(12);
        //first titles row
        cell.setPhrase(new Phrase("Firstname", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Phone no.", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("StartDate", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("EndDate", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("PayStatus", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("UnitNumber", font));
        table.addCell(cell);

        // Iterating over the list of utils
        for (Tenant tenant: tenants) {

            table.addCell(tenant.getFirstName());
            table.addCell(tenant.getPhone());
            table.addCell(String.valueOf(tenant.getStart()));
            table.addCell(String.valueOf(tenant.getEnded()));
            table.addCell(tenant.getPayStatus());
            table.addCell(tenant.getUnit().getUnitNumber());

        }

        document.add(table);
        document.close();
    }

    public Tenant saveTenant(TenantReqDto tenantReqDto) {
        Optional<Unit> unit = unitService.findByUnitNumber(tenantReqDto.getUnitNumber());

        if(unit.isPresent() && unit.get().isStatus()){
            unit.get();
            unit.get().setStatus(false);
            Tenant tenant = Tenant.builder()
                    .firstName(tenantReqDto.getFirstName())
                    .lastName(tenantReqDto.getLastName())
                    .phone(tenantReqDto.getPhone())
                    .start(LocalDate.now())
                    .ended(null)
                    .payStatus("unpaid")
                    .unit(unit.get())
                    .build();
           
            unitService.saveUnit(unit.get());
           return tenantRepository.save(tenant);
        }
        return null;

    }
    public Tenant saveTenant(Tenant tenant) {
       return tenantRepository.save(tenant);

    }

    public String deleteTenant(String id) {
        Optional<Tenant> tenant = tenantRepository.findById(Long.parseLong(id));

        if (tenant.isPresent()){
            Unit unit = tenant.get().getUnit();
            unit.setStatus(true);
            unitService.saveUnit(unit);
            tenantRepository.deleteById(Long.parseLong(id));
            return null;
        }
        return "tenant id doesn't exist";
    }

    public Optional<Tenant> findByPhone(String phone) {
        return tenantRepository.findByPhone(phone);
    }

    public Optional<Tenant> findByTenantId(long l) {
       return  tenantRepository.findById(l);
    }
}
