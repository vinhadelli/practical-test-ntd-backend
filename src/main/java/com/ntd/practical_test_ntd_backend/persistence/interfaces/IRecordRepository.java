package com.ntd.practical_test_ntd_backend.persistence.interfaces;

import com.ntd.practical_test_ntd_backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ntd.practical_test_ntd_backend.entities.Record;

@Repository
public interface IRecordRepository extends JpaRepository<Record, Long>
{
    // Get the latest operation of the user.
    Record findTopByUserIdOrderByCreationDateDesc(Long userId);
    // Gets all the records of the user that are not deleted.
    @Query("select r from Record r where r.user.id = ?1 and r.deletionDate is null")
    Page<Record> findByUser(Long userId, Pageable pageable);
    // Gets all the records of the user that are not deleted, and searches the result column for the provided string.
    @Query("select r from Record r where r.user.id = ?1 and r.operationResponse like CONCAT('%',?2,'%') and r.deletionDate is null")
    Page<Record> findByUserWithSearch(Long userId, String search, Pageable pageable);
    // Gets all the records of the user that are not deleted, and filters then by the provided operation type.
    @Query("select r from Record r where r.user.id = ?1 and r.operation.type = ?2 and r.deletionDate is null")
    Page<Record> findByUserWithOperation(Long userId, int operationType, Pageable pageable);
    // Gets all the records of the user that are not deletes, filters then by the provided operation type and searches the result column for the provided string.
    @Query("select r from Record r where r.user.id = ?1 and r.operation.type = ?2 and r.operationResponse like concat('%',?3,'%') and r.deletionDate is null")
    Page<Record> findByUserWithSearchAndOperation(Long userId, int operationType, String search, Pageable pageable);
    // Created for use in the Tests
    @Modifying
    @Query("delete from Record r where r.user.id = ?1")
    void deleteByUser(Long id);
}
