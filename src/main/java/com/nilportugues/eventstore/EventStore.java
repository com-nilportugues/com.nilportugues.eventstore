package com.nilportugues.eventstore;

import java.util.List;
import java.util.stream.Stream;


public interface EventStore {

    boolean exists(String streamName);
    Stream<Event> load(String streamName);
    Stream<Event> load(EventFilter eventFilter);
    void appendTo(String streamName, long version, List<Event> events);
    void delete(String streamName);
}