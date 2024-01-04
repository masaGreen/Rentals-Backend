package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.utils.UtilsReqDto;
import com.masagreen.RentalUnitsManagement.dtos.utils.UtilsResDto;
import com.masagreen.RentalUnitsManagement.services.UtilitiesPaymentsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/utilities")
@Tag(name = "tracking - utilities")
@SecurityRequirement(name="bearerAuth")
public class UtilitiesController {
    @Autowired
    private UtilitiesPaymentsService utilitiesPaymentsService;

    @Operation(summary = " Endpoint to register a Util-payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "util-payment registered successfully", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommonResponseMessageDto.class))) }),

    })
    @PostMapping()
    public ResponseEntity<CommonResponseMessageDto> saveUtilityPayment(@RequestBody UtilsReqDto utilsReqDto) {

        return new ResponseEntity<>(utilitiesPaymentsService.saveUtilitiesPayments(utilsReqDto), HttpStatus.CREATED);

    }

    @Operation(summary = " Endpoint to download all UtilitiesPayments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "downloaded UtilitiesPayments successfully", content = {
                    @Content(mediaType = "application/pdf", array = @ArraySchema(schema = @Schema(implementation = byte.class))) }),

            @ApiResponse(responseCode = "500", description = "error downloading try later", content = @Content(examples = @ExampleObject(value = "{'message': 'server error try again later'}"))),

    })
    @GetMapping("/download/allUtilPayments")
    public ResponseEntity<byte[]> downloadAllUtilsPayments(HttpServletResponse response) {
        return new ResponseEntity<>(utilitiesPaymentsService.handleAllUtilsDownloads(response), HttpStatus.OK);
    }

    @Operation(summary = " Endpoint to download all UtilitiesPayments with pending bills")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "downloaded UtilitiesPayments with pending bills successfully", content = {
                    @Content(mediaType = "application/pdf", array = @ArraySchema(schema = @Schema(implementation = byte.class))) }),

            @ApiResponse(responseCode = "500", description = "error downloading try later", content = @Content(examples = @ExampleObject(value = "{'message': 'server error try again later'}"))),

    })
    @GetMapping("/download/allUtilPaymentsWithPendingBills")
    public ResponseEntity<byte[]> downloadAllUtilsWithPendingBills(HttpServletResponse response) {

        return new ResponseEntity<>(utilitiesPaymentsService.handleUtilsWithPendingBills(response), HttpStatus.OK);
    }

    @Operation(summary = " Endpoint to download all UtilitiesPayments by unitNUmber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "downloaded UtilitiesPayments by unitNumber successfully", content = {
                    @Content(mediaType = "application/pdf", array = @ArraySchema(schema = @Schema(implementation = byte.class))) }),

            @ApiResponse(responseCode = "500", description = "error downloading try later", content = @Content(examples = @ExampleObject(value = "{'message': 'server error try again later'}"))),

    })
    @GetMapping("/download/allUtilPaymentsPerUnit/{unitNumber}")
    public ResponseEntity<byte[]> downloadAllUtilsPaymentsForSingleUnit(@PathVariable("unitNumber") String unitNumber,
            HttpServletResponse response) {

        return new ResponseEntity<>(utilitiesPaymentsService.handleAllUtilsForSingleUnit(response, unitNumber),
                HttpStatus.OK);
    }

    @Operation(summary = " Endpoint to fetch all util-payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched all util payments successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UtilsResDto.class)) }),

    })
    @GetMapping
    public ResponseEntity<UtilsResDto> getAllUtilsPayments() {

        UtilsResDto utilsResDto = UtilsResDto.builder()
                .utilsPayments(utilitiesPaymentsService.findAllUtilitiesPayments()).build();

        return new ResponseEntity<>(utilsResDto, HttpStatus.OK);

    }

    @Operation(summary = " Endpoint to fetch all until-payments by unitNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched until-payments  successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UtilsResDto.class)) }),

            @ApiResponse(responseCode = "404", description = "unit by the unitNumber not found", content = @Content(examples = @ExampleObject(value = "{'message': 'unit by the unitNumber not found'}"))),

    })
    @GetMapping("/getByUnit/{unitNumber}")
    public ResponseEntity<UtilsResDto> findByUnitNumber(@PathVariable("unitNumber") String unitNumber) {
        UtilsResDto utilsResDto = UtilsResDto.builder()
                .utilsPayments(utilitiesPaymentsService.findAllByUnitNumber(unitNumber)).build();

        return new ResponseEntity<>(utilsResDto, HttpStatus.OK);

    }

    @Operation(summary = " Endpoint to fetch all until-payments by their payment status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched until-payments by status successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UtilsResDto.class)) }),

    })
    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<UtilsResDto> findByUnitWithArrears(@PathVariable("status") String status) {

        UtilsResDto utilsResDto = UtilsResDto.builder().utilsPayments(utilitiesPaymentsService.findByStatus(status))
                .build();

        return new ResponseEntity<>(utilsResDto, HttpStatus.OK);
    }

    @Operation(summary = " Endpoint to delete a utilpayment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "deleted until-payment  successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

            @ApiResponse(responseCode = "404", description = "util-payment not found", content = @Content(examples = @ExampleObject(value = "{'message': 'util payment not found'}"))),
                @ApiResponse(responseCode = "403", description = "util-payment not found", content = @Content(examples = @ExampleObject(value = "{'message': 'Unauthorized, must be admin'}"))),
    })

    @DeleteMapping("/deleteUtilities/{id}")
    public ResponseEntity<CommonResponseMessageDto> deleteUtility(@PathVariable("id") String id) {

        return new ResponseEntity<>(CommonResponseMessageDto.builder()
                .message(utilitiesPaymentsService.deleteUtility(id))
                .build(), HttpStatus.OK);

    }

}
