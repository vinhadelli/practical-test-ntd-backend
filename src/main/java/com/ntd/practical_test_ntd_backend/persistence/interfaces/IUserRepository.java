package com.ntd.practical_test_ntd_backend.persistence.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ntd.practical_test_ntd_backend.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>
{
    // Find a user by its username
    @Query("select u from User u where u.username = ?1")
    public Optional<User> findByUsername(String username);
    
}
