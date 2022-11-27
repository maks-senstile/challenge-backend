package com.example.rest;

import com.example.model.User;
import com.example.repositories.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class UserController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/find-all-users")
    public List<User> findAll() {
        return context.getBean(UserRepository.class).findAll();
    }

    @GetMapping("/find-user-by-id/{id}")
    public User findById(@PathVariable Long id) {
        return context.getBean(UserRepository.class).findById(id).orElseThrow();
    }
}
