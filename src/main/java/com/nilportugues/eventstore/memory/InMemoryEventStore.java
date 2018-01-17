package com.nilportugues.eventstore.memory;

import com.nilportugues.eventstore.Event;
import com.nilportugues.eventstore.EventFilter;
import com.nilportugues.eventstore.EventStore;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryEventStore implements EventStore {

    private Map<String, Map<Long, List<Event>>> events = new HashMap<>();

    @Override
    public Stream<Event> load(final String streamName, Long streamVersion) {

        final Map<Long, List<Event>> streams = this.events.get(streamName);
        if (null == streams) {
            return Stream.empty();
        }

        final List<Event> eventStream = streams.get(streamVersion);
        if (null == eventStream) {
            return Stream.empty();
        }

        return eventStream.stream();
    }

    @Override
    public Stream<Event> load(final EventFilter eventFilter) {

        final String streamName = eventFilter.getStreamName().orElse(null);
        final Map<Long, List<Event>> streams = this.events.get(streamName);

        if (null == streams) {
            return Stream.empty();
        }

        final Long streamVersion = eventFilter.getStreamVersion().orElse(null);
        final List<Event> eventStream = streams.get(streamVersion);

        if (null == eventStream) {
            return Stream.empty();
        }

        return eventStream.stream().filter(event -> filterTimeStart(event, eventFilter))
            .filter(event -> filterTimeEnd(event, eventFilter))
            .filter(event -> filterAggregateId(event, eventFilter))
            .filter(event -> filterAggregateName(event, eventFilter))
            .filter(event -> filterEventId(event, eventFilter)).filter(event -> filterEventName(event, eventFilter))
            .filter(event -> filterEventVersion(event, eventFilter));

    }

    @Override
    public void appendTo(String streamName, Long version, Event event) {
        final ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(event);

        appendTo(streamName, version, eventList);
    }

    private boolean filterTimeStart(Event event, EventFilter eventFilter) {
        final Optional<ZonedDateTime> timeStartOptional = eventFilter.getTimeStart();

        return !timeStartOptional.isPresent() || !Optional.ofNullable(event.getOccurredOn()).isPresent()
            || ChronoUnit.NANOS.between(timeStartOptional.get(), event.getOccurredOn()) >= 0;

    }

    private boolean filterTimeEnd(Event event, EventFilter eventFilter) {
        final Optional<ZonedDateTime> timeEndOptional = eventFilter.getTimeEnd();

        return !timeEndOptional.isPresent() || !Optional.ofNullable(event.getOccurredOn()).isPresent()
            || ChronoUnit.NANOS.between(timeEndOptional.get(), event.getOccurredOn()) <= 0;

    }

    private boolean filterAggregateId(Event event, EventFilter eventFilter) {
        final Optional<String> aggregateIdOptional = eventFilter.getAggregateId();

        return !Optional.ofNullable(event.getAggregateId()).isPresent() || !aggregateIdOptional.isPresent()
            || event.getAggregateId().equals(aggregateIdOptional.get());
    }

    private boolean filterAggregateName(Event event, EventFilter eventFilter) {
        final Optional<String> aggregateNameOptional = eventFilter.getAggregateName();

        return !Optional.ofNullable(event.getAggregateName()).isPresent() || !aggregateNameOptional.isPresent()
            || event.getAggregateName().equals(aggregateNameOptional.get());
    }

    private boolean filterEventId(Event event, EventFilter eventFilter) {
        final Optional<String> eventIdOptional = eventFilter.getEventId();

        return !Optional.ofNullable(event.getEventId()).isPresent() || !eventIdOptional.isPresent()
            || event.getEventId().equals(eventIdOptional.get());
    }

    private boolean filterEventName(Event event, EventFilter eventFilter) {
        final Optional<String> eventNameOptional = eventFilter.getEventName();

        return !Optional.ofNullable(event.getEventName()).isPresent() || !eventNameOptional.isPresent()
            || event.getEventName().equals(eventNameOptional.get());
    }

    private boolean filterEventVersion(Event event, EventFilter eventFilter) {
        final Optional<String> eventVersionOptional = eventFilter.getEventVersion();

        return !Optional.ofNullable(event.getEventVersion()).isPresent() || !eventVersionOptional.isPresent()
            || event.getEventVersion().equals(eventVersionOptional.get());
    }

    @Override
    public void appendTo(final String streamName, final Long version, final List<Event> events) {
        this.events.putIfAbsent(streamName, (new HashMap<>()));
        this.events.get(streamName).putIfAbsent(version, new ArrayList<>());
        this.events.get(streamName).get(version).addAll(events);
    }

    @Override
    public void delete(final String streamName) {
        delete(streamName, null);
    }

    @Override
    public boolean exists(final String streamName) {
        return exists(streamName, null, null, null);
    }

    @Override
    public boolean exists(String streamName, Long streamVersion) {
        return exists(streamName, streamVersion, null, null);
    }

    @Override
    public boolean exists(String streamName, Long streamVersion, String eventName) {
        return exists(streamName, streamVersion, eventName, null);
    }

    @Override
    public boolean exists(String streamName, Long streamVersion, String eventName, String eventVersion) {

        if (null == streamVersion && null == eventName && null == eventVersion) {
            return Optional.ofNullable(events.get(streamName)).isPresent();
        }

        if (null != streamVersion && null == eventName && null == eventVersion) {
            if (Optional.ofNullable(events.get(streamName)).isPresent()) {
                final Map<Long, List<Event>> map = events.get(streamName);
                return Optional.ofNullable(map.get(streamVersion)).isPresent();
            }
        }

        if (null != streamVersion && null != eventName && null == eventVersion) {
            if (Optional.ofNullable(events.get(streamName)).isPresent()) {
                final Map<Long, List<Event>> map = events.get(streamName);
                if (Optional.ofNullable(map.get(streamVersion)).isPresent()) {
                    final List<Event> events = map.get(streamVersion);

                    return events.stream().filter(event -> event.getEventName().equals(eventName)).findFirst()
                        .isPresent();
                }
            }
        }

        if (null != streamVersion && null != eventName && null != eventVersion) {
            if (Optional.ofNullable(events.get(streamName)).isPresent()) {
                final Map<Long, List<Event>> map = events.get(streamName);
                if (Optional.ofNullable(map.get(streamVersion)).isPresent()) {
                    final List<Event> namedEvents = map.get(streamVersion).stream()
                        .filter(event -> event.getEventName().equals(eventName)).collect(Collectors.toList());

                    if (Optional.ofNullable(namedEvents).isPresent() && namedEvents.size() > 0) {
                        return namedEvents.stream().filter(event -> event.getEventVersion().equals(eventVersion))
                            .findFirst().isPresent();
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void delete(String streamName, Long version) {

        if (null == version) {
            events.remove(streamName);
            return;
        }

        final Optional<Map<Long, List<Event>>> stream = Optional.ofNullable(this.events.get(streamName));
        if (stream.isPresent()) {
            final Map<Long, List<Event>> streamVersions = stream.get();

            final Optional<List<Event>> streamVersion = Optional.ofNullable(streamVersions.get(version));
            if (streamVersion.isPresent()) {
                events.get(streamName).remove(version);
            }
        }
    }
}
