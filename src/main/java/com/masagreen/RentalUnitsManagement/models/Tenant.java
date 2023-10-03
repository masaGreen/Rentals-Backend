package com.masagreen.RentalUnitsManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="firstname")
    private String firstName;
    @Column(name="lastname")
    private String lastName;
    private String phone;
    private LocalDate start;
    private LocalDate ended;
    @Column(name="payStatus")
    private String payStatus;


    @OneToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

}
