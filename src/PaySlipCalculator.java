import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PaySlipCalculator {
    private FulltimeSalaryScalesReader salaryReader; // Reads salary data
    private PaySlipWriter writer; // Writes payslip data to file

    /**
     * Constructor to initialize Salary Reader and Payslip Writer.
     *
     * @param salaryReader The reader for FulltimeSalaryScales.csv.
     * @param writer       The writer for PaySlips.csv.
     */
    public PaySlipCalculator(FulltimeSalaryScalesReader salaryReader, PaySlipWriter writer) {
        this.salaryReader = salaryReader;
        this.writer = writer;
    }

    /**
     * Calculates and writes a payslip for an employee.
     *
     * @param employeeId     The ID of the employee.
     * @param employeeReader The reader for EmployeeInfo.csv.
     * @param today
     * @return The net salary of the employee.
     */



    public double calculateAndWritePayslip(String employeeId, EmployeeInfoReader employeeReader, LocalDate today) {
        try {


            // Step 1: Get job title and scale point from EmployeeInfo.csv
            String jobTitle = employeeReader.getJobTitle(employeeId);
            String scalePoint = employeeReader.getScalePoint(employeeId);
            String name = employeeReader.getName(employeeId);

            // Step 2: Get gross salary from FulltimeSalaryScales.csv
            double grossSalary = salaryReader.getSalary(jobTitle, scalePoint);
            double grossPay = grossSalary / 12; // Divide salary for monthly payslips


            // Step 3: Calculate deductions
            double incomeTax = calculateIncomeTax(grossPay);
            double prsi = calculatePRSI(grossPay);
            double usc = calculateUSC(grossPay);
            double unionFee = grossPay * 0.08; // 8% union fee
            double totalDeductions = incomeTax + prsi + usc + unionFee;
            double netPay = grossPay - totalDeductions;
            double healthInsurance = 0.001;

            // Step 4: Prepare payslip data
            String[] payslipData = {
                    employeeId,
                    name,
                    today.toString(),
                    jobTitle,
                    scalePoint,
                    String.format("%.2f", grossPay),        // Format to 2 decimal places
                    String.format("%.2f", incomeTax),
                    String.format("%.2f", prsi),
                    String.format("%.2f", usc),
                    String.format("%.2f", unionFee),
                    String.format("%.2f", netPay),

            };
            // Step 5: Write payslip to PaySlips.csv
            writer.writePayslip(payslipData, today);

            System.out.println("Payslip successfully generated for Employee ID: " + employeeId);
            return netPay;

        } catch (IOException e) {
            System.err.println("Error processing payslip for Employee ID " + employeeId + ": " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Calculate income tax (20% for first 42k, 40% for the rest).
     *
     * @param grossSalary The gross salary.
     * @return The income tax amount.
     */
    private double calculateIncomeTax(double grossSalary) {
        if (grossSalary <= 42000) {
            return grossSalary * 0.20;
        } else {
            return 42000 * 0.20 + (grossSalary - 42000) * 0.40;
        }
    }

    /**
     * Calculate PRSI (4.1% of gross salary).
     *
     * @param grossSalary The gross salary.
     * @return The PRSI amount.
     */
    private double calculatePRSI(double grossSalary) {
        return grossSalary * 0.041;
    }

    /**
     * Calculate USC based on income thresholds.
     *
     * @param grossSalary The gross salary.
     * @return The USC amount.
     */
    private double calculateUSC(double grossSalary) {
        if (grossSalary <= 12012) {
            return grossSalary * 0.005;
        } else if (grossSalary <= 25760) {
            return 12012 * 0.005 + (grossSalary - 12012) * 0.02;
        } else if (grossSalary <= 70044) {
            return 12012 * 0.005 + (25760 - 12012) * 0.02 + (grossSalary - 25760) * 0.04;
        } else {
            return 12012 * 0.005 + (25760 - 12012) * 0.02 + (70044 - 25760) * 0.04 + (grossSalary - 70044) * 0.08;
        }
    }

    /**
     * Submits a pay claim for an employee based on hours worked and their hourly rate.
     *
     * <p>This method retrieves the employee's job title and hourly rate, prompts for the
     * number of hours worked, calculates the total pay, and appends the pay claim to the
     * "PayClaims.csv" file.</p>
     *
     * @param username the username of the employee submitting the pay claim
     * @param employeeReader an {@link EmployeeInfoReader} to retrieve employee information
     * @throws IOException if an error occurs while reading or writing files
     */
    public void submitPayClaim(String username, EmployeeInfoReader employeeReader) throws IOException {
        // Step 1: Retrieve job title and scale point using username
        String jobTitle = employeeReader.getJobTitleByUsername(username);
        String scalePointStr = employeeReader.getScalePointForPartTime(username);

        if (jobTitle == null || jobTitle.isEmpty()) {
            System.out.println("Job title not found for username: " + username);
            return;
        }

        if (scalePointStr == null || scalePointStr.isEmpty()) {
            System.out.println("Scale point not found for username: " + username);
            return;
        }

        int scalePoint;
        try {
            scalePoint = Integer.parseInt(scalePointStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid scale point for username: " + username);
            return;
        }

        // Step 2: Retrieve hourly rate using job title and scale point
        double hourlyRate = salaryReader.getHourlyRate(jobTitle, scalePoint);
        if (hourlyRate <= 0) {
            System.out.println("Hourly rate not found for job title: " + jobTitle + " and scale point: " + scalePoint);
            return;
        }

        System.out.println("Your job title is: " + jobTitle);
        System.out.println("Your scale point is: " + scalePoint);
        System.out.println("Your hourly rate is: €" + hourlyRate);

        // Step 3: Prompt for hours worked with validation
        Scanner input = new Scanner(System.in);
        int hoursWorked = -1;
        while (hoursWorked < 0 || hoursWorked > 160) {
            System.out.println("Enter the number of hours worked (0-160):");
            try {
                hoursWorked = input.nextInt();
                if (hoursWorked < 0 || hoursWorked > 160) {
                    System.out.println("Invalid hours worked. Please enter a value between 0 and 160.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                input.next(); // Clear invalid input
            }
        }

        // Step 4: Calculate total pay
        double totalPay = hoursWorked * hourlyRate;
        LocalDate today = LocalDate.now();

        // Step 5: Display pay claim summary
        System.out.printf("Pay Claim Summary:\nDate: %s\nHours Worked: %d\nHourly Rate: %.2f\nTotal Pay: %.2f\n",
                today, hoursWorked, hourlyRate, totalPay);

        // Step 6: Append the claim to PayClaims.csv
        try (BufferedWriter claimWriter = new BufferedWriter(new FileWriter("PayClaims.csv", true))) {
            String record = String.format("%s,%s,%d,%.2f,%.2f,%d", username, today, hoursWorked, hourlyRate, totalPay, scalePoint);
            claimWriter.write(record);
            claimWriter.newLine();
            System.out.println("Pay claim submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to PayClaims.csv: " + e.getMessage());
            throw e;
        }
    }


}



