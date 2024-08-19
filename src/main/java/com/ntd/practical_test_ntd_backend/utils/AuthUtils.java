package com.ntd.practical_test_ntd_backend.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ntd.practical_test_ntd_backend.services.interfaces.IUserService;

@Component
public class AuthUtils {

    public static Long getLoggedUserId(IUserService userService)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return userService.getByUsername(name).getId();
    }
}
