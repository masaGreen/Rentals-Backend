package com.masagreen.RentalUnitsManagement.controllers;

import com.masagreen.RentalUnitsManagement.models.Tenant;
import com.masagreen.RentalUnitsManagement.models.Unit;
import com.masagreen.RentalUnitsManagement.services.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TenantControllerTest {
    private Tenant tenant;
    private Unit unit;
    @MockBean
    private TenantService tenantService;


    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public  void setUp(){


        long id = 1;

        tenant = Tenant.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .phone("0712345678")
                .start(LocalDate.now())
                .ended(null)
                .payStatus("unpaid")
                .unit(unit)
                .build();

    }
    @Test
    void testGetAllTenants() throws Exception {
        Mockito.when(tenantService.findAllTenants()).thenReturn(List.of(tenant));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/tenants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

    }

     @Test
     public void TestFindByPhone() throws Exception {

         Mockito.when(tenantService.findByPhone("0712345678")).thenReturn(Optional.ofNullable(tenant));

         mockMvc.perform(MockMvcRequestBuilders.get("/v1/tenants/getByPhone/0712345678")
                         .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.phone").value("0712345678"));

     }

}