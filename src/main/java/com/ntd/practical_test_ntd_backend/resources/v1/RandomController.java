package com.ntd.practical_test_ntd_backend.resources.v1;

import com.ntd.practical_test_ntd_backend.exception.InsufficientBalance;
import com.ntd.practical_test_ntd_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.practical_test_ntd_backend.utils.AuthUtils;
import com.ntd.practical_test_ntd_backend.services.RandomService;

@RestController
@CrossOrigin
public class RandomController {
    @Autowired
    private RandomService randomService;
    @Autowired
    private UserService userService;

    @GetMapping("/v1/random/{length}")
    public ResponseEntity<String> getRandomString(@PathVariable int length)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            String result = randomService.generateRandomString(length, userId);
            return ResponseEntity.ok(result);
        } catch (InsufficientBalance e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
