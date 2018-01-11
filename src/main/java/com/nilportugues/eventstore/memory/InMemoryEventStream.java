package com.nilportugues.eventstore.memory;

import com.nilportugues.eventstore.Event;
import com.nilportugues.eventstore.EventStream;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventStream extends ArrayList<Event> implements EventStream {

    private long version;

    public InMemoryEventStream(List<Event> stream) {
        super();
        super.addAll(stream);
        this.version = 0;
    }

    public InMemoryEventStream(List<Event> stream, long version) {
        super();
        super.addAll(stream);
        this.version = version;
    }

    public InMemoryEventStream(long version) {
        super();
        this.version = version;
    }

    public static InMemoryEventStream empty() {
        return new InMemoryEventStream(new ArrayList<>());
    }

    public long version() {
        return 0;
    }

    @Override
    public void addAll(List<Event> changes) {
        super.addAll(changes);
    }
}