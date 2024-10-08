package com.ntd.practical_test_ntd_backend.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ntd.practical_test_ntd_backend.dto.RecordDTO;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.exception.RecordNotFoundException;
import com.ntd.practical_test_ntd_backend.exception.UnexpectedUserException;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;
import com.ntd.practical_test_ntd_backend.services.interfaces.IRecordService;
import com.ntd.practical_test_ntd_backend.utils.OperationUtils;

@Service
public class RecordService implements IRecordService {
    
    @Autowired
    private IRecordRepository recordRepository;

    // Function to get the Records of the logged user. It supports pagination and sorting
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {page} - Number of the requested page.
    // Parameter: {pageItemCount} - Numbers of itens per page.
    // Parameter: {sortBy} - Parameter to sort the returning list. Defaults to Creation Date.
    // Parameter: {direction} - Direction of the Sort
    // Parameter: {search} - String to search for in the Operation Results
    // Returns: Page<Record> - Page of Records.
    @Override
    public Page<RecordDTO> getUserRecords(Long userId, int page, int pageItemCount, String sortBy, String direction, String search, int operationType)
    {
        sortBy = parseSortParameters(sortBy == null ? "" : sortBy);
        if(direction == null)
        {
            direction = "desc";
        }
        Sort.Direction direct = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direct, sortBy);
        Pageable pageable = PageRequest.of(page, pageItemCount, sort);
        Page<Record> result;
        if(search != null && !search.isEmpty())
        {
            if (operationType != 0)
            {
                result = recordRepository.findByUserWithSearchAndOperation(userId,operationType, search,pageable);
            }
            else
            {
                result = recordRepository.findByUserWithSearch(userId, search, pageable);
            }
        }
        else if (operationType != 0)
        {
            result = recordRepository.findByUserWithOperation(userId, operationType, pageable);
        }
        else
        {
            result = recordRepository.findByUser(userId,pageable);
        }
        return result.map(record -> new RecordDTO(record.getId(),
                OperationUtils.getNameOfOperation(record.getOperation().getType()), record.getAmount(),
                record.getUserBalance(),
                record.getOperationResponse(),
                record.getCreationDate()));
    }
    // Function to translate the received sortby string to the value in the Records table
    private String parseSortParameters(String sortBy) {
        return switch (sortBy) {
            case "result" -> "operationResponse";
            case "amount" -> "amount";
            default -> "creationDate";
        };
    }

    // Function to soft-delete the received record.
    // Parameter: {recordId} - The ID of the Record to be deleted.
    // Throws: UnexpectedUserException
    @Override
    public void deleteRecord(Long userId, Long recordId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        if(record.getUser().getId() != userId) //Throw in case the logged user is not the owner of the record
            throw new UnexpectedUserException();
        record.setDeletionDate(Instant.now());
        recordRepository.save(record);
    }
}
