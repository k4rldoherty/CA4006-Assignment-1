import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    // Getters and setters
}

public class BookSection {
    private int id;
    private List<Book> books;

    public BookSection(int id, List<Book> books) {
        this.id = id;
        this.books = books;
    }

    // Getters and setters
}

public class Assistant implements Runnable {
    private int id;
    private List<Book> carrying;
    private Bookstore bookstore;

    public Assistant(int id, Bookstore bookstore) {
        this.id = id;
        this.bookstore = bookstore;
    }

    public void run() {
        // Implement assistant logic
    }

    // Methods to move from one section to another and to stock a section with books
}

public class Delivery {
    private int id;
    private List<Book> books;
    private long timeStamp;

    public Delivery(int id, List<Book> books, long timeStamp) {
        this.id = id;
        this.books = books;
        this.timeStamp = timeStamp;
        
    }

    // Getters and setters
}

public class Customer implements Runnable {
    private int id;
    private int bookSection;
    private Bookstore bookstore;

    public Customer(int id, int bookSection, Bookstore bookstore) {
        this.id = id;
        this.bookSection = bookSection;
        this.bookstore = bookstore;
    }

    public void run() {
        // Implement customer logic
    }

    // Method to wait for a book in their section to become available
}

public class Bookstore {
    private List<BookSection> sections;
    private List<Assistant> assistants;
    private List<Delivery> deliveries;
    private List<Customer> customers;

    public Bookstore(List<BookSection> sections, List<Assistant> assistants,
                     List<Delivery> deliveries, List<Customer> customers) {
        this.sections = sections;
        this.assistants = assistants;
        this.deliveries = deliveries;
        this.customers = customers;
    }

    public void createDelivery() {
        // Implement delivery creation logic
        // (Avg every 100 tics) (list of 10 bookID from any section arrive)
    }

    public void addDelivery(Delivery delivery) {
        // Implement delivery addition logic
        // If assistant is free, take deliveryID to corresponding section
        // 10 ticks from delivery area to section
        // 1 tick to put bookID in section
        // 10 ticks from section to section
        // 10 ticks when finished deliveryID to walk back to area
    }

    public void createCustomer() {
        // Implement customer creation logic
    }

    public void addCustomer(Customer customer) {
        // Implement customer addition logic
    }

    public void runSimulation() {
        // Implement simulation logic
    }
}

public class Main {
    public static void main(String[] args) {
        // Create an instance of the bookstore and start the simulation
        Bookstore bookstore = new Bookstore(/* sections, assistants, deliveries, customers */);
        bookstore.runSimulation();
    }
}