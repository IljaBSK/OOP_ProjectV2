import java.util.*;

public class LoginSystem {
    private HashMap<String, User> users;

    public LoginSystem(){
        users = CSVManager.readValidUsers();
    }

    public void loginFunction() {
        Scanner input = new Scanner(System.in);
        boolean loggedIn = false;

        System.out.println("Select your role: A)dmin E)mployee H)R");
        String command = input.nextLine().trim(); // Trim to remove extra spaces

        System.out.println("Enter your username");
        String usernameIn = input.nextLine().trim();
        System.out.println("Enter your password");
        String passwordIn = input.nextLine().trim();

        if (users.containsKey(usernameIn)) {
            User validUser = users.get(usernameIn); // retrieves the user from the hashmap
            if (validUser.getPassword().equals(passwordIn)) {
                if (command.equalsIgnoreCase("A") && validUser instanceof Admin) {
                    System.out.println("Login successful as Admin.");
                    this.adminLoggedIn((Admin) validUser);
                } else if (command.equalsIgnoreCase("E") && validUser instanceof Employee) {
                    System.out.println("Login successful as Employee.");
                    this.employeeLoggedIn((Employee) validUser);
                } else if (command.equalsIgnoreCase("H") && validUser instanceof HR) {
                    System.out.println("Login successful as HR.");
                    this.hrLoggedIn((HR) validUser);
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