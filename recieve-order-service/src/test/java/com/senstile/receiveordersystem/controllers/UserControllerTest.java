package com.senstile.receiveordersystem.controllers;

import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.User;
import com.senstile.receiveordersystem.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void testFindAll() throws Exception {
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
        final List<User> users = List.of(user);
        when(mockUserService.findAll()).thenReturn(users);

        final MockHttpServletResponse response = mockMvc.perform(get("/find-all-users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testFindAll_UserServiceReturnsNoItems() throws Exception {
        when(mockUserService.findAll()).thenReturn(Collections.emptyList());

        final MockHttpServletResponse response = mockMvc.perform(get("/find-all-users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testFindById() throws Exception {
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
        when(mockUserService.findById(0L)).thenReturn(user);

        final MockHttpServletResponse response = mockMvc.perform(get("/find-user-by-id/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
