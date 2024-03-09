package homework.dialog;

import homework.RegisterSystem;
import homework.model.User;

import java.io.BufferedReader;

public class CustumerMenuDialog extends DialogOption {
    public CustumerMenuDialog(BufferedReader reader, RegisterSystem rs, User user) {
        super(reader, rs);
        dialogOptionList.add(new MakeOrderDialog(reader, rs, user));
        dialogOptionList.add(new AddMealToOrderDialog(reader, rs, user));
        dialogOptionList.add(new CancelOrderDialog(reader, rs, user));
        dialogOptionList.add(new ShowOrderStatusDialog(reader, rs, user));
        dialogOptionList.add(new PayDialog(reader, rs, user));
        dialogOptionList.add(new ReturnToStartMenuDialog(reader, rs));
    }

    @Override
    public String getName() {
        return "Administrator menu";
    }
}
