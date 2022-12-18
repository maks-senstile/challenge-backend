package org.example.service.integrations.rest.controllers.users;

import lombok.RequiredArgsConstructor;
import org.example.domain.services.users.UserService;
import org.example.service.integrations.rest.controllers.users.dto.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users")
    public List<UserResponse> findAll(@RequestParam("offset") Long offset, @RequestParam("count") Integer count) {
        return service.getAllUsers(offset, count).stream()
                .map(UserResponse::create)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public UserResponse findById(@PathVariable("id") Long id) {
        return UserResponse.create(service.getUserById(id));
    }
}
