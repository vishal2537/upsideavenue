package com.dbmsproject.upsideavenue.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbmsproject.upsideavenue.models.Role;
import com.dbmsproject.upsideavenue.models.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query(value = """
            select u from User u where u.role = :role\s
              """)
    List<User> findAllUserByRole(Role role);
}
