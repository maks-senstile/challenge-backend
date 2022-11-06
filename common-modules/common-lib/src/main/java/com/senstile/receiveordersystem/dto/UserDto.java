package com.senstile.receiveordersystem.dto;

import lombok.Data;

import java.util.List;


@Data
public class UserDto {

    private long id;
    private String firstName;
    private List<AddressDto> addresses;

}
