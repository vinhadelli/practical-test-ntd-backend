package com.ntd.practical_test_ntd_backend.resources.v1;

import com.ntd.practical_test_ntd_backend.utils.AuthUtils;
import com.ntd.practical_test_ntd_backend.dto.RecordDTO;
import com.ntd.practical_test_ntd_backend.dto.RecordRequestDTO;
import com.ntd.practical_test_ntd_backend.services.RecordService;
import com.ntd.practical_test_ntd_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/v1/record", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity GetUserRecords(@RequestBody RecordRequestDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            Page<RecordDTO> result = recordService.getUserRecords(userId, request.getPageNumber(), request.getPageItemCount(), request.getSortBy(), request.getSortDirection(), request.getSearchResult(), request.getOperationType());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/v1/record/{id}", method = RequestMethod.DELETE, produces="application/json")
    public ResponseEntity DeleteUserRecord(@PathVariable Long id)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            recordService.deleteRecord(userId, id);
            return ResponseEntity.ok(null);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
