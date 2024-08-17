package com.ntd.practical_test_ntd_backend.resources;

import com.ntd.practical_test_ntd_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.practical_test_ntd_backend.auth.AuthUtils;
import com.ntd.practical_test_ntd_backend.services.RandomService;

@RestController
public class RandomController {
    @Autowired
    private RandomService randomService;
    @Autowired
    private UserService userService;

    @GetMapping("/random/{length}")
    public ResponseEntity<String> getRandomString(int length)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            String result = randomService.generateRandomString(length, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
