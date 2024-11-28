import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    private String[] loginData;    // Holds login data: username, password, job type
    private String[] employeeData; // Holds employee data: ID, username, name, DOB, PPS number, job title, scale point

    public Admin(String username, String password, String jobType) {
        super(username, password, jobType);
    }

    /**
     * Collects and writes employee data to CSV files for both login and employee details.
     *
     * @param salaryScalesFilePath Path to the CSV file containing job titles and scale points.
     * @param validLoginsFilePath  Path to the CSV file for login data.
     * @param employeeFilePath     Path to the CSV file for employee data.
     */
    public void createEmployee(String salaryScalesFilePath, String validLoginsFilePath, String employeeFilePath) {
        Scanner scanner = new Scanner(System.in); // Create a scanner to take user input

        // ===== Collect Login Information =====
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();

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

        // ===== Collect Employee-Specific Data =====
        System.out.println("Enter Employee ID: ");
        String id = scanner.nextLine();

        System.out.println("Enter Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Date of Birth (DD/MM/YYYY): ");
        String dob = scanner.nextLine();

        // Validate PPS number
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

        // ===== Read Job Titles and Scale Points =====
        List<String[]> jobTitleData = readJobTitlesFromCSV(salaryScalesFilePath);

        // Select Job Title
        String jobTitle = "";
        List<Integer> validScalePoints = new ArrayList<>();
        if (!jobTitleData.isEmpty()) {
            while (true) {
                System.out.println("Select Job Title:");
                for (int i = 0; i < jobTitleData.size(); i++) {
                    System.out.println((i + 1) + ") " + jobTitleData.get(i)[0]);
                }
                System.out.print("Enter the number corresponding to the job title: ");
                String command = scanner.nextLine();
                try {
                    int choice = Integer.parseInt(command);
                    if (choice >= 1 && choice <= jobTitleData.size()) {
                        jobTitle = jobTitleData.get(choice - 1)[0];
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

        // Validate Scale Point
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

        employeeData = new String[]{id, username, name, dob, ppsNo, jobTitle, scalePoint};

        // ===== Write Data to CSV Files =====
        CSVWriter writer = new CSVWriter();
        try {
            // Write login data to the login file
            writer.writeToCSV(loginData, validLoginsFilePath);

            // Write employee data to the employee file
            writer.writeToCSV(employeeData, employeeFilePath);

            System.out.println("Data successfully written to CSV files.");
        } catch (IOException e) {
            System.err.println("Error writing data to files: " + e.getMessage());
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
            reader.readLine(); // Skip header
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
}
