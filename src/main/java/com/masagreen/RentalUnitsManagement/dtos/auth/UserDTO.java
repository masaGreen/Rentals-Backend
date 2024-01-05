package com.masagreen.RentalUnitsManagement.dtos.auth;

import com.masagreen.RentalUnitsManagement.models.entities.AppUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String email;
    private Boolean status;
    private String role;

    public UserDTO(AppUser appUser) {
        this.id = appUser.getId();
        this.email = appUser.getEmail();
        this.status = appUser.isStatus();
        this.role = appUser.getRole();
    }
}
