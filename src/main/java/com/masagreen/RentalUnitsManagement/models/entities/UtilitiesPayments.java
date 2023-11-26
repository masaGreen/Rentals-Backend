package com.masagreen.RentalUnitsManagement.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.masagreen.RentalUnitsManagement.models.SuperClass;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilitiesPayments  extends SuperClass{
    
    private LocalDateTime date;
    @Column(name="waterbill")
    private String waterBill;
    private String garbage;
    @Column(name="unitnumber")
    private String unitNumber;
    @Column(name="amountpaid")
    private String amountPaid;
    private String carriedForward;
    private String status;
    
}
