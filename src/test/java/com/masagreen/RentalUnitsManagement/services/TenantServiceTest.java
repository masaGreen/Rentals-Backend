// package com.masagreen.RentalUnitsManagement.services;

// import com.masagreen.RentalUnitsManagement.models.Tenant;
// import com.masagreen.RentalUnitsManagement.repositories.TenantRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// @SpringBootTest
// class TenantServiceTest {
//     private Tenant tenant;
//     private long id;
//     @Autowired
//     private TenantService tenantService;

//     @MockBean
//     private TenantRepository tenantRepository;

//     @BeforeEach
//     void setup(){
//          id=1;
//          tenant = Tenant.builder()
//                 .id(id).firstName("Dave").lastName("Macha").phone("0712456797").start(LocalDate.now())
//                 .ended(null).payStatus("unpaid")
//                 .build();


//     }


//     @Test
//     void findAllTenants() {
//         Mockito.when(tenantRepository.findAll()).thenReturn(List.of(tenant));
//         List<Tenant> tenants = tenantService.findAllTenants();
//         assertEquals(tenants.size(), 1);
//     }

//     @Test
//     void saveTenant() {
//         Mockito.when(tenantRepository.save(tenant)).thenReturn(tenant);
//        Tenant t =  tenantService.saveTenant(tenant);
//        assertEquals(t.getId(), 1);

//     }

//     @Test
//     void deleteTenant() {
//         Mockito.when(tenantRepository.save(tenant)).thenReturn(tenant);
//         tenantRepository.deleteById(id);
//         assertTrue(tenantService.findByTenantId(id).isEmpty());
//     }

//     @Test
//     void findByPhone() {
//         Mockito.when(tenantRepository.findByPhone("0712456797")).thenReturn(Optional.of(tenant));
//         assertTrue(tenantService.findByPhone("0712456797").isPresent());
//     }

//     @Test
//     void findByTenantId() {
//         Mockito.when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));
//         assertTrue(tenantService.findByTenantId(id).isPresent());
//     }
// }