package homework.dialog;

import homework.OrderManager;
import homework.RegisterSystem;
import homework.RevenueManager;
import homework.model.Order;
import homework.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class PayDialog extends DialogOption {
    private final User user;

    public PayDialog(BufferedReader reader, RegisterSystem rs, User user) {
        super(reader, rs);
        this.user = user;
    }

    @Override
    public String getName() {
        return "Pay for the order";
    }

    @Override
    public void run() throws IOException {
        List<Order> orders = OrderManager.getInstance()
                .getOrders().stream()
                .filter(o -> o.getCustomer().equals(user)).toList();

        for (Order curr : orders) {
            synchronized (curr) {
                if (curr.isReady() && !curr.isCancelled() && !curr.isPayed()) {
                    curr.pay();
                    RevenueManager.getInstance().increaseRevenue(curr.calculatePrice());
                    System.out.println("The order is payed successfully.");
                    System.out.println();
                    return;
                }
            }
        }

        System.out.println("You have no orders to pay for.");
        System.out.println();
    }
}
