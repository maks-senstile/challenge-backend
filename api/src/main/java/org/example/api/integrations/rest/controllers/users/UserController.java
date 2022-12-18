package org.example.api.integrations.rest.controllers.users;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.example.api.integrations.rest.controllers.users.dto.UserResponse;
import org.example.domain.services.users.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public UserResponse getById(@PathVariable("id") Long id) {
        return UserResponse.create(userService.getUserById(id));
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers(@RequestParam("offset") long offset, @RequestParam("count") int count) {
        return userService.getAllUsers(offset, count).stream()
                .map(UserResponse::create)
                .collect(Collectors.toList());
    }
}
