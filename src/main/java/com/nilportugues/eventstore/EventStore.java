package com.nilportugues.eventstore;

import java.util.List;
import java.util.stream.Stream;

public interface EventStore {

	boolean exists(String streamName);
	Stream<Event> load(String streamName, Long streamVersion);
	Stream<Event> load(EventFilter eventFilter);
	void appendTo(String streamName, Long version, Event event);
	void appendTo(String streamName, Long version, List<Event> events);
	void delete(String streamName);
	boolean exists(String streamName, Long streamVersion);
	boolean exists(String streamName, Long streamVersion, String eventName);
	boolean exists(String streamName, Long streamVersion, String eventName, String eventVersion);
	void delete(String streamName, Long version);

}