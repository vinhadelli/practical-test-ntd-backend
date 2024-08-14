package com.ntd.practical_test_ntd_backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.practical_test_ntd_backend.auth.AuthUtils;
import com.ntd.practical_test_ntd_backend.services.RandomService;

@RestController
public class RandomController {
    @Autowired
    private RandomService randomService;

    @GetMapping("/random")
    public String getRandomString(int length)
    {
        Long userId = AuthUtils.getLoggedUserId();
        return randomService.generateRandomString(length, userId);
    }
}
