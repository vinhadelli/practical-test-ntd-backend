package com.ntd.practical_test_ntd_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntd.practical_test_ntd_backend.entities.Operation;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IOperationRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IUserRepository;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRecordRepository recordRepository;
    @Autowired
    private IOperationRepository operationRepository;

    private Double createdUserCredits = 20.0;

    // Function to find a user by it's ID.
    // Parameter: {id} - Id of the User.
    // Returns: User - The User returned by the repository or null, if it doesn't exist.
    public User getUser(Long id)
    {
        return userRepository.findById(id).orElse(null);
    }

    // Function to find a user by it's username
    // Parameter: {username} - Username/Email of the User.
    // Returns: User - The User returned by the repository or null, if it doesn't exist.
    public User getByUsername(String username)
    {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Function to create a new User. The new User is created with the default balace of 20 credits and it's active by default.
    // Parameter: {user} - An user object containing the username and the hash of the password.
    @Transactional
    public void createUser(User user)
    {
        userRepository.save(user);
        addBalance(user, this.createdUserCredits);
    }

    // Function to get the updated balance of the user. It queries the records table.
    // Parameter: {userId} - Id of the user to be queried.
    // Returns: Double - The User's balace. 
    public Double getUserBalance(Long userId)
    {
        Record record = recordRepository.findTopByOrderByCreationDate(userId);
        return record.getUserBalance();
    }

    // Function to add an amount of credits to a user.
    // Parameter: {user} - User to receive the deposit.
    // Parameter: {amount} - The amount to be credited for the user.
    public void addBalance(User user, Double amount)
    {
        Operation operation = operationRepository.findByType(OperationTypesEnum.ADD_CREDITS.Value);
        recordRepository.save(new Record(operation, user, amount, getUserBalance(user.getId()), "CREDITS ADDED SUCCESSFULLY"));
    }
}
