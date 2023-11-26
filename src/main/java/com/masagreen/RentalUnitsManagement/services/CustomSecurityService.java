package com.masagreen.RentalUnitsManagement.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.masagreen.RentalUnitsManagement.repositories.AppUserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomSecurityService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         // using email as my username
        UserDetails userDetails = appUserRepository.findByEmail(username).orElseThrow(()-> new EntityNotFoundException(username +"  not found"));
        return userDetails;
    }
    

}
