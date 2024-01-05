package com.masagreen.RentalUnitsManagement.repositories;

import com.masagreen.RentalUnitsManagement.models.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByPassword(String encode);

    Boolean existsByEmail(String email);

    Optional<AppUser> findByValidationCode(String code);
}
