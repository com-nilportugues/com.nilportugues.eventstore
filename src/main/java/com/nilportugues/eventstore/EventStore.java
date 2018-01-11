package com.nilportugues.eventstore;

import java.util.List;


public interface EventStore {

    boolean exists(String streamName);
    EventStream load(String streamName);
    EventStream load(EventFilter eventFilter);
    void appendTo(String streamName, long version, List<Event> events);
    void delete(String streamName);
}