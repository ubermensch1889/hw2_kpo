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

public class DeleteMealDialog extends DialogOption {
    public DeleteMealDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
    }
    @Override
    public String getName() {
        return "Delete meal";
    }

    @Override
    public void run() throws IOException {
        String choice = null;
        do {
            Dao<Meal> dao = new MealDao("data" + File.separator + "meal.json");
            List<Meal> meals = dao.getAll();

            if (meals.isEmpty()) {
                System.out.println("Menu is empty.");
                return;
            }

            for (int i = 0; i < meals.size(); ++i) {
                System.out.printf("%d %s%n", i + 1, meals.get(i).getName());
            }

            System.out.print("Enter the number of meal you want to delete> ");

            try {
                int number = Integer.parseInt(reader.readLine());

                if (number <= 0 || number > meals.size()) {
                    System.out.println("Wrong input.");
                    continue;
                }

                dao.delete(meals.get(number - 1));
                System.out.println("The meal was successfully deleted.");
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
