package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.StatusUpdateReqDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantDTO;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantReqDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantsResponseDto;
import com.masagreen.RentalUnitsManagement.services.TenantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tenants")
@Tag(name = "Tenants")
@RequiredArgsConstructor
public class TenantController {

        private final TenantService tenantService;

        @Operation(summary = " Endpoint to fetch all tenants")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "fetched tenants successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TenantsResponseDto.class)) }),

        })
        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<TenantsResponseDto> getAllTenants() {

                return new ResponseEntity<>(tenantService.findAllTenants(), HttpStatus.OK);

        }

        @Operation(summary = " Endpoint to download all tenants")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "downloaded tenants successfully", content = {
                                        @Content(mediaType = "application/pdf", array = @ArraySchema(schema = @Schema(implementation = byte.class))) }),

                        @ApiResponse(responseCode = "500", description = "error downloading try later", content = @Content(examples = @ExampleObject(value = "{'message': 'server error try again later'}"))),

        })
        @GetMapping("/download/allTenants")
        public ResponseEntity<?> downloadAllTenants(HttpServletResponse response) {

                byte[] fileBytes = tenantService.downloadAllTenants(response);
                if (Objects.isNull(fileBytes)) {
                        return new ResponseEntity<>(CommonResponseMessageDto.builder().message("download error"),
                                        HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(fileBytes, HttpStatus.OK);

        }

        @Operation(summary = " Endpoint to download all tenants with arrears")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "downloaded tenants with arrears successfully", content = {
                                        @Content(mediaType = "application/pdf", array = @ArraySchema(schema = @Schema(implementation = byte.class))) }),

                        @ApiResponse(responseCode = "500", description = "error downloading try later", content = @Content(examples = @ExampleObject(value = "{'message': 'server error try again later'}"))),

        })
        @GetMapping("/download/allTenantsWithArrears")
        public ResponseEntity<?> downloadAllTenantsWithArrears(HttpServletResponse response) {

                byte[] fileBytes = tenantService.handleAllTenantsWithArrearsDownloads(response);
                if (Objects.isNull(fileBytes)) {
                        return new ResponseEntity<>(CommonResponseMessageDto.builder().message("download error"),
                                        HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(fileBytes, HttpStatus.OK);

        }

        @Operation(summary = " Endpoint to fetch all tenants with arrears")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "fetched tenants with arrears successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TenantsResponseDto.class)) }),

        })
        @GetMapping("/withArrears")
        public ResponseEntity<TenantsResponseDto> getAllTenantsWithArrears() {

                return new ResponseEntity<>(tenantService.getAllTenantsWithArrears(), HttpStatus.OK);

        }

        @Operation(summary = " Endpoint to register a tenant")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "tenant fetched successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

                        @ApiResponse(responseCode = "409", description = "tenant exists or unit unavailable", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) })

        })
        @PostMapping
        public ResponseEntity<CommonResponseMessageDto> registerTenant(@RequestBody TenantReqDto tenantReqDto) {

                if (tenantService.saveTenant(tenantReqDto).equals("saved")) {
                        return new ResponseEntity<>(
                                        CommonResponseMessageDto.builder().message("saved successfully").build(),
                                        HttpStatus.OK);
                }

                return new ResponseEntity<>(CommonResponseMessageDto.builder()
                                .message("not saved, unit assigned or tenantd exists").build(), HttpStatus.BAD_REQUEST);

        }

        @Operation(summary = " Endpoint to delete a tenant")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "deleted tenant  successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TenantsResponseDto.class)) }),

                        @ApiResponse(responseCode = "404", description = "tenant not found", content = @Content(examples = @ExampleObject(value = "{'message': 'tenant not found'}"))),

        })
        @DeleteMapping("/deleteTenant/{id}")
        public ResponseEntity<?> deleteTenant(@PathVariable("id") String id) {
                return new ResponseEntity<>(tenantService.deleteTenant(id), HttpStatus.OK);
        }

        @Operation(summary = " Endpoint tfetch a tenant by his phone")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "fetched tenant successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TenantDTO.class)) }),

                        @ApiResponse(responseCode = "404", description = "not found", content = @Content(examples = @ExampleObject(value = "{'message': 'tenant not found'}"))),

        })
        @GetMapping("/getByPhone/{phone}")
        public ResponseEntity<TenantDTO> findByPhone(@PathVariable("phone") String phone) {

                return new ResponseEntity<>(tenantService.getByPhone(phone), HttpStatus.OK);

        }

        @Operation(summary = " Endpoint to update payment status")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "202", description = "payment updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

                        @ApiResponse(responseCode = "404", description = "tenant to update not found", content = @Content(examples = @ExampleObject(value = "{'message': 'tenant to update not found'}"))),

        })
        @PostMapping("/updatePaymentStatus")
        public ResponseEntity<CommonResponseMessageDto> updatePaymentStatus(
                        @RequestBody StatusUpdateReqDto statusUpdateReqDto) {

                return new ResponseEntity<>(tenantService.updatePaymentStatus(statusUpdateReqDto), HttpStatus.ACCEPTED);

        }

}
