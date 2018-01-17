package com.nilportugues.eventstore;

import java.time.ZonedDateTime;

public class Event {
    private String eventId;
    private String eventName;
    private String eventVersion;
    private String payload;
    private String aggregateId;
    private String aggregateName;
    private String metadata;
    private ZonedDateTime occurredOn;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(final String eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(final String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateName() {
        return aggregateName;
    }

    public void setAggregateName(final String aggregateName) {
        this.aggregateName = aggregateName;
    }

    public ZonedDateTime getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(final ZonedDateTime occurredOn) {
        this.occurredOn = occurredOn;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }
}
