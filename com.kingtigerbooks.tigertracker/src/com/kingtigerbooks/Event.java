package com.kingtigerbooks;

import java.util.Calendar;

public class Event {
	
	// all values except type and time can be null if not applicable
	
	public final String player;
	public final int type;
	public final String time;
	public final String details;
	public final Integer x;
	public final Integer y;
	public final Integer z;
	public final String mob;
	public final String item;
	
	public Event(int type, String player, Calendar time, Integer x, Integer y, Integer z, String mob, String item, String details) {
		this.type = type;
		this.player = player;
		this.details = details;
		this.x = x;
		this.y = y;
		this.z = z;
		this.mob = mob;
		this.item = item;
		
		// TODO: These Calendar functions are locale specific
		String s = String.valueOf(time.get(Calendar.YEAR)) + "-" + String.valueOf(time.get(Calendar.MONTH) + 1) + "-" + String.valueOf(time.get(Calendar.DAY_OF_MONTH));
		s += " ";
		s += String.valueOf(time.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(time.get(Calendar.MINUTE)) + ":" + String.valueOf(time.get(Calendar.SECOND));
		this.time = s;
	}
}
