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
                if (command.equalsIgnoreCase("A") && validUser.getJobType().equalsIgnoreCase("Admin")) {
                    System.out.println("Login successful as Admin.");
                    this.adminLoggedIn();
                } else if (command.equalsIgnoreCase("E") && validUser.getJobType().equalsIgnoreCase("Employee")) {
                    System.out.println("Login successful as Employee.");
                } else if (command.equalsIgnoreCase("H") && validUser.getJobType().equalsIgnoreCase("HR")) {
                    System.out.println("Login successful as HR.");
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


    public void adminLoggedIn(){

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL Admin Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("C)reate a new employee");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if(command.equalsIgnoreCase("C")){

        }else if(command.equalsIgnoreCase("L")){

        }
    }

    public void hrLoggedIn(){

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL HR Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("P)romote an employee");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();
    }

    public void employeeLoggedIn(){

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL Employee Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("V)iew my Payslips");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();
    }
}