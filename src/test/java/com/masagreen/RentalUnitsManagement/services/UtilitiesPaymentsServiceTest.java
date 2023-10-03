package com.masagreen.RentalUnitsManagement.services;

import com.masagreen.RentalUnitsManagement.models.Unit;
import com.masagreen.RentalUnitsManagement.models.UtilitiesPayments;
import com.masagreen.RentalUnitsManagement.repositories.UtilitiesPaymentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UtilitiesPaymentsServiceTest {
    private UtilitiesPayments utilitiesPayments;
    private Unit unit;
    private long id;
    @MockBean
    private UtilitiesPaymentsRepository utilitiesPaymentsRepository;
    @Autowired
    private UtilitiesPaymentsService utilitiesPaymentsService;

    @BeforeEach
    void setUp(){
        id=1;
        unit =  Unit.builder()
                .id(id).plotName("plotA").unitNumber("plotA-A1").rent(10).tag("unavailable").status(false).build();
        utilitiesPayments = UtilitiesPayments.builder()
                .id(1).unitNumber("plotA-A1").unit(unit).date(LocalDateTime.now()).amountPaid("10").garbage("5")
                .waterBill("3").status("paid")
                .build();
    }

    @Test
    void getAllUtils() {
        Mockito.when(utilitiesPaymentsRepository.findAll()).thenReturn(List.of(utilitiesPayments));
        assertEquals(utilitiesPaymentsService.getAllUtils().size(), 1);
    }

    @Test
    void findByUnitNumber() {
        Mockito.when(utilitiesPaymentsRepository.findAllByUnitNumber("1"))
                .thenReturn(List.of(utilitiesPayments));
        assertEquals(utilitiesPaymentsService.findByUnitNumber("plotA-A1").size(),0);
    }

    @Test
    void findByStatus() {
        Mockito.when(utilitiesPaymentsRepository.findAllByStatus("1"))
                .thenReturn(List.of(utilitiesPayments));
        assertEquals(utilitiesPaymentsService.findByStatus("plotA-A1").size(), 0);
    }

    @Test
    void saveUtilitiesPayments() {
        Mockito.when(utilitiesPaymentsRepository.save(utilitiesPayments)).thenReturn(utilitiesPayments);
        assertEquals(utilitiesPaymentsService.saveUtilitiesPayments(utilitiesPayments).getId(),utilitiesPayments.getId());
    }

    @Test
    void deleteUtility() {
        Mockito.when(utilitiesPaymentsRepository.save(utilitiesPayments)).thenReturn(utilitiesPayments);
        utilitiesPaymentsService.deleteUtility("1");
        assertTrue(utilitiesPaymentsService.findUtilityById("1").isEmpty());
    }


}