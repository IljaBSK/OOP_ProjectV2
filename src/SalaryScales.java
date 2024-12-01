import java.io.*;
import java.util.*;

public class SalaryScales {

    // Store salary scale
    private List<String[]> salaryScales;

    // Constructor
    public SalaryScales() {
        salaryScales = new ArrayList<>();
    }

    /**
     * Loads salary scales from the "Fulltime_Salary_scales.csv" file.
     *
     * <p>This method reads the CSV file, skips the header, and parses each line to extract
     * job type, scale point, and salary. Valid entries are added to the `salaryScales` list.</p>
     *
     * @throws IOException if an error occurs while reading the file
     */
    public void loadSalaryScales() {
        try (Scanner scanner = new Scanner(new File("Fulltime_Salary_scales.csv"))) {
            // Skip the header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            // Read each line and split into parts
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    salaryScales.add(new String[]{
                            parts[0].trim(),                 // jobType
                            parts[1].trim(),                 // scalePoint
                            parts[2].trim()                  // salary
                    });
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
            System.out.println("Salary scales loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the salary scales file: " + e.getMessage());
        }
    }

    /**
     * Retrieves the salary for an employee based on their job type and scale point.
     *
     * <p>This method searches the {@code salaryScales} list for a matching job type and
     * scale point. If a match is found, the corresponding salary is returned. If no match
     * is found, it prints an error message and returns {@code 0.0}.</p>
     *
     * @param employee the {@link Employee} object containing the job type and scale point
     * @return the matching salary, or {@code 0.0} if no match is found
     */
    public double getSalary(Employee employee) {
        String jobType = employee.getJobType();
        int scalePoint = employee.getScalePoint();

        // Search through the list for a matching jobType and scalePoint
        for (String[] entry : salaryScales) {
            String entryJobType = entry[0];
            int entryScalePoint = Integer.parseInt(entry[1]);
            double entrySalary = Double.parseDouble(entry[2]);

            if (entryJobType.equalsIgnoreCase(jobType) && entryScalePoint == scalePoint) {
                return entrySalary;
            }
        }

        System.err.println("Salary not found for jobType: " + jobType + " and scalePoint: " + scalePoint);
        return 0.0; // Default value if no matching entry is found
    }

    /**
     * Prints the loaded salary scales to the console.
     *
     * <p>This method iterates through the {@code salaryScales} list and displays
     * each job type, scale point, and salary in a readable format.</p>
     */
    public void printSalaryScales() {
        System.out.println("Loaded Salary Scales:");
        for (String[] entry : salaryScales) {
            System.out.println("Job Type: " + entry[0] + ", Scale Point: " + entry[1] + ", Salary: " + entry[2]);
        }
    }
    /**
     * Reads employee information from the "EmployeeInfo.csv" file.
     *
     * <p>This method reads all rows from the file, skipping the header row, and
     * returns the data as a list of string arrays where each array represents an employee's record.</p>
     *
     * @return a list of string arrays, each representing an employee's record
     * @throws IOException if an error occurs while reading the file
     */
    private List<String[]> readEmployeeInfo() throws IOException {
        List<String[]> employeeData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("EmployeeInfo.csv"))) {
            String line;

            // Skip the header row
            if ((line = reader.readLine()) != null && line.toLowerCase().contains("id")) {
                // Header detected, move to the next line
                line = reader.readLine();
            }

            while (line != null) {
                employeeData.add(line.split(",")); // Split each line into an array
                line = reader.readLine();
            }
        }
        return employeeData;
    }

    /**
     * Writes updated employee information to the "EmployeeInfo.csv" file.
     *
     * <p>This method overwrites the file with the provided employee data, including
     * the header row and all employee records.</p>
     *
     * @param employeeData a list of string arrays, where each array represents an employee's record
     * @throws IOException if an error occurs while writing to the file
     */
    private void writeEmployeeInfo(List<String[]> employeeData) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("EmployeeInfo.csv"))) {
            // Write the header
            writer.write("id,username,name,dob,ppsNumber,jobType,scalePoint");
            writer.newLine();

            // Write each employee's data
            for (String[] employee : employeeData) {
                writer.write(String.join(",", employee));
                writer.newLine();
            }

            // Debugging message to confirm data is written
            System.out.println("EmployeeInfo.csv updated successfully!");
        }
    }
}