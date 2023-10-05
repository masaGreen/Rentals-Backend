
package com.masagreen.RentalUnitsManagement.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.auth.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.masagreen.RentalUnitsManagement.jwt.JwtFilter;
import com.masagreen.RentalUnitsManagement.jwt.JwtService;
import com.masagreen.RentalUnitsManagement.models.AppUser;
import com.masagreen.RentalUnitsManagement.services.AppUserService;
import com.masagreen.RentalUnitsManagement.utils.SendMail;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RequestMapping("/v1/auth")
@RestController
@Tag(name="Authorization")
@CrossOrigin("http://localhost:5173")
public class AuthController {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SendMail sendMail;


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> fetchAllUsers(){
        try{
            List<AppUser> users = appUserService.findAllUsers();
            List<SignUpResponseDto> usersWithoutPassword = users.stream().map(user -> SignUpResponseDto.builder()
                    .id((long) user.getId())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .status(user.isStatus())
                    .build()
                    ).toList();
            return new ResponseEntity<>(UsersResponseDto.builder().users(usersWithoutPassword).build(),HttpStatus.OK);
        }catch (Exception e){
           
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("internal server error").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpReqDto signUpReqDto) {
        AppUser user = AppUser.builder()
                .email(signUpReqDto.getEmail())
                .password(passwordEncoder.encode(signUpReqDto.getPassword()))
                .status(false)
                .role(signUpReqDto.getRole())
                .build();

        try {
            AppUser userToBeSaved = appUserService.saveUser(user);
            if (userToBeSaved != null) {
                SignUpResponseDto signUpResponseDto = SignUpResponseDto.builder()
                        .email(userToBeSaved.getEmail())
                        .role(userToBeSaved.getRole())
                        .build();
                return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
            } else {

                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("user already exists").build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
             return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthReqBodyDto authReqBodyDto) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authReqBodyDto.getEmail(), authReqBodyDto.getPassword()));

            Optional<AppUser> user = appUserService.findByEmail(authReqBodyDto.getEmail());
            if (user.get().isStatus()) {

                return new ResponseEntity<>(
                        AuthResDto.builder()
                                .id(user.get().getId())
                                .email(user.get().getEmail())
                                .message("user approved ")
                                .token(jwtService.generateToken(user.get(), user.get().getRole()))
                                .role(user.get().getRole())
                                .build(),
                        HttpStatus.OK);

            } else {
                return new ResponseEntity<>(AuthResDto.builder().message("wait for approval")
                        .build(), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(AuthResDto.builder().message(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/approveUser")
    public ResponseEntity<String> updateUser(@RequestBody ApprovalDto approvalDto) {

        try {
            if (jwtFilter.isAdmin()) {
                Optional<AppUser> user = appUserService.findByEmail(approvalDto.getEmail());
                System.out.println(user.get());
                if (user.isPresent()) {
                    user.get().setStatus(!user.get().isStatus());

                    appUserService.updateUser(user.get());

                    // notify other admins via mail
//                    notifyAdmins(jwtFilter.getCurrentUserEmail(), "approval");

                    return new ResponseEntity<>("user status changed", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
                }

            } else {
                return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/forgotPassword")

    public ResponseEntity<?> forgotPassword(@RequestBody String email) {

        try {
            Optional<AppUser> user = appUserService.findByEmail(email);

            if (user.isPresent()) {
                // send password to mail
                sendMail.sendPassword(email, "<h2>New password, login and change it</h2>");
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("successfully sent password to email").build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("You haven't signed up, signup please").build(),
                        HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        

    }
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChnagePasswordReqDto changePasswordReqDto) {

        try {
            Optional<AppUser> found = appUserService.findByEmail(jwtFilter.getCurrentUserEmail());

            if (found.isPresent()) {
                if (passwordEncoder.matches(changePasswordReqDto.getOldPassword(), found.get().getPassword())) {

                    found.get().setPassword(passwordEncoder.encode(changePasswordReqDto.getNewPassword()));
                    appUserService.updateUser(found.get());

                    return new ResponseEntity<>(CommonResponseMessageDto.builder().message("successfully changed your password").build(), HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(CommonResponseMessageDto.builder().message("unauthorized").build(), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("unauthorized").build(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message("unauthorized").build(), HttpStatus.UNAUTHORIZED);

        }
    }
    @SecurityRequirement(name = "bearerAuth")
     @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?>  deleteTenant(@PathVariable("id") String id){
        try{
            String res = appUserService.deleteAppUser(id);
            if(res==null && jwtFilter.isAdmin()) {
                notifyAdmins(jwtFilter.getCurrentUserEmail(), "Deletion");
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("successfully deleted").build(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(CommonResponseMessageDto.builder().message("id doesn't exist").build(), HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(CommonResponseMessageDto.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }


    // initializing a user who will approve others as by default users are
    // registered unapproved
    @PostConstruct
    private void initUser() {
        appUserService.saveUser(
                AppUser.builder()
                        .email("root@gmail.com")
                        .password(passwordEncoder.encode("rootpassword"))
                        .status(true)
                        .role("ADMIN")
                        .build());
    }

    private void notifyAdmins(String email, String message){
         List<AppUser> allAdmins = appUserService.findAllUsers()
                            .stream().filter(adm -> "admin".equalsIgnoreCase(adm.getRole())).toList();

                    // remove current admin doing the changes and send the mail notification to all
                    // the other admins
                    List<String> allAdminsProcessed = allAdmins.stream().map(AppUser::getEmail)
                            .filter(
                                    adminEmail -> !Objects.equals(adminEmail, email)).toList();

                    String[] emailList = new String[allAdmins.size()];
                    for (int i = 0; i < allAdmins.size(); i++) {
                        emailList[i] = allAdminsProcessed.get(i);
                    }
                    // send the message
                    sendMail.sendApprovedBy(
                            emailList,
                            "<h2>User "+message+"</h2>",
                            "<p> User with email: <strong>" + email
                                    + "</strong> by Admin with email: <strong>" + jwtFilter.getCurrentUserEmail()
                                    + "</strong>"

                    );
    }
}
