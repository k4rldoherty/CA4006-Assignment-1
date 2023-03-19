import java.util.ArrayList;
import java.util.List;

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
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int sectionIndex = (int) (Math.random() * sections.size());
            BookSection section = sections.get(sectionIndex);
            int bookIndex = (int) (Math.random() * section.getBooks().size());
            Book book = section.getBooks().get(bookIndex);
            books.add(book);
        }
        Delivery delivery = new Delivery(books);
        addDelivery(delivery);
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        for (Assistant assistant : assistants) {
            if (assistant.isFree()) {
                assistant.takeDelivery(delivery);
                break;
            }
        }
    }

    public void createCustomer() {
        int sectionIndex = (int) (Math.random() * sections.size());
        BookSection section = sections.get(sectionIndex);
        int bookIndex = (int) (Math.random() * section.getBooks().size());
        Book book = section.getBooks().get(bookIndex);
        Customer customer = new Customer(book);
        addCustomer(customer);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        for (Assistant assistant : assistants) {
            if (assistant.isFree()) {
                assistant.takeCustomer(customer);
                break;
            }
        }
    }

    public void runSimulation(int numTicks) {
        for (int i = 0; i < numTicks; i++) {
            if (i % 100 == 0) {
                createDelivery();
            }
            for (Assistant assistant : assistants) {
                assistant.tick();
            }
            for (Delivery delivery : deliveries) {
                delivery.tick();
            }
            for (Customer customer : customers) {
                customer.tick();
            }
        }
    }
}
