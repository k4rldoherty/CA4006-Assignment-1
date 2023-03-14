import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bookstore {
    private List<BookSection> sections;
    private Box box;
    private Random random;
    private int tick;

    public Bookstore() {
        sections = new ArrayList<>();
        sections.add(new BookSection("Fiction"));
        sections.add(new BookSection("Horror"));
        sections.add(new BookSection("Romance"));
        sections.add(new BookSection("Fantasy"));
        sections.add(new BookSection("Poetry"));
        sections.add(new BookSection("History"));
        box = new Box();
        random = new Random();
        tick = 0;
    }

    public void startSimulation() throws InterruptedException {
        System.out.println("Starting simulation...");
    
        Thread deliveryThread = new Thread();
        deliveryThread.start();
    
        Thread[] assistantThreads = new Thread[3];
        for (int i = 0; i < assistantThreads.length; i++) {
            assistantThreads[i] = new Thread(new Assistant(sections, box));
            assistantThreads[i].start();
        }
    
        Thread[] customerThreads = new Thread[10];
        for (int i = 0; i < customerThreads.length; i++) {
            Customer customer = new Customer(sections);
            customerThreads[i] = new Thread(customer);
            customerThreads[i].start();
        }
    
        while (tick < 1000) {
            // Thread.sleep(100);
    
            if (tick % 100 == 0) {
                int numBooks = 10;
                for (BookSection section : sections) {
                    int num = random.nextInt(numBooks);
                    for (int i = 0; i < num; i++) {
                        Book book = section.getRandomBook();
                        if (book != null) {
                            sections.add(section);
                            section.takeBook();
                        }
                    }
                    numBooks -= num;
                }
                if (!sections.isEmpty()) { // Only add the delivery to the box if it is not empty
                    box.addDelivery(new Delivery(sections));
                    System.out.println(tick + " Delivery arrived with " + sections.size() + " sections.");
                }
            }
    
            tick++;
    
            if (tick % 10 == 0) {
                BookSection section = sections.get(random.nextInt(sections.size()));
                if (section.hasBook()) {
                    section.buyBook();
                }
            }
        }
    
        System.out.println("Simulation finished.");
    }

    public static void main(String[] args) {
        Bookstore bookstore = new Bookstore();
        try {
            bookstore.startSimulation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}