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

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].trim().equals(jobTitle) && fields[1].trim().equals(scalePoint)) {
                    return Double.parseDouble(fields[2].trim());
                }
            }
        }

        throw new IOException("Salary not found for Job Title: " + jobTitle + " and Scale Point: " + scalePoint);
    }

    /**
     * Retrieves the hourly rate for a given job title.
     *
     * <p>This method reads from a CSV file to find the annual salary associated with the specified
     * job title. It calculates the hourly rate by dividing the annual salary by 2080
     * (the typical number of working hours in a year).</p>
     *
     * @param jobTitle   the job title for which to retrieve the hourly rate
     * @param scalePoint
     * @return the calculated hourly rate, or <code>0.0</code> if the job title is not found
     * @throws IOException           if an error occurs while reading the file
     * @throws NumberFormatException if the salary field cannot be parsed as a double
     */
    public double getHourlyRate(String jobTitle, int scalePoint) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;


            reader.readLine();


            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length >= 3) {
                    String currentJobTitle = fields[0].trim();
                    int currentScalePoint = Integer.parseInt(fields[1].trim());


                    if (currentJobTitle.equalsIgnoreCase(jobTitle) && currentScalePoint == scalePoint) {

                        double annualSalary = Double.parseDouble(fields[2].trim());
                        return annualSalary / 2080;
                    }
                }
            }
        }


        System.err.println("No match found for jobTitle: " + jobTitle + " and scalePoint: " + scalePoint);
        return 0.0;
    }


}

