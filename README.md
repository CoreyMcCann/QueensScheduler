# QueensScheduler
## Overview
The QueensScheduler is a powerful tool that allows users to generate all possible course schedules for a specified number of courses. By providing a CSV file with course information, the scheduler will calculate all the possible schedule combinations and output them to a Google Sheet.
This system is ideal for students who want to visualize different ways to structure their weekly course schedule and ensure there are no time conflicts between selected courses.

## Features

* Input Course Data: Users input all potential courses they might want to take in a CSV file.
* Automatic Schedule Generation: The system generates all possible schedules that meet the criteria (e.g., selecting 5 out of 6 available courses).
* Google Sheets Integration: The schedules are automatically exported to a Google Sheet for easy viewing and comparison.

## How It Works
### Input Data:
Populate the ```courseData.csv``` file, located in ```src/main/resources```, with the courses you want to consider for your schedule.
The CSV file should have the following format (example included below):

Course Name,Course Code,Section,Day 1,Time 1,Day 2,Time 2,Day 3,Time 3,Instructor

Algorithms I,CISC365,01,Tuesday,12:30-01:30,Thursday,11:30-12:30,Friday,01:30-02:30,Ting Hu

### Run the Scheduler:
Once the CSV file is set up, run the QueensScheduler application. It will read the data from the file and generate all possible schedules that select a specific number of courses from the list.
### Export to Google Sheets:
Once the schedules are generated, they will be uploaded to a Google Sheet. This allows users to:
* View the generated schedules
* Compare potential schedules at a glance
* Share the sheet with others for collaboration

## Usage Instructions

1. Clone this repository:

```bash 
git clone https://github.com/your-username/QueensScheduler.git
cd QueensScheduler
```

2. Ensure that you have properly configured your Google Sheets API credentials. Ensure that the ```tokens/``` directory and ```google-sheets-client-secret.json``` file are not tracked in your repository to avoid exposing sensitive information.
3. Add your course information to the ```src/main/resources/courseData.csv``` file using the specified format.
4. Run the application using your IDE or the following command:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.QueensScheduler.Main"
```

5. View the resulting schedules in your linked Google Sheets document.

## Security Notice
DO NOT SHARE YOUR API CREDENTIALS: The ```google-sheets-client-secret.json``` file contains sensitive information. Ensure it is added to the ```.gitignore``` file and never committed to the repository.

DO NOT EXPOSE YOUR STORED CREDENTIALS: The ```tokens/``` directory contains OAuth tokens used to access Google APIs. Ensure this is also added to ```.gitignore.```


## Screenshots

![output](screenshots/output.png)
