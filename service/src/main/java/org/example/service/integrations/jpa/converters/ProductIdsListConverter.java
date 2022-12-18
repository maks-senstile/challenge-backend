package org.example.service.integrations.jpa.converters;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductIdsListConverter implements AttributeConverter<List<Long>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Long> ids) {
        if(Objects.isNull(ids)) {
            return null;
        } else {
            return ids.stream().map(Objects::toString).collect(Collectors.joining(DELIMITER));
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String s) {
        if(Objects.isNull(s)) {
            return null;
        } else {
            return Arrays.stream(s.split(DELIMITER)).map(Long::parseLong).collect(Collectors.toList());
        }
    }
}
