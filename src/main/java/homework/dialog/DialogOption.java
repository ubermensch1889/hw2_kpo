package homework.dialog;

import homework.RegisterSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// наследники этого класса отвечают за определенную опцию в диалоге с системой
// я хотел с помощью этого сделать систему лучше расширяемой и понятной, нежели
// статическое консолькое приложение, но насколько хорошо у меня получилось - судите сами.
public abstract class DialogOption {
    protected final BufferedReader reader;
    protected final List<DialogOption> dialogOptionList = new ArrayList<>();
    protected final RegisterSystem rs;
    public DialogOption(BufferedReader reader, RegisterSystem rs) {
        this.reader = reader;
        this.rs = rs;
    }
    public void run() throws IOException {
        while (true) {
            System.out.println("What do you need to do?");
            System.out.println("Available options:");

            for (int i = 0; i < dialogOptionList.size(); ++i) {
                System.out.printf("%d - %s%n", i + 1, dialogOptionList.get(i).getName());
            }

            System.out.println();
            System.out.print("Your choice> ");
            String choice = reader.readLine();
            System.out.println();

            try {
                dialogOptionList.get(Integer.parseInt(choice) - 1).run();
            }
            catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Incorrect input.");
            }
        }
    }
    public String getName() {
        return null;
    }
}
