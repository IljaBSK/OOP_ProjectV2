public class User{
    private String username;
    private String password;
    private String jobType;

    public User(String username, String password, String jobType) {
        this.username = username;
        this.password = password;
        this.jobType = jobType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getJobType() {
        return jobType;
    }
    //
}
