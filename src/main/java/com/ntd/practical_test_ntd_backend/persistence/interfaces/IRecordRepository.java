package com.ntd.practical_test_ntd_backend.persistence.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ntd.practical_test_ntd_backend.entities.Record;

@Repository
public interface IRecordRepository extends JpaRepository<Record, Long>
{
    @Query("select r from Record r where r.user.id = ?1 order by r.creationDate desc")
    public Record findTopByOrderByCreationDate (Long userId);
}
