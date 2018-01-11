package com.nilportugues.eventstore.memory;

import com.nilportugues.eventstore.Event;
import com.nilportugues.eventstore.EventFilter;
import com.nilportugues.eventstore.EventStore;
import com.nilportugues.eventstore.EventStream;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryEventStore implements EventStore {

    private Map<String, EventStream> events = new HashMap<>();

    @Override
    public boolean exists(final String streamName) {
        return Optional.ofNullable(events.get(streamName)).isPresent();
    }

    @Override
    public EventStream load(final String streamName) {
        final EventStream eventStream = this.events.get(streamName);
        if (null == eventStream) {
            return InMemoryEventStream.empty();
        }
        return eventStream;
    }

    @Override
    public EventStream load(final EventFilter eventFilter) {

        final EventStream eventStream = this.events.get(eventFilter.getStreamName().orElse(null));
        if (null == eventStream) {
            return InMemoryEventStream.empty();
        }

        final List<Event> eventList = eventStream
                .stream()
                .filter(event -> filterTimeStart(event, eventFilter))
                .filter(event -> filterTimeEnd(event, eventFilter))
                .filter(event -> filterAggregateId(event, eventFilter))
                .filter(event -> filterAggregateName(event, eventFilter))
                .filter(event -> filterEventId(event, eventFilter))
                .filter(event -> filterEventName(event, eventFilter))
                .filter(event -> filterEventVersion(event, eventFilter))
                .collect(Collectors.toList());

        return new InMemoryEventStream(eventList);
    }
    
    private boolean filterTimeStart(Event event, EventFilter eventFilter) {
        final Optional<ZonedDateTime> timeStartOptional = eventFilter.getTimeStart();

        return !timeStartOptional.isPresent()
               || !Optional.ofNullable(event.getOccurredOn()).isPresent()
               || ChronoUnit.NANOS.between(timeStartOptional.get(), event.getOccurredOn()) >= 0;

    }
    
    private boolean filterTimeEnd(Event event, EventFilter eventFilter) {
        final Optional<ZonedDateTime> timeEndOptional = eventFilter.getTimeEnd();

        return !timeEndOptional.isPresent()
               || !Optional.ofNullable(event.getOccurredOn()).isPresent()
               || ChronoUnit.NANOS.between(timeEndOptional.get(), event.getOccurredOn()) <= 0;

    }
    
    private boolean filterAggregateId(Event event, EventFilter eventFilter) {
        final Optional<String> aggregateIdOptional = eventFilter.getAggregateId();

        return !Optional.ofNullable(event.getAggregateId()).isPresent()
                || !aggregateIdOptional.isPresent()
                || event.getAggregateId().equals(aggregateIdOptional.get());
    }
    
    private boolean filterAggregateName(Event event, EventFilter eventFilter) {
        final Optional<String> aggregateNameOptional = eventFilter.getAggregateName();

        return !Optional.ofNullable(event.getAggregateName()).isPresent()
                || !aggregateNameOptional.isPresent()
                || event.getAggregateName().equals(aggregateNameOptional.get());
    }
    
    private boolean filterEventId(Event event, EventFilter eventFilter) {
        final Optional<String> eventIdOptional = eventFilter.getEventId();

        return! Optional.ofNullable(event.getEventId()).isPresent()
                || !eventIdOptional.isPresent()
                || event.getEventId().equals(eventIdOptional.get());
    }
    
    private boolean filterEventName(Event event, EventFilter eventFilter) {
        final Optional<String> eventNameOptional = eventFilter.getEventName();

        return !Optional.ofNullable(event.getEventName()).isPresent()
                || !eventNameOptional.isPresent()
                || event.getEventName().equals(eventNameOptional.get());
    }
    
    private boolean filterEventVersion(Event event, EventFilter eventFilter) {
        final Optional<String> eventVersionOptional = eventFilter.getEventVersion();

        return !Optional.ofNullable(event.getEventVersion()).isPresent()
                || !eventVersionOptional.isPresent()
                || event.getEventVersion().equals(eventVersionOptional.get());
    }

    @Override
    public void appendTo(final String streamName, final long version, final List<Event> events) {
        this.events.putIfAbsent(streamName, InMemoryEventStream.empty());
        this.events.get(streamName).addAll(events);
    }

    @Override
    public void delete(final String streamName) {
        events.remove(streamName);
    }
}