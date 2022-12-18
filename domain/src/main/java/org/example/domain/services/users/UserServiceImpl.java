package org.example.domain.services.users;

import lombok.RequiredArgsConstructor;
import org.example.domain.exceptions.EntityNotFoundDomainException;
import org.example.domain.model.users.User;
import org.example.domain.model.users.UserRepository;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundDomainException::new);
    }

    @Override
    public List<User> getAllUsers(long offset, int count) {
        return repository.findAllUsers(offset, count);
    }
}
