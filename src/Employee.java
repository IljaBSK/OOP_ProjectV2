public class Employee extends User {

    private String name;
    private String doB;
    private int ppsNo;
    private int id;
    private String jobTitle;
    private int scalePoint;

    //Fields to track the promotion status
    private String pendingPromotion;
    private boolean hasPendingPromotion;

    public Employee(String username, String password, String jobType, String name, String doB, int pps, int id, String jobTitle, int scalePoint) {
        super(username, password, jobType);
        this.name = name;
        this.doB = doB;
        this.ppsNo = pps;
        this.id = id;
        this.jobTitle = jobTitle;
        this.scalePoint = scalePoint;
        this.pendingPromotion = null;
        this.hasPendingPromotion = false;
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
    //Method to set a pending promotion
    public void setPendingPromotion(String newJobTitle) {
        this.pendingPromotion = newJobTitle;
        this.hasPendingPromotion = true;
    }

    //Method to get the pending promotion title
    public String getPendingPromotion() {
        return pendingPromotion;
    }

    //Check if there is a pending promotion
    public boolean hasPendingPromotion() {
        return hasPendingPromotion;
    }

    //Method for the employee to confirm they have seen the promotion
    public void confirmPromotionSeen() {
        if (hasPendingPromotion) {
            System.out.println(name + " has seen the promotion to " + pendingPromotion + ".");
            // Clear the pending promotion after confirmation
            this.pendingPromotion = null;
            this.hasPendingPromotion = false;
        } else {
            System.out.println("No pending promotion for " + name + ".");
        }
    }
}