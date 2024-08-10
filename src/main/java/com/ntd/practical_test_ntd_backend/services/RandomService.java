package com.ntd.practical_test_ntd_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ntd.practical_test_ntd_backend.entities.Operation;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IOperationRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;

@Service
public class RandomService {
    @Autowired
    private UserService userService;
    @Autowired
    private IRecordRepository recordRepository;
    @Autowired
    private IOperationRepository operationRepository;
    @Value("${api.random.url}")
    private String apiUrl;
    @Value("${api.random.key}")
    private String apiKey;

    public String generateRandomString(int length)
    {
        return apiUrl;
    }

    // This function processes the operation received. It goes to the database to retrieve the correct type of operation
    // and user. It also gets the remaining balance of the user and subtracts for the cost of the operation.
    // Parameter: {type} - Type of operation.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {message} - Message to be stored in the record. Contains the result and the parameters.
    private void ProcessOperation(OperationTypesEnum type, Long userId, String message)
    {
        Operation operation = operationRepository.findByType(type.Value);
        User user = userService.getUser(userId);
        Double remainingBalance = userService.getUserBalance(userId) - operation.getCost();
        recordRepository.save(new Record(operation, user, operation.getCost(), remainingBalance, message));
    }
}
