package com.dbmsproject.upsideavenue.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbmsproject.upsideavenue.models.Property;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query(value = """
                select p from Property p inner join User u\s
                        on p.owner.username = u.username\s
                where u.username = :ownername\s
            """)
    List<Property> findAllPropertyByOwner(String ownername);

    @Query(value = """
                select p from Property p inner join User u\s
                        on p.owner.username = u.username\s
                where u.username = :ownername\s
            """)
    // Available property logic to be implemented here
    List<Property> findAllAvailablePropertyByOwner(String ownername);

}
