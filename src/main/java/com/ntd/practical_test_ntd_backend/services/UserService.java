package com.ntd.practical_test_ntd_backend.services;

import java.math.BigDecimal;
import java.util.List;

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

    public User getUser(Long id)
    {
        return userRepository.findById(id).orElseThrow(() ->new RuntimeException("User not found"));
    }

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    @Transactional
    public void createUser(User user)
    {
        userRepository.save(user);
        addBalance(user, 20.0);
    }

    public Double getUserBalance(Long userId)
    {
        Record record = recordRepository.findTopByOrderByCreationDate(userId);
        return record.getUserBalance();
    }

    public void addBalance(User user, Double amount)
    {
        Operation operation = operationRepository.findByType(OperationTypesEnum.ADD_CREDITS.Value);
        recordRepository.save(new Record(operation, user, amount, getUserBalance(user.getId()), "CREDITS ADDED SUCCESSFULLY"));
    }
}
