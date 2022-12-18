package org.example.service.integrations.rest.controllers.users.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.model.addresses.Address;
import org.example.domain.model.users.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    private Long id;

    private String firstName;

    private List<Long> addresses;

    public static UserResponse create(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getAddresses().stream().map(Address::getId).collect(Collectors.toList())
        );
    }
}
