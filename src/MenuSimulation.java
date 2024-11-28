import java.io.*;

public class MenuSimulation {

    /* Bri's code

    public static void main(String[] args)
               throws IOException {
            LoginSystem login = new LoginSystem();
           PaySlipMenu menu = new PaySlipMenu();
          menu.run(login);
    */
    public static void main(String[] args) {
        // Define File Paths
        String validLoginsFile = "ValidLogins.csv"; // Path to the file storing login data (username, password, job type)
        String employeeFile = "EmployeeInfo.csv";   // Path to the file storing employee information
        String salaryScalesFile = "FullTimeSalaryScales.csv";   // Path to the file containing job titles and their corresponding scale points

        // Create an Admin Instance
        // The Admin class handles data collection and validation for login and employee information
        Admin admin = new Admin();

        // Collect data from the user, passing the salary scales file for validation
        admin.collectData(salaryScalesFile);

        // Write Data to Files
        // Create an instance of CSVWriter to handle writing data to CSV files
        CSVWriter writer = new CSVWriter();

        try {
            writer.writeToCSV(admin.getLoginData(), validLoginsFile);   // Write login data (username, password, job type) to the ValidLogins file

            writer.writeToCSV(admin.getEmployeeData(), employeeFile);   // Write employee-specific data (ID, username, etc.) to the EmployeeInfo file

            System.out.println("Data successfully written to files.");
        } catch (IOException e) {
            System.err.println("Error writing data: " + e.getMessage());
        }
    }
}
