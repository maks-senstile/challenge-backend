package com.senstile.receiveordersystem.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberValidator implements ConstraintValidator<ValidNumber, String> {

    public static final String VALID_NUMBER_REGEX = "^[0-9]+$";

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.isEmpty(value)) {
            return value.matches(VALID_NUMBER_REGEX);
        }
        return true;
    }
}
