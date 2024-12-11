package com.QueensScheduler;

import java.util.Set;

public class Course {
    private String courseName;
    private String courseCode;
    private String section;
    private Schedule sched; // Same as days, consider more structured approach if needed
    private String instructorName;

    // Constructor, getters, and setters
    public Course(String courseName, String courseCode, String section, Schedule sched, String instructorName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.section = section;
        this.sched = sched;
        this.instructorName = instructorName;
    }

    public String getCourseName() {
    	return this.courseName;
    }
    
    public String getCourseCode() {
    	return this.courseCode;
    }
    
    public String getSection() {
    	return this.section;
    }
    
    public Schedule getSched() {
    	return this.sched;
    }
    
    public String getInstructorName() {
    	return this.instructorName;
    }
    
    @Override
    public String toString() {
        return String.format("%s\n%s - %s\n%s%s\n",
            this.getCourseName(),
            this.getCourseCode(),
            this.getSection(),
            this.getSched().toString(),
            this.getInstructorName());
    }
    
    public boolean overlaps(Course course) {
    	Set<String> keys = course.sched.courseSchedule.keySet();
    	for (String day : keys) {
    		// test whether the day is apart of both courses schedules
    		if (this.sched.courseSchedule.containsKey(day)) {
    			// tests to see if the courses are at the same time on that specific day
    			if (course.sched.courseSchedule.get(day).equals(this.sched.courseSchedule.get(day))) {
    				return true;
    			}
    		}
    		
    	}
		return false;
    }

}
