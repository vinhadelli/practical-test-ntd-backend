package com.ntd.practical_test_ntd_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntd.practical_test_ntd_backend.dto.TokenDTO;
import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.entities.Operation;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import com.ntd.practical_test_ntd_backend.exception.RecordNotFoundException;
import com.ntd.practical_test_ntd_backend.exception.UsernameAlreadyExistFoundException;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IOperationRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IUserRepository;
import com.ntd.practical_test_ntd_backend.services.interfaces.IJwtService;
import com.ntd.practical_test_ntd_backend.services.interfaces.IUserService;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRecordRepository recordRepository;
    @Autowired
    private IOperationRepository operationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IJwtService jwtService;

    private Double createdUserCredits = 20.0;

    // Function to find a user by its ID.
    // Parameter: {id} - ID of the User.
    // Returns: User - The User returned by the repository or null, if it doesn't exist.
    public User getUser(Long id)
    {
        return userRepository.findById(id).orElse(null);
    }

    // Function to find a user by its username
    // Parameter: {username} - Username/Email of the User.
    // Returns: User - The User returned by the repository or null, if it doesn't exist.
    public User getByUsername(String username)
    {
        return userRepository.findByUsername(username).orElse(null);
    }

    public TokenDTO login(UserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        User user = getByUsername(input.getUsername());

        String jwtToken = jwtService.generateToken(user);

        return new TokenDTO(jwtToken, jwtService.getExpirationTime());
    }

    // Function to create a new User. The new User is created with the default balance of 20 credits, and it's active by default.
    // Parameter: {user} - An UserDTO object containing the username and the password of the user.
    @Transactional
    public void createUser(UserDTO input)
    {
        User user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setStatus(true);

        try{
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new UsernameAlreadyExistFoundException();
        }
        addBalanceToNewUser(user);
    }

    // Function to get the updated balance of the user. It queries the records table.
    // Parameter: {userId} - Id of the user to be queried.
    // Returns: Double - The User's balace. 
    public Double getUserBalance(Long userId)
    {
        Record record = recordRepository.findTopByUserIdOrderByCreationDateDesc(userId);
        if(record != null)
            return record.getUserBalance();
        else
            throw new RecordNotFoundException();
    }

    // Function to add an amount of credits to a user.
    // Parameter: {user} - User to receive the deposit.
    // Parameter: {amount} - The amount to be credited for the user.
    public void addBalance(User user, Double amount)
    {
        Operation operation = operationRepository.findByType(OperationTypesEnum.ADD_CREDITS.Value);
        Record record = new Record(operation, user, amount, getUserBalance(user.getId()), "CREDITS ADDED SUCCESSFULLY");
        recordRepository.save(record);
    }

    // Function to add the default amount of credits to a new user.
    // Parameter: {user} - User to receive the deposit.
    public void addBalanceToNewUser(User user)
    {
        Operation operation = operationRepository.findByType(OperationTypesEnum.ADD_CREDITS.Value);
        Record record = new Record(operation, user, createdUserCredits, createdUserCredits, "CREDITS ADDED SUCCESSFULLY");
        recordRepository.save(record);
    }
}
