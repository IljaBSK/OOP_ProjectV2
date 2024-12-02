import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PaySlipGenerator {
    /**
     * Generates payslips for all employees by processing their data and pay claims.
     * Handles both full-time and part-time employees:
     * <ul>
     *   <li>For full-time employees, calculates and writes payslips based on salary scales.</li>
     *   <li>For part-time employees, processes valid pay claims before generating payslips.</li>
     * </ul>
     *
     * <p>Utilizes multiple data sources such as employee information, salary scales,
     * and pay claims to produce comprehensive payslip records.</p>
     *
     * @throws RuntimeException If an error occurs during payslip generation.
     */
    public void payslipGenerator() {

        EmployeeInfoReader employeeReader = new EmployeeInfoReader("EmployeeInfo.csv");
        FulltimeSalaryScalesReader salaryReader = new FulltimeSalaryScalesReader("FulltimeSalaryScales.csv");
        PaySlipWriter paySlipWriter = new PaySlipWriter("PaySlips.csv");
        PayClaimsReader payClaimsReader = new PayClaimsReader("PayClaims.csv");


        Map<String, String> workStatusMap = loadWorkStatus("EmployeeStatus.csv");


        PaySlipCalculator calculator = new PaySlipCalculator(salaryReader, paySlipWriter);


        try {
            for (String[] employeeData : CSVManager.readEmployeeInfo()) {
                if (employeeData == null || employeeData.length < 2) {
                    continue;
                }

                String username = employeeData[1].trim();
                String workStatus = workStatusMap.get(username);

                if (workStatus == null) {
                    continue;
                }

                LocalDate today = LocalDate.now();

                if ("Full-Time".equalsIgnoreCase(workStatus)) {

                    calculator.calculateAndWritePayslip(username, employeeReader, today);
                } else if ("Part-Time".equalsIgnoreCase(workStatus)) {

                    if (payClaimsReader.hasPayClaim(username)) {
                        processPartTimeEmployee(username, payClaimsReader, calculator, today);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error generating payslips: " + ex.getMessage(), ex);
        }
    }

    /**
     * Processes a part-time employee's pay claims and generates a payslip if a valid claim exists.
     *
     * <p>Steps:</p>
     * <ul>
     *   <li>Fetches employee details from the EmployeeInfo.csv file.</li>
     *   <li>Reads pay claims from the PayClaims.csv file to find the latest valid claim.</li>
     *   <li>Validates that the claim belongs to the current month and year.</li>
     *   <li>Calculates and writes the payslip using the provided calculator if the claim is valid.</li>
     * </ul>
     *
     * @param username The username of the part-time employee.
     * @param payClaimsReader The reader to access pay claims data.
     * @param calculator The calculator used to generate payslip calculations.
     * @param today The current date used to verify the claim's validity.
     */
    private void processPartTimeEmployee(String username, PayClaimsReader payClaimsReader, PaySlipCalculator calculator, LocalDate today) {
        try (BufferedReader reader = new BufferedReader(new FileReader("PayClaims.csv"))) {

            EmployeeInfoReader employeeReader = new EmployeeInfoReader("EmployeeInfo.csv");


            String[] employeeData = employeeReader.getEmployeeDataByUsername(username);

            if (employeeData == null) {
                System.err.println("Error: Employee with username " + username + " not found in EmployeeInfo.csv.");
                return;
            }

            String employeeId = employeeData[0].trim(); // Fetch ID
            String name = employeeData[2].trim();      // Fetch Name
            String line;
            LocalDate latestClaimDate = null;
            double hoursWorked = 0.0;
            double hourlyPay = 0.0;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5 || fields[0].trim().isEmpty() || !fields[0].trim().equals(username)) {
                    continue;
                }

                LocalDate claimDate = LocalDate.parse(fields[1].trim());
                double claimHoursWorked = Double.parseDouble(fields[2].trim());
                double claimHourlyPay = Double.parseDouble(fields[3].trim());

                if (latestClaimDate == null || claimDate.isAfter(latestClaimDate)) {
                    latestClaimDate = claimDate;
                    hoursWorked = claimHoursWorked;
                    hourlyPay = claimHourlyPay;
                }
            }

            if (latestClaimDate != null && latestClaimDate.getMonth() == today.getMonth() && latestClaimDate.getYear() == today.getYear()) {
                calculator.calculateAndWritePartTimePayslip(employeeId, name, hoursWorked, hourlyPay, latestClaimDate, today);
            } else if (latestClaimDate == null) {
                System.err.println("No valid pay claim found for part-time Employee: " + username);
            } else {
                System.err.println("Latest pay claim is not from the current month for Employee: " + username);
            }
        } catch (IOException e) {
            System.err.println("Error processing part-time Employee: " + username + ", " + e.getMessage());
        }
    }

    /**
     * Loads the work status of employees from a CSV file and maps each username to its work status.
     *
     * <p>Reads data from the specified file and populates a map where the key is the username
     * and the value is the work status (e.g., "Full-Time" or "Part-Time").</p>
     *
     * @param filePath The path to the CSV file containing usernames and work statuses.
     * @return A map containing usernames as keys and their corresponding work statuses as values.
     */
    private Map<String, String> loadWorkStatus(String filePath) {
        Map<String, String> workStatusMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 2) {
                    String username = fields[0].trim();
                    String workStatus = fields[1].trim();
                    workStatusMap.put(username, workStatus);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading work status data: " + e.getMessage());
        }
        return workStatusMap;
    }
}
