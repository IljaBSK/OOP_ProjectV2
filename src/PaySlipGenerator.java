import java.io.IOException;
import java.time.LocalDate;

public class PaySlipGenerator{
    public void payslipGenerator(){
        // Load necessary readers and writers
        EmployeeInfoReader employeeReader = new EmployeeInfoReader("EmployeeInfo.csv");
        FulltimeSalaryScalesReader salaryReader = new FulltimeSalaryScalesReader("FulltimeSalaryScales.csv");
        PaySlipWriter paySlipWriter = new PaySlipWriter("PaySlips.csv");

        // Initialize the PaySlipCalculator
        PaySlipCalculator calculator = new PaySlipCalculator(salaryReader, paySlipWriter);

        // Load employee data
        try {
            for (String[] employeeData : CSVManager.readEmployeeInfo()) {
                String employeeId = employeeData[0].trim();

                // Generate payslip for each employee
                LocalDate today = LocalDate.now();
                calculator.calculateAndWritePayslip(employeeId, employeeReader, today);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Payslips generated successfully.");

    }

    }