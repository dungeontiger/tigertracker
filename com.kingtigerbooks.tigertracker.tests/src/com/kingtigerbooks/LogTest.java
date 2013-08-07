package com.kingtigerbooks;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class LogTest {
	@Test
	public void testCreate() {
		Log log = new Log("test.log");
		log.release();
		File file = new File("test.log");
		assertTrue(file.exists());
		assertTrue(file.delete());
	}
	
	@Test
	public void testWrite() {
		Log log = new Log("test.log");
		log.writeError("This is an error message");
		IOException e = new IOException();
		log.writeException(e, "This is an exception");
		log.release();
		File file = new File("test.log");
		assertTrue(file.exists());
		assertTrue(file.delete());
	}
}
