package com.ntd.practical_test_ntd_backend.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;

@Service
public class RecordService {
    
    @Autowired
    private IRecordRepository recordRepository;

    // Function to get the Records of the logged user. It supports pagination and sorting
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {page} - Number of the requested page.
    // Parameter: {pageItemCount} - Numbers of itens per page.
    // Parameter: {sortBy} - Parameter to sort the returning list. Defaults to Creation Date.
    // Returns: Page<Record> - Page of Records.
    public Page<Record> getUserRecords(Long userId, int page, int pageItemCount, String sortBy)
    {
        sortBy = (sortBy == null || sortBy.equals("")) ? "creationDate" : sortBy;
        return recordRepository.findByUser(userId, PageRequest.of(page, pageItemCount, Sort.by(sortBy)));
    }

    // Function to soft-delete the received record.
    // Parameter: {recordId} - The ID of the Record to be deleted.
    public void deleteRecord(Long recordId)
    {
        Record record = recordRepository.findById(recordId).orElse(null);
        record.setDeletionDate(Instant.now());
        recordRepository.save(record);
    }
    
}
