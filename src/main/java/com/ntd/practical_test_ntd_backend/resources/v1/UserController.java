package com.ntd.practical_test_ntd_backend.resources.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ntd.practical_test_ntd_backend.utils.AuthUtils;
import com.ntd.practical_test_ntd_backend.dto.TokenDTO;
import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.services.interfaces.IUserService;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * Creates a new User and gives it the default value of credits.
     * @param user UserDTO
     * @return ResponseEntity
     */
    @RequestMapping(value = "/v1/auth/signup", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity CreateUser(@RequestBody UserDTO user)
    {
        try {
            userService.createUser(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(true);
    }

    /**
     * Login function
     * @param loginUserDto UserDTO
     * @return ResponseEntity
     */
    @PostMapping("/v1/auth/login")
    public ResponseEntity login(@RequestBody UserDTO loginUserDto) 
    {
        TokenDTO token;
        try {
            token = userService.login(loginUserDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(token);
    }

    /**
     * Returns the user balance
     * @return ResponseEntity
     */
    @GetMapping("/v1/user/balance")
    public ResponseEntity GetUserBalance()
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            return ResponseEntity.ok(userService.getUserBalance(userId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
