public class PaySlipCalculator {
    private SalaryScales salaryScales;

    // Constructor
    public PaySlipCalculator(SalaryScales salaryScales) {
        this.salaryScales = salaryScales;
    }

    // Calculate net salary
    public double calculateNetSalary(Employee employee) {
        double grossSalary = salaryScales.getSalary(employee);
        if (grossSalary <= 0) {
            System.err.println("Error: Unable to calculate salary for employee " + employee.getUsername());
            return 0.0;
        }

        // Deductions
        double incomeTax = calculateIncomeTax(grossSalary);
        double prsi = calculatePRSI(grossSalary);
        double usc = calculateUSC(grossSalary);
        double unionFee = 0.008;

        double totalDeductions = incomeTax + prsi + usc;
        double netSalary = grossSalary - totalDeductions;

        printPaySlip(employee, grossSalary, incomeTax, prsi, usc, unionFee, netSalary);

        return netSalary;
    }

    // Calculate income tax
    private double calculateIncomeTax(double grossSalary) {
        double tax = 0.0;
        if (grossSalary <= 42000) {
            tax = grossSalary * 0.20; // 20% tax
        } else {
            tax = 42000 * 0.20 + (grossSalary - 42000) * 0.40; // 20% on first 42k, 40% on remaining
        }
        return tax;
    }

    // Calculate PRSI
    private double calculatePRSI(double grossSalary) {
        return grossSalary * 0.041; // 4.1% PRSI
    }

    // Calculate USC
    private double calculateUSC(double grossSalary) {
        double usc = 0.0;

        if (grossSalary <= 12012) {
            usc = grossSalary * 0.005; // 0.5% for income <= 12,012
        } else if (grossSalary <= 25760) {
            usc = 12012 * 0.005 + (grossSalary - 12012) * 0.02; // 2% for income between 12,012 and 25,760
        } else if (grossSalary <= 70044) {
            usc = 12012 * 0.005 + (25760 - 12012) * 0.02 + (grossSalary - 25760) * 0.04; // 4% for income between 25,760 and 70,044
        } else {
            usc = 12012 * 0.005 + (25760 - 12012) * 0.02 + (70044 - 25760) * 0.04 + (grossSalary - 70044) * 0.08; // 8% for income above 70,044
        }
        return usc;
    }

    // Print a payslip breakdown
    private void printPaySlip(Employee employee, double grossSalary, double incomeTax, double prsi, double usc, double unionFee, double netSalary) {
        System.out.println("Payslip for Employee: " + employee.getUsername());
        System.out.println("Job Type: " + employee.getJobType());
        System.out.println("Gross Salary: " + grossSalary);
        System.out.println("Deductions:");
        System.out.println("  Income Tax: " + incomeTax);
        System.out.println("  PRSI: " + prsi);
        System.out.println("  USC: " + usc);
        System.out.println("  Union Fee: " + unionFee);
        System.out.println("Net Salary: " + netSalary);
    }
}