package com.ntd.practical_test_ntd_backend.persistence.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ntd.practical_test_ntd_backend.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>
{

}
