import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeInfoReader {
    private String filePath;
    private static final int ID_INDEX = 0;
    private static final int USERNAME_INDEX = 1;
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
     * Retrieves the job title for a specific employee ID.
     *
     * @param employeeId The ID of the employee.
     * @return The job title of the employee.
     * @throws IOException If the file cannot be read or the employee ID is not found.
     */
    public String getJobTitle(String employeeId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length < SCALE_POINT_INDEX + 1) continue; // Ensure enough fields
                if (fields[ID_INDEX].trim().equals(employeeId)) {
                    return fields[JOB_TITLE_INDEX].trim();
                }
            }
        }

        throw new IOException("Employee with ID " + employeeId + " not found.");
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


            reader.readLine();


            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");


                if (fields[0].trim().equals(employeeId)) {
                    return fields[7].trim();
                }
            }
        }

        throw new IOException("Employee with ID " + employeeId + " not found in EmployeeInfo.csv.");
    }

    public String getName(String employeeId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length < NAME_INDEX + 1) continue; // Ensure enough fields
                if (fields[ID_INDEX].trim().equals(employeeId)) {
                    return fields[NAME_INDEX].trim();
                }
            }
        }

        throw new IOException("Employee with ID " + employeeId + " not found.");
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