import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PaySlipGenerator {
    public void payslipGenerator() {

        EmployeeInfoReader employeeReader = new EmployeeInfoReader("EmployeeInfo.csv");
        FulltimeSalaryScalesReader salaryReader = new FulltimeSalaryScalesReader("FulltimeSalaryScales.csv");
        PaySlipWriter paySlipWriter = new PaySlipWriter("PaySlips.csv");
        PayClaimsReader payClaimsReader = new PayClaimsReader("PayClaims.csv");

        Map<String, String> workStatusMap = loadWorkStatus("EmployeeStatus.csv");

        PaySlipCalculator calculator = new PaySlipCalculator(salaryReader, paySlipWriter);

        try {
            for (String[] employeeData : CSVManager.readEmployeeInfo()) {
                String username = employeeData[1].trim();
                String workStatus = workStatusMap.get(username);

                if (workStatus == null) {
                    System.out.println("Work status not found for username: " + username);
                    continue;
                }

                LocalDate today = LocalDate.now();

                if ("Full-Time".equalsIgnoreCase(workStatus)) {
                    calculator.calculateAndWritePayslip(username, employeeReader, today);
                } else if ("Part-Time".equalsIgnoreCase(workStatus)) {
                    if (payClaimsReader.hasPayClaim(username)) {
                        calculator.calculateAndWritePayslip(username, employeeReader, today);
                    } else {
                        System.out.println("No pay claim found for part-time Employee: " + username);
                    }
                } else {
                    System.out.println("Unknown work status for username: " + username + ", Status: " + workStatus);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error generating payslips: " + ex.getMessage(), ex);
        }

        System.out.println("Payslips generated successfully.");
    }

    private Map<String, String> loadWorkStatus(String filePath) {
        Map<String, String> workStatusMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

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
