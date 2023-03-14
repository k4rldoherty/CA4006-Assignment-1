import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

public class Box {
    private Queue<Delivery> deliveries;

    public Box() {
        deliveries = new LinkedList<>();
    }

    public synchronized void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        notifyAll();
    }

    public synchronized Delivery getDelivery() throws InterruptedException {
        while (deliveries.isEmpty()) {
            wait();
        }
        return deliveries.remove();
    }

    public synchronized void addDeliverySections(List<BookSection> sections) {
        Delivery delivery = new Delivery(sections);
        deliveries.add(delivery);
        notifyAll();
    }

    public boolean isAvailable() {
        return !deliveries.isEmpty();
    }
}