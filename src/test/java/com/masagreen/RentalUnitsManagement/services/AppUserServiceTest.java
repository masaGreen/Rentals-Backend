 package com.masagreen.RentalUnitsManagement.services;

 import com.masagreen.RentalUnitsManagement.TestData;
 import com.masagreen.RentalUnitsManagement.dtos.auth.*;
 import com.masagreen.RentalUnitsManagement.exceptions.PasswordMismatchException;
 import com.masagreen.RentalUnitsManagement.models.entities.AppUser;
 import com.masagreen.RentalUnitsManagement.repositories.AppUserRepository;
 import com.masagreen.RentalUnitsManagement.security.jwt.JwtService;
 import jakarta.persistence.EntityExistsException;
 import jakarta.persistence.EntityNotFoundException;
 import jakarta.servlet.http.HttpServletRequest;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.mockito.Mock;
 import org.mockito.Mockito;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
 import org.springframework.security.authentication.AuthenticationManager;
 import org.springframework.security.authentication.BadCredentialsException;
 import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.crypto.password.PasswordEncoder;

 import java.util.List;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.ArgumentMatchers.any;
 import static org.mockito.ArgumentMatchers.anyIterable;
 import static org.mockito.Mockito.verify;
 import static org.mockito.Mockito.when;

 @SpringBootTest
 class AppUserServiceTest {
     @Autowired
     private AppUserService appUserService;
     @MockBean
     private AppUserRepository appUserRepository;
     @MockBean
     private Authentication authentication;
     @MockBean
     private PasswordEncoder passwordEncoder;
     @MockBean
     private AuthenticationManager authenticationManager;
     @MockBean
     private HttpServletRequest httpServletRequest;
     @MockBean
     private JwtService jwtService;
     @Test
     void shouldSaveUserTest() {
         var signUpReqDto = TestData.getSignUpReqDto();
//         var signUpResponseDto = TestData.getSignupResponseDto();
         var appUser = TestData.getAppUser();
         Mockito.when(appUserRepository.existsByEmail(signUpReqDto.getEmail())).thenReturn(false);
         Mockito.when(passwordEncoder.encode(signUpReqDto.getPassword())).thenReturn("encoded-password");

         Mockito.when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         var signupRes = appUserService.saveUser(signUpReqDto);
         System.out.println(signupRes);
         assertEquals(appUser.getEmail(),signupRes.getEmail());
         assertEquals("app-user",appUser.getId());

     }



     @Test
     @DisplayName("Trying to save user whose id-number exists should throw EntityExistsException")
     void shouldThrowEntityExistsExceptionTest() {
        var signUpReqDto = TestData.getSignUpReqDto();
         Mockito.when(appUserRepository.existsByEmail(signUpReqDto.getEmail())).thenReturn(true);

         assertThrows(EntityExistsException.class, () -> appUserService.saveUser(signUpReqDto));
     }
     @Test
     @DisplayName("should throw EntityNotFoundException if user not found")
     void shouldThrowEntityNotFoundExceptionTest(){
         Mockito.when(appUserRepository.findByEmail("wrong@gmail.com")).thenThrow(EntityNotFoundException.class);
         assertThrows(EntityNotFoundException.class,()->appUserService.findAppUserByEmail("wrong@gmail.com"));
     }

     @Test
     void shouldFindAppUserByEmailTest() {
         var appUser = TestData.getAppUser();
         Mockito.when(appUserRepository.findByEmail("masa@gmail.com")).thenReturn(Optional.of(appUser));
         var user = appUserService.findAppUserByEmail("masa@gmail.com");
         assertEquals( appUser, user);
         assertNotNull(user);
     }


     @Test
     void shouldFindAllUsersTest() {
         var appUser = TestData.getAppUser();
         Mockito.when(appUserRepository.findAll()).thenReturn(List.of(appUser));
         List<UserDTO> users =appUserService.findAllUsers().getUsers();
         assertEquals(1,users.size());
         assertEquals( "masa@gmail.com", users.get(0).getEmail());
     }

     @Test
     void shouldLoginUserTest(){
         var appUser = TestData.getAppUser();
         var authReqBodyDto = TestData.getAuthReqBodyDto();

         Mockito.when(passwordEncoder.matches(authReqBodyDto.password(), appUser.getPassword())).thenReturn(true);
         Mockito.when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         Mockito.when(appUserRepository.findByEmail(authReqBodyDto.email())).thenReturn(Optional.of(appUser));
         Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
         Mockito.when(jwtService.generateToken(any(AppUser.class))).thenReturn("token");

         var loginRes = appUserService.loginUser(authReqBodyDto);
         assertEquals(appUser.getEmail(), loginRes.getEmail());
         assertEquals("token", loginRes.getToken() );

         verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
     }
     @Test
     @DisplayName("Bad credentials exception should be thrown when email or password is incorrect")
     void shouldThrowBadCredentialsExceptionTest(){
         var authReqDto = TestData.getAuthReqBodyDto();
         var appUser = TestData.getAppUser();
         when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         when(appUserRepository.findByEmail(any(String.class))).thenReturn(Optional.of(appUser));
         when(passwordEncoder.matches(authReqDto.password(),appUser.getPassword())).thenReturn(false);
         assertThrows(BadCredentialsException.class,()->appUserService.loginUser(authReqDto));
     }
     @Test
     void shouldValidateEmailTest(){
         var user =TestData.getAppUser();
         Mockito.when(appUserRepository.findByValidationCode("code")).thenReturn(Optional.of(user));
         assertEquals("successfully validated", appUserService.validateEmail("code").getMessage() );
     }
     @Test
     void shouldHandleForgotPasswordTest(){
         var appUser = TestData.getAppUser();
         Mockito.when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         when(appUserRepository.findByEmail(any(String.class))).thenReturn(Optional.of(appUser));
         Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");

         assertEquals("successfully sent new password to "+ appUser.getEmail(),
                 appUserService.handleForgotPassword(new ForgotPasswordDTO(appUser.getEmail())).getMessage());

     }
     @Test
     void shouldHandleChangePasswordTest(){
         var appUser = TestData.getAppUser();
         var changePasswordDto = new ChangePasswordReqDto("password","newpassword");

         Mockito.when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         Mockito.when(httpServletRequest.getAttribute("email")).thenReturn(appUser.getEmail());
         when(appUserRepository.findByEmail(any(String.class))).thenReturn(Optional.of(appUser));
         Mockito.when(passwordEncoder.matches(changePasswordDto.oldPassword(),appUser.getPassword())).thenReturn(true);
         Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");

         assertEquals("successfully changed your password",
                 appUserService.handleChangePassword(changePasswordDto, httpServletRequest).getMessage());
     }
     @Test
     @DisplayName("Should throw PasswordMismatchException when old and new passwords don't match")
     void shouldThrowPasswordMismatchExceptionTest(){
         var changePasswordDto = new ChangePasswordReqDto("password","newpassword");
           var appUser = TestData.getAppUser();
         Mockito.when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         Mockito.when(httpServletRequest.getAttribute("email")).thenReturn(appUser.getEmail());
         when(appUserRepository.findByEmail(any(String.class))).thenReturn(Optional.of(appUser));
         Mockito.when(passwordEncoder.matches(changePasswordDto.oldPassword(),appUser.getPassword())).thenReturn(false);
         Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");
         assertThrows(PasswordMismatchException.class,()->appUserService.handleChangePassword(changePasswordDto,httpServletRequest));
     }
     @Test
     void shouldDeleteAppUserTest(){
         var appUser = TestData.getAppUser();
         Mockito.when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
         Mockito.when(appUserRepository.existsById(appUser.getId())).thenReturn(true);
         var res = appUserService.deleteAppUser(appUser.getId());

         assertNotNull(res);
         assertEquals("successfully deleted", res.getMessage());

     }
 }