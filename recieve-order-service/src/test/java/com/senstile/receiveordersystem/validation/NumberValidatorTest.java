package com.senstile.receiveordersystem.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberValidatorTest {

    private NumberValidator numberValidatorUnderTest;

    @BeforeEach
    void setUp() {
        numberValidatorUnderTest = new NumberValidator();
    }

    @Test
    void testIsValid() {
        assertThat(numberValidatorUnderTest.isValid("value", null)).isFalse();
    }
}
