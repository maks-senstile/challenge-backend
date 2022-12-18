package org.example.service.integrations.rest.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.domain.model.orders.embedded.Schedule;

import java.io.IOException;

public class ScheduleSerializer extends StdSerializer<Schedule> {

    public ScheduleSerializer() {
        this(null);
    }

    public ScheduleSerializer(Class<Schedule> t) {
        super(t);
    }

    @Override
    public void serialize(Schedule schedule, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(schedule.toString());
    }
}
