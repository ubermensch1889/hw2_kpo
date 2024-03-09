package homework.dialog;

import homework.RegisterSystem;

import java.io.BufferedReader;

public class StartDialog extends DialogOption{
    public StartDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
        dialogOptionList.add(new SignInDialog(reader, rs));
        dialogOptionList.add(new SignUpDialog(reader, rs));
    }
}
