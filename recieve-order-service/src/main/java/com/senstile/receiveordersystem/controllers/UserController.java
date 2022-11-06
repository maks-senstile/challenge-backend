package com.senstile.receiveordersystem.controllers;


import com.senstile.receiveordersystem.model.User;
import com.senstile.receiveordersystem.services.UserService;
import com.senstile.receiveordersystem.validation.ValidNumber;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@AllArgsConstructor
@Validated
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/find-all-users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/find-user-by-id/{id}")
    public User findById(@ValidNumber @PathVariable(required = true) Long id) {
        return userService.findById(id);
    }
}
