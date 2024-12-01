
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.util.HashMap;


public class CSVManager {


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
//test for commit attempt since mine is not workingwq dqwd qw


