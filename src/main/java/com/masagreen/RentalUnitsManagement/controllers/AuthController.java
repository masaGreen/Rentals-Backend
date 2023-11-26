
package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.dtos.CommonResponseMessageDto;
import com.masagreen.RentalUnitsManagement.dtos.auth.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.masagreen.RentalUnitsManagement.services.AppUserService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/v1/auth")
@RestController
@Tag(name = "Authorization")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;

    @Operation(summary = " Endpoint to fetch all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetched successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UsersResponseDto.class)) })
    })
    @GetMapping
    public ResponseEntity<UsersResponseDto> fetchAllUsers() {
        return new ResponseEntity<>(appUserService.findAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = " endpoint to sign up user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "saved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpResponseDto.class)) }),
            @ApiResponse(responseCode = "409", description = "User exists", content = @Content(examples = @ExampleObject(value = "{'message': 'email already signedup, login'}"))),

    })
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpReqDto signUpReqDto) {
        return new ResponseEntity<>(appUserService.saveUser(signUpReqDto), HttpStatus.CREATED);

    }

    @Operation(summary = " Endpoint to log user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "logged in successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "email not signed up", content = @Content(examples = @ExampleObject(value = "{'message': 'email not signed up'}"))),
            @ApiResponse(responseCode = "403", description = "invalid credentials", content = @Content(examples = @ExampleObject(value = "{'message': 'wrong credentials or you are have not been approved'}"))),

    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthReqBodyDto authReqBodyDto) {

        return new ResponseEntity<>(appUserService.loginUser(authReqBodyDto), HttpStatus.OK);
    }

    @Operation(summary = " Endpoint for admin to approve other users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user approved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),
            @ApiResponse(responseCode = "404", description = "email not signed up", content = @Content(examples = @ExampleObject(value = "{'message': 'email not signed up'}"))),
            @ApiResponse(responseCode = "403", description = "must be admin to approve", content = @Content(examples = @ExampleObject(value = "{'message': 'wrong credentials or you are not admin'}"))),

    })
    @PostMapping("/approveUser")
    public ResponseEntity<CommonResponseMessageDto> updateUser(@RequestBody ApprovalDto approvalDto) {

        return new ResponseEntity<>(appUserService.manageUserStatus(approvalDto), HttpStatus.ACCEPTED);

    }

    @Operation(summary = " Endpoint to handle forgotten password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "new pasword sent to email successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

            @ApiResponse(responseCode = "404", description = "email not signed up", content = @Content(examples = @ExampleObject(value = "{'message': 'email not signed up'}"))),

    })
    @PostMapping("/forgotPassword")
    public ResponseEntity<CommonResponseMessageDto> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {

        return new ResponseEntity<>(appUserService.handleForgotPassword(forgotPasswordDTO), HttpStatus.ACCEPTED);

    }

    @Operation(summary = " Endpoint change user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "passssword changed successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

            @ApiResponse(responseCode = "403", description = "mismatching passwords", content = @Content(examples = @ExampleObject(value = "{'message': 'passwords dont match'}"))),

    })
    @PostMapping("/changePassword")
    public ResponseEntity<CommonResponseMessageDto> changePassword(
            @Valid @RequestBody ChangePasswordReqDto changePasswordReqDto) {
        return new ResponseEntity<>(appUserService.handleChangePassword(changePasswordReqDto), HttpStatus.ACCEPTED);
    }

    @Operation(summary = " Endpoint to delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponseMessageDto.class)) }),

            @ApiResponse(responseCode = "403", description = "not authorized", content = @Content(examples = @ExampleObject(value = "{'message': 'only admin can delete a suer'}"))),
    })
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<CommonResponseMessageDto> deleteTenant(@PathVariable("id") String id) {
        return new ResponseEntity<>(appUserService.deleteAppUser(id), HttpStatus.ACCEPTED);
    }

    // initializing a user who will approve others as by default users are
    // registered unapproved

    // private void notifyAdmins(String email, String message){
    // List<AppUser> allAdmins = appUserService.findAllUsers().getUsers()
    // .stream().filter(adm -> "admin".equalsIgnoreCase(adm.getRole())).toList();

    // // remove current admin doing the changes and send the mail notification to
    // all
    // // the other admins
    // List<String> allAdminsProcessed = allAdmins.stream().map(AppUser::getEmail)
    // .filter(
    // adminEmail -> !Objects.equals(adminEmail, email)).toList();

    // String[] emailList = new String[allAdmins.size()];
    // for (int i = 0; i < allAdmins.size(); i++) {
    // emailList[i] = allAdminsProcessed.get(i);
    // }
    // // send the message
    // sendMail.sendApprovedBy(
    // emailList,
    // "<h2>User "+message+"</h2>",
    // "<p> User with email: <strong>" + email
    // + "</strong> by Admin with email: <strong>" + jwtFilter.getCurrentUserEmail()
    // + "</strong>"

    // );
    // }
}
