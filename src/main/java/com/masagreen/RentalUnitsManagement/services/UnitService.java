package com.masagreen.RentalUnitsManagement.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.unit.UnitDataResponseDto;
import com.masagreen.RentalUnitsManagement.dtos.unit.UnitReqDto;
import com.masagreen.RentalUnitsManagement.exceptions.DeletionNotAllowedCurrentlyException;
import com.masagreen.RentalUnitsManagement.models.entities.Unit;
import com.masagreen.RentalUnitsManagement.repositories.UnitRepository;
import com.masagreen.RentalUnitsManagement.utils.ProcessDownloadResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;

    public CommonResponseMessageDto saveUnit(UnitReqDto unitReqDto) {
        //check if already exists
        Boolean isExistent = unitRepository.existsByUnitNumber(unitReqDto.unitNumber());
        if (isExistent) {
            throw new EntityExistsException("unit already exists");
        } else {
            Unit unit = Unit.builder()
                    .plotName(unitReqDto.plotName())
                    .unitNumber(unitReqDto.plotName() + unitReqDto.unitNumber())
                    .tag(unitReqDto.tag())
                    .status(true)
                    .rent(unitReqDto.rent())
                    .build();
            unitRepository.save(unit);
            return CommonResponseMessageDto.builder().message("unit successfully created").build();
        }


    }


    public byte[] downloadAllUnits(HttpServletResponse response) {

        List<Unit> units = unitRepository.findAll();


        ProcessDownloadResponse.processResponse(response);

        try {
            log.info("preparing pdf for all units");
            return generate("All Units", units);


        } catch (DocumentException | IOException e) {
            log.error("error processing units download {}", e.getCause());
            return null;
        }

    }

    public UnitDataResponseDto getAvailableUnits() {

        List<Unit> units = unitRepository.findAllUnitsAvailable();

        return UnitDataResponseDto.builder().units(units).build();


    }

    public Unit getByUnitNumber(String id) {
        Unit unit = unitRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("unit not found")
        );
        return unit;

    }

    public CommonResponseMessageDto updateAvailability(String unitNumber) {
        Unit unit = unitRepository.findByUnitNumber(unitNumber).orElseThrow(
                () -> new EntityNotFoundException("unit not found")
        );

        unit.setStatus(!(unit.isStatus()));

        unitRepository.save(unit);

        return CommonResponseMessageDto.builder().message("status successfully changed").build();

    }

    public UnitDataResponseDto findAllUnits() {

        List<Unit> units = unitRepository.findAll();
        return UnitDataResponseDto.builder().units(units).build();

    }

    public CommonResponseMessageDto deleteUnit(String id) {

        Unit unit = unitRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("unit by id: " + id + "does not exist"));

        if (unit.isStatus()) {
            unitRepository.deleteById(id);
            return CommonResponseMessageDto.builder().message("deleted successfully").build();

        } else {
            throw new DeletionNotAllowedCurrentlyException("can't delete occupied unit, delete tenant first");
        }


    }

    private byte[] generate(String title, List<Unit> units) throws DocumentException, IOException {


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

        PdfPTable table = new PdfPTable(5);


        table.setWidthPercentage(100f);
        table.setWidths(new int[]{2, 2, 2, 2, 2});
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
        cell.setPhrase(new Phrase("Is_available", font));
        table.addCell(cell);


        // Iterating over the list of utils
        for (Unit unit : units) {

            table.addCell(unit.getPlotName());
            table.addCell(unit.getUnitNumber());
            table.addCell(unit.getTag());
            table.addCell(String.valueOf(unit.getRent()));
            table.addCell(String.valueOf(unit.isStatus()));

        }

        document.add(table);


        document.close();
        writer.close();
        return byteArrayOutputStream.toByteArray();

    }


}
