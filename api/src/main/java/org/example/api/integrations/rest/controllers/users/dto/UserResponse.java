package org.example.api.integrations.rest.controllers.users.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.example.domain.model.users.User;
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    public static UserResponse create(User user) {
        return new UserResponse();
    }
}
