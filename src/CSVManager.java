
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.HashMap;


public class CSVManager {
    private static final String filename = "ValidLogins.csv";

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

    public static HashMap<String, User> readValidUsers() {
        HashMap<String, User> users = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.readLine();

            String line;

            while((line = reader.readLine()) != null){
                String[] details = line.split(",");
                if(details.length == 3){
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
                }else{
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
            reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].trim().equals(empID)) {
                    // Assuming the CSV structure is: ID,userName,,DoB,PPSNo,Username,Password,JobType,ScalePoint
                    String id = details[0].trim();
                    String username = details[1].trim();
                    String name = details[2].trim();
                    String dob = details[3].trim();
                    String ppsNo = details[4].trim();
                    String password = details[5].trim();
                    String jobType = details[6].trim();
                    int scalePoint = Integer.parseInt(details[7].trim());

                    return new Employee(username, jobType, dob, name, password, ppsNo, Integer.parseInt(id), jobType, scalePoint);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: EmployeeInfo.csv", e);
        }
        return null;
    }

    public static void updateEmployeeDetails(Employee employee, int flag) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");

                if (details[0].trim().equals(String.valueOf(employee.getId()))) {
                    // Update job title, scale point, and promotion flag
                    details[6] = employee.getJobTitle(); // Job title column
                    details[7] = String.valueOf(employee.getScalePoint()); // Scale point column
                    details[8] = String.valueOf(flag); // Promotion flag
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

}