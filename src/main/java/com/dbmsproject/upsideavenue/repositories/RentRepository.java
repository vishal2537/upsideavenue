package com.dbmsproject.upsideavenue.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbmsproject.upsideavenue.models.Rent;

public interface RentRepository extends JpaRepository<Rent, UUID> {
    @Query("""
            SELECT r from Rent r\s
            where r.property.propertyId = :propertyId\s
            """)
    List<Rent> findAllByProperty(UUID propertyId);

    @Query("""
            SELECT r from Rent r\s
            where r.renter.username = :renter\s
            """)
    List<Rent> findAllByRenter(String renter);
}
