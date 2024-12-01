public class User{
    private String username;
    private String password;
    private String jobType;


    /**
     * Constructs a new {@code User} with the specified username, password, and job type.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param jobType  the job type of the user (e.g., "Admin", "Employee")
     */
    public User(String username, String password, String jobType) {
        this.username = username;
        this.password = password;
        this.jobType = jobType;
    }
    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */

    public String getPassword() {
        return password;
    }
    /**
     * Retrieves the job type of the user.
     *
     * @return the job type of the user
     */

    public String getJobType() {
        return jobType;
    }
    //
}
