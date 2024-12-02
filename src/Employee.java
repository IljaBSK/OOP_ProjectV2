import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Employee extends User {

    private String name;
    private String doB;
    private String ppsNo;
    private int id;
    private String jobTitle;
    private int scalePoint;
    private int yearsAtTop;

    //Fields to track the promotion status
    private int pendingPromotionFlag;
    private String previousJobTitle;
    private int previousScalePoint;
    /**
     * Constructs an Employee with full details.
     *
     * @param username the username of the employee
     * @param password the password of the employee
     * @param name the name of the employee
     * @param jobType the job type (e.g., full-time or part-time)
     * @param doB the date of birth of the employee
     * @param pps the PPS number of the employee
     * @param id the unique ID of the employee
     * @param jobTitle the current job title of the employee
     * @param scalePoint the current scale point of the employee
     */
    public Employee(String username, String password, String name, String jobType, String doB,String pps, int id, String jobTitle, int scalePoint) {
        super(username, password, jobType);
        this.name = name;
        this.doB = doB;
        this.ppsNo = pps;
        this.id = id;
        this.jobTitle = jobTitle;
        this.scalePoint = scalePoint;
        this.previousJobTitle = jobTitle;
        this.previousScalePoint = scalePoint;
    }
    /**
     * Constructs an Employee with basic credentials.
     *
     * @param userName the username of the employee
     * @param userPassword the password of the employee
     * @param userJob the job type of the employee
     */
    public Employee(String userName, String userPassword, String userJob) {
        super(userName, userPassword, userJob);
    }
    /**
     * Gets the name of the employee.
     *
     * @return the name of the employee
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the unique ID of the employee.
     *
     * @return the ID of the employee
     */
    public int getId(){return id;}
    public int getYearsAtTop() {
        return yearsAtTop;
    }
    /**
     * Gets the current job title of the employee.
     *
     * @return the job title of the employee
     */
    public String getJobTitle() {
        return jobTitle;
    }
    /**
     * Gets the current scale point of the employee.
     *
     * @return the scale point of the employee
     */
    public int getScalePoint() {
        return scalePoint;
    }
    /**
     * Sets the job title of the employee.
     *
     * @param jobTitle the new job title
     */
    public void setJobTitle(String jobTitle){
        this.jobTitle=jobTitle;
    }
    /**
     * Sets the scale point of the employee.
     *
     * @param scalePoint the new scale point
     */
    public void setScalePoint(int scalePoint){
        this.scalePoint=scalePoint;
    }
    /**
     * Checks if the employee has a pending promotion flag.
     *
     * @return true if there is a pending promotion, false otherwise
     */
    public boolean hasPendingPromotionFlag() {
        return this.pendingPromotionFlag == 1;
    }
    /**
     * Sets the pending promotion flag for the employee.
     *
     * @param flag the value of the pending promotion flag (1 for pending, 0 otherwise)
     */
    public void setPendingPromotionFlag(int flag) {
        this.pendingPromotionFlag = flag;
    }
    /**
     * Sets the previous job title of the employee.
     *
     * @param previousJobTitle the previous job title
     */

    public void setPreviousJobTitle(String previousJobTitle) {
        this.previousJobTitle = previousJobTitle;
    }
    /**
     * Sets the previous scale point of the employee.
     *
     * @param previousScalePoint the previous scale point
     */
    public void setPreviousScalePoint(int previousScalePoint) {
        this.previousScalePoint = previousScalePoint;
    }
    /**
     * Gets the previous job title of the employee.
     *
     * @return the previous job title
     */
    public String getPreviousJobTitle() {
        return this.previousJobTitle;
    }
    /**
     * Gets the previous scale point of the employee.
     *
     * @return the previous scale point
     */
    public int getPreviousScalePoint() {
        return this.previousScalePoint;
    }
    /**
     * Rejects the employee's pending promotion.
     *
     * <p>If the employee has a pending promotion, this method reverts their job title
     * and scale point to the previous values and updates the system accordingly.</p>
     *
     * @throws IOException if an error occurs while reverting the promotion
     */
    public void rejectPromotion() {
        if (hasPendingPromotionFlag()) {
            System.out.println("Promotion rejected. Reverting to previous position...");

            String[] revertedDetails = CSVManager.revertPromotion(this);

            System.out.println("Your position has been reverted to: " + revertedDetails[0] +
                    " (Scale Point: " + revertedDetails[1] + ")");
        } else {
            System.out.println("No promotion to reject.");
        }
    }

    /**
     * Confirms the employee's pending promotion.
     *
     * <p>If the employee has a pending promotion, this method updates their role
     * and resets the promotion flag in the system after the employee acknowledges
     * the promotion.</p>
     *
     * @throws IOException if an error occurs while updating the promotion flag
     */
    public void confirmPromotion() {
        if (hasPendingPromotionFlag()) {
            System.out.println("Promotion confirmed. Congratulations on your new position!");

            this.jobTitle = this.previousJobTitle;
            this.scalePoint = this.previousScalePoint;

            this.yearsAtTop = 0;

            this.pendingPromotionFlag = 0;

            this.previousJobTitle = null;
            this.previousScalePoint = 0;

            CSVManager.updateEmployeeDetails(this);
        } else {
            System.out.println("No promotion to confirm.");
        }
    }

    public void viewPayslips() {
        int employeeId = this.getId();

        Scanner input = new Scanner(System.in);
        System.out.println("Input Payslip Month: (1-12)");
        String month = input.nextLine().trim();
        System.out.println("Input Payslip Year (e.g. 2024):");
        String year = input.nextLine().trim();

        String inputDate = month + "/" + year;

        String filename = "PaySlips.csv";
        boolean payslipFound = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 11) {
                    int id = Integer.parseInt(details[0].trim());
                    String date = details[2].trim();

                    if (id == employeeId && date.equals(inputDate)) {
                        String name = details[1].trim();
                        String jobTitle = details[3].trim();
                        String scalePoint = details[4].trim();
                        String grossPay = details[5].trim();
                        String incomeTax = details[6].trim();
                        String prsi = details[7].trim();
                        String usc = details[8].trim();
                        String unionFee = details[9].trim();
                        String netPay = details[10].trim();

                        System.out.println("------------------------------------------------------------------");
                        System.out.println("Company Name: University Of Limerick    Employee ID: " + id);
                        System.out.println("Emp. Name: " + name + "     Date Payslip Created: 25/" + inputDate);
                        System.out.println("Emp. Title: " + jobTitle + "    Emp. Scale Point: " + scalePoint);
                        System.out.println("------------------------------------------------------------------");
                        System.out.println("--------Payments---------------------------------Deductions-------");
                        System.out.println("Gross Pay: " + grossPay + "                     Tax: " + incomeTax);
                        System.out.println("                                                    PRSI: " + prsi);
                        System.out.println("                                                    USC: " + usc);
                        System.out.println("------------------------------------------------------------------");
                        System.out.println("Net Pay This Month: " + netPay);
                        System.out.println("------------------------------------------------------------------");

                        payslipFound = true;
                    }
                }
            }

            reader.close();

            if (!payslipFound) {
                System.out.println("No payslip found for the provided date.");
            }

        } catch (FileNotFoundException e) {
            System.err.println("The file " + filename + " was not found.");
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}