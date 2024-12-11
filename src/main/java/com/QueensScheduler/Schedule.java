package com.QueensScheduler;
import java.util.HashMap;
import java.util.Map;

public class Schedule {
	HashMap<String,String> courseSchedule;
	
	public Schedule() {
		this.courseSchedule = new HashMap<>(); 
	}
	
	public void addSchedule(String day, String time) {
		this.courseSchedule.put(day, time);
	}
	
	@Override
	public String toString() {
	    StringBuilder output = new StringBuilder();
	    for (Map.Entry<String, String> entry : courseSchedule.entrySet()) {
	        String key = entry.getKey();
	        String val = entry.getValue();
	        output.append(key).append(": ").append(val).append("\n");  // Customize the format as needed
	    }
	    return output.toString();
	}

}
