package com.masagreen.RentalUnitsManagement.models.entities;

import com.masagreen.RentalUnitsManagement.models.SuperClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant extends SuperClass {

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String phone;
    private String start;
    private String ended;
    @Column(name = "payStatus")
    private String payStatus;
    private String unitNumber;

}
