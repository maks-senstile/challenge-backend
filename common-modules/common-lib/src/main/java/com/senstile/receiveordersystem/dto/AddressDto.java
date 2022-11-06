package com.senstile.receiveordersystem.dto;


import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private UserDto user;
    private String addressLine;
    private String city;
    private String country;
    private String postalCode;

}
