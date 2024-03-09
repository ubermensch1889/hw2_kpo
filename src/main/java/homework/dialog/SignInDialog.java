package homework.dialog;

import homework.RegisterSystem;
import homework.model.User;
import homework.model.UserRole;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class SignInDialog extends DialogOption{
    public SignInDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
    }
    @Override
    public void run() throws IOException {
        // Вывод интерфейса входа в аккаунт
        String choice;
        do {
            System.out.print("Please enter your name> ");
            String name = reader.readLine();
            System.out.println();
            System.out.print("Please enter your password> ");
            String password = reader.readLine();
            System.out.println();

            try {
                User user = rs.findUser(name, password);
                if (user != null) {
                    System.out.println("Access granted.");

                    if (user.getRole().equals(UserRole.Administrator)) {
                        DialogOption dialog = new AdministratorMenuDialog(reader, rs);
                        dialog.run();
                    }
                    else {
                        DialogOption dialog = new CustumerMenuDialog(reader, rs, user);
                        dialog.run();
                    }

                    break;
                } else {
                    System.out.println("Access denied.");
                }
            } catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to return to previous menu or something else to try again> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));
    }

    @Override
    public String getName() {
        return "Sign in";
    }
}
