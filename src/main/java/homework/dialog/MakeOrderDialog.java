package homework.dialog;

import homework.OrderManager;
import homework.RegisterSystem;
import homework.dao.Dao;
import homework.dao.MealDao;
import homework.model.Meal;
import homework.model.Order;
import homework.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MakeOrderDialog extends DialogOption {
    private final User user;
    public MakeOrderDialog(BufferedReader reader, RegisterSystem rs, User user) {
        super(reader, rs);
        this.user = user;
    }

    @Override
    public void run() throws IOException {
        String choice = null;
        Map<Meal, Integer> map = new HashMap<>();
        do {
            List<Order> orders = OrderManager.getInstance()
                    .getOrders().stream()
                    .filter(o -> o.getCustomer().equals(user)).toList();

            for (Order order : orders) {
                synchronized (order) {
                    if (!order.isReady() && !order.isCancelled()) {
                        System.out.println("You have already ordered something!");
                        return;
                    }
                }
            }


            Dao<Meal> dao = new MealDao("data" + File.separator + "meal.json");
            List<Meal> meals = dao.getAll();

            System.out.println("Menu:");
            for (int i = 0; i < meals.size(); ++i) {
                System.out.printf("%d - %s%n", i + 1, meals.get(i).toString());
            }

            System.out.print("Please enter the number of the meal you want to order> ");
            String numberString = reader.readLine();
            System.out.println();

            System.out.print("Please enter the count of dishes you want to order> ");
            String countString = reader.readLine();
            System.out.println();

            try {
                int index = Integer.parseInt(numberString) - 1;
                int count = Integer.parseInt(countString);
                Meal meal = meals.get(index);

                if (meal.getCount() >= count) {
                    map.put(meal, count);
                    // будем считать, что мы начали готовить и сразу же истратили продукты
                    meal.setCount(meal.getCount() - count);
                    dao.saveChanges();
                }
                else {
                    System.out.println("Not enough dishes for your order.");
                }

            } catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to order and return to previous menu or something else to add a meal> ");
            choice = reader.readLine();
            System.out.println();

            if (Objects.equals(choice, "q")) {
                Order order = new Order(map, user);
                order.getThread().start();
                OrderManager.getInstance().getOrders().add(order);
                break;
            }
        } while (true);


    }

    @Override
    public String getName() {
        return "Make an order";
    }
}
