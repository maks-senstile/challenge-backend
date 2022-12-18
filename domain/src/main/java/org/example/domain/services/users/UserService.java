package org.example.domain.services.users;

import org.example.domain.model.users.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers(long offset, int count);
}
