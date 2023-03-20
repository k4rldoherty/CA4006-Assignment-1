import java.util.*;

public class Delivery {
    static Object Books = new Books();
    static int size = 10;
    public static List<Books> deliveryList = new ArrayList<Books>();
    static int DeliveryCount = 0;

    public static List<Books> GenerateDelivery() {
        int i = 0;

        while (i < size) {
            Books book = new Books();

            book.setCategory();

            deliveryList.add(book);

            i++;
        }
        return deliveryList;
    }

    public int size() {
        int Size = deliveryList.size();
        return Size;
    }

    @Override
    public String toString() {
        return "" + deliveryList;
    }

    public static String NextDeliveryTime() {
        List<String> ProbabilityOfDelivery = new ArrayList<String>();
        var i = 0;
        while (i < 100) {
            if (i != 99) {
                ProbabilityOfDelivery.add("False");
            } else {
                ProbabilityOfDelivery.add("True");
            }
            i++;
        }

        int UpperRange = ProbabilityOfDelivery.size();

        Random rand = new Random();
        int Index = rand.nextInt(UpperRange);

        String IsDelivery = ProbabilityOfDelivery.get(Index);

        return IsDelivery;
    }

    public static void main(String[] args) {
        String isDelivery = NextDeliveryTime();
        if (isDelivery == "True") {
            System.out.println(GenerateDelivery());
            DeliveryCount++;
        }

    }

}
