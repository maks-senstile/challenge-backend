package org.example.service.integrations.rest.converters;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.domain.model.orders.embedded.Schedule;

import java.io.IOException;

public class ScheduleDeserializer extends StdDeserializer<Schedule> {

    public ScheduleDeserializer() {
        this(null);
    }

    public ScheduleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return Schedule.parse(node.asText());
    }
}
