import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
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
     * @return The net salary of the employee.
     */
    public double calculateAndWritePayslip(String employeeId, EmployeeInfoReader employeeReader) {
        try {
            // Step 1: Get job title and scale point from EmployeeInfo.csv
            String jobTitle = employeeReader.getJobTitle(employeeId);
            String scalePoint = employeeReader.getScalePoint(employeeId);

            // Step 2: Get gross salary from FulltimeSalaryScales.csv
            double grossSalary = salaryReader.getSalary(jobTitle, scalePoint);
            double grossPay = grossSalary / 52; // Divide salary for weekly payslips


            // Step 3: Calculate deductions
            double incomeTax = calculateIncomeTax(grossPay);
            double prsi = calculatePRSI(grossPay);
            double usc = calculateUSC(grossPay);
            double unionFee = grossPay * 0.08; // 8% union fee
            double totalDeductions = incomeTax + prsi + usc + unionFee;
            double netPay = grossPay - totalDeductions;

            // Step 4: Prepare payslip data
            String[] payslipData = {
                    employeeId, // Employee ID
                    jobTitle,   // Job Title
                    scalePoint, // Scale Point
                    String.valueOf(grossPay),
                    String.valueOf(incomeTax),
                    String.valueOf(prsi),
                    String.valueOf(usc),
                    String.valueOf(unionFee),
                    String.valueOf(netPay),
                    LocalDate.now().toString() // Current date
            };

            // Step 5: Write payslip to PaySlips.csv
            writer.writePayslip(payslipData);

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
    public void submitPayClaim(String username, EmployeeInfoReader employeeReader) throws IOException {
        // Step 1: Retrieve job title from EmployeeInfo.csv
        String jobTitle = employeeReader.getJobTitleByUsername(username);
        if (jobTitle == null || jobTitle.isEmpty()) {
            System.out.println("Job title not found for username: " + username);
            return;
        }

        // Step 2: Retrieve hourly rate from FulltimeSalaryScales.csv
        double hourlyRate = salaryReader.getHourlyRate(jobTitle);
        if (hourlyRate <= 0) {
            System.out.println("Hourly rate not found for job title: " + jobTitle);
            return;
        }

        System.out.println("Your job title is: " + jobTitle);
        System.out.println("Your hourly rate is: €" + hourlyRate);

        // Step 3: Prompt for hours worked
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of hours worked:");
        int hoursWorked = input.nextInt();

        // Step 4: Calculate total pay
        double totalPay = hoursWorked * hourlyRate;
        LocalDate today = LocalDate.now();

        // Step 5: Display pay claim summary
        System.out.printf("Pay Claim Summary:\nDate: %s\nHours Worked: %d\nHourly Rate: %.2f\nTotal Pay: %.2f\n",
                today, hoursWorked, hourlyRate, totalPay);

        // Step 6: Append the claim to PayClaims.csv
        try (BufferedWriter claimWriter = new BufferedWriter(new FileWriter("PayClaims.csv", true))) {
            String record = String.format("%s,%s,%d,%.2f,%.2f", username, today, hoursWorked, hourlyRate, totalPay);
            claimWriter.write(record);
            claimWriter.newLine();
            System.out.println("Pay claim submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to PayClaims.csv: " + e.getMessage());
            throw e;
        }
    }
}


