package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.StatusUpdateReqDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantReqDto;
import com.masagreen.RentalUnitsManagement.dtos.tenant.TenantsResponseDto;
import com.masagreen.RentalUnitsManagement.jwt.JwtFilter;
import com.masagreen.RentalUnitsManagement.models.Tenant;
import com.masagreen.RentalUnitsManagement.services.TenantService;
import com.masagreen.RentalUnitsManagement.utils.ProcessDownloadResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/tenants")
@CrossOrigin("http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
@Tag(name="Tenants")
public class TenantController {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private JwtFilter jwtFilter;

    @GetMapping(  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTenants(){
        
        try{
            
            return new ResponseEntity<>(TenantsResponseDto.builder().tenants(
                    tenantService.findAllTenants()).build(), HttpStatus.OK
            );
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatusCode.valueOf(500));
        }
       
    }
    @GetMapping("/download/allTenants")
    public ResponseEntity<?> downloadAllTenants(HttpServletResponse response) {
        try {
            List<Tenant> tenants = tenantService.findAllTenants();

            HttpServletResponse httpServletResponse = ProcessDownloadResponse.processResponse(response);

            tenantService.generate(httpServletResponse, "AllTenants", tenants);

            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("downloading").build(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }
    @GetMapping("/download/allTenantsWithArrears")
    public ResponseEntity<?> downloadAllTenantsWithArrears(HttpServletResponse response) {
        try {
            List<Tenant> tenants = tenantService.findAllTenants();
            List<Tenant> list = tenants.stream()
                            .filter(tenant->"unpaid".equalsIgnoreCase(tenant.getPayStatus()))
                            .toList();

            HttpServletResponse httpServletResponse = ProcessDownloadResponse.processResponse(response);

            tenantService.generate(httpServletResponse, "AllTenantsWithArrears", list);

            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("downloading").build(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            e.printStackTrace();
              return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
      
    }
    @GetMapping("/withArrears")
    public ResponseEntity<?> getAllTenantsWithArrears(){
        try{
            return new ResponseEntity<>(TenantsResponseDto.builder().tenants(
                    tenantService.findAllTenants().stream()
                            .filter(tenant->"unpaid".equalsIgnoreCase(tenant.getPayStatus()))
                            .toList()).build()
                    , HttpStatus.OK
            );
        } catch (Exception e){
            e.printStackTrace();
             return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        

    }
    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody TenantReqDto tenantReqDto){

           try {
               Tenant tenant = tenantService.saveTenant(tenantReqDto);
               if (tenant != null) {
                   return new ResponseEntity<>(CommonResponseMessageDto.builder().message("success").build(), HttpStatus.CREATED);
               } else {
                   return new ResponseEntity<>(CommonResponseMessageDto.builder().message("unit already occupied/doesn't exist").build(), HttpStatus.BAD_REQUEST);
               }
           }catch (Exception e){
               e.printStackTrace();
               return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
           }
          
    }
    @DeleteMapping("/deleteTenant/{id}")
    public ResponseEntity<?>  deleteTenant(@PathVariable("id") String id){
        try{
            String res = tenantService.deleteTenant(id);
            if(res==null && jwtFilter.isAdmin()) {
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("successfully deleted").build(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("id doesn't exist / unauthorized").build(), HttpStatus.FORBIDDEN);
            }

        } catch (Exception e){
            e.printStackTrace();
           return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
        

    @GetMapping("/getByPhone/{phone}")
    public ResponseEntity<?> findByPhone(@PathVariable("phone") String phone){

        try{
            Optional<Tenant> tenant = tenantService.findByPhone(phone);
            if(tenant.isPresent()) {
                return new ResponseEntity<>(tenant, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("phone doesn't exist").build(), HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }
        
    @PostMapping("/updatePaymentStatus")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody StatusUpdateReqDto statusUpdateReqDto){
        try{
            Optional<Tenant> tenant = tenantService.findByPhone(statusUpdateReqDto.getPhone());
            if(tenant.isPresent() && jwtFilter.isAdmin()) {
                tenant.get().setPayStatus(statusUpdateReqDto.getPayStatus());
                tenantService.saveTenant(tenant.get());
                return new ResponseEntity<>(tenant, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("id doesn't exist or Unauthorized").build(), HttpStatus.FORBIDDEN);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

}
