import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Admin extends User {
    // Array to store login data: username, password, and job type
    private String[] loginData;
    // Array to store employee data: ID, username, name, DOB, PPS number, job title, and scale point
    private String[] employeeData;
    /**
     * Constructs an Admin user with the specified username, password, and job type.
     *
     * @param username the username for the Admin user
     * @param password the password for the Admin user
     * @param jobType  the job type associated with the Admin user
     */
    public Admin(String username, String password, String jobType) {
        super(username, password, jobType);
    }

    /**
     * This method collects all necessary data for login and employee details.
     * It validates job titles and scale points using data from a CSV file.
     *
     * @param salaryScalesFilePath Path to the CSV file containing job titles and their scale points.
     */
    public void createEmployee(String salaryScalesFilePath) {
        Scanner scanner = new Scanner(System.in); // Create a scanner to take input from the user
        String flag = "0";
        // Collect Login Information
        String username;
        while (true) {
            System.out.println("Enter Username: ");
            username = scanner.nextLine();
            if (isUniqueUsername(username)) {
                break; // Exit the loop if the username is unique
            } else {
                System.out.println("Username already exists. Please enter a unique username.");
            }
        }

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        // Prompt the user to select a job type
        String jobType = "";
        while (true) { // Loop until the user provides a valid job type
            System.out.println("Select Job Type:");
            System.out.println("1) Employee   2) HR   3) Admin");
            System.out.print("Enter corresponding number: ");
            String command = scanner.nextLine(); // Read the user's choice

            // Check the input and assign the corresponding job type
            if (command.equals("1")) {
                jobType = "Employee";
                break; // Exit the loop if the input is valid
            } else if (command.equals("2")) {
                jobType = "HR";
                break;
            } else if (command.equals("3")) {
                jobType = "Admin";
                break;
            } else {
                System.out.println("Invalid input. Enter 1, 2, or 3."); // Re-prompt on invalid input
            }
        }

        // Store the login data into the array
        loginData = new String[]{username, password, jobType};

        String id;
        while (true) {
            System.out.println("Enter Employee ID (5 digits, cannot start with 0): ");
            id = scanner.nextLine();
            if (id.matches("^[1-9][0-9]{4}$") && isUniqueID(id)) {
                break;
            } else {
                System.out.println("Invalid or duplicate Employee ID. Please enter a valid 5-digit ID.");
            }
        }

        System.out.println("Enter Name: ");
        String name = scanner.nextLine();

        String dob;
        while (true) {
            System.out.println("Enter Date of Birth (DD/MM/YYYY): ");
            dob = scanner.nextLine();
            if (isValidDate(dob)) {
                break;
            } else {
                System.out.println("Invalid date format. Please enter a valid date in DD/MM/YYYY format.");
            }
        }

        // Validate PPS number (must be 7 or 8 characters long)
        String ppsNo;
        while (true) { // Loop until a valid PPS number is entered
            System.out.println("Enter PPS Number: ");
            ppsNo = scanner.nextLine(); // Read the PPS number

            if (ppsNo.length() == 7 || ppsNo.length() == 8) { // Check length
                break; // Exit the loop if valid
            } else {
                System.out.println("Invalid PPS Number. Must be 7 or 8 characters."); // Re-prompt on invalid input
            }
        }

        // Step 3: Read Job Titles and Scale Points from CSV
        // This list will hold job titles and their scale points from the CSV file
        List<String[]> jobTitleData = readJobTitlesFromCSV(salaryScalesFilePath);

        // Step 4: Select Job Title
        String jobTitle = ""; // Variable to store the selected job title
        List<Integer> validScalePoints = new ArrayList<>(); // List of valid scale points for the selected job title

        if (!jobTitleData.isEmpty()) { // Ensure there are job titles available
            while (true) { // Loop until a valid job title is selected
                System.out.println("Select Job Title:");
                Set<String> displayedTitles = new HashSet<>(); // Track displayed job titles
                int displayIndex = 1; // Counter for options

                // Display unique job titles
                for (String[] data : jobTitleData) {
                    String currentJobTitle = data[0]; // Get job title
                    if (!displayedTitles.contains(currentJobTitle)) {
                        displayedTitles.add(currentJobTitle); // Mark job title as displayed
                        System.out.println(displayIndex + ") " + currentJobTitle); // Print unique job title
                        displayIndex++;
                    }
                }

                System.out.print("Enter the number corresponding to the job title: ");
                String command = scanner.nextLine(); // Read the user's choice

                try {
                    int choice = Integer.parseInt(command); // Convert input to a number
                    if (choice >= 1 && choice < displayIndex) { // Check valid range
                        // Find the job title based on display order
                        displayedTitles.clear();
                        int selectedIndex = 1; // Counter to match the displayed order
                        for (String[] data : jobTitleData) {
                            String currentJobTitle = data[0];
                            if (!displayedTitles.contains(currentJobTitle)) {
                                if (selectedIndex == choice) {
                                    jobTitle = currentJobTitle; // Assign the selected job title
                                    break;
                                }
                                displayedTitles.add(currentJobTitle);
                                selectedIndex++;
                            }
                        }

                        validScalePoints.clear(); // Clear scale points
                        // Populate scale points for the selected job title
                        for (String[] data : jobTitleData) {
                            if (data[0].equals(jobTitle)) {
                                validScalePoints.add(Integer.parseInt(data[1])); // Add scale points
                            }
                        }
                        break; // Exit loop after valid selection
                    } else {
                        System.out.println("Invalid choice. Please select a number from the list."); // Handle invalid input
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number."); // Handle non-numeric input
                }
            }
        } else {
            System.out.println("No job titles available in the file."); // Exit if the file is empty
            return; // Exit the method
        }


        // Step 5: Validate Scale Point
        String scalePoint = ""; // Variable to store the scale point
        while (true) { // Loop until a valid scale point is entered
            System.out.println("Enter Scale Point (" + validScalePoints + "): ");
            scalePoint = scanner.nextLine(); // Read the scale point

            try {
                int scalePointInt = Integer.parseInt(scalePoint); // Convert input to a number
                if (validScalePoints.contains(scalePointInt)) { // Check if the scale point is valid
                    break; // Exit the loop if valid
                } else {
                    System.out.println("Invalid Scale Point. Please enter a valid point for " + jobTitle + "."); // Re-prompt
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Scale Point must be a number."); // Handle non-numeric input
            }
        }

        String workStatus = "";
        while (true) {
            System.out.println("Select Work Status:");
            System.out.println("1) Full-Time   2) Part-Time");
            System.out.print("Enter corresponding number: ");
            String command = scanner.nextLine();

            if (command.equals("1")) {
                workStatus = "Full-Time";
                break;
            } else if (command.equals("2")) {
                workStatus = "Part-Time";
                break;
            } else {
                System.out.println("Invalid input. Enter 1 or 2.");
            }
        }

        // Step 6: Store Employee Data
        employeeData = new String[]{id, username, name, dob, ppsNo, password, jobTitle, scalePoint, flag};

        System.out.println("Data collected successfully.");

        CSVWriter csvWriter = new CSVWriter();

        try {
            if (getLoginData() != null) {
                // Write login data to ValidLogins.csv
                csvWriter.writeToCSV(getLoginData(), "ValidLogins.csv");
            }

            if (getEmployeeData() != null) {
                // Write employee data to EmployeeInfo.csv
                csvWriter.writeToCSV(getEmployeeData(), "EmployeeInfo.csv");
            }
            csvWriter.writeToCSV(new String[]{username, workStatus}, "EmployeeStatus.csv");

            System.out.println("Employee data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }

    /**
     * Reads job titles and their corresponding scale points from the given CSV file.
     *
     * @param filePath Path to the CSV file.
     * @return A list of job titles and their scale points.
     */
    private List<String[]> readJobTitlesFromCSV(String filePath) {
        List<String[]> jobTitles = new ArrayList<>(); // List to store job titles and scale points
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(","); // Split each line into fields
                if (fields.length > 1) {
                    jobTitles.add(new String[]{fields[0].trim(), fields[1].trim()}); // Add job title and scale point
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading job titles from file: " + e.getMessage()); // Handle file read errors
        }
        return jobTitles; // Return the list of job titles and scale points
    }

    /**
     * Returns the login data collected from the user.
     *
     * @return An array containing username, password, and job type.
     */
    public String[] getLoginData() {
        return loginData;
    }

    /**
     * Returns the employee data collected from the user.
     *
     * @return An array containing employee-specific data.
     */
    public String[] getEmployeeData() {
        return employeeData;
    }
    private boolean isValidDate(String date) {
        try {
            String[] parts = date.split("/");
            if (parts.length != 3) {
                return false;
            }
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12) {
                return false;
            }

            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            if (month == 2 && isLeapYear(year)) {
                daysInMonth[1] = 29;
            }

            return day >= 1 && day <= daysInMonth[month - 1];
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Checks if a given year is a leap year.
     *
     * <p>A year is considered a leap year if it is divisible by 4 but not by 100,
     * unless it is also divisible by 400.</p>
     *
     * @param year the year to check
     * @return <code>true</code> if the year is a leap year, <code>false</code> otherwise
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Checks if a given ID is unique in the "EmployeeInfo.csv" file.
     *
     * <p>This method reads the CSV file and compares the given ID with existing IDs
     * to determine if it is unique.</p>
     *
     * @param id the ID to check
     * @return <code>true</code> if the ID is unique, <code>false</code> otherwise
     * @throws IOException if an error occurs while reading the file
     */
    private boolean isUniqueID(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(id)) {
                    return false; // ID already exists
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading EmployeeInfo.csv: " + e.getMessage());
        }
        return true; // ID is unique
    }

    private boolean isUniqueUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].equals(username)) { // Check if the username exists
                    return false; // Username already exists
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading EmployeeInfo.csv: " + e.getMessage());
        }
        return true; // Username is unique
    }
}

