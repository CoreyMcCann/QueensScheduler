package com.QueensScheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SheetsServiceUtil {
    private static final String APPLICATION_NAME = "Google Sheets Example";

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = GoogleAuthorizeUtil.authorize();
        return new Sheets.Builder(
          GoogleNetHttpTransport.newTrustedTransport(), 
          JacksonFactory.getDefaultInstance(), credential)
          .setApplicationName(APPLICATION_NAME)
          .build();
    }
 
    public static String createSpreadsheet() throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                .setTitle("New Spreadsheet"));
        spreadsheet = service.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();
        System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
        return spreadsheet.getSpreadsheetId();  // Returns the new spreadsheet ID
    }
    
    public static ValueRange prepareScheduleData(int planNumber, List<Course> courses) {
        List<List<Object>> values = new ArrayList<>();
        // Add the plan title
        values.add(Arrays.<Object>asList("Plan " + planNumber));

        // Add the days of the week
        values.add(Arrays.asList((Object)"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

        // List of time slots
        String[] timeSlots = {"08:30-09:30", "09:30-10:30", "10:30-11:30", "11:30-12:30", "12:30-01:30",
                              "01:30-02:30", "02:30-03:30", "03:30-04:30", "04:30-05:30", "05:30-06:30"};

        // Initialize rows for time slots with empty data
        for (String timeSlot : timeSlots) {
            List<Object> row = new ArrayList<>(Arrays.asList((Object) timeSlot, "", "", "", "", ""));
            // Logic to place courses in the correct slots 
            for (Course course : courses) {
                Schedule courseSched = course.getSched();
                for (String key : courseSched.courseSchedule.keySet()) {
                    if (courseSched.courseSchedule.get(key).equals(timeSlot)) {
                        switch (key) {
                            case "Monday":
                                row.set(1, course.getCourseCode());
                                break;
                            case "Tuesday":
                                row.set(2, course.getCourseCode());
                                break;
                            case "Wednesday":
                                row.set(3, course.getCourseCode());
                                break;
                            case "Thursday":
                                row.set(4, course.getCourseCode());
                                break;
                            case "Friday":
                                row.set(5, course.getCourseCode());
                                break;
                        }
                    }
                }
            }
            values.add(row);
        }

        return new ValueRange().setValues(values);
    }

    
    public static void writePlanToSheet(String spreadsheetId, int planNumber, List<Course> courses) throws IOException, GeneralSecurityException {
        Sheets service = getSheetsService();

        // Calculate starting row based on the plan number
        int startRow = 1 + (planNumber - 1) * 13; // Each plan takes 13 rows, adjust if different
        String range = "Sheet1!A" + startRow; // Continuous range in the same sheet

        // Prepare the data for the plan
        ValueRange body = prepareScheduleData(planNumber, courses);

        // Write the data to the calculated range in the sheet
        UpdateValuesResponse result = service.spreadsheets().values()
            .update(spreadsheetId, range, body)
            .setValueInputOption("USER_ENTERED")
            .execute();

        System.out.println("Cells updated: " + result.getUpdatedCells() + " in range: " + range);
    }
    
    public static void generateAllPlans(String spreadsheetId, List<List<Course>> allCombinations) throws IOException, GeneralSecurityException {
        int planNumber = 1;
        for (List<Course> courses : allCombinations) {
            writePlanToSheet(spreadsheetId, planNumber++, courses);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Create a new spreadsheet
            String spreadsheetId = createSpreadsheet();
            System.out.println("Created a new spreadsheet with ID: " + spreadsheetId);
            
            Scheduler allCombinations = new Scheduler();
            allCombinations.readCourseData();
            allCombinations.getValidSchedules(); 

            // Generate all plans in the new spreadsheet
            generateAllPlans(spreadsheetId, allCombinations.returnAllValidSchedules());
            System.out.println("Generated all plans in the spreadsheet.");
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("Failed to create spreadsheet or write data.");
        }
    }
}
