package com.masagreen.RentalUnitsManagement;

import com.masagreen.RentalUnitsManagement.dtos.auth.AuthReqBodyDto;
import com.masagreen.RentalUnitsManagement.dtos.auth.LoginResponseDTO;
import com.masagreen.RentalUnitsManagement.dtos.auth.SignUpReqDto;
import com.masagreen.RentalUnitsManagement.dtos.auth.SignUpResponseDto;
import com.masagreen.RentalUnitsManagement.models.entities.AppUser;
import com.masagreen.RentalUnitsManagement.models.entities.Tenant;
import com.masagreen.RentalUnitsManagement.models.entities.Unit;
import com.masagreen.RentalUnitsManagement.models.entities.UtilitiesPayments;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class TestData {
    public static AppUser getAppUser(){
        AppUser appUser = new AppUser();
        appUser.setId("app-user");
        appUser.setCreatedAt(Instant.now());
        appUser.setEmail("masa@gmail.com");
        appUser.setPassword("password");
        appUser.setRole("ADMIN");
        appUser.setEmailValidated(true);
        appUser.setStatus(true);
        appUser.setValidationCode("code");
        return appUser;
    }

    public static Tenant getTenant(){
        Tenant tenant = new Tenant();
        tenant.setId("tenant");
        tenant.setCreatedAt(Instant.now());
        tenant.setPhone("0716794207");
        tenant.setFirstName("John");
        tenant.setLastName("Doe");
        tenant.setUnitNumber("AlphaA1");
        tenant.setPayStatus("paid");
        tenant.setStart(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd")));
        return tenant;
    }

    public static Unit getUnit(){
        Unit unit = new Unit();
        unit.setId("unit");
        unit.setCreatedAt(Instant.now());
        unit.setStatus(false);
        unit.setUnitNumber("AlphaA1");
        unit.setRent(10000);
        unit.setTag("1BR");
        unit.setPlotName("Alpha");
        return  unit;
    }
    public static UtilitiesPayments utilitiesPayments(){
        UtilitiesPayments utilitiesPayments = new UtilitiesPayments();
        utilitiesPayments.setId("utilities");
        utilitiesPayments.setStatus("unpaid");
        utilitiesPayments.setDate(LocalDateTime.now());
        utilitiesPayments.setCarriedForward("20");
        utilitiesPayments.setGarbage("100");
        utilitiesPayments.setWaterBill("100");
        utilitiesPayments.setAmountPaid("220");
        utilitiesPayments.setUnitNumber("AlphaA1");
        utilitiesPayments.setCreatedAt(Instant.now());
        return utilitiesPayments;

    }

    public static SignUpReqDto getSignUpReqDto(){

        return SignUpReqDto.builder()
                .email("masa@gmail.com")
                .password("password")
                .role("ADMIN")
                .build();
    }
    public static SignUpResponseDto getSignupResponseDto(){
        return SignUpResponseDto.builder()
                .email("masa@gmail.com")
                .id("app-user")
                .build();
    }
    public static LoginResponseDTO getLoginResponseDTO(){
        return LoginResponseDTO.builder()
                .id("app-user")
                .email("masa@gmail.com")
                .status(true)
                .role("ADMIN")
                .token("token")
                .build();
    }
    public static AuthReqBodyDto getAuthReqBodyDto(){
        return new AuthReqBodyDto("masa@gmail.com","password");
    }
}
