package com.dbmsproject.upsideavenue.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbmsproject.upsideavenue.models.Photo;

public interface PhotoRepository extends JpaRepository<Photo, UUID>{
    
}
