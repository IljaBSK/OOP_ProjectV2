import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeInfoReader {
    private String filePath;

    /**
     * Constructor to initialize the path to the EmployeeInfo.csv file.
     *
     * @param filePath Path to the CSV file containing employee data.
     */
    public EmployeeInfoReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Retrieves the job title for a specific employee ID.
     *
     * @param employeeId The ID of the employee.
     * @return The job title of the employee.
     * @throws IOException If the file cannot be read or the employee ID is not found.
     */
    public String getJobTitle(String employeeId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            reader.readLine();

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the employee ID matches
                if (fields[0].trim().equals(employeeId)) {
                    return fields[5].trim(); // Job Title is in column 6
                }
            }
        }

        throw new IOException("Employee with ID " + employeeId + " not found in EmployeeInfo.csv.");
    }

    /**
     * Retrieves the scale point for a specific employee ID.
     *
     * @param employeeId The ID of the employee.
     * @return The scale point of the employee.
     * @throws IOException If the file cannot be read or the employee ID is not found.
     */
    public String getScalePoint(String employeeId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            reader.readLine();

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the employee ID matches
                if (fields[0].trim().equals(employeeId)) {
                    return fields[6].trim(); // Scale Point is in column 7
                }
            }
        }

        throw new IOException("Employee with ID " + employeeId + " not found in EmployeeInfo.csv.");
    }
}