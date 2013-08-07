package com.kingtigerbooks;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class EventTest {

	@Test
	public void test1() {
		Calendar time = Calendar.getInstance();
		time.set(2000, 7, 15, 10, 29, 00);
		Event e = new Event(EventType.PLUGIN_ENABLED, null, time, null, null, null, null, null, null);
		assertEquals(EventType.PLUGIN_ENABLED, e.type);
		assertTrue(e.time.compareTo("2000-8-15 10:29:0") == 0);
		assertNull(e.x);
		assertNull(e.y);
		assertNull(e.z);
		assertNull(e.player);
		assertNull(e.mob);
		assertNull(e.item);
		assertNull(e.details);
	}
	
	@Test
	public  void test2() {
		Calendar time = Calendar.getInstance();
		time.set(2010, 7, 15, 10, 29, 00);
		Event e = new Event(EventType.PLUGIN_ENABLED, "DungeonTiger", time, -120, 300, 90, "ZOMBIE", "LOG", "Stuff");
		assertEquals(EventType.PLUGIN_ENABLED, e.type);
		assertTrue(e.time.compareTo("2010-8-15 10:29:0") == 0);
		assertEquals(e.x.intValue(), -120);
		assertEquals(e.y.intValue(), 300);
		assertEquals(e.z.intValue(), 90);
		assertTrue(e.details.compareTo("Stuff") == 0);
		assertTrue(e.mob.compareTo("ZOMBIE") == 0);
		assertTrue(e.item.compareTo("LOG") == 0);
	}
}
