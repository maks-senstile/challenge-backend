package org.example.domain.model.users;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    List<User> findAllUsers(long offset, int count);
}
