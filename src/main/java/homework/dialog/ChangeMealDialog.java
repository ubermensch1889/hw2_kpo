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

public class ChangeMealDialog extends DialogOption {
    public ChangeMealDialog(BufferedReader reader, RegisterSystem rs) {
        super(reader, rs);
    }
    @Override
    public String getName() {
        return "Change meal";
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

            for (int i = 0; i < meals.size(); ++i) {
                System.out.printf("%d %s%n", i + 1, meals.get(i).getName());
            }

            System.out.print("Enter the number of meal you want to change> ");

            try {
                int number = Integer.parseInt(reader.readLine());

                changeItem(meals.get(number - 1));
                dao.saveChanges();
            } catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to return to previous menu or something else to try again> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));
    }

    private void changeItem(Meal meal) throws IOException {
        while(true) {
            System.out.println("Please enter the number of field you want to change.");
            System.out.println("1 - Name");
            System.out.println("2 - Time to cook");
            System.out.println("3 - Number of meals");
            System.out.println("4 - Price");
            System.out.print("> ");

            String choice = reader.readLine();
            System.out.println();

            try{
                switch (choice){
                    case "1":
                        System.out.print("Please enter a new name> ");
                        String name = reader.readLine();
                        System.out.println();
                        meal.setName(name);
                        return;
                    case "2":
                        System.out.print("Please enter a new time> ");
                        String time = reader.readLine();
                        System.out.println();
                        meal.setTimeToCook(Integer.parseInt(time));
                        return;
                    case "3":
                        System.out.print("Please enter a new number> ");
                        String count = reader.readLine();
                        System.out.println();
                        meal.setCount(Integer.parseInt(count));
                        return;
                    case "4":
                        System.out.print("Please enter a new price> ");
                        String price = reader.readLine();
                        System.out.println();
                        meal.setPrice(Integer.parseInt(price));
                        return;
                    default:
                        System.out.println("Wrong input");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Wrong input");
            }

        }
    }
}
