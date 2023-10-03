package com.masagreen.RentalUnitsManagement.services;

import com.masagreen.RentalUnitsManagement.models.AppUser;
import com.masagreen.RentalUnitsManagement.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AppUserServiceTest {
    private int id;
    private AppUser appUser;
    @Autowired
    private AppUserService appUserService;
    @MockBean
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp(){
        id=1;
        appUser = AppUser.builder()
                .id(id).email("root@gmail.com").password("password").role("ADMIN").status(false)
                .build();

    }

    @Test
    void saveUser() {
        Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);
        assertEquals(appUserService.saveUser(appUser),appUser);

    }

    @Test
    void findById() {
        Mockito.when(appUserRepository.findById(1)).thenReturn(Optional.of(appUser));
        assertTrue(appUserService.findById(1).isPresent());

    }

    @Test
    void findByEmail() {
        Mockito.when(appUserRepository.findByEmail("root@gmail.com")).thenReturn(Optional.of(appUser));
        assertTrue(appUserService.findByEmail("root@gmail.com").isPresent());
    }


    @Test
    void findAllUsers() {
        Mockito.when(appUserRepository.findAll()).thenReturn(List.of(appUser));
        assertEquals(appUserService.findAllUsers().size(),1);
    }

    @Test
    void  deleteAppUser() {
        Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);
        appUserService.deleteAppUser("1");
        assertTrue(appUserService.findById(1).isEmpty());
    }
}