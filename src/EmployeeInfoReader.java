import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeInfoReader {
    private String filePath;
    private static final int ID_INDEX = 0;
    private static final int JOB_TITLE_INDEX = 6;
    private static final int SCALE_POINT_INDEX = 7;
    private static final int NAME_INDEX = 2;
    /**
     * Constructor to initialize the path to the EmployeeInfo.csv file.
     *
     * @param filePath Path to the CSV file containing employee data.
     */
    public EmployeeInfoReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the job title of an employee by their username.
     *
     * <p>This method reads a CSV file to find the employee with the given username
     * and returns their job title.</p>
     *
     * @param username the username of the employee
     * @return the job title of the employee, or <code>null</code> if not found
     * @throws IOException if an error occurs while reading the file
     */
    public String getJobTitleByUsername(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            reader.readLine();

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Match by username
                if (fields[1].trim().equalsIgnoreCase(username)) {
                    return fields[6].trim(); // Job title is in column 7
                }
            }
        }

        return null; // Return null if not found
    }

    public String getScalePointForPartTime(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            reader.readLine();

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Match by username
                if (fields[1].trim().equalsIgnoreCase(username)) {
                    return fields[7].trim(); // Scale point is in column 8
                }
            }
        }

        return null; // Return null if not found
    }

    public String[] getEmployeeDataByUsername(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip the header row

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Match by username (assuming it's in column 2, index 1)
                if (fields[1].trim().equalsIgnoreCase(username)) {
                    return fields; // Return the entire row as an array
                }
            }
        }

        return null; // Return null if username is not found
    }

}