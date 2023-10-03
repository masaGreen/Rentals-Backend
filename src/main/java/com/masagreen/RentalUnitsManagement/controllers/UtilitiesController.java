package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.utils.UtilsReqDto;
import com.masagreen.RentalUnitsManagement.dtos.utils.UtilsResDto;
import com.masagreen.RentalUnitsManagement.models.Unit;
import com.masagreen.RentalUnitsManagement.models.UtilitiesPayments;
import com.masagreen.RentalUnitsManagement.services.UnitService;
import com.masagreen.RentalUnitsManagement.services.UtilitiesPaymentsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.masagreen.RentalUnitsManagement.utils.ProcessDownloadResponse.processResponse;

@RestController
@RequestMapping("/v1/utilities")
@SecurityRequirement(name = "bearerAuth")
@Tag(name="utilities&Payments")
public class UtilitiesController {
    @Autowired
    private UtilitiesPaymentsService utilitiesPaymentsService;
    @Autowired
    private UnitService unitService;

    @PostMapping()
    public ResponseEntity<?> saveUtilityPayment(@RequestBody UtilsReqDto utilsReqDto){

        UtilitiesPayments utilitiesPayments = processSaveUtilitiesPayment(utilsReqDto);
        try {
            UtilitiesPayments utilitiesPayment = utilitiesPaymentsService.saveUtilitiesPayments(utilitiesPayments);
            if (utilitiesPayment != null) {

                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("successfully created").build(), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("can't create utilities payments for non-existent unit").build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/download/allUtilPayments")
    public ResponseEntity<?> downloadAllUtilsPayments(HttpServletResponse response) {
        try {
            List<UtilitiesPayments> allUtils = utilitiesPaymentsService.getAllUtils();
            for(UtilitiesPayments utilitiesPayments:allUtils){
                System.out.println(utilitiesPayments.getId());
            }
            HttpServletResponse httpServletResponse = processResponse(response);

            utilitiesPaymentsService.generate(httpServletResponse, "AllUtilitiesPayments", allUtils);

            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("successfully created").build(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/download/allUtilPaymentsWithPendingBills")
    public ResponseEntity<?> downloadAllUtilsPaymentsWithPendingBills(HttpServletResponse response) {
        try {
            List<UtilitiesPayments> allUtils = utilitiesPaymentsService.getAllUtils()
                    .stream()
                    .filter(util->"unpaid".equalsIgnoreCase(util.getStatus()))
                    .collect(Collectors.toList());

            HttpServletResponse httpServletResponse = processResponse(response);

           utilitiesPaymentsService.generate(httpServletResponse, "AllUtilitiesPayments", allUtils);

            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("downloading").build(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/download/allUtilPaymentsPerUnit/{unitNumber}")
    public ResponseEntity<?> downloadAllUtilsPaymentsForSingleUnit(@PathVariable("unitNumber") String unitNumber, HttpServletResponse response) {
        try {
            List<UtilitiesPayments> utilPayments = utilitiesPaymentsService.findByUnitNumber(unitNumber);

            HttpServletResponse httpServletResponse = processResponse(response);

            utilitiesPaymentsService.generate(httpServletResponse, "AllUtilitiesPayments", utilPayments);

            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("downloading").build(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping
    public ResponseEntity<?> getAllUtilsPayments(){
        try{
            return new ResponseEntity<>(UtilsResDto.builder()
                    .utilsPayments(utilitiesPaymentsService.getAllUtils())
                    .build(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getByUnit/{unitNumber}")
    public ResponseEntity<?> findByUnitNumber(@PathVariable("unitNumber") String unitNumber){
        try{
            List<UtilitiesPayments> utilPayments = utilitiesPaymentsService.findByUnitNumber(unitNumber);
            if(!utilPayments.isEmpty()) {
                return new ResponseEntity<>(UtilsResDto.builder().utilsPayments(utilPayments).build(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("unit doesn't exist").build(), HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<?> findByUnitWithArrears(@PathVariable("status") String status){
        try{
            List<UtilitiesPayments> utilPayments = utilitiesPaymentsService.findByStatus(status)
                    .stream()
                    .filter(util->"unpaid".equalsIgnoreCase(util.getStatus()))
                    .collect(Collectors.toList());

            if(!utilPayments.isEmpty()) {
                return new ResponseEntity<>(UtilsResDto.builder().utilsPayments(utilPayments).build(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("all unit bills paid").build(), HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
    @DeleteMapping("/deleteUtilities/{id}")
    public ResponseEntity<?>  deleteUtility(@PathVariable("id") String id){
        try{
            String res = utilitiesPaymentsService.deleteUtility(id);
            if(res==null) {
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("deleted successfully").build(), HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("id doesn't exist").build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private UtilitiesPayments processSaveUtilitiesPayment(UtilsReqDto utilsReqDto){

        Optional<Unit> unit = unitService.findByUnitNumber(utilsReqDto.getUnitNumber());
        if(unit.isEmpty()){
            return null;
        }else{
            boolean isPaid = ( Integer.parseInt(utilsReqDto.getAmountPaid()) - (Integer.parseInt(
                    utilsReqDto.getGarbage())+Integer.parseInt(utilsReqDto.getWaterBill()))) >=0;
            String status = isPaid ? "paid":"unpaid";

            UtilitiesPayments utilitiesPayments = UtilitiesPayments.builder()
                    .date(LocalDateTime.now())
                    .waterBill(utilsReqDto.getWaterBill())
                    .garbage(utilsReqDto.getGarbage())
                    .amountPaid(utilsReqDto.getAmountPaid())
                    .status(status)
                    .unitNumber(unit.get().getUnitNumber())
                    .unit(unit.get())
                    .build();
            return  utilitiesPayments;
        }


    }


}
