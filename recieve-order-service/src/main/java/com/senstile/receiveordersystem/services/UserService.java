package com.senstile.receiveordersystem.services;

import com.senstile.receiveordersystem.aspect.LogExecutionTime;
import com.senstile.receiveordersystem.model.User;
import com.senstile.receiveordersystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @LogExecutionTime
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @LogExecutionTime
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
