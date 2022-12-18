package org.example.domain.model.orders.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Schedule {

    private Urgency urgency;

    private Instant executeAt;

    public static Schedule executeAsap() {
        return new Schedule(Urgency.ASAP, Instant.now());
    }

    public static Schedule executeAt(Instant at) {
        return new Schedule(Urgency.SCHEDULED, at);
    }

    public boolean isAsapExecutionRequired() {
        return Urgency.ASAP == this.urgency;
    }

    @Override
    public String toString() {
        if(Urgency.ASAP == this.urgency) {
            return Urgency.ASAP.name();
        } else {
            return this.executeAt.toString();
        }
    }

    public static Schedule parse(String origin) {
        if(Objects.isNull(origin)) {
            return null;
        } if(Urgency.ASAP.name().equals(origin)) {
            return Schedule.executeAsap();
        } else {
            return Schedule.executeAt(Instant.parse(origin));
        }
    }

    public enum Urgency {
        ASAP, SCHEDULED
    }
}
