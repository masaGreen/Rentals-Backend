package com.masagreen.RentalUnitsManagement.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SuperClass {
    
    @Id
    private String id;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    void generateUUID(){
    if(id == null){
        this.id = UUID.randomUUID().toString();
    }
    if(createdAt == null){
        this.createdAt = Instant.now();
    }

}

}
