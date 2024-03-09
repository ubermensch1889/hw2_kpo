package homework.dialog;

import homework.OrderManager;
import homework.RegisterSystem;
import homework.model.Order;
import homework.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ShowOrderStatusDialog extends DialogOption {
    private final User user;
    public ShowOrderStatusDialog(BufferedReader reader, RegisterSystem rs, User user) {
        super(reader, rs);
        this.user = user;
    }
    @Override
    public void run() throws IOException {
        List<Order> orders = OrderManager.getInstance()
                .getOrders().stream()
                .filter(o -> o.getCustomer().equals(user)).toList();

        for (int i = 0; i < orders.size(); i++) {
            synchronized (orders.get(i)) {
                System.out.printf("Order №%d%n", i + 1);

                System.out.println(orders.get(i));
            }
        }

        if (orders.isEmpty()) System.out.println("You have no orders.");
    }

    @Override
    public String getName() {
        return "Show status of orders";
    }
}
