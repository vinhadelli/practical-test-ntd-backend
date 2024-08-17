package com.ntd.practical_test_ntd_backend.utils;

import com.ntd.practical_test_ntd_backend.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    public static Long getLoggedUserId(UserService userService)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return userService.getByUsername(name).getId();
    }
}
