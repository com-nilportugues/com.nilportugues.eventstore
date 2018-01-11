package com.nilportugues.eventstore;

import java.util.List;

public interface EventStream extends List<Event> {
    long version();

    void addAll(List<Event> changes);
}