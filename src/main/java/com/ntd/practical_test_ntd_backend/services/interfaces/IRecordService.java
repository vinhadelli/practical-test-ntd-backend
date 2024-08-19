package com.ntd.practical_test_ntd_backend.services.interfaces;

import org.springframework.data.domain.Page;

import com.ntd.practical_test_ntd_backend.dto.RecordDTO;

public interface IRecordService {
    Page<RecordDTO> getUserRecords(Long userId, int page, int pageItemCount, String sortBy, String direction, String search, int operationType);
    void deleteRecord(Long userId, Long recordId);
}
