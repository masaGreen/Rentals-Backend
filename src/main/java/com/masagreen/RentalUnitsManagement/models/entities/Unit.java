package com.masagreen.RentalUnitsManagement.models.entities;


import com.masagreen.RentalUnitsManagement.models.SuperClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "units")
public class Unit extends SuperClass {

    @Column(name = "plotname")
    private String plotName;
    @Column(name = "unitnumber")
    private String unitNumber;
    private String tag;
    private boolean status;
    private int rent;


}
