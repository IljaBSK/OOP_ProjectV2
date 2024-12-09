
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
            String line;
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

                if (details[0].trim().equalsIgnoreCase(jobTitle)) {
                    int currentScalePoint = Integer.parseInt(details[1].trim());
                    maxScalePoint = Math.max(maxScalePoint, currentScalePoint);
                }
            }

            return scalePoint > 0 && scalePoint <= maxScalePoint;
        } catch (IOException e) {
            System.err.println("Error reading FulltimeSalaryScales.csv: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in FulltimeSalaryScales.csv.");
        }

        return false;
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
     * @throws IOException if an error occurs while reading or writing the file
     */
    public static void updateEmployeeDetails(Employee employee) {
        try {
            List<String[]> employeeData = readEmployeeInfo();

            for (String[] record : employeeData) {
                if (record[0].equals(String.valueOf(employee.getId()))) {
                    record[6] = employee.getJobTitle();
                    record[7] = String.valueOf(employee.getScalePoint());
                    record[8] = "1";
                    record[9] = employee.getPreviousJobTitle() == null ? "" : employee.getPreviousJobTitle();
                    record[10] = String.valueOf(employee.getPreviousScalePoint());
                }
            }

            writeEmployeeInfo(employeeData);
            System.out.println("Employee details updated successfully.");
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
                    if (record.length < 11) {
                        record = Arrays.copyOf(record, 11);
                    }

                    revertedJobTitle = record[9];
                    revertedScalePoint = record[10];

                    record[6] = revertedJobTitle;
                    record[7] = revertedScalePoint;
                    record[8] = "0";

                    record[9] = "";
                    record[10] = "0";

                    employeeData.set(i, record);
                    break;
                }
            }

            writeEmployeeInfo(employeeData);
            System.out.println("Promotion reverted and employee details written to CSV.");

            return new String[]{revertedJobTitle, revertedScalePoint};
        } catch (IOException e) {
            System.err.println("Error reverting promotion: " + e.getMessage());
            return new String[]{"", "0"};
        }
    }

    public static void updateSalaryScales() {
        try {
            List<String[]> employeeData = readEmployeeInfo();

            for (String[] employee : employeeData) {
                if (employee.length < 12) {
                    employee = Arrays.copyOf(employee, 12);
                    employee[11] = "0";
                }

                String jobType = employee[6];
                int currentScalePoint = Integer.parseInt(employee[7]);
                int yearsAtTop = Integer.parseInt(employee[11]);

                int maxScalePoint = getMaxScalePoint(jobType);

                if (maxScalePoint == -1) {
                    System.err.println("No salary scales found for job type: " + jobType);
                    continue;
                }

                if (currentScalePoint < maxScalePoint) {
                    employee[7] = String.valueOf(currentScalePoint + 1);
                    employee[11] = "0";
                } else {
                    System.out.println("Employee ID " + employee[0] + " has reached the maximum scale point for " + jobType);
                    employee[11] = String.valueOf(yearsAtTop + 1);
                }
            }

            writeEmployeeInfo(employeeData);
            System.out.println("EmployeeInfo.csv updated successfully!");
        } catch (IOException e) {
            System.err.println("Error updating salary scales: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing scale point in EmployeeInfo.csv.");
        }
    }

    /**
     * Retrieves the maximum scale point for a specific job type from the salary scales data.
     *
     * <p>This method iterates through the salary scales file to find the highest scale point
     * associated with the given job type. If no matching job type is found, an error message
     * is logged, and the method returns {@code -1}.</p>
     *
     * @param jobType the job type to look up in the salary scales (e.g., "Senior Administrative Officer II").
     * @return the maximum scale point for the given job type, or {@code -1} if no match is found.
     */
    public static int getMaxScalePoint(String jobType) {
        int maxScalePoint = Integer.MIN_VALUE;

        try (BufferedReader reader = new BufferedReader(new FileReader("FulltimeSalaryScales.csv"))) {
            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    System.err.println("Invalid row format in FulltimeSalaryScales.csv: " + line);
                    continue;
                }

                String currentJobTitle = parts[0].trim();

                try {
                    int scalePoint = Integer.parseInt(parts[1].trim());

                    if (currentJobTitle.equalsIgnoreCase(jobType)) {
                        maxScalePoint = Math.max(maxScalePoint, scalePoint);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid scale point format in FulltimeSalaryScales.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading FulltimeSalaryScales.csv: " + e.getMessage());
        }

        if (maxScalePoint == Integer.MIN_VALUE) {
            System.err.println("No salary scales found for job type: " + jobType);
            return -1;
        }

        return maxScalePoint;
    }

    public static List<String[]> readEmployeeInfo() throws IOException {
        List<String[]> employeeData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;

            if ((line = reader.readLine()) != null && line.toLowerCase().contains("id")) {
                line = reader.readLine();
            }
            while (line != null) {
                employeeData.add(line.split(","));
                line = reader.readLine();
            }
        }
        return employeeData;
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
            String header = "id,username,name,dob,ppsNumber,password,jobTitle,scalePoint,pendingPromotionFlag,previousJobTitle,previousScalePoint,yearsAtTop";
            writer.write(header);
            writer.newLine();

            for (String[] record : employeeData) {
                if (record.length < 12) {
                    record = Arrays.copyOf(record, 12);
                    record[11] = "0";
                }
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
            reader.readLine();

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



