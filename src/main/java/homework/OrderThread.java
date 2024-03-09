package homework;

import homework.model.Meal;
import homework.model.Order;

import java.util.ArrayList;
import java.util.List;

// класс представляет процесс приготовления заказа из нескольких блюд
public class OrderThread extends Thread{
    private final Order order;
    private final List<MealThread> threadList = new ArrayList<>();
    public OrderThread(Order order) {
        this.order = order;
    }

    public List<MealThread> GetThreadList() { return threadList; }
    public void run() {
        // будем считать, что все блюда готовятся параллельно

        for (Meal meal : order.getMealIntegerMap().keySet()) {
            for (int i = 0; i < order.getMealIntegerMap().get(meal); ++i) {
                MealThread mealThread = new MealThread(meal.getTimeToCook());
                mealThread.start();
                threadList.add(mealThread);
            }
        }

        // ждем приготовления блюд, в том числе добавленных позже
        while (true) {
            for (int i = 0; i < threadList.size(); ++i) {
                try {
                    threadList.get(i).join();
                } catch (InterruptedException ignored) {
                    // поток прерван, пока мы ждали завершения приготовления блюд
                    synchronized (order) {
                        order.cancel();
                    }
                    return;
                }
            }

            // проверяем, что все потоки завершились и новых не добавилось
            for (int i = 0; i < threadList.size(); ++i) {
                if (threadList.get(i).isAlive()) {
                    continue;
                }
            }

            break;
        }
        synchronized (order) {
            order.setReady();
        }
    }
}
