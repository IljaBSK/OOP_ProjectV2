import java.util.Scanner;

public class HR extends User {
    /**
     * Constructs an HR user with the specified username, password, and job type.
     *
     * @param username the username for the HR user
     * @param password the password for the HR user
     * @param jobType  the job type associated with the HR user
     */
    public HR(String username, String password, String jobType) {
        super(username, password, jobType);
    }
    /**
     * Promotes an employee by updating their job title and scale point.
     * <p>This method guides the user through the process of promoting an employee. It:
     *     <li>Prompts the user to enter the employee's ID.</li>
     *     <li>Validates the employee ID and fetches the employee's current details.</li>
     *     <li>Asks for a new job title and scale point, validating both against the system's rules.</li>
     *     <li>Updates the employee's job title and scale point, sets the promotion flag,
     *         and saves the changes to the employee record.</li>
     * </p>
     * @throws NumberFormatException if the scale point input is invalid
     */
    public void promoteEmployee() {
        Scanner input = new Scanner(System.in);
        Employee employee = null;

        while (employee == null) {
            System.out.println("Enter the Employee ID to promote:");
            String empID = input.nextLine().trim();

            employee = CSVManager.getEmployeeByID(empID);

            if (employee == null) {
                System.out.println("Employee not found. Please enter a valid Employee ID.");
            }
        }

        System.out.println("Current Details:");
        System.out.println("Name: " + employee.getName());
        System.out.println("Job Title: " + employee.getJobTitle());
        System.out.println("Scale Point: " + employee.getScalePoint());
        System.out.println("Years at Top Scale Point: " + employee.getYearsAtTop());

        employee.setPreviousJobTitle(employee.getJobTitle());
        employee.setPreviousScalePoint(employee.getScalePoint());

        String newJobTitle = null;
        int newScalePoint = -1;

        while (newJobTitle == null || !CSVManager.isValidJobTitle(newJobTitle)) {
            System.out.println("Enter new job title (must be valid):");
            newJobTitle = input.nextLine().trim();
            if (!CSVManager.isValidJobTitle(newJobTitle)) {
                System.out.println("Invalid job title. Please try again.");
            }
        }

        while (newScalePoint == -1 || !CSVManager.isValidScalePoint(newJobTitle, newScalePoint)) {
            System.out.println("Enter new scale point (must be within the valid range for the job title):");
            try {
                newScalePoint = Integer.parseInt(input.nextLine().trim());
                if (!CSVManager.isValidScalePoint(newJobTitle, newScalePoint)) {
                    System.out.println("Invalid scale point. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric scale point.");
            }
        }

        employee.setJobTitle(newJobTitle);
        employee.setScalePoint(newScalePoint);
        employee.setPendingPromotionFlag(1);

        CSVManager.updateEmployeeDetails(employee);

        System.out.println("Employee's role updated successfully. Awaiting confirmation.");
    }
}