package com.ntd.practical_test_ntd_backend.persistence.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ntd.practical_test_ntd_backend.entities.Operation;

@Repository
public interface IOperationRepository extends JpaRepository<Operation, Long>
{
    // Find a operatiopn by it's type.
    @Query("select o from Operation o where o.type = ?1")
    public Operation findByType(Integer type);
}
