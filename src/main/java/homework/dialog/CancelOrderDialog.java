package homework.dialog;

import homework.OrderManager;
import homework.RegisterSystem;
import homework.model.Order;
import homework.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class CancelOrderDialog extends DialogOption {
    private final User user;
    public CancelOrderDialog(BufferedReader reader, RegisterSystem rs, User user) {
        super(reader, rs);
        this.user = user;
    }

    @Override
    public void run() throws IOException {
        List<Order> orders = OrderManager.getInstance()
                .getOrders().stream()
                .filter(o -> o.getCustomer() == user).toList();

        for (Order curr : orders) {
            synchronized (curr) {
                if (!curr.isReady() && !curr.isCancelled()) {
                    curr.getThread().interrupt();
                    System.out.println("The order was cancelled successfully.");
                    break;
                }
            }
        }

        System.out.println("You have no orders to cancel.");
    }

    @Override
    public String getName() {
        return "Cancel the order";
    }
}
