package com.ntd.practical_test_ntd_backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.practical_test_ntd_backend.Auth.AuthUtils;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired

    @RequestMapping(value = "/user/create", method = RequestMethod.POST, produces="application/json")
    public void CreateUser(User user)
    {
        userService.createUser(user);
    }
    @GetMapping("/user/balance")
    public Double GetUserBalance()
    {
        Long userId = AuthUtils.getLoggedUserId();
        return userService.getUserBalance(userId);
    }
}
