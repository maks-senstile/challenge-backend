package org.example.service.integrations.jpa.repositories;

import org.example.domain.model.users.User;
import org.example.domain.model.users.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface JpaUserRepository extends UserRepository {

    @Override
    @Query(value = "SELECT * FROM users u WHERE u.id = :id", nativeQuery = true)
    Optional<User> findById(Long id);

    @Override
    @Query(value = "SELECT * FROM users u LIMIT :count OFFSET :offset", nativeQuery = true)
    List<User> findAllUsers(long offset, int count);
}
