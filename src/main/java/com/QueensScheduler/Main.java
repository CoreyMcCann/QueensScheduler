package com.QueensScheduler;

public class Main {

	public static void main(String[] args) {
		Scheduler app = new Scheduler();
        app.readCourseData();
        
        // creates all valid schedules
        app.getValidSchedules();
        // print all valid schedules
        app.displaySchedules();

	}

}