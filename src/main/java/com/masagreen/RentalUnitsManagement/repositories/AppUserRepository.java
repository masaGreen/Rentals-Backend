package com.masagreen.RentalUnitsManagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masagreen.RentalUnitsManagement.models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer>{

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByPassword(String encode);
    
}
