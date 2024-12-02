import java.io.*;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PaySlipCalculator {
    private FulltimeSalaryScalesReader salaryReader;
    private PaySlipWriter writer;

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
     * Calculates the payslip for a full-time employee based on their annual salary,
     * then writes the generated payslip data to a file.
     *
     *
     *
     * @param username The username of the full-time employee.
     * @param employeeReader The reader used to fetch employee details from EmployeeInfo.csv.
     * @param today The current date for payslip generation.
     * @throws IOException If an error occurs while fetching employee data or writing the payslip data to the file.
     */

    public void calculateAndWritePayslip(String username, EmployeeInfoReader employeeReader, LocalDate today) throws IOException {
        String[] employeeData = employeeReader.getEmployeeDataByUsername(username);

        if (employeeData == null) {
            throw new IOException("Employee with username " + username + " not found.");
        }

        String employeeId = employeeData[0].trim();
        String name = employeeData[2].trim();
        String jobTitle = employeeData[6].trim();
        String scalePoint = employeeData[7].trim();
        double annualSalary = salaryReader.getSalary(jobTitle, String.valueOf(Integer.parseInt(scalePoint)));
        double grossPay = annualSalary / 12;

        double incomeTax = calculateIncomeTax(grossPay);
        double prsi = calculatePRSI(grossPay);
        double usc = calculateUSC(grossPay);
        double unionFee = grossPay * 0.08;
        double totalDeductions = incomeTax + prsi + usc + unionFee;
        double netPay = grossPay - totalDeductions;

        String[] payslipData = {
                employeeId,
                name,
                today.toString(),
                jobTitle,
                scalePoint,
                String.format("%.2f", grossPay),
                String.format("%.2f", incomeTax),
                String.format("%.2f", prsi),
                String.format("%.2f", usc),
                String.format("%.2f", unionFee),
                String.format("%.2f", netPay)
        };

        writer.writePayslip(payslipData);
    }




    /**
     * Calculates the payslip for a part-time employee based on hours worked and hourly pay,
     * then writes the generated payslip data to a file.
     *
     * @param employeeId The unique ID of the employee.
     * @param name The name of the employee.
     * @param hoursWorked The total hours worked by the employee.
     * @param hourlyPay The hourly pay rate for the employee.
     * @param claimDate The date of the pay claim.
     * @param today The current date for payslip generation.
     * @throws IOException If an error occurs while writing the payslip data to the file.
     */


    public void calculateAndWritePartTimePayslip(String employeeId, String name, double hoursWorked, double hourlyPay, LocalDate claimDate, LocalDate today) throws IOException {

        double grossPay = hoursWorked * hourlyPay;
        double incomeTax = calculateIncomeTax(grossPay);
        double prsi = calculatePRSI(grossPay);
        double usc = calculateUSC(grossPay);
        double unionFee = grossPay * 0.08;
        double totalDeductions = incomeTax + prsi + usc + unionFee;
        double netPay = grossPay - totalDeductions;


        String[] payslipData = {
                employeeId,
                name,
                claimDate.toString(),
                String.format("%.2f", hoursWorked),
                String.format("%.2f", hourlyPay),
                String.format("%.2f", grossPay),
                String.format("%.2f", incomeTax),
                String.format("%.2f", prsi),
                String.format("%.2f", usc),
                String.format("%.2f", unionFee),
                String.format("%.2f", netPay)
        };


        writer.writePayslip(payslipData);
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

        String jobTitle = employeeReader.getJobTitleByUsername(username);
        String scalePointStr = employeeReader.getScalePointForPartTime(username);

        LocalDate today = LocalDate.now();

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


        double hourlyRate = salaryReader.getHourlyRate(jobTitle, scalePoint);
        if (hourlyRate <= 0) {
            System.out.println("Hourly rate not found for job title: " + jobTitle + " and scale point: " + scalePoint);
            return;
        }

        System.out.println("Your job title is: " + jobTitle);
        System.out.println("Your scale point is: " + scalePoint);
        System.out.println("Your hourly rate is: €" + hourlyRate);


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


        double totalPay = hoursWorked * hourlyRate;


        System.out.printf("Pay Claim Summary:\nDate: %s\nHours Worked: %d\nHourly Rate: %.2f\nTotal Pay: %.2f\n",
                today, hoursWorked, hourlyRate, totalPay);


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



