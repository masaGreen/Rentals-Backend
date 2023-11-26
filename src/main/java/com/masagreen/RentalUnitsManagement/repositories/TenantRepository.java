package com.masagreen.RentalUnitsManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.masagreen.RentalUnitsManagement.models.entities.Tenant;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant,String> {

    Optional<Tenant> findByPhone(String id);

    @Query("SELECT t FROM Tenant t WHERE t.payStatus = 'unpaid' ")
    List<Tenant> findAllTenantsWithArrears();
    
}
