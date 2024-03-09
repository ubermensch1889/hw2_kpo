package homework;

// класс представляет процесс приготовления блюда
public class MealThread extends Thread {
    private final int time;

    public MealThread(int time) {
        this.time = time;
    }

    public void run() {
        try{
            Thread.sleep(time * 1000L);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }
    }
}
