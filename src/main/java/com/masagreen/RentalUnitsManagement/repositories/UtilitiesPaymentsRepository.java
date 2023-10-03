package com.masagreen.RentalUnitsManagement.repositories;

import com.masagreen.RentalUnitsManagement.models.UtilitiesPayments;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface UtilitiesPaymentsRepository extends JpaRepository<UtilitiesPayments, Long> {



    List<UtilitiesPayments> findAllByStatus(String status);

    List<UtilitiesPayments> findAllByUnitNumber(String unit);
}
