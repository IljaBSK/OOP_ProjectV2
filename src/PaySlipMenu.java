import java.io.IOException;
public class PaySlipMenu {
    /**
     * Executes the login functionality by invoking the {@code loginFunction} method on the provided {@link LoginSystem} instance.
     *
     * @param login the {@link LoginSystem} instance used to perform the login operation
     * @throws IOException if an I/O error occurs during the login process
     */
    public void run(LoginSystem login) throws IOException {
        login.loginFunction();
    }
}