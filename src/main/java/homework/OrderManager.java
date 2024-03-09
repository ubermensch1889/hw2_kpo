package homework;

import homework.model.Order;
import homework.model.User;

import java.util.ArrayList;
import java.util.List;

// синглетон для получения списка заказов из любой точки программы
public class OrderManager {
    private List<Order> orders;
    private static OrderManager instance;
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }

        return instance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private OrderManager() { orders = new ArrayList<>(); }
}
