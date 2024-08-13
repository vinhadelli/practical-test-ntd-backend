package com.ntd.practical_test_ntd_backend.persistence.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ntd.practical_test_ntd_backend.entities.Record;

@Repository
public interface IRecordRepository extends JpaRepository<Record, Long>
{
    // Get the latest operation of the user.
    @Query("select r from Record r where r.user.id = ?1 order by r.creationDate desc")
    public Record findTopByOrderByCreationDate (Long userId);
    // Gets all the records of the user that are not deleted.
    @Query("select r from Record r where r.user.id = ?1 and r.deletionDate = null order by r.creationDate desc")
    public Page<Record> findByUser (Long userId, Pageable pageable);
}
