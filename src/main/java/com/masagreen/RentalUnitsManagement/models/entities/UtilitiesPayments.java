package com.masagreen.RentalUnitsManagement.models.entities;

import com.masagreen.RentalUnitsManagement.models.SuperClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilitiesPayments extends SuperClass {

    private LocalDateTime date;
    @Column(name = "waterbill")
    private String waterBill;
    private String garbage;
    @Column(name = "unitnumber")
    private String unitNumber;
    @Column(name = "amountpaid")
    private String amountPaid;
    private String carriedForward;
    private String status;

}
