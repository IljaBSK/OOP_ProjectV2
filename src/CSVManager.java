
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.util.HashMap;


public class CSVManager {


    /**
     * Checks if the given job title is valid by verifying it against the entries in a CSV file.
     *
     * <p>This method reads the "FulltimeSalaryScales.csv" file and compares the given job title
     * to the entries in the first column. If a match is found, the job title is considered valid.</p>
     *
     * @param jobTitle the job title to validate
     * @return <code>true</code> if the job title is valid, <code>false</code> otherwise
     */
    public static boolean isValidJobTitle(String jobTitle) {
        String filename = "FulltimeSalaryScales.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 1 && details[0].trim().equalsIgnoreCase(jobTitle)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: " + filename, e);
        }
        return false;
    }
    /**
     * Validates if a scale point is valid for a given job title.
     *
     * <p>This method checks the "FulltimeSalaryScales.csv" file to ensure the scale point
     * is within the allowed range for the specified job title.</p>
     *
     * @param jobTitle the job title to validate the scale point against
     * @param scalePoint the scale point to validate
     * @return <code>true</code> if the scale point is valid, <code>false</code> otherwise
     */
    public static boolean isValidScalePoint(String jobTitle, int scalePoint) {
        try (BufferedReader reader = new BufferedReader(new FileReader("FulltimeSalaryScales.csv"))) {
            String line;
            int maxScalePoint = Integer.MIN_VALUE;

            // Read each line
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");

                // Check if the job title matches
                if (details[0].trim().equalsIgnoreCase(jobTitle)) {
                    int currentScalePoint = Integer.parseInt(details[1].trim());
                    maxScalePoint = Math.max(maxScalePoint, currentScalePoint); // Track the max scale point
                }
            }

            // Validate the scale point
            return scalePoint > 0 && scalePoint <= maxScalePoint;
        } catch (IOException e) {
            System.err.println("Error reading FulltimeSalaryScales.csv: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in FulltimeSalaryScales.csv.");
        }

        return false; // Return false if an error occurs or no matching job title is found
    }
    /**
     * Reads valid user details from the "ValidLogins.csv" file and creates a map of users.
     *
     * <p>This method parses a CSV file containing user credentials and job roles. It creates
     * and returns a `HashMap` where the keys are usernames, and the values are user objects
     * (`HR`, `Admin`, or `Employee`) based on their job type.</p>
     *
     * @return a `HashMap` containing valid users, with usernames as keys and user objects as values
     * @throws RuntimeException if the file is not found or an error occurs while reading the file
     */
    public static HashMap<String, User> readValidUsers() {
        String filename = "ValidLogins.csv";
        HashMap<String, User> users = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3) {
                    String userName = details[0].trim();
                    String userPassword = details[1].trim();
                    String userJob = details[2].trim();

                    User newUser;
                    if (userJob.equalsIgnoreCase("HR")) {
                        newUser = new HR(userName, userPassword, userJob);
                    } else if (userJob.equalsIgnoreCase("Admin")) {
                        newUser = new Admin(userName, userPassword, userJob);
                    } else if (userJob.equalsIgnoreCase("Employee")) {
                        newUser = new Employee(userName, userPassword, userJob);
                    } else {
                        continue;
                    }

                    users.put(userName, newUser);
                } else {
                    System.out.println("Too many details input");
                }
            }

            reader.close();
            return users;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves an employee's details by their employee ID from the "EmployeeInfo.csv" file.
     *
     * <p>This method searches the CSV file for a matching employee ID and constructs an
     * `Employee` object with the retrieved details. If no match is found or an error occurs,
     * it returns <code>null</code>.</p>
     *
     * @param empID the ID of the employee to retrieve
     * @return an `Employee` object if the ID is found, or <code>null</code> if not found
     * @throws IOException if an error occurs while reading the file
     * @throws NumberFormatException if numeric fields in the file are invalid
     */
    public static Employee getEmployeeByID(String empID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");

                if (details.length >= 9 && details[0].trim().equals(empID)) {
                    String id = details[0].trim();
                    String username = details[1].trim();
                    String name = details[2].trim();
                    String dob = details[3].trim();
                    String ppsNo = details[4].trim();
                    String password = details[5].trim();
                    String jobTitle = details[6].trim();
                    int scalePoint = Integer.parseInt(details[7].trim());
                    int promotionFlag = Integer.parseInt(details[8].trim());

                    Employee employee = new Employee(username, password, name, jobTitle, dob, ppsNo, Integer.parseInt(id), jobTitle, scalePoint);
                    employee.setPreviousJobTitle(details.length > 9 ? details[9].trim() : "");
                    employee.setPreviousScalePoint(details.length > 10 ? Integer.parseInt(details[10].trim()) : 0);
                    return employee;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading EmployeeInfo.csv: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in EmployeeInfo.csv.");
        }
        return null;
    }
    /**
     * Updates the details of an employee in the "EmployeeInfo.csv" file.
     *
     * <p>This method modifies the employee's job title, scale point, promotion flag,
     * and previous job details in the CSV file. If the employee's record is found,
     * it updates the corresponding fields and writes the changes back to the file.</p>
     *
     * @param employee the employee whose details need to be updated
     * @param promotionFlag the promotion flag value to set (e.g., 1 for pending, 0 for none)
     * @throws IOException if an error occurs while reading or writing the file
     */
    public static void updateEmployeeDetails(Employee employee, int promotionFlag) {
        try {
            List<String[]> employeeData = readEmployeeInfo();

            for (int i = 0; i < employeeData.size(); i++) {
                String[] record = employeeData.get(i);

                if (record[0].equals(String.valueOf(employee.getId()))) {
                    // Ensure the record has all required fields
                    if (record.length < 11) {
                        record = Arrays.copyOf(record, 11);
                    }

                    // Update job title and scale point
                    record[6] = employee.getJobTitle(); // Current job title
                    record[7] = String.valueOf(employee.getScalePoint()); // Current scale point
                    record[8] = String.valueOf(promotionFlag); // Promotion flag

                    // Save previous job title and scale point
                    record[9] = employee.getPreviousJobTitle(); // Previous job title
                    record[10] = String.valueOf(employee.getPreviousScalePoint()); // Previous scale point

                    // Replace the updated record in the list
                    employeeData.set(i, record);
                    break;
                }
            }

            // Write back to the CSV
            writeEmployeeInfo(employeeData);
            System.out.println("Employee details written to CSV.");
        } catch (IOException e) {
            System.err.println("Error updating employee details: " + e.getMessage());
        }
    }
    /**
     * Reverts the promotion of an employee, restoring their previous job title and scale point.
     *
     * <p>This method updates the employee's record in the "EmployeeInfo.csv" file by setting
     * their current job title and scale point to the previous values, resetting the promotion
     * flag, and clearing the previous job details.</p>
     *
     * @param employee the employee whose promotion is being reverted
     * @return a `String[]` containing the reverted job title and scale point
     *         (e.g., `{"JobTitle", "ScalePoint"}`)
     * @throws IOException if an error occurs while reading or writing the file
     */
    public static String[] revertPromotion(Employee employee) {
        try {
            List<String[]> employeeData = readEmployeeInfo();
            String revertedJobTitle = "";
            String revertedScalePoint = "";

            for (int i = 0; i < employeeData.size(); i++) {
                String[] record = employeeData.get(i);

                if (record[0].equals(String.valueOf(employee.getId()))) {
                    // Ensure the record has all required fields
                    if (record.length < 11) {
                        record = Arrays.copyOf(record, 11);
                    }

                    // Fetch previous job title and scale point
                    revertedJobTitle = record[9];
                    revertedScalePoint = record[10];

                    // Revert current job title and scale point
                    record[6] = revertedJobTitle; // Revert job title
                    record[7] = revertedScalePoint; // Revert scale point
                    record[8] = "0"; // Reset promotion flag

                    // Clear previous job title and scale point
                    record[9] = ""; // Clear previous job title
                    record[10] = "0"; // Clear previous scale point

                    // Update the record in the list
                    employeeData.set(i, record);
                    break;
                }
            }

            writeEmployeeInfo(employeeData);
            System.out.println("Promotion reverted and employee details written to CSV.");

            return new String[]{revertedJobTitle, revertedScalePoint};
        } catch (IOException e) {
            System.err.println("Error reverting promotion: " + e.getMessage());
            return new String[]{"", "0"}; // Return empty details on error
        }
    }


    public static List<String[]> readEmployeeInfo() throws IOException {
        List<String[]> employeeData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;

            // Skip the header row
            if ((line = reader.readLine()) != null && line.toLowerCase().contains("id")) {
                line = reader.readLine(); // Move to the first data row
            }

            // Read each row and split into columns
            while (line != null) {
                employeeData.add(line.split(","));
                line = reader.readLine();
            }
        }
        return employeeData;
    }
    /**
     * Reads employee information from the "EmployeeInfo.csv" file.
     *
     * <p>This method reads all the rows from the CSV file, skipping the header row,
     * and returns a list of string arrays where each array represents an employee's record.</p>
     *
     * @return a list of string arrays, where each array contains the data of one employee
     * @throws IOException if an error occurs while reading the file
     */
    public static void updatePromotionFlag(int employeeId, int flag) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");

                if (details[0].trim().equals(String.valueOf(employeeId))) {
                    details[8] = String.valueOf(flag); // Reset the promotion flag
                }

                lines.add(String.join(",", details));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: EmployeeInfo.csv", e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("EmployeeInfo.csv"))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to the file: EmployeeInfo.csv", e);
        }
    }
    /**
     * Updates the promotion flag of an employee in the "EmployeeInfo.csv" file.
     *
     * <p>This method searches for an employee record by ID and updates the promotion flag
     * to the specified value. The updated records are written back to the file.</p>
     *
     * @param username the ID of the employee whose promotion flag needs to be updated
     * @throws RuntimeException if an error occurs while reading or writing the file
     */
    public static Employee getEmployeeByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");

                if (details.length >= 9 && details[1].trim().equalsIgnoreCase(username)) {
                    // Map CSV fields to Employee object
                    int id = Integer.parseInt(details[0].trim());
                    String name = details[2].trim();
                    String dob = details[3].trim();
                    String ppsNo = details[4].trim();
                    String password = details[5].trim();
                    String jobTitle = details[6].trim();
                    int scalePoint = Integer.parseInt(details[7].trim());
                    int pendingPromotionFlag = Integer.parseInt(details[8].trim());

                    Employee employee = new Employee(username, password, name, jobTitle, dob, ppsNo, id, jobTitle, scalePoint);
                    employee.setPendingPromotionFlag(pendingPromotionFlag);
                    return employee;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: EmployeeInfo.csv", e);
        }
        return null;
    }
    /**
     * Writes employee information to the "EmployeeInfo.csv" file.
     *
     * <p>This method overwrites the file with updated employee data, including
     * the header row and all records provided in the list.</p>
     *
     * @param employeeData a list of string arrays, where each array represents an employee's record
     * @throws IOException if an error occurs while writing to the file
     */
    private static void writeEmployeeInfo(List<String[]> employeeData) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("EmployeeInfo.csv"))) {
            // Write the header
            String header = "id,username,name,dob,ppsNumber,password,jobTitle,scalePoint,pendingPromotionFlag,previousJobTitle,previousScalePoint";
            writer.write(header);
            writer.newLine();

            // Write each record
            for (String[] record : employeeData) {
                String line = String.join(",", record);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("CSV file updated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to EmployeeInfo.csv: " + e.getMessage());
        }
    }




    /**
     * Reads the work status of employees from the "EmployeeStatus.csv" file.
     *
     * <p>This method parses the CSV file and creates a map where each key is a username
     * and each value is the corresponding work status.</p>
     *
     * @return a `HashMap` containing usernames as keys and work statuses as values
     * @throws IOException if an error occurs while reading the file
     */
    public static HashMap<String, String> readEmployeeStatus() {
        String filename = "EmployeeStatus.csv";
        HashMap<String, String> employeeStatusMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // Skip the header row

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 2) {
                    String username = details[0].trim();
                    String workStatus = details[1].trim();
                    employeeStatusMap.put(username, workStatus);
                } else {
                    System.err.println("Invalid line format in EmployeeStatus.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading EmployeeStatus.csv: " + e.getMessage());
        }

        return employeeStatusMap;
    }
}



