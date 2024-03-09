package homework;

import java.io.*;

// синглетон для управления и сохранения информации о прибыли
public class RevenueManager {
    private int revenue;
    private static RevenueManager instance;
    public static RevenueManager getInstance() {
        if (instance == null) {
            instance = new RevenueManager();
        }

        return instance;
    }

    public void increaseRevenue(int change) {
        revenue += change;
        try {
            PrintWriter writer = new PrintWriter("data" + File.separator + "revenue.txt");
            writer.print(revenue);

            writer.close();
        } catch (IOException ignored) {
            System.out.println(ignored);
        }
    }

    public int getRevenue() {
        return revenue;
    }

    private RevenueManager() {
        try {
            FileReader reader = new FileReader("data" + File.separator + "revenue.txt");

            // Читаем ключ посимвольно
            int c;
            StringBuilder sb = new StringBuilder();
            while((c = reader.read())!=-1){
                sb.append((char) c);
            }

            revenue = sb.isEmpty() ? 0 : Integer.parseInt(sb.toString());
        } catch (IOException | NumberFormatException e) {
            revenue = 0;
        }

    }
}
