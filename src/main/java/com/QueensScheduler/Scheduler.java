package com.QueensScheduler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Scheduler {

    private List<Course> courses;
    private List<List<Course>> validSchedules;
    private static final int NUMBER_OF_COURSES_PER_SCHEDULE = 5; // change this if you want a different number of courses in your schedule

    public Scheduler() {
        this.courses = new ArrayList<>();
        this.validSchedules = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void readCourseData() {
        String filePath = "/Users/familymccann/Documents/QueensScheduler/src/main/resources/courseData.csv";  // Adjust the path according to your project structure
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line) {
    	Schedule sched = new Schedule();
    	
        String[] parts = line.split(",");  // Assuming CSV format
        if (parts.length == 10) {
            String courseName = parts[0].trim();
            String courseCode = parts[1].trim();
            String section = parts[2].trim();
            sched.addSchedule(parts[3].trim(), parts[4].trim());
            sched.addSchedule(parts[5].trim(), parts[6].trim());
            sched.addSchedule(parts[7].trim(), parts[8].trim());
            String instructorName = parts[9].trim();

            Course course = new Course(courseName, courseCode, section, sched, instructorName);
            addCourse(course);
        } else {
            System.out.println("Invalid line format: " + line);
        }
    }
    
    public void generateSchedules(List<Course> currentSchedule, int index) {
        if (currentSchedule.size() == NUMBER_OF_COURSES_PER_SCHEDULE) {
            validSchedules.add(new ArrayList<>(currentSchedule));
            return;
        }
        for (int i = index; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (!hasConflict(currentSchedule, course) && !courseInCurrentSchedule(currentSchedule, course)) {
                currentSchedule.add(course);
                generateSchedules(currentSchedule, i + 1);
                currentSchedule.remove(currentSchedule.size() - 1);
            }
        }
    }

    public boolean hasConflict(List<Course> currentSchedule, Course newCourse) {
        for (Course scheduledCourse : currentSchedule) {
            if (scheduledCourse.overlaps(newCourse)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean courseInCurrentSchedule(List<Course> currentSchedule, Course newCourse) {
    	for (Course course : currentSchedule) {
    		if (course.getCourseName().equals(newCourse.getCourseName())) {
    			return true;
    		}	
    	}
    	return false;
    }

    public void getValidSchedules() {
        generateSchedules(new ArrayList<Course>(), 0);
        
    }
    public List<List<Course>> returnAllValidSchedules() {
    	return this.validSchedules;
    }
    
    // example method to display all possible schedules
    public void displaySchedules() {
    	for (List<Course> singleSchedule : validSchedules) {
    		displaySchedule(singleSchedule);
    	}
    }

    // example method to display a single schedule
    public void displaySchedule(List<Course> singleSchedule) {
    	for (Course course : singleSchedule) {
    		System.out.println(course);
    	}
    	System.out.println("--------------------");
    }
    
    // Example method to display all of the courses that have been read from the csv file
    public void displayCourses() {
        for (Course course : courses) {
            System.out.println(course); // Assuming Course class has a proper toString() method
        }
    }
    
}
