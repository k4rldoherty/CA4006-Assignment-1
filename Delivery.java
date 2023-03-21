import java.util.*;

public class Delivery implements Runnable {
    public static Box box = new Box();
    public static List<Book> deliveryList = new ArrayList<Book>();

    public static List<Book> getNewDelivery() {
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            deliveryList.add(book);
        }
        return deliveryList;
    }

    public int size() {
        int Size = deliveryList.size();
        return Size;
    }

    @Override
    public String toString() {
        return deliveryList.toString();
    }

    public static String deliveryProbality() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(100);
    
        if (randomNumber == 99) {
            return "True";
        } else {
            return "False";
        }    
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1 * Main.TICK_TIME_SIZE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long threadId = Thread.currentThread().getId();
            String newDelivery = Delivery.deliveryProbality(); 
            if (newDelivery == "True") {
                Thread boxThread = new Thread(box);
                System.out.println("<" + Main.tickCount + "><" + threadId + ">" + "New Delivery!");
                boxThread.run();
            }
        }
    }    
}
