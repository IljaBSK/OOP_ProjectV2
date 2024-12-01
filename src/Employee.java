import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Employee extends User {

    private String name;
    private String doB;
    private String ppsNo;
    private int id;
    private String jobTitle;
    private int scalePoint;

    //Fields to track the promotion status
    private int pendingPromotionFlag;
    private String previousJobTitle;
    private int previousScalePoint;

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

    public Employee(String userName, String userPassword, String userJob) {
        super(userName, userPassword, userJob);
    }

    public String getName() {
        return name;
    }

    public int getId(){return id;}

    public String getJobTitle() {
        return jobTitle;
    }

    public int getScalePoint() {
        return scalePoint;
    }

    public void setJobTitle(String jobTitle){
        this.jobTitle=jobTitle;
    }

    public void setScalePoint(int scalePoint){
        this.scalePoint=scalePoint;
    }

    public boolean hasPendingPromotionFlag() {
        return this.pendingPromotionFlag == 1;
    }

    public void setPendingPromotionFlag(int flag) {
        this.pendingPromotionFlag = flag;
    }
    public void setPreviousJobTitle(String previousJobTitle) {
        this.previousJobTitle = previousJobTitle;
    }

    public void setPreviousScalePoint(int previousScalePoint) {
        this.previousScalePoint = previousScalePoint;
    }

    public String getPreviousJobTitle() {
        return this.previousJobTitle;
    }

    public int getPreviousScalePoint() {
        return this.previousScalePoint;
    }

    public void rejectPromotion() {
        if (hasPendingPromotionFlag()) {
            System.out.println("Promotion rejected. Reverting to previous position...");

            // Delegate to CSVManager to handle the reversion
            String[] revertedDetails = CSVManager.revertPromotion(this);

            // Log the reverted details for confirmation
            System.out.println("Your position has been reverted to: " + revertedDetails[0] +
                    " (Scale Point: " + revertedDetails[1] + ")");
        } else {
            System.out.println("No promotion to reject.");
        }
    }


    public void confirmPromotion() {
        if (hasPendingPromotionFlag()) {
            System.out.println("Your role has been updated to: " + getJobTitle());
            System.out.println("Press Enter to acknowledge your promotion.");
            Scanner input = new Scanner(System.in);
            input.nextLine(); // Wait for acknowledgment

            // Reset the promotion flag in the CSV
            CSVManager.updatePromotionFlag(this.getId(), 0);

            System.out.println("Promotion acknowledged. Thank you!");
        } else {
            System.out.println("No promotion to confirm.");
        }
    }

    public void viewPayslips(){
        int employeeId = this.getId();

        Scanner input = new Scanner(System.in);
        System.out.println("Input Payslip Month: (1-12)");
        String month = input.nextLine().trim();
        System.out.println("Input Payslip Year:");
        String year = input.nextLine().trim();

        String inputDate = month + "/" + year;

        String filename = "PaySlips.csv";
        HashMap<String, String> payslipInfo = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            boolean payslipFound = false;
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 11) {
                    int id = Integer.parseInt(details[0].trim());
                    String date = details[2].trim();

                    if(id==employeeId && date.equals(inputDate)){
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
                        System.out.println("Gross Pay: " + grossPay + "                     Tax: "+ incomeTax);
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

            if(!payslipFound){
                System.out.println("No payslip was found for this date");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}