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
        Scanner scanner = new Scanner(System.in);
        String flag = "0";
        String username;
        while (true) {
            System.out.println("Enter Username: ");
            username = scanner.nextLine();
            if (isUniqueUsername(username)) {
                break;
            } else {
                System.out.println("Username already exists. Please enter a unique username.");
            }
        }

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        String jobType = "";
        while (true) {
            System.out.println("Select Job Type:");
            System.out.println("1) Employee   2) HR   3) Admin");
            System.out.print("Enter corresponding number: ");
            String command = scanner.nextLine();

            if (command.equals("1")) {
                jobType = "Employee";
                break;
            } else if (command.equals("2")) {
                jobType = "HR";
                break;
            } else if (command.equals("3")) {
                jobType = "Admin";
                break;
            } else {
                System.out.println("Invalid input. Enter 1, 2, or 3.");
            }
        }

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

        String ppsNo;
        while (true) {
            System.out.println("Enter PPS Number: ");
            ppsNo = scanner.nextLine();

            if (ppsNo.length() == 7 || ppsNo.length() == 8) {
                break;
            } else {
                System.out.println("Invalid PPS Number. Must be 7 or 8 characters.");
            }
        }

        List<String[]> jobTitleData = readJobTitlesFromCSV(salaryScalesFilePath);

        String jobTitle = "";
        List<Integer> validScalePoints = new ArrayList<>();

        if (!jobTitleData.isEmpty()) {
            while (true) {
                System.out.println("Select Job Title:");
                Set<String> displayedTitles = new HashSet<>();
                int displayIndex = 1;

                for (String[] data : jobTitleData) {
                    String currentJobTitle = data[0];
                    if (!displayedTitles.contains(currentJobTitle)) {
                        displayedTitles.add(currentJobTitle);
                        System.out.println(displayIndex + ") " + currentJobTitle);
                        displayIndex++;
                    }
                }

                System.out.print("Enter the number corresponding to the job title: ");
                String command = scanner.nextLine();

                try {
                    int choice = Integer.parseInt(command);
                    if (choice >= 1 && choice < displayIndex) {
                        displayedTitles.clear();
                        int selectedIndex = 1;
                        for (String[] data : jobTitleData) {
                            String currentJobTitle = data[0];
                            if (!displayedTitles.contains(currentJobTitle)) {
                                if (selectedIndex == choice) {
                                    jobTitle = currentJobTitle;
                                    break;
                                }
                                displayedTitles.add(currentJobTitle);
                                selectedIndex++;
                            }
                        }

                        validScalePoints.clear();
                        for (String[] data : jobTitleData) {
                            if (data[0].equals(jobTitle)) {
                                validScalePoints.add(Integer.parseInt(data[1]));
                            }
                        }
                        break;
                    } else {
                        System.out.println("Invalid choice. Please select a number from the list.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        } else {
            System.out.println("No job titles available in the file.");
            return;
        }

        String scalePoint = "";
        while (true) {
            System.out.println("Enter Scale Point (" + validScalePoints + "): ");
            scalePoint = scanner.nextLine();

            try {
                int scalePointInt = Integer.parseInt(scalePoint);
                if (validScalePoints.contains(scalePointInt)) {
                    break;
                } else {
                    System.out.println("Invalid Scale Point. Please enter a valid point for " + jobTitle + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Scale Point must be a number.");
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

        employeeData = new String[]{id, username, name, dob, ppsNo, password, jobTitle, scalePoint, flag};

        System.out.println("Data collected successfully.");

        CSVWriter csvWriter = new CSVWriter();

        try {
            if (getLoginData() != null) {
                csvWriter.writeToCSV(getLoginData(), "ValidLogins.csv");
            }

            if (getEmployeeData() != null) {
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
        List<String[]> jobTitles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 1) {
                    jobTitles.add(new String[]{fields[0].trim(), fields[1].trim()});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading job titles from file: " + e.getMessage());
        }
        return jobTitles;
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
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading EmployeeInfo.csv: " + e.getMessage());
        }
        return true;
    }

    private boolean isUniqueUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].equals(username)) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading EmployeeInfo.csv: " + e.getMessage());
        }
        return true;
    }
}

