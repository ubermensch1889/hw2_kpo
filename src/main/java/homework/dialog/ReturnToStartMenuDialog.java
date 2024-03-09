package homework.dialog;

import homework.RegisterSystem;

import java.io.BufferedReader;
import java.io.IOException;

public class ReturnToStartMenuDialog extends DialogOption {
    public ReturnToStartMenuDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
    }

    @Override
    public String getName() {
        return "Return to start menu";
    }
    @Override
    public void run() throws IOException {
        new StartDialog(reader, rs).run();
    }
}
