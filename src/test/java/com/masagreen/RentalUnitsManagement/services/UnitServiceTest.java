// package com.masagreen.RentalUnitsManagement.services;

// import com.masagreen.RentalUnitsManagement.models.Unit;
// import com.masagreen.RentalUnitsManagement.repositories.UnitRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;

// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// @SpringBootTest
// class UnitServiceTest {

//     private Unit unit;
//     private long id;

//     @Autowired
//     private UnitService unitService;
//     @MockBean
//     private UnitRepository unitRepository;

//     @BeforeEach
//     void setUp(){
//         id=1;
//         unit =  Unit.builder()
//                .id(id).plotName("plotA").unitNumber("plotA-A1").rent(10).tag("unavailable").status(false).build();

//     }

//     @Test
//     void saveUnit() {
//         Mockito.when(unitRepository.save(unit)).thenReturn(unit);
//         assertEquals(unitService.saveUnit(unit).getId(),unit.getId());
//     }

//     @Test
//     void findByUnitNumber() {
//         Mockito.when(unitRepository.findByUnitNumber("plotA-A1")).thenReturn(Optional.of(unit));
//         assertTrue(unitService.findByUnitNumber("plotA-A1").isPresent());
//     }

//     @Test
//     void findAllUnits() {
//         Mockito.when(unitRepository.findAll()).thenReturn(List.of(unit));
//         assertEquals(unitService.findAllUnits().size(), 1);
//     }

//     @Test
//     void deleteUnit() {
//         Mockito.when(unitRepository.save(unit)).thenReturn(unit);

//         unitRepository.deleteById(id);
//         assertTrue(unitService.findAllUnits().size() == 0);
//     }

//     @Test
//     void getSingleUnit() {
//         Mockito.when(unitRepository.findById(id)).thenReturn(Optional.of(unit));
//         assertTrue(unitService.getSingleUnit("1").isPresent());
//     }
// }