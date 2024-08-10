package com.ntd.practical_test_ntd_backend.resources;

import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.services.CalculatorService;
import com.ntd.practical_test_ntd_backend.services.RandomService;
import com.ntd.practical_test_ntd_backend.services.UserService;
// import io.swagger.annotations.ApiOperation;
// import io.swagger.annotations.ApiResponse;
// import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CalculatorService calculatorService;
    @Autowired
    private RandomService randomService;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces="application/json")
    // @ApiOperation(value = "Return a List containing all Users.")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "Returns all Users"),
    //         @ApiResponse(code = 403, message = "You're not authorized to access this resource"),
    //         @ApiResponse(code = 500, message = "Internal Server Error"),
    // })
    public List<User> GetAll()
    {
        return userService.getAll();
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces="application/json")
    public void CreateUser(User user)
    {
        userService.createUser(user);
    }
    @RequestMapping(value = "/usercreate", method = RequestMethod.GET, produces="application/json")
    public void CreateUser()
    {
        User user = new User("nathan@fenrir.app.br", "password".hashCode()+"", true);
        userService.createUser(user);
    }
    @GetMapping("/getbalance")
    public Double GetUserBalance(Long userId)
    {
        return userService.getUserBalance(userId);
    }
    @GetMapping("/random")
    public String getRandomString()
    {
        return randomService.generateRandomString(1);
    }
}
