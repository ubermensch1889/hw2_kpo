package homework;

import homework.dialog.StartDialog;

import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ConsoleApp {

    public void start() {
        RegisterSystem rs = new RegisterSystem();
        StartDialog startDialog = new StartDialog(new BufferedReader(new InputStreamReader(System.in)), rs);

        // Запускаем наше приложение
        try {
            rs.Init("data" + File.separator + "key.txt", "data" + File.separator + "user.json");

            startDialog.run();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 InvalidAlgorithmParameterException e) {
            System.out.println("Oops... Something with cipher went wrong.");
        } catch (IOException e) {
            System.out.println("Oops... Probably you forgot to write a key.");
        }
    }
}