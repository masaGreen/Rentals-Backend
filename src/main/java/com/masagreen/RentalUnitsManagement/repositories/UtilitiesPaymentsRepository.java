package com.masagreen.RentalUnitsManagement.repositories;

import com.masagreen.RentalUnitsManagement.models.entities.UtilitiesPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilitiesPaymentsRepository extends JpaRepository<UtilitiesPayments, String> {

    @Query("SELECT u FROM UtilitiesPayments u WHERE u.status = :status")
    List<UtilitiesPayments> findAllByStatus(@Param("status") String status);

    @Query("SELECT u FROM UtilitiesPayments u WHERE u.unitNumber = :unitNumber")
    List<UtilitiesPayments> findAllByUnitNumber(@Param("unitNumber") String unitNumber);

    Optional<UtilitiesPayments> findByUnitNumber(String unitNumber);
}
