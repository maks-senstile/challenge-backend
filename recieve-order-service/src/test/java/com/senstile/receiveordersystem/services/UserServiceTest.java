package com.senstile.receiveordersystem.services;

import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.User;
import com.senstile.receiveordersystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(mockUserRepository);
    }

    @Test
    void testFindAll() {
        // Setup
        final User user = new User();
        user.setId(0L);
        user.setFirstName("firstName");
        final Address address = new Address();
        address.setId(0L);
        address.setAddressLine("addressLine");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");
        user.setAddresses(List.of(address));
        final List<User> expectedResult = List.of(user);

        // Configure UserRepository.findAll(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setFirstName("firstName");
        final Address address1 = new Address();
        address1.setId(0L);
        address1.setAddressLine("addressLine");
        address1.setCity("city");
        address1.setCountry("country");
        address1.setPostalCode("postalCode");
        user1.setAddresses(List.of(address1));
        final List<User> users = List.of(user1);
        when(mockUserRepository.findAll()).thenReturn(users);

        // Run the test
        final List<User> result = userServiceUnderTest.findAll();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindAll_UserRepositoryReturnsNoItems() {
        // Setup
        when(mockUserRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<User> result = userServiceUnderTest.findAll();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testFindById() {
        // Setup
        final User expectedResult = new User();
        expectedResult.setId(0L);
        expectedResult.setFirstName("firstName");
        final Address address = new Address();
        address.setId(0L);
        address.setAddressLine("addressLine");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");
        expectedResult.setAddresses(List.of(address));

        // Configure UserRepository.findById(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setFirstName("firstName");
        final Address address1 = new Address();
        address1.setId(0L);
        address1.setAddressLine("addressLine");
        address1.setCity("city");
        address1.setCountry("country");
        address1.setPostalCode("postalCode");
        user1.setAddresses(List.of(address1));
        final Optional<User> user = Optional.of(user1);
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Run the test
        final User result = userServiceUnderTest.findById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindById_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.findById(0L)).isInstanceOf(NoSuchElementException.class);
    }
}
