import java.util.*;
import java.util.concurrent.Semaphore;

public class Assistant implements Runnable {
    private int assistantTicks = 0;
    List<Book> carriedBooks = new ArrayList<Book>();
    private static Semaphore assistantSemaphore = new Semaphore(1);
    private static Semaphore breakSemaphore = new Semaphore(1);
    static Random random = new Random();
    String name;
    int bookCount = 0;
    static int capacity = 10;

    public Assistant(String name, List<Book> carriedBooks, int bookCount) {
        this.carriedBooks = carriedBooks;
        this.name = name;
        this.bookCount = bookCount;
    }

    @Override
    public void run() {
        while (true) {
            assistantTicks++;
            long threadId = Thread.currentThread().getId(); 
            try {

                Thread.sleep(1 * Main.TICK_TIME_SIZE); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                assistantSemaphore.acquire();
                if (!Box.boxBooks.isEmpty()) {
                    if (carriedBooks.size() == 0) {
                        try {
                            Thread.sleep(10 * Main.TICK_TIME_SIZE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        carriedBooks = Box.getBooks();
                    }
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name
                            + " collected 10 books from the box: " + carriedBooks);
                    try {
                        Thread.sleep((carriedBooks.size() + 10) * Main.TICK_TIME_SIZE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                assistantSemaphore.release();
            }
            if (carriedBooks.size() != 0) {
                Map<String, Integer> booksByCategory = new HashMap<>();
                for (Book book : carriedBooks) {
                    String category = book.toString();
                    booksByCategory.put(category, booksByCategory.getOrDefault(category, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : booksByCategory.entrySet()) {
                    String category = entry.getKey();
                    int count = entry.getValue();
                    Iterator<Book> iterator = carriedBooks.iterator();
                    int bookCount = 0;
                    while (iterator.hasNext()) {
                        Book book = iterator.next();
                        if (book.toString().equals(category)) {
                            Section.AddBooksToShelves(book);
                            iterator.remove();
                            bookCount++;
                            try {
                                Thread.sleep(1 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!iterator.hasNext()) {
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name
                                    + " began stocking " + category.toUpperCase() + " section with " + bookCount
                                    + " books");
                            bookCount = 0;
                            try {
                                Thread.sleep((count + 10) * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            int randomNumber = random.nextInt(100) + 200;
            if (assistantTicks % randomNumber == 0) {
                try {
                    breakSemaphore.acquire();
                    System.out.printf("<%d><%d>%s is taking a break.%n", Main.tickCount, threadId, name);
                    Thread.sleep(150 * Main.TICK_TIME_SIZE);
                    randomNumber = random.nextInt(100) + 200;
                } catch (InterruptedException e) {
                    // Thread.currentThread().interrupt();
                } finally {
                    breakSemaphore.release();
                    System.out.printf("<%d><%d>%s is back from break, back to work!%n", Main.tickCount, threadId, name);
                }
            }
        }
    }
}