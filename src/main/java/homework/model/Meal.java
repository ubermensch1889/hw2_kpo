package homework.model;

public class Meal {
    private String name;
    private int timeToCook;
    private int count;
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeToCook(int timeToCook) {
        this.timeToCook = timeToCook;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTimeToCook() {
        return timeToCook;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public Meal(String name, int timeToCook, int count, int price) {
        this.name = name;
        this.timeToCook = timeToCook;
        this.count = count;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("Name - %s - Count - %d - Price - %d", name, count, price);
    }
}
