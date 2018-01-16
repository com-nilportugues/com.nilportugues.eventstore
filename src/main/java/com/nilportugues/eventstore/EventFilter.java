package com.nilportugues.eventstore;

import java.time.ZonedDateTime;
import java.util.Optional;

public class EventFilter {
	private Long streamVersion;
	private String streamName;
	private ZonedDateTime timeStart;
	private ZonedDateTime timeEnd;
	private String aggregateId;
	private String aggregateName;
	private String eventId;
	private String eventName;
	private String eventVersion;

	public EventFilter(final String streamName, final Long streamVersion) {
		this.streamVersion = streamVersion;
		this.streamName = streamName;
	}

	public Optional<Long> getStreamVersion() {
		return Optional.ofNullable(streamVersion);
	}

	public void setTimeStart(ZonedDateTime timeStart) {
		this.timeStart = timeStart;
	}

	public void setTimeEnd(ZonedDateTime timeEnd) {
		this.timeEnd = timeEnd;
	}

	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
	}

	public void setAggregateName(String aggregateName) {
		this.aggregateName = aggregateName;
	}

	public Optional<String> getStreamName() {
		return Optional.ofNullable(streamName);
	}

	public Optional<ZonedDateTime> getTimeStart() {
		return Optional.ofNullable(timeStart);
	}

	public Optional<ZonedDateTime> getTimeEnd() {
		return Optional.ofNullable(timeEnd);
	}

	public Optional<String> getAggregateId() {
		return Optional.ofNullable(aggregateId);
	}

	public Optional<String> getAggregateName() {
		return Optional.ofNullable(aggregateName);
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setEventVersion(String eventVersion) {
		this.eventVersion = eventVersion;
	}

	public Optional<String> getEventId() {
		return Optional.ofNullable(eventId);
	}

	public Optional<String> getEventName() {
		return Optional.ofNullable(eventName);
	}

	public Optional<String> getEventVersion() {
		return Optional.ofNullable(eventVersion);
	}
}
