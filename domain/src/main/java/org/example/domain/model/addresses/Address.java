package org.example.domain.model.addresses;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {

    @EqualsAndHashCode.Include
    private Long id;

    private Long userId;

    private String addressLine;

    private String city;

    private String country;

    private String postalCode;
}
