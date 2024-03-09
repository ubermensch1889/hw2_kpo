package homework.dialog;

import homework.MealThread;
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

public class AddMealToOrderDialog extends DialogOption {
    private final User user;
    public AddMealToOrderDialog(BufferedReader reader, RegisterSystem rs, User user) {
        super(reader, rs);
        this.user = user;
    }
    @Override
    public void run() throws IOException {
        String choice;
        do {
            Dao<Meal> dao = new MealDao("data" + File.separator + "meal.json");
            List<Meal> meals = dao.getAll();

            if (meals.isEmpty()) {
                System.out.println("Menu is empty.");
                return;
            }

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
                    List<Order> orders = OrderManager.getInstance()
                            .getOrders().stream()
                            .filter(o -> o.getCustomer().equals(user)).toList();

                    Order order = null;
                    for (Order curr : orders) {
                        synchronized (curr) {
                            if (!curr.isReady() && !curr.isCancelled()) {
                                order = curr;
                                break;
                            }
                        }
                    }

                    if (orders.isEmpty()) {
                        System.out.println("You haven't ordered anything so you can't add meal to an order.");
                        return;
                    }

                    if (order == null) {
                        System.out.println("You can't add a meal.");
                    }

                    for (int i = 0; i < count; ++i) {
                        MealThread mt = new MealThread(meal.getTimeToCook());
                        mt.start();
                        order.getThread().GetThreadList().add(mt);
                    }
                    order.getMealIntegerMap().put(meal, count);

                    meals.get(index).setCount(meal.getCount() - count);
                    dao.saveChanges();
                }
                else {
                    System.out.println("Not enough dishes for your order.");
                }

            } catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to return to previous menu or something else to add a meal> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));
    }

    @Override
    public String getName() {
        return "Add a meal to the order";
    }
}
