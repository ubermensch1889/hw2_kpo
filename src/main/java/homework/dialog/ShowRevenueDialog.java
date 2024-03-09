package homework.dialog;

import homework.RegisterSystem;
import homework.RevenueManager;

import java.io.BufferedReader;

public class ShowRevenueDialog extends DialogOption {
    public ShowRevenueDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
    }
    @Override
    public String getName() {
        return "Show revenue";
    }

    @Override
    public void run() {
        System.out.printf("The revenue = %d%n", RevenueManager.getInstance().getRevenue());
    }
}
