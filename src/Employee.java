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
}