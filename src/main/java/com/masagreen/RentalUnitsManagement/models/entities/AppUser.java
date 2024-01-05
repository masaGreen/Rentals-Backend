package com.masagreen.RentalUnitsManagement.models.entities;

import com.masagreen.RentalUnitsManagement.models.SuperClass;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class AppUser extends SuperClass {

    private String email;
    private String password;
    private boolean status;
    private String validationCode;
    private boolean isEmailValidated;

    private String role;
}

