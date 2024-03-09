package homework.model;

import homework.OrderThread;

import java.util.Map;

public class Order {
    private final Map<Meal, Integer> mealIntegerMap;
    private final User customer;

    private boolean ready = false;
    private boolean cancelled = false;
    private boolean payed = false;

    private final OrderThread thread = new OrderThread(this);

    public Order(Map<Meal, Integer> mealIntegerMap, User customer) {
        this.mealIntegerMap = mealIntegerMap;
        this.customer = customer;
    }

    public Map<Meal, Integer> getMealIntegerMap() {
        return mealIntegerMap;
    }

    public User getCustomer() {
        return customer;
    }

    public boolean isReady() { return ready; }

    public void setReady() { ready = true; }

    public boolean isCancelled() { return cancelled; }

    public void pay() { payed = true; }

    public boolean isPayed() { return payed; }

    public OrderThread getThread() { return thread; }

    public void cancel() { cancelled = true; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Meals:\n");
        for (Meal meal : mealIntegerMap.keySet()) {
            sb.append(String.format("Name - %s - Count - %d%n", meal.getName(), mealIntegerMap.get(meal)));
        }
        synchronized (this) {
            sb.append("Cancelled: ").append(cancelled ? "true\n" : "false\n");
            sb.append("Ready: ").append(ready ? "true\n" : "false\n");
            sb.append("Payed: ").append(payed ? "true\n" : "false\n");
        }

        return sb.toString();
    }

    public int calculatePrice() {
        int price = 0;

        for (Meal meal : mealIntegerMap.keySet()) {
            price += mealIntegerMap.get(meal) * meal.getPrice();
        }

        return price;
    }
}
