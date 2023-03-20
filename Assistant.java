import java.util.*;
import java.util.concurrent.Semaphore;

public class Assistant implements Runnable {
    private int assistantTicks = 0;
    List<Book> booksInHands = new ArrayList<Book>();
    private static Semaphore assistant = new Semaphore(1);
    private static Semaphore assistantBreak = new Semaphore(1);
    static Random rand = new Random();
    private static int randomNumber = rand.nextInt(101) + 200;
    String name;
    int booksCounter = 0;
    static int carrySpace = 10;

    public Assistant(String name, List<Book> booksInHands, int booksCounter) {
        this.booksInHands = booksInHands;
        this.name = name;
        this.booksCounter = booksCounter;
    }

    // Take books from the box for each assistant with new BooksToTake variable each
    // time, only called if no one is waiting in a queue

    public List<Book> takeBooksFromBox() {
        List<Book> books = Box.getBooks();
        List<Book> booksToTake = new ArrayList<>();
    
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext() && booksToTake.size() < carrySpace) {
            Book book = iterator.next();
            iterator.remove();
            booksToTake.add(book);
        }
    
        if (booksToTake.isEmpty()) {
            return null;
        } else {
            return booksToTake;
        }
    }


    @Override
    public void run() {
        while (true) {
            long threadId = Thread.currentThread().getId(); // get current threadID
            try {

                Thread.sleep(1 * Main.TICK_TIME_SIZE); // sleep for 1 tick
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assistantTicks++;

            try {
                assistant.acquire(); // semaphore used to to ensure two assistants aren't going to box at the same
                                     // time
                if (!Box.BooksInBox.isEmpty()) {
                    try {
                        // as long as assitant has no books in hand and there is no one waiting in a
                        // queue, take books from the box
                        if (booksInHands.size() == 0) {
                            try {
                                Thread.sleep(10 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            booksInHands = Box.getBooks();
                        }
                        System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name
                                + " collected 10 books from the box: " + booksInHands);
                    } finally {

                    }
                    try {
                        Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE); // sleep for 10 ticks
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                assistant.release(); // release the semaphore so another assistant can perform this action

            }
            // as long as books in hands doesn't equal 0, so assistant has books in hands
            if (booksInHands.size() != 0) {
                Map<String, Integer> booksByCategory = new HashMap<>();
                for (Book book : booksInHands) {
                    String category = book.toString();
                    booksByCategory.put(category, booksByCategory.getOrDefault(category, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : booksByCategory.entrySet()) {
                    String category = entry.getKey();
                    int count = entry.getValue();
                    Iterator<Book> iterator = booksInHands.iterator();
                    int booksCounter = 0;
                    while (iterator.hasNext()) {
                        Book book = iterator.next();
                        if (book.toString().equals(category)) {
                            Section.AddBooksToShelves(book);
                            iterator.remove();
                            booksCounter++;
                            try {
                                Thread.sleep(1 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!iterator.hasNext()) {
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name
                                    + " began stocking " + category.toUpperCase() + " section with " + booksCounter + " books");
                            booksCounter = 0;
                            try {
                                Thread.sleep((count + 10) * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            // if the assistantsTicks modulo the random number between 200-300
            if (assistantTicks % randomNumber == 0) {
                try {
                    assistantBreak.acquire(); // semaphore acquire so only one assitant can take a break at a time
                    System.out
                            .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " is taking a break.");
                    Thread.sleep(150 * Main.TICK_TIME_SIZE); // sleep for 150 ticks
                    randomNumber = rand.nextInt(101) + 200 + assistantTicks; // generate another random number between
                                                                             // 200-300 and add the current assistant
                                                                             // ticks to it
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    assistantBreak.release(); // release the sempahore so another assistant can take a break
                    System.out.println(
                            "<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " is back from break, back to work!");

                }
            }
        }
    }

    public static void main(String[] args) {
    }
}