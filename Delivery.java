import java.util.*;

public class Delivery {
    static Object Books = new Books();
    static int size = 10;
    public static List<Books> DeliveryList = new ArrayList<Books>();
    static int DeliveryCount = 0;

    public static List<Books> GenerateDelivery() {
        int i = 0;

        while (i < size) {
            Books book = new Books();

            book.setCategory();

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

}
