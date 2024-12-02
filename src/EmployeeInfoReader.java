import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeInfoReader {
    private String filePath;
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
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[1].trim().equalsIgnoreCase(username)) {
                    return fields[6].trim();
                }
            }
        }
        return null;
    }

    public String getScalePointForPartTime(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[1].trim().equalsIgnoreCase(username)) {
                    return fields[7].trim();
                }
            }
        }
        return null;
    }

    public String[] getEmployeeDataByUsername(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[1].trim().equalsIgnoreCase(username)) {
                    return fields;
                }
            }
        }
        return null;
    }
}