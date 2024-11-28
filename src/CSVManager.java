
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.HashMap;


public class CSVManager {
    private static final String filename = "ValidLogins.csv";


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

    public static void updateEmployeeDetails(String username, String newJobType, int newScalePoint) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].trim().equalsIgnoreCase(username)) {
                    if (details.length >= 4) { // Assuming structure: username,password,jobType,scalePoint,...
                        details[2] = newJobType; // Update jobType
                        details[3] = String.valueOf(newScalePoint); // Update scalePoint
                        updated = true;
                    }
                }
                lines.add(String.join(",", details));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading from file: EmployeesExample.csv", e);
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("EmployeeInfo.csv"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Employee details updated successfully.");
            } catch (IOException e) {
                throw new RuntimeException("Error writing to file: EmployeesExample.csv", e);
            }
        } else {
            System.out.println("Employee not found.");
        }
    }

    public static Employee getEmployeeByID(String empID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].trim().equals(empID)) {
                    // Assuming the CSV structure is: ID,Name,DoB,PPSNo,Username,Password,JobType,ScalePoint
                    String id = details[0].trim();
                    String name = details[1].trim();
                    String dob = details[2].trim();
                    int ppsNo = Integer.parseInt(details[3].trim());
                    String username = details[4].trim();
                    String password = details[5].trim();
                    String jobType = details[6].trim();
                    int scalePoint = Integer.parseInt(details[7].trim());

                    return new Employee(username, password, jobType, name, dob, ppsNo, Integer.parseInt(id), jobType, scalePoint);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: EmployeesExample.csv", e);
        }
        return null;
    }

    public static void updateEmployee(Employee employee) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].trim().equals(String.valueOf(employee.getId()))) {
                    // Update the specific employee line
                    details[6] = employee.getJobTitle(); // Update job title
                    details[7] = String.valueOf(employee.getScalePoint()); // Update scale point
                    updated = true;
                }
                lines.add(String.join(",", details));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: EmployeesExample.csv", e);
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("EmployeesInfo.csv"))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error writing to the file: EmployeesExample.csv", e);
            }
        } else {
            System.out.println("Employee ID not found. Update failed.");
        }
    }

}