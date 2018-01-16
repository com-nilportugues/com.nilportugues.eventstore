package com.nilportugues.eventstore;

import com.nilportugues.eventstore.memory.InMemoryEventStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryEventStoreTest {
	private static final String EVENT_STREAM = "user_stream";
	private static final Long VERSION_1 = 1L;
	private static final String MY_EVENT_ID = "00000000-0000-0000-0000-000000000000";
	private static final String MY_AGGREGATE_ID = "00000000-0000-0000-0000-000000000000";
	private static final String MY_AGGREGATE_NAME = "user";
	private static final String MY_EVENT_NAME = "user_registered";
	private static final String MY_EVENT_VERSION = "1.0.0";

	private InMemoryEventStore eventStore;

	@Before
	public void setUp() {
		final Event event = new Event();
		event.setAggregateId(MY_AGGREGATE_ID);
		event.setAggregateName(MY_AGGREGATE_NAME);
		event.setEventId(MY_EVENT_ID);
		event.setEventName(MY_EVENT_NAME);
		event.setEventVersion(MY_EVENT_VERSION);
		event.setPayload("{\"some_key\":\"some_value\"}");
		event.setOccurredOn(ZonedDateTime.parse("2017-03-22T00:00:00Z"));

		final List<Event> eventStream = new ArrayList<>();
		eventStream.add(event);

		eventStore = new InMemoryEventStore();
		eventStore.appendTo(EVENT_STREAM, VERSION_1, new ArrayList<>(eventStream));
	}

	@Test
	public void testAppendToStream() {
		eventStore.appendTo(EVENT_STREAM, VERSION_1, new ArrayList<>());
		Assert.assertTrue(eventStore.exists(EVENT_STREAM));
	}

	@Test
	public void testDeleteStream() {
		eventStore.appendTo(EVENT_STREAM, VERSION_1, new ArrayList<>());
		Assert.assertTrue(eventStore.exists(EVENT_STREAM));

		eventStore.delete(EVENT_STREAM);
		Assert.assertFalse(eventStore.exists(EVENT_STREAM));
	}

	@Test
	public void testIfStreamExists() {
		Assert.assertFalse(eventStore.exists("new_stream"));
	}

	@Test
	public void testLoadEventStream() {
		Assert.assertNotNull(eventStore.load(EVENT_STREAM, VERSION_1));
	}

	@Test
	public void testLoadEventFilter() {
		final EventFilter eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);

		Assert.assertNotNull(eventStore.load(eventFilter));
	}

	@Test
	public void testLoadEventFilterStartTime() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setTimeStart(ZonedDateTime.parse("2017-03-22T00:00:00Z"));
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setTimeStart(ZonedDateTime.parse("2017-03-23T00:00:00Z"));
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);
	}

	@Test
	public void testLoadEventFilterEndTime() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setTimeEnd(ZonedDateTime.parse("2017-03-22T00:00:00Z"));
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setTimeEnd(ZonedDateTime.parse("2017-03-21T00:00:00Z"));
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);
	}

	@Test
	public void testLoadEventFilterAggregateId() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setAggregateId(UUID.randomUUID().toString());
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setAggregateId(MY_AGGREGATE_ID);
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);
	}

	@Test
	public void testLoadEventFilterAggregateName() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setAggregateName("dogs");
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setAggregateName(MY_AGGREGATE_NAME);
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);
	}

	@Test
	public void testLoadEventFilterEventId() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setEventId(UUID.randomUUID().toString());
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setEventId(MY_EVENT_ID);
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);
	}

	@Test
	public void testLoadEventFilterEventName() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setEventName("dogs");
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setEventName(MY_EVENT_NAME);
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);
	}

	@Test
	public void testLoadEventFilterEventVersion() {
		EventFilter eventFilter;

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setEventVersion("51.0.1");
		Assert.assertTrue(eventStore.load(eventFilter).count() == 0);

		eventFilter = new EventFilter(EVENT_STREAM, VERSION_1);
		eventFilter.setEventVersion(MY_EVENT_VERSION);
		Assert.assertTrue(eventStore.load(eventFilter).count() == 1);
	}
}
