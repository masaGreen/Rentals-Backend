package com.masagreen.RentalUnitsManagement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.StatusUpdateReqDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantDTO;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantReqDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantsResponseDto;
import com.masagreen.RentalUnitsManagement.models.entities.Tenant;
import com.masagreen.RentalUnitsManagement.models.entities.Unit;
import com.masagreen.RentalUnitsManagement.repositories.TenantRepository;
import com.masagreen.RentalUnitsManagement.repositories.UnitRepository;
import com.masagreen.RentalUnitsManagement.utils.ProcessDownloadResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UnitRepository unitRepository;

    public TenantsResponseDto findAllTenants() {
        List<TenantDTO> tenantDTOs = tenantRepository.findAll().stream().map(u -> new TenantDTO(u)).toList();

        return TenantsResponseDto.builder().tenants(tenantDTOs).build();

    }


    public byte[] downloadAllTenants(HttpServletResponse response) {

        List<Tenant> tenants = tenantRepository.findAll();


        ProcessDownloadResponse.processResponse(response);

        try {
            log.info("preparing pdf for all tenants");
            return generate("All Tenants", tenants);


        } catch (DocumentException | IOException e) {
            log.error("error processing tenants download {}", e.getMessage());
            return null;
        }

    }

    public byte[] handleAllTenantsWithArrearsDownloads(HttpServletResponse response) {

        List<Tenant> tenants = tenantRepository.findAllTenantsWithArrears();

        ProcessDownloadResponse.processResponse(response);

        try {
            log.info("preparing pdf for all tennats");
            return generate("All Tenants With Arrears", tenants);

        } catch (DocumentException | IOException e) {
            log.error("error processing tenants with arrears download {}", e.getMessage());
            return null;
        }
    }

    public TenantsResponseDto getAllTenantsWithArrears() {

        List<TenantDTO> tenantDTOs = tenantRepository.findAllTenantsWithArrears().stream().map(TenantDTO::new)
                .toList();

        return TenantsResponseDto.builder().tenants(tenantDTOs).build();

    }

    @Transactional
    public String saveTenant(TenantReqDto tenantReqDto) {
        // a tenant to be saved must be allocated a unit and the unit must be available.
        Optional<Tenant> existingTenant = tenantRepository.findByPhone(tenantReqDto.phone());

        if (existingTenant.isEmpty()) {

            Unit unit = unitRepository.findByUnitNumber(tenantReqDto.unitNumber()).orElseThrow(
                    () -> new EntityNotFoundException("unit to be assigned does not exist"));

            if (unit.isStatus()) {
                unit.setStatus(false);

                Tenant tenant = new Tenant();

                tenant.setFirstName(tenantReqDto.firstName());
                tenant.setLastName(tenantReqDto.lastName());
                tenant.setPhone(tenantReqDto.phone());
                tenant.setStart(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd")));
                tenant.setEnded(null);
                tenant.setPayStatus("unpaid");
                tenant.setUnitNumber(tenantReqDto.unitNumber());
                // tenant.setUnit(unit);
                tenantRepository.save(tenant);

                unitRepository.save(unit);

                return "saved";
            } else {
                throw new IllegalStateException("not saved, unit is already assigned");
            }

        } else {

            return "not saved";
        }

    }

    public Tenant saveTenant(Tenant tenant) {
        return tenantRepository.save(tenant);

    }

    @Transactional
    public CommonResponseMessageDto deleteTenant(String id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("tenant does not exist"));

        tenantRepository.deleteById(id);
        Unit unit = unitRepository.findByUnitNumber(tenant.getUnitNumber()).orElseThrow(
                () -> new EntityNotFoundException("unit to be assigned not found"));
        // make the unit available
        unit.setStatus(true);
        unitRepository.save(unit);
        return CommonResponseMessageDto.builder().message("deleted successfully").build();

    }

    public TenantDTO getByPhone(String phone) {
        Tenant tenant = tenantRepository.findByPhone(phone).orElseThrow(
                () -> new EntityNotFoundException("No tenant found"));
        return new TenantDTO(tenant);

    }

    public CommonResponseMessageDto updatePaymentStatus(StatusUpdateReqDto statusUpdateReqDto) {

        Tenant tenant = tenantRepository.findByPhone(statusUpdateReqDto.phone()).orElseThrow(
                () -> new EntityNotFoundException("tenant not found"));

        tenant.setPayStatus(statusUpdateReqDto.payStatus());
        tenantRepository.save(tenant);
        return CommonResponseMessageDto.builder().message("updated successfully").build();

    }

    private byte[] generate(String title, List<Tenant> tenants)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);

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
        // first titles row
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
        for (Tenant tenant : tenants) {

            table.addCell(tenant.getFirstName());
            table.addCell(tenant.getPhone());
            table.addCell(String.valueOf(tenant.getStart()));
            table.addCell(String.valueOf(tenant.getEnded()));
            table.addCell(tenant.getPayStatus());
            table.addCell(tenant.getUnitNumber());

        }

        document.add(table);
        document.close();
        pdfWriter.close();
        return byteArrayOutputStream.toByteArray();
    }

}
