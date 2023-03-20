import java.util.*;

public class Delivery implements Runnable {
    public static Box box = new Box();
    static Object Book = new Book();
    static int size = 10;
    public static List<Book> DeliveryList = new ArrayList<Book>();
    static int DeliveryCount = 0;

    public static List<Book> GenerateDelivery() {
        int i = 0;
        while (i < size) {
            Book book = new Book();
            DeliveryList.add(book);
            i++;
        }
        return DeliveryList;
    }

    public int size() {
        int Size = DeliveryList.size();
        return Size;
    }

    @Override
    public String toString() {
        return "" + DeliveryList;
    }

    public static String NextDeliveryTime() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(100);
    
        if (randomNumber == 99) {
            return "True";
        } else {
            return "False";
        }    
    }

    public static void main(String[] args) {
        String isDelivery = NextDeliveryTime();
        if (isDelivery == "True") {
            System.out.println(GenerateDelivery());
            DeliveryCount++;
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
            String isDelivery = Delivery.NextDeliveryTime(); 
            if (isDelivery == "True") {
                Thread boxThread = new Thread(box);
                System.out.println("<" + Main.tickCount + "><" + threadId + ">" + "New Delivery!");
                boxThread.run();
            }
        }
    }    
}
