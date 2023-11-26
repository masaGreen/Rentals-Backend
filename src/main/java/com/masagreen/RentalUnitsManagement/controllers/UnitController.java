package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.unit.UnitDataResponseDto;
import com.masagreen.RentalUnitsManagement.dtos.unit.UnitReqDto;
import com.masagreen.RentalUnitsManagement.models.entities.Unit;
import com.masagreen.RentalUnitsManagement.services.UnitService;

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
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/units")
@Tag(name = "units")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UnitController {

   private final UnitService unitService;

   @Operation(summary = " Endpoint to register a unit")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "unit registered successfully", content = {
               @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommonResponseMessageDto.class))) }),

         @ApiResponse(responseCode = "409", description = "unit already exists", content = @Content(examples = @ExampleObject(value = "{'message': 'unit already exists'}"))),

   })
   @PostMapping
   public ResponseEntity<CommonResponseMessageDto> rentUnit(@RequestBody UnitReqDto unitReqDto) {
      return new ResponseEntity<>(unitService.saveUnit(unitReqDto), HttpStatus.CREATED);

   }

   @Operation(summary = " Endpoint to fetch all units")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "fetched units successfully", content = {
               @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDataResponseDto.class)) }),

   })
   @GetMapping
   public ResponseEntity<UnitDataResponseDto> getUnits() {
      return new ResponseEntity<>(unitService.findAllUnits(), HttpStatus.OK);

   }

   @Operation(summary = " Endpoint to download all units")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "downloaded units successfully", content = {
               @Content(mediaType = "application/pdf", array = @ArraySchema(schema = @Schema(implementation = byte.class))) }),

         @ApiResponse(responseCode = "500", description = "error downloading try later", content = @Content(examples = @ExampleObject(value = "{'message': 'server error try again later'}"))),

   })
   @GetMapping("/download/allUnits")
   public ResponseEntity<?> downloadAllUnits(HttpServletResponse response) {
      byte[] fileBytes = unitService.downloadAllUnits(response);
      if (Objects.isNull(fileBytes)) {
         return new ResponseEntity<>(CommonResponseMessageDto.builder().message("download error"),
               HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(fileBytes, HttpStatus.OK);

   }

   @Operation(summary = " Endpoint to fetch only available units")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "fetched available units successfully", content = {
               @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDataResponseDto.class)) }),

   })
   @GetMapping("/getAvailableUnits")
   public ResponseEntity<UnitDataResponseDto> getAvailableUnits() {
      return new ResponseEntity<>(unitService.getAvailableUnits(), HttpStatus.OK);

   }

   @Operation(summary = " Endpoint to fetch a unit by unitNumber")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "fetched unit  successfully", content = {
               @Content(mediaType = "application/json", schema = @Schema(implementation = Unit.class)) }),

         @ApiResponse(responseCode = "404", description = "unit not found", content = @Content(examples = @ExampleObject(value = "{'message': 'unit not found'}"))),

   })
   @GetMapping("/getByUnitNumber/{unitNumber}")
   public ResponseEntity<Unit> findByName(@PathVariable("unitNumber") String unitNum) {
      return new ResponseEntity<>(unitService.getByUnitNumber(unitNum), HttpStatus.OK);
   }

   @Operation(summary = " Endpoint to update unit's availabilty status  by unitNumber")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "202", description = "updated status successfully", content = {
               @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

         @ApiResponse(responseCode = "404", description = "unit not found", content = @Content(examples = @ExampleObject(value = "{'message': 'unit not found'}"))),

   })
   @GetMapping("/updateUnitStatus/{unitNumber}")
   public ResponseEntity<CommonResponseMessageDto> updateAvailability(@PathVariable("unitNumber") String unitNumber) {

      return new ResponseEntity<>(unitService.updateAvailability(unitNumber), HttpStatus.ACCEPTED);
   }

   @Operation(summary = " Endpoint to delete a unit by id")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "deleted unit  successfully", content = {
               @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

         @ApiResponse(responseCode = "404", description = "unit not found", content = @Content(examples = @ExampleObject(value = "{'message': 'unit not found'}"))),

   })
   @DeleteMapping("/deleteUnit/{id}")
   public ResponseEntity<CommonResponseMessageDto> deleteUnit(@PathVariable("id") String id) {
      return new ResponseEntity<>(unitService.deleteUnit(id), HttpStatus.OK);
   }

}
