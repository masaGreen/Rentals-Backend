package com.masagreen.RentalUnitsManagement.models.entities;


import com.masagreen.RentalUnitsManagement.models.SuperClass;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name="units")
public class Unit extends SuperClass{
    
    @Column(name="plotname")
    private String plotName;
    @Column(name="unitnumber")
    private String unitNumber;
    private  String tag;
    private boolean status;
    private int rent;


    
}
