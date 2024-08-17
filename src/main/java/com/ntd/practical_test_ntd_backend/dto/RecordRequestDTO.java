package com.ntd.practical_test_ntd_backend.dto;

import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecordRequestDTO {
    private int page;
    private int pageItemCount;
    private String sortBy;
    private String sortDirection;
    private String searchResult;
    private int operationType;
}
