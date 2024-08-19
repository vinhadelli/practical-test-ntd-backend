package com.ntd.practical_test_ntd_backend.services;

import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import com.ntd.practical_test_ntd_backend.services.interfaces.IUserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private IUserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        user = new User(1L, "user@test.com", "password", true);
        userDTO = new UserDTO("user@test.com", "password");
    }

    // JUnit test for createUser, it also tests if the balance was added!
    @DisplayName("JUnit test for createUser method")
    @Test
    public void createUserTest(){
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        userService.createUser(userDTO);

        User savedUser = userService.getByUsername(userDTO.getUsername());
        Double userBalance = userService.getUserBalance(savedUser.getId());

        assertThat(savedUser).isNotNull();
        assertThat(userBalance).isEqualTo(20.0);
    }
}
