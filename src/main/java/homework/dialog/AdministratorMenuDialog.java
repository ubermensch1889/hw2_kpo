package homework.dialog;

import homework.RegisterSystem;

import java.io.BufferedReader;

public class AdministratorMenuDialog extends DialogOption {
    public AdministratorMenuDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
        dialogOptionList.add(new AddMealDialog(reader, rs));
        dialogOptionList.add(new ChangeMealDialog(reader, rs));
        dialogOptionList.add(new DeleteMealDialog(reader, rs));
        dialogOptionList.add(new ShowRevenueDialog(reader, rs));
        dialogOptionList.add(new ReturnToStartMenuDialog(reader, rs));
    }

    @Override
    public String getName() {
        return "Administrator menu";
    }
}
