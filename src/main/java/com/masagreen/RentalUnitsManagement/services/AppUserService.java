package com.masagreen.RentalUnitsManagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.masagreen.RentalUnitsManagement.models.AppUser;
import com.masagreen.RentalUnitsManagement.repositories.AppUserRepository;
@Service
public class AppUserService implements UserDetailsService{

    @Autowired
    private AppUserRepository appUserRepository;
    // using email as my username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
           
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        if(user.isEmpty()) throw new UsernameNotFoundException(email+"user with that email not found");
        return user.get();
    }

    public AppUser saveUser(AppUser user){
        
        Optional<AppUser> userExists =  appUserRepository.findByEmail(user.getEmail());
        
        if(userExists.isEmpty()){
            return appUserRepository.save(user);
        }
        return null;
    }
    public AppUser updateUser(AppUser user){
       
        return appUserRepository.save(user);
    } 

    public Optional<AppUser> findById(int id) {
        return appUserRepository.findById(id);
    }
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }


    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }


    public String deleteAppUser(String id) {
        Optional<AppUser> user = appUserRepository.findById(Integer.parseInt(id));

        if (user.isPresent()){
            appUserRepository.deleteById(Integer.parseInt(id));
            return null;
        }
        return "id doesn't exist";
    }
}
