import java.io.*;

public class MenuSimulation {
    /**
     * The main method that starts the menu simulation.
     *
     * <p>This method initializes the {@link LoginSystem} and {@link PaySlipMenu} instances
     * and invokes the {@code run} method of the menu to execute the application's core functionality.</p>
     *
     * @param args command-line arguments (not used in this application)
     * @throws IOException if an I/O error occurs during the application's execution
     */
    public static void main(String[] args)
               throws IOException {
            LoginSystem login = new LoginSystem();
           PaySlipMenu menu = new PaySlipMenu();
          menu.run(login);
    }
}
