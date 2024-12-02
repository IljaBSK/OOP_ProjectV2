import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class LoginSystem {
    private HashMap<String, User> users;
    private HashMap<String, String> employeeStatuses;

    public LoginSystem() {
        users = CSVManager.readValidUsers();
        employeeStatuses = CSVManager.readEmployeeStatus();

    }

    /**
     * Handles the login functionality and system navigation.
     * <p>This method provides a login interface for different user roles (Admin, Employee, HR).
     * It also allows users to simulate moving the system date forward by days, weeks, or months
     * before logging in. Special functionality is triggered if the date moves to October, such as
     * updating salary scales.</p>
     * <h3>Features:</h3>
     * <ul>
     *     <li>Simulates date advancement (Day, Week, Month).</li>
     *     <li>Updates salary scales in October.</li>
     *     <li>Handles user login for Admins, Employees (Full-Time and Part-Time), and HR roles.</li>
     *     <li>Performs role and password validation.</li>
     * </ul>
     *
     * @throws IOException if an error occurs during salary scale updates or other file operations
     */
    public void loginFunction() {
        Scanner input = new Scanner(System.in);
        boolean loggedIn = false;

        // Initialize the current date
        LocalDate today = LocalDate.now();

        CSVManager csvManager = new CSVManager();

        while (!loggedIn) {


            if (today.getDayOfMonth() == 25) {
                System.out.println("Sending payslips out...");
                try {
                    PaySlipGenerator generator = new PaySlipGenerator();
                    generator.payslipGenerator();
                } catch (Exception e) {
                    System.err.println("Error while sending payslips: " + e.getMessage());
                }
            }


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
                if (today.getMonth() == Month.OCTOBER ) {
                    System.out.println("It's October! Updating everyone's salary scale...");
                    csvManager.updateSalaryScales();
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
                            String status = employeeStatuses.getOrDefault(usernameIn, "Unknown");
                            if (status.equalsIgnoreCase("Full-Time")) {
                                System.out.println("Login successful as Full-Time Employee.");
                                Employee employee = CSVManager.getEmployeeByUsername(validUser.getUsername());
                                this.FulltimeemployeeLoggedIn((Employee) employee);
                                loggedIn = true;
                            } else if (status.equalsIgnoreCase("Part-Time")) {
                                System.out.println("Login successful as Part-Time Employee.");
                                Employee employee = CSVManager.getEmployeeByUsername(validUser.getUsername());
                                this.ParttimeemployeeLoggedIn((Employee) employee, today);

                                loggedIn = true;
                            } else {
                                System.out.println("Unknown Employee Status. Please contact Admin.");
                            }
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

    /**
     * Handles the admin login process and presents the admin menu options.
     *
     * <p>This method displays the admin menu and allows the admin to perform specific actions:</p>
     * <ul>
     *     <li>Create a new employee by invoking the {@code createEmployee} method with the file {@code FullTimeSalaryScales.csv}.</li>
     *     <li>Log out by selecting the corresponding menu option.</li>
     * </ul>
     *
     * @param adminUser the {@link Admin} object representing the logged-in admin
     */
    public void adminLoggedIn(Admin adminUser) {

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL Admin Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("C)reate a new employee");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if (command.equalsIgnoreCase("C")) {
            adminUser.createEmployee("FullTimeSalaryScales.csv");
        } else if (command.equalsIgnoreCase("L")) {

        }
    }

    /**
     * Displays the HR menu for the logged-in HR user and handles user actions.
     *
     * <p>This method provides a text-based menu interface for an HR user,
     * allowing them to select options to promote an employee or log out.
     * Invalid inputs are handled gracefully by prompting the user to try again.
     * The menu continues to display until the user chooses to log out.</p>
     *
     * @param hrUser the logged-in HR user who will interact with the menu
     */
    public void hrLoggedIn(HR hrUser) {

        System.out.println("-------------------------------------------");
        System.out.println("        Welcome to the UL HR Menu       ");
        System.out.println("-------------------------------------------");
        System.out.println("Please select what you would like to do:");
        System.out.println("P)romote an employee");
        System.out.println("L)og Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if (command.equalsIgnoreCase("P")) {
            hrUser.promoteEmployee();
        } else if (command.equalsIgnoreCase("L")) {

        }
    }

    /**
     * Handles the menu and actions for a logged-in full-time employee.
     *
     * <p>This method displays the menu for a full-time employee, including options to:
     * <ul>
     *     <li>Check for and respond to pending promotions</li>
     *     <li>View payslips</li>
     *     <li>Log out</li>
     * </ul>
     * </p>
     *
     * <p>If the employee has a pending promotion, they can accept or reject it.
     * Additional functionality such as viewing payslips can be implemented as needed.</p>
     *
     * @param employee the full-time employee who has logged in
     */
    public void FulltimeemployeeLoggedIn(Employee employee) {
        System.out.println("----------------------------------------------");
        System.out.println("  Welcome to the Full-time Employee Menu");
        System.out.println("----------------------------------------------");

        // Fetch updated employee details from EmployeeInfo.csv
        Employee updatedEmployee = CSVManager.getEmployeeByUsername(employee.getUsername());

        if (updatedEmployee != null && updatedEmployee.hasPendingPromotionFlag()) {
            System.out.println("You have a pending promotion to: " + updatedEmployee.getJobTitle() + " (Scale Point: " + updatedEmployee.getScalePoint() + ")");
            System.out.println("Do you want to accept or reject the promotion?");
            System.out.println("A) Accept    R) Reject");

            Scanner input = new Scanner(System.in);
            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("A")) {
                updatedEmployee.confirmPromotion(); // Accept promotion
            } else if (command.equalsIgnoreCase("R")) {
                updatedEmployee.rejectPromotion(); // Reject promotion
            } else {
                System.out.println("Invalid input. No action taken.");
            }
        }

        System.out.println("Please select an option:");
        System.out.println("V) View Payslips");
        System.out.println("L) Log Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        if (command.equalsIgnoreCase("V")) {
            employee.viewPayslips();
        }

    }

    /**
     * Handles the menu and actions for a logged-in part-time employee.
     *
     * <p>This method displays the menu for a part-time employee, including options to:
     * <ul>
     *     <li>Check for and respond to pending promotions</li>
     *     <li>Submit a pay claim</li>
     *     <li>View payslips</li>
     *     <li>Log out</li>
     * </ul>
     * </p>
     *
     * <p>If the employee has a pending promotion, they can accept or reject it.
     * The method also facilitates the submission of a pay claim by integrating with
     * external readers and writers for employee information and salary scales.</p>
     *
     * @param employee the part-time employee who has logged in
     */


    public void ParttimeemployeeLoggedIn(Employee employee, LocalDate today) {
        System.out.println("----------------------------------------------");
        System.out.println("   Welcome to the Part-time Employee Menu     ");
        System.out.println("----------------------------------------------");

        // Fetch updated employee details from EmployeeInfo.csv
        Employee updatedEmployee = CSVManager.getEmployeeByUsername(employee.getUsername());

        // Handle pending promotions
        if (updatedEmployee != null && updatedEmployee.hasPendingPromotionFlag()) {
            System.out.println("You have a pending promotion to: " + updatedEmployee.getJobTitle() +
                    " (Scale Point: " + updatedEmployee.getScalePoint() + ")");
            System.out.println("Do you want to accept or reject the promotion?");
            System.out.println("A) Accept    R) Reject");

            Scanner input = new Scanner(System.in);
            String promotionCommand = input.nextLine().trim();

            if (promotionCommand.equalsIgnoreCase("A")) {
                updatedEmployee.confirmPromotion(); // Accept promotion
            } else if (promotionCommand.equalsIgnoreCase("R")) {
                updatedEmployee.rejectPromotion(); // Reject promotion
            } else {
                System.out.println("Invalid input. No action taken.");
            }
        }

        System.out.println("Please select an option:");
        System.out.println("S) Submit Pay Claim");
        System.out.println("V) View Payslips");
        System.out.println("L) Log Out");

        Scanner input = new Scanner(System.in);
        String command = input.nextLine().trim();

        // Calculate the second Friday of the current month
        LocalDate secondFriday = getSecondFriday(today);

        if (command.equalsIgnoreCase("S")) {
            if (today.isAfter(secondFriday)) {
                System.out.println("Pay claims can only be submitted on or before the second Friday of the month.");
                return; // Exit the method if the date is invalid
            } else {
                // Initialize dependencies
                String employeeInfoFile = "EmployeeInfo.csv";
                String salaryScalesFile = "FulltimeSalaryScales.csv";

                try {
                    EmployeeInfoReader employeeReader = new EmployeeInfoReader(employeeInfoFile);
                    FulltimeSalaryScalesReader salaryReader = new FulltimeSalaryScalesReader(salaryScalesFile);
                    PaySlipWriter writer = new PaySlipWriter("PayClaim.csv");

                    // Initialize PaySlipCalculator
                    PaySlipCalculator calculator = new PaySlipCalculator(salaryReader, writer);

                    // Call the submit pay-claim method
                    calculator.submitPayClaim(employee.getUsername(), employeeReader);
                    return; // Exit after successfully handling the pay-claim
                } catch (IOException e) {
                    System.err.println("Error during pay claim submission: " + e.getMessage());
                }
            }
        } else if (command.equalsIgnoreCase("V")) {
            employee.viewPayslips();
            System.out.println("Feature not implemented yet.");
        } else if (command.equalsIgnoreCase("L")) {
            System.out.println("Logging out...");
        } else {
            System.out.println("Invalid option. Please try again.");
        }
        input.close();
    }

    /**
     * Calculates the second Friday of the current month.
     *
     * @param date the current date
     * @return the LocalDate representing the second Friday
     */
    private LocalDate getSecondFriday(LocalDate date) {
        // Get the first day of the current month
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);

        // Find the first Friday of the month
        LocalDate firstFriday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        // Add 7 days to the first Friday to get the second Friday
        return firstFriday.plusDays(7);
    }



}
