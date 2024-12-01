import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FulltimeSalaryScalesReader {
    private String filePath;

    /**
     * Constructor to initialize the path to the FulltimeSalaryScales.csv file.
     *
     * @param filePath Path to the CSV file containing salary scales.
     */
    public FulltimeSalaryScalesReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Retrieves the salary for a specific job title and scale point.
     *
     * @param jobTitle   The job title of the employee.
     * @param scalePoint The scale point of the employee.
     * @return The salary corresponding to the job title and scale point.
     * @throws IOException If the file cannot be read or the combination is not found.
     */
    public double getSalary(String jobTitle, String scalePoint) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            reader.readLine();

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Validate and match the job title and scale point
                if (fields[0].trim().equals(jobTitle) && fields[1].trim().equals(scalePoint)) {
                    return Double.parseDouble(fields[2].trim()); // Salary is in column 3
                }
            }
        }

        throw new IOException("Salary not found for Job Title: " + jobTitle + " and Scale Point: " + scalePoint);
    }
}
//TESTING COMMITING3wertrwertwetwetwgnfgnfg343