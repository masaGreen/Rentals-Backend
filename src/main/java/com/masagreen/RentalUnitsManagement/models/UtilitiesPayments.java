package com.masagreen.RentalUnitsManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilitiesPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime date;
    @Column(name="waterbill")
    private String waterBill;
    private String garbage;
    @Column(name="unitnumber")
    private String unitNumber;
    @Column(name="amountpaid")
    private String amountPaid;
    private String status;
    @OneToOne
    @JoinColumn(name="unit_id")
    private Unit unit;
}
