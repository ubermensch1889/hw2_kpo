package homework.dialog;

import homework.RegisterSystem;
import homework.dao.Dao;
import homework.dao.MealDao;
import homework.model.Meal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddMealDialog extends DialogOption {
    public AddMealDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);

    }

    @Override
    public String getName() {
        return "Add meal";
    }

    @Override
    public void run() throws IOException {
        String choice = null;
        do {
            System.out.print("Please enter the name of the meal> ");
            String name = reader.readLine();
            System.out.println();
            System.out.print("Please enter the cooking time in seconds> ");
            String stringTime = reader.readLine();
            System.out.println();
            System.out.print("Please enter the number of meals> ");
            String stringNumber = reader.readLine();
            System.out.println();
            System.out.print("Please enter the price of the meal> ");
            String stringPrice = reader.readLine();
            System.out.println();

            try {
                int time = Integer.parseInt(stringTime);
                int number = Integer.parseInt(stringNumber);
                int price = Integer.parseInt(stringPrice);

                if (time <= 0 || number <= 0 || price <= 0) {
                    System.out.println("Wrong input.");
                    continue;
                }

                Dao<Meal> dao = new MealDao("data" + File.separator + "meal.json");
                List<Meal> meals = dao.getAll();

                boolean isAdded = false;
                for (Meal meal : meals) {
                    if (meal.getName().equals(name)) {
                        System.out.println("This meal is already added.");
                        isAdded = true;
                        break;
                    }
                }

                if (!isAdded) {
                    dao.save(new Meal(name, time, number, price));
                    System.out.println("The meal is added successfully.");
                }
                else {
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to return to previous menu or something else to try again> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));


    }
}
