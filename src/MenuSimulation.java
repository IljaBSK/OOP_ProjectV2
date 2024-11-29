import java.io.*;

public class MenuSimulation {
    public static void main(String[] args)
               throws IOException {
            LoginSystem login = new LoginSystem();
           PaySlipMenu menu = new PaySlipMenu();
          menu.run(login);
    }
}
