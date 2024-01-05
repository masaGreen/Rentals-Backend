package com.masagreen.RentalUnitsManagement.services;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.auth.*;
import com.masagreen.RentalUnitsManagement.exceptions.PasswordMismatchException;
import com.masagreen.RentalUnitsManagement.exceptions.WrongEmailValidationCode;
import com.masagreen.RentalUnitsManagement.jwt.JwtFilter;
import com.masagreen.RentalUnitsManagement.jwt.JwtService;
import com.masagreen.RentalUnitsManagement.models.entities.AppUser;
import com.masagreen.RentalUnitsManagement.repositories.AppUserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {


    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtFilter jwtFilter;


    public AppUser findAppUserByEmail(String email, String errorMessage) {
        return appUserRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(errorMessage));
    }

    public SignUpResponseDto saveUser(SignUpReqDto signUpReqDto) {
        Boolean userExists = appUserRepository.existsByEmail(signUpReqDto.getEmail());
        if (!userExists) {
            AppUser user = AppUser.builder()
                    .email(signUpReqDto.getEmail())
                    .password(passwordEncoder.encode(signUpReqDto.getPassword()))
                    .status(true)
                    .validationCode(UUID.randomUUID().toString())
                    .role(signUpReqDto.getRole())
                    .build();
            AppUser savedUser = appUserRepository.save(user);

            //send mail with the validation code

            return SignUpResponseDto.builder().id(savedUser.getId()).email(savedUser.getEmail()).build();
        } else {
            throw new EntityExistsException("user with email: " + signUpReqDto.getEmail() + " exists");
        }
    }

    public LoginResponseDTO loginUser(AuthReqBodyDto authReqBodyDto) {

        //check if user exists else throw error and return

        AppUser user = findAppUserByEmail(authReqBodyDto.email(), "email/password incorrect");
        //if(!user.isEmailValidated()) throw new UserNotValidatedException("user email not validated, validate first");
        boolean isPasswordMatch = passwordEncoder.matches(authReqBodyDto.password(), user.getPassword());
        //if password and emails match authenticate the user

        if (isPasswordMatch && user.isStatus()) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authReqBodyDto.email(), authReqBodyDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("logged in user: {}", authReqBodyDto.email());

            return LoginResponseDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .token(jwtService.generateToken(user))
                    .status(user.isStatus())
                    .role(user.getRole())
                    .build();


        } else {
            throw new BadCredentialsException("email or password incorrect");
        }


    }

    public CommonResponseMessageDto validateEmail(String code) {
        AppUser user = appUserRepository.findByValidationCode(code).orElseThrow(
                () -> new WrongEmailValidationCode("incorrect validation code")
        );
        user.setEmailValidated(true);
        appUserRepository.save(user);
        return CommonResponseMessageDto.builder().message("successfully validated").build();
    }

    public CommonResponseMessageDto manageUserStatus(ApprovalDto approvalDto) {

            AppUser user = findAppUserByEmail(approvalDto.email(), "not found");
            user.setStatus(!user.isStatus());

            appUserRepository.save(user);

            // notify other admins via mail

            return CommonResponseMessageDto.builder().message("user status changed").build();


    }

    public CommonResponseMessageDto handleForgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        AppUser user = findAppUserByEmail(forgotPasswordDTO.email(), "email not found");
        String newPassword = UUID.randomUUID().toString().substring(0, 6);
        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);

        // send newPassword to mail
        return CommonResponseMessageDto.builder().message("successfully sent new password to " + user.getEmail()).build();


    }

    public CommonResponseMessageDto handleChangePassword(ChangePasswordReqDto changePasswordReqDto, HttpServletRequest request) {

        //must be logged in
        AppUser loggedUser = findAppUserByEmail((String) request.getAttribute("email"), "email user must be logged in");


        if (passwordEncoder.matches(changePasswordReqDto.getOldPassword(), loggedUser.getPassword())) {

            loggedUser.setPassword(passwordEncoder.encode(changePasswordReqDto.getNewPassword()));
            appUserRepository.save(loggedUser);

            return CommonResponseMessageDto.builder().message("successfully changed your password").build();
        } else {
            throw new PasswordMismatchException("Unmatching passwords");
        }


    }

    public UsersResponseDto findAllUsers() {

        List<AppUser> users = appUserRepository.findAll();

        List<UserDTO> usersDtos = users.stream().map(UserDTO::new).toList();

        return UsersResponseDto.builder().users(usersDtos).build();

    }


    public CommonResponseMessageDto deleteAppUser(String id) {
        boolean existsById = appUserRepository.existsById(id);
        if (existsById) {

                appUserRepository.deleteById(id);
                // notifyAdmins(jwtFilter.getCurrentUserEmail(), "Deletion");
                return CommonResponseMessageDto.builder().message("successfully deleted").build();
        } else {
            throw new EntityNotFoundException("user doesn't exist");
        }
    }

    // @PostConstruct
    // private void initUser() {
    //     appUserRepository.save(
    //             AppUser.builder()
    //                     .email(email)
    //                     .password(passwordEncoder.encode(password))
    //                     .status(true)
    //                     .role("ADMIN")
    //                     .build());
    // }
}
