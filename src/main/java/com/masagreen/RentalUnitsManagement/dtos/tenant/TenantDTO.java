package com.masagreen.RentalUnitsManagement.dtos.tenant;

import com.masagreen.RentalUnitsManagement.models.entities.Tenant;

import lombok.Data;

@Data
public class TenantDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String start;
    private String ended;
    private String payStatus;  
    private String unitNumber;
    
    public TenantDTO(Tenant tenant){
        
        this.id = tenant.getId();
        this.firstName = tenant.getFirstName();
        this.lastName = tenant.getLastName();
        this.phone = tenant.getPhone();
        this.start = tenant.getStart();
        this.ended = tenant.getEnded();
        this.payStatus  = tenant.getPayStatus(); 
        this.unitNumber = tenant.getUnitNumber();

    }
}
