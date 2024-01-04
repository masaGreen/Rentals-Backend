package com.masagreen.RentalUnitsManagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masagreen.RentalUnitsManagement.models.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, String>{

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByPassword(String encode);

    Boolean existsByEmail(String email);

    Optional<AppUser> findByValidationCode(String code);
}
