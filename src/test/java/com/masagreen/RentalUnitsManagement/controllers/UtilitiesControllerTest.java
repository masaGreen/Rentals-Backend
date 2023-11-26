// package com.masagreen.RentalUnitsManagement.controllers;

// import com.masagreen.RentalUnitsManagement.models.Unit;
// import com.masagreen.RentalUnitsManagement.models.UtilitiesPayments;
// import com.masagreen.RentalUnitsManagement.services.UtilitiesPaymentsService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// import java.time.LocalDateTime;
// import java.util.List;

// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// @SpringBootTest
// @AutoConfigureMockMvc
// class UtilitiesControllerTest {
//     private UtilitiesPayments utilitiesPayments;
//     private  Unit unit;
//     @MockBean
//     private UtilitiesPaymentsService utilitiesPaymentsService;
//     @Autowired
//     private MockMvc mockMvc;

//     @BeforeEach
//     private void setUP(){
//         long id = 1;
//         unit = Unit.builder().id(id).plotName("pe").unitNumber("dlo-s1").tag("available").status(true).rent(410).build();
//         utilitiesPayments = UtilitiesPayments.builder()
//                 .id(id)
//                 .unit(unit)
//                 .date(LocalDateTime.now())
//                 .amountPaid("10000")
//                 .status("paid")
//                 .waterBill("100")
//                 .garbage("50")
//                 .unitNumber("dlo-s1")
//                 .build();
//     }
//     @Test
//     void getAllUtilsPayments() throws Exception {
//         Mockito.when(utilitiesPaymentsService.getAllUtils()).thenReturn(List.of(utilitiesPayments));

//         mockMvc.perform(MockMvcRequestBuilders.get("/v1/utilities")
//                 .contentType(MediaType.APPLICATION_JSON)

//         ).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1));
//     }

//     @Test
//     void findByUnitNumber() throws Exception {
//         Mockito.when(utilitiesPaymentsService.findByUnitNumber("dlo-s1")).thenReturn(List.of(utilitiesPayments));

//         mockMvc.perform(MockMvcRequestBuilders.get("/v1/utilities/getByUnit/dlo-s1")
//                 .contentType(MediaType.APPLICATION_JSON)

//         ).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1));
//     }
// }