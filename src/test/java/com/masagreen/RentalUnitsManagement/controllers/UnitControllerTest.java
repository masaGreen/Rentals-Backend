// package com.masagreen.RentalUnitsManagement.controllers;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.masagreen.RentalUnitsManagement.jwt.JwtService;
// import com.masagreen.RentalUnitsManagement.models.Unit;
// import com.masagreen.RentalUnitsManagement.repositories.UnitRepository;
// import com.masagreen.RentalUnitsManagement.services.UnitService;
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


// import java.util.List;

// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// @SpringBootTest
// @AutoConfigureMockMvc
// class UnitControllerTest {

//     private Unit unit;
//       private  long id;
//     @MockBean
//     private JwtService jwtService;

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private UnitService unitService;
//     @MockBean
//     private UnitRepository unitRepository;

//     @BeforeEach
//     void setUp(){
//         id=54464;
//         unit = Unit.builder().id(id).plotName("pe").unitNumber("dlo-s1").tag("available").status(true).rent(410).build();

//     }


//     @Test

//     void rentUnit() throws Exception {

//         Mockito.when(unitService.saveUnit(unit)).thenReturn(unit);

//         mockMvc.perform(MockMvcRequestBuilders.post("/v1/units")
//                              .contentType(MediaType.APPLICATION_JSON)
//                              .content(new ObjectMapper().writeValueAsString(unit))
//                     )
//                 .andExpect(status().isBadRequest());


//     }

//     @Test
//     void getUnits() throws Exception {

//         Mockito.when(unitService.findAllUnits()).thenReturn(List.of(unit));
//         mockMvc.perform(MockMvcRequestBuilders.get("/v1/units")
//                            .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.size()").value(1));
//     }


// }