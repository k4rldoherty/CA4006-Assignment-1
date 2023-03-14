import java.util.*;

public class Bookstore {
    private final int TICKS_PER_DAY = 1000;
    private final List<String> BOOK_CATEGORIES = Arrays.asList("fiction", "horror", "romance", "fantasy", "poetry", "history");
    private final int DELIVERY_FREQUENCY = 100;
    private final int BOOKS_PER_DELIVERY = 10;
    private final int CUSTOMERS_PER_TICK = 10;
    private final int MAX_BOOKS_PER_CATEGORY = 50;
    private final int ASSISTANT_BOOK_CAPACITY = 10;
    private final int ASSISTANT_WALK_TIME_PER_BOOK = 1;
    private final int ASSISTANT_WALK_TIME_TO_DELIVERY = 10;
    private final int ASSISTANT_WALK_TIME_TO_SECTION = 10;
    private final int ASSISTANT_STOCKING_TIME_PER_BOOK = 1;
    private final int BOOK_WAIT_TIME = 5;

    private int tickCount;
    private Map<String, Integer> bookInventory = new HashMap<>();
    private Queue<Book> deliveryBox = new LinkedList<>();
    private List<Assistant> assistants = new ArrayList<>();
    private Queue<Customer> customerQueue = new LinkedList<>();

    public Bookstore() {
        for (String category : BOOK_CATEGORIES) {
            bookInventory.put(category, 0);
        }
    }

    public void runSimulation(int numDays) {
        Assistant assistant = new Assistant(1);
        for (int day = 1; day <= numDays; day++) {
            System.out.println("Day " + day + " starting...");
            for (tickCount = (day - 1) * TICKS_PER_DAY; tickCount < day * TICKS_PER_DAY; tickCount++) {
                handleDelivery(assistant);
                //handleCustomers();
            }
            System.out.println("Day " + day + " ending...");
        }
    }

    private void handleDelivery(Assistant assistant) {
        if (Math.random() < 1.0 / DELIVERY_FREQUENCY) {
            System.out.println(getTimestamp() + " Delivery arrived!");
            for (int i = 0; i < BOOKS_PER_DELIVERY; i++) {
                Random random = new Random();
                int categoryIndex = random.nextInt(BOOK_CATEGORIES.size());
                deliveryBox.add(new Book(BOOK_CATEGORIES.get(categoryIndex), 1));
            }
            System.out.println(getTimestamp() + " " + Thread.currentThread().getId() + " Deposited a box of books ");
        }

        if (assistant.getCurrentTask() == Task.WAITING) {
            Book book = deliveryBox.peek();
            if (book != null){
                int i = 0;
                while (book != null && i < ASSISTANT_BOOK_CAPACITY) {
                    assistant.setCurrentTask(Task.GETTING_BOOKS);
                    assistant.getBookFromBox(book);
                    deliveryBox.remove();
                    book = deliveryBox.peek();
                    i++;
                }
            System.out.println("Assistant has 10 books " + getTimestamp()+ deliveryBox.size());
            assistant.setCurrentTask(Task.CARRYING_BOOKS);
            assistant.startCounter();
            }
        }

        if (assistant.getCurrentTask() == Task.CARRYING_BOOKS){
            assistant.move();
        }

        if (assistant.getCurrentTask() == Task.STOCKING_BOOKS){
            assistant.stock();
        }
        // for (Assistant assistant : assistants) {
        //     if (assistant.getCurrentTask() == Task.WAITING) {
        //         Book book = deliveryBox.peek();
        //         if (book != null) {
        //             assistant.setCurrentTask(Task.GETTING_BOOKS);
        //             assistant.getBooksFromBox(book);
        //             deliveryBox.remove();
        //         }
        //     }
        // }
        // for (Assistant assistant : assistants) {
        //     if (assistant.getCurrentTask() == Task.CARRYING_BOOKS) {
        //         assistant.move();
        //     }
        // }
        // for (Assistant assistant : assistants) {
        //     if (assistant.getCurrentTask() == Task.STOCKING_BOOKS) {
        //         assistant.stock();
        //         if (assistant.getCurrentTask() == Task.WAITING) {
        //             System.out.println(getTimestamp() + " " + Thread.currentThread().getId() + " finished stocking " + assistant.getCurrentSection().toUpperCase() + " section with " + assistant.getBooks().size() + " books");
        //         }
        //     }
        // }
    }

    // private void handleCustomers() {
    //     for (int i = 0; i < CUSTOMERS_PER_TICK; i++) {
    //         String category = BOOK_CATEGORIES.get((int) (Math.random() * BOOK_CATEGORIES.size()));
    //         int numBooks = bookInventory.get(category);
    //         if (numBooks == 0) {
    //             System.out.println(getTimestamp() + " Customer waiting for book in " + category + " section...");
    //             customerQueue.add(new Customer(category));
    //         } else {
    //             bookInventory.put(category, numBooks - 1);
    //             System.out.println(getTimestamp() + " Customer bought book from " + category + " section!");
    //         }
    //     }
    //     for (Customer customer : customerQueue) {
    //         customer.decrementWaitTime();
    //         if (customer.getWaitTime() == 0) {
    //             System.out.println(getTimestamp() + " " + Thread.currentThread().getId() + " Customer-" + customer.getId() + " collected a " + customer.getCategory().toUpperCase() + " book having waited " + BOOK_WAIT_TIME + " TICKS");
    //         }
    //     }
    //     customerQueue.removeIf(customer -> customer.getWaitTime() <= 0);
    //}

    private String getTimestamp() {
        return String.format("%-5d T%-2d", tickCount, Thread.currentThread().getId());
    }

    private enum Task {
        WAITING,
        GETTING_BOOKS,
        CARRYING_BOOKS,
        STOCKING_BOOKS
    }

    private class Book {
        private String category;
        private int quantity;

        public Book(String category, int quantity) {
            this.category = category;
            this.quantity = quantity;
        }

        public String getCategory() {
            return category;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    private class Assistant {
        private int currentTick;
        private Task currentTask;
        private List<Book> books;
        private int currentBookIndex;
        private String currentSection;
        private int id;

        public Assistant(int id) {
            currentTick = 0;
            currentTask = Task.WAITING;
            books = new ArrayList<>();
            currentBookIndex = 0;
            currentSection = "";
            this.id = id;
        }

        public void startCounter() {
            currentTick = 0;
        }

        public Task getCurrentTask() {
            return currentTask;
        }

        public void setCurrentTask(Task currentTask) {
            this.currentTask = currentTask;
        }

        public void getBookFromBox(Book book) {
           books.add(book);
            // currentBookIndex += numBooksToGet;
            // if (currentBookIndex == book.getQuantity()) {
            //     currentTask = Task.CARRYING_BOOKS;
            //     currentBookIndex = 0;
            // }
        }

        public void move() {
            currentTick++;
            if (currentTick == ASSISTANT_WALK_TIME_TO_SECTION + ASSISTANT_WALK_TIME_PER_BOOK * books.size()) {
                currentTask = Task.STOCKING_BOOKS;
                currentSection = books.get(currentBookIndex).getCategory();
                int i = 0;
                for (Book book : books){
                    if (book.getCategory() == currentSection){
                        i++;
                    }
                }
                System.out.println(getTimestamp() + " " + Thread.currentThread().getId() + " Assistant-" + id + " began stocking " + currentSection.toUpperCase() + " section with " + i + " books");

            }
        }

        public void stock() {
            currentTick++;
            if (books.get(currentBookIndex).getCategory() == currentSection ){
                System.out.println("Stocking" + currentSection);
                currentBookIndex++;
            }
            else {
                System.out.println("On the move again");
                currentTask = Task.CARRYING_BOOKS;
            }
            // if (currentTick == ASSISTANT_WALK_TIME_TO_DELIVERY + ASSISTANT_WALK_TIME_TO_SECTION + ASSISTANT_WALK_TIME_PER_BOOK * books.size() + ASSISTANT_STOCKING_TIME_PER_BOOK * books.size()) {
            //     for (Book book : books) {
            //         int numBooks = bookInventory.get(book.getCategory());
            //         bookInventory.put(book.getCategory(), numBooks + 1);
            //     }
            //     currentTask = Task.WAITING;
            //     books.clear();
            //     currentBookIndex = 0;
            //}
        }

        public int getId() {
            return id;
        }

        public List<Book> getBooks() {
            return books;
        }

        public String getCurrentSection() {
            return currentSection;
        }
    }

    private class Customer {
        private String category;
        private int waitTime;
        private int id;

        public Customer(String category) {
            this.category = category;
            waitTime = BOOK_WAIT_TIME;
            id = System.identityHashCode(this);
        }

        public String getCategory() {
            return category;
        }

        public int getWaitTime() {
            return waitTime;
        }

        public void decrementWaitTime() {
            waitTime--;
        }

        public int getId() {
            return id;
        }
    }

    public static void main(String[] args) {
        Bookstore bookstore = new Bookstore();
        bookstore.runSimulation(5);
    }
}