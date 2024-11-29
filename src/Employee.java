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

    public Employee(String username, String password, String name, String jobType, String doB,String pps, int id, String jobTitle, int scalePoint) {
        super(username, password, jobType);
        this.name = name;
        this.doB = doB;
        this.ppsNo = pps;
        this.id = id;
        this.jobTitle = jobTitle;
        this.scalePoint = scalePoint;
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