package com.masagreen.RentalUnitsManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="units")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="plotname")
    private String plotName;
    @Column(name="unitnumber")
    private String unitNumber;
    private  String tag;
    private boolean status;
    private int rent;
}
