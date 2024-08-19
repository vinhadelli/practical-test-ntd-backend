package com.ntd.practical_test_ntd_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecordRequestDTO {
    private int pageNumber;
    private int pageItemCount;
    private String sortBy;
    private String sortDirection;
    private String searchResult;
    private int operationType;
}
