import java.io.*;
import java.util.*;

public class SalaryScales {

    // Store salary scale
    private List<String[]> salaryScales;

    // Constructor
    public SalaryScales() {
        salaryScales = new ArrayList<>();
    }

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

    // Method to get the salary for an employee based on jobType and scalePoint
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

    // Optional debugging method to print all salary scales
    public void printSalaryScales() {
        System.out.println("Loaded Salary Scales:");
        for (String[] entry : salaryScales) {
            System.out.println("Job Type: " + entry[0] + ", Scale Point: " + entry[1] + ", Salary: " + entry[2]);
        }
    }
}