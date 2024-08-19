package com.ntd.practical_test_ntd_backend.services.interfaces;

import com.ntd.practical_test_ntd_backend.dto.TokenDTO;
import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.entities.User;

public interface IUserService {
    User getUser(Long id);
    User getByUsername(String username);
    TokenDTO login(UserDTO input);
    void createUser(UserDTO input);
    Double getUserBalance(Long userId);
    void addBalance(User user, Double amount);
    void addBalanceToNewUser(User user);
}
