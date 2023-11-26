
package com.masagreen.RentalUnitsManagement.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponseDto {
    private String id;
    
    private String email;
   
}

