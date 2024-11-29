import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.*;

public class LoginSystem {
    private HashMap<String, User> users;

    public void loginFunction() {
        Scanner input = new Scanner(System.in);
        boolean loggedIn = false;

        // Initialize the current date
        LocalDate today = LocalDate.now();

        SalaryScales salaryScales = new SalaryScales();

        while (!loggedIn) {
            // Display the current date and day of the week
            System.out.println("-------------------------------------------");
            System.out.println("       Welcome to the UL System");
            System.out.println("       Current Date: " + today + " (" + today.getDayOfWeek() + ")");
            System.out.println("-------------------------------------------");
            System.out.println("Enter D)ay, W)eek, or M)onth to move forward or press Enter to proceed with login:");

            // Get user input
            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("D") || command.equalsIgnoreCase("D)ay")) {
                // Move forward by 1 day
                today = today.plusDays(1);
            } else if (command.equalsIgnoreCase("W") || command.equalsIgnoreCase("W)eek")) {
                // Move forward by 1 week
                today = today.plusWeeks(1);
            } else if (command.equalsIgnoreCase("M") || command.equalsIgnoreCase("M)onth")) {
                // Move forward by 1 month
                today = today.plusMonths(1);

                // Check if it's October
                if (today.getMonth() == Month.OCTOBER) {
                    System.out.println("It's October! Updating everyone's salary scale...");
                    salaryScales.updateSalaryScales();
                }
            } else {
                // Proceed with login
                System.out.println("Select your role: A)dmin E)mployee H)R");
                String roleCommand = input.nextLine().trim();
                System.out.println("Enter your username:");
                String usernameIn = input.nextLine().trim();
                System.out.println("Enter your password:");
                String passwordIn = input.nextLine().trim();

                if (users.containsKey(usernameIn)) {
                    User validUser = users.get(usernameIn);
                    if (validUser.getPassword().equals(passwordIn)) {
                        if (roleCommand.equalsIgnoreCase("A") && validUser instanceof Admin) {
                            System.out.println("Login successful as Admin.");
                            this.adminLoggedIn((Admin) validUser);
                            loggedIn = true;
                        } else if (roleCommand.equalsIgnoreCase("E") && validUser instanceof Employee) {
                            System.out.println("Login successful as Employee.");
                            this.employeeLoggedIn((Employee) validUser);
                            loggedIn = true;
                        } else if (roleCommand.equalsIgnoreCase("H") && validUser instanceof HR) {
                            System.out.println("Login successful as HR.");
                            this.hrLoggedIn((HR) validUser);
                            loggedIn = true;
                        } else {
                            System.out.println("Incorrect User Type. Please Try Again!");
                        }
                    } else {
                        System.out.println("Incorrect password. Please Try Again!");
                    }
                } else {
                    System.out.println("Incorrect username. Please Try Again!");
                }
            }
        }
        input.close();
    }

    public void adminLoggedIn(Admin adminUser){

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL Admin Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("C)reate a new employee");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if(command.equalsIgnoreCase("C")){
            adminUser.createEmployee("FullTimeSalaryScales.csv" );
        }else if(command.equalsIgnoreCase("L")){

        }
    }

    public void hrLoggedIn(HR hrUser){

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL HR Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("P)romote an employee");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if(command.equalsIgnoreCase("P")){
            hrUser.promoteEmployee();
        }else if(command.equalsIgnoreCase("L")){

        }
    }

    public void employeeLoggedIn(Employee employee) {
        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the Employee Menu       ");
        System.out.println("-------------------------------------------");

        // Fetch updated employee details from EmployeeInfo.csv
        Employee updatedEmployee = CSVManager.getEmployeeByUsername(employee.getUsername());

        if (updatedEmployee != null && updatedEmployee.hasPendingPromotionFlag()) {
            updatedEmployee.confirmPromotion(); // Handle promotion acknowledgment
            // Update the flag in the CSV after acknowledgment
            CSVManager.updatePromotionFlag(updatedEmployee.getId(), 0);
        }

        System.out.println("Please select an option:");
        System.out.println("V) View Payslips");
        System.out.println("L) Log Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if (command.equalsIgnoreCase("V")) {
            // Payslip logic
        }
    }
}