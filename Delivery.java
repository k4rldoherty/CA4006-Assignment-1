import java.util.*;

public class Delivery implements Runnable {
    public static Box box = new Box();
    public static List<Book> DeliveryList = new ArrayList<Book>();

    public static List<Book> getNewDelivery() {
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            DeliveryList.add(book);
        }
        return DeliveryList;
    }

    public int size() {
        int Size = DeliveryList.size();
        return Size;
    }

    @Override
    public String toString() {
        return DeliveryList.toString();
    }

    public static String deliveryProbality() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(100);
    
        if (randomNumber == 1) {
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
