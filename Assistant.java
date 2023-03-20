import java.util.*;
import java.util.concurrent.Semaphore;

public class Assistant implements Runnable {
    private int assistantTicks = 0;
    List<String> priorityType = new ArrayList<String>();
    List<Books> booksInHands = new ArrayList<Books>();
    private static Semaphore assistant = new Semaphore(1);
    private static Semaphore assistantBreak = new Semaphore(1);
    static Random rand = new Random();
    private static int randomNumber = rand.nextInt(101) + 200;
    String name;
    int booksCounter = 0;
    static int carrySpace = 10;

    public Assistant(String name, List<Books> booksInHands, int booksCounter) {
        this.booksInHands = booksInHands;
        this.name = name;
        this.booksCounter = booksCounter;
    }

    // Take books from the box for each assistant with new BooksToTake variable each
    // time, only called if no one is waiting in a queue

    public List<Books> takeBooksFromBox() {
        List<Books> books = Box.getBooks();
        List<Books> booksToTake = new ArrayList<>();
    
        Iterator<Books> iterator = books.iterator();
        while (iterator.hasNext() && booksToTake.size() < carrySpace) {
            Books book = iterator.next();
            iterator.remove();
            booksToTake.add(book);
        }
    
        if (booksToTake.isEmpty()) {
            return null;
        } else {
            return booksToTake;
        }
    }

    // Take books from the box for each assistant with new BooksToTake variable each
    // time, only called if someone is waiting in a queue
    // with a list of genres being passed through where customers are waiting
    public List<Books> takePriorityBooksFromBox(List<String> priorityType) {
        List<Books> books = Box.getBooks();
        List<Books> booksToTake = new ArrayList<Books>();

        int i = 0;
        if (!books.isEmpty()) {
            while (booksToTake.size() < carrySpace) {
                for (Books book : books) {
                    while (i < priorityType.size()) {
                        if (book.toString() == priorityType.get(i)) {
                            booksToTake.add(book);
                        }
                        i++;
                    }

                    booksToTake.add(book);
                }
            }
            books.removeAll(booksToTake);
            return booksToTake;
        } else {
            return null;
        }
    }

    // Checks to see if a customer is waiting at a particular section
    public boolean isWaiting(Queue<String> Section) {
        boolean isWaiting = false;
        if (Section.size() != 0) {
            isWaiting = true;
        }

        return isWaiting;
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
                    if (isWaiting(Section.CrimeWaitingLine)) {
                        priorityType.add("Crime");
                    }
                    if (isWaiting(Section.HorrorWaitingLine)) {
                        priorityType.add("Horror");
                    }
                    if (isWaiting(Section.RomanceWaitingLine)) {
                        priorityType.add("Romance");
                    }
                    if (isWaiting(Section.FantasyWaitingLine)) {
                        priorityType.add("Fantasy");
                    }
                    if (isWaiting(Section.FictionWaitingLine)) {
                        priorityType.add("Fiction");
                    }
                    if (isWaiting(Section.SportWaitingLine)) {
                        priorityType.add("Sport");
                    }
                    try {
                        // as long as assitant has no books in hand and there is no one waiting in a
                        // queue, take books from the box
                        if (booksInHands.size() == 0 && priorityType.size() == 0) {
                            try {
                                Thread.sleep(10 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            booksInHands = takeBooksFromBox();
                        } // as long as assitant has no books in hand and there is somone waiting in
                          // queues, take books from the box
                        else if (booksInHands.size() == 0 && priorityType.size() != 0) {
                            try {
                                Thread.sleep(10 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            booksInHands = takePriorityBooksFromBox(priorityType);
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
                for (Books book : booksInHands) {
                    String category = book.Category;
                    booksByCategory.put(category, booksByCategory.getOrDefault(category, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : booksByCategory.entrySet()) {
                    String category = entry.getKey();
                    int count = entry.getValue();
                    Iterator<Books> iterator = booksInHands.iterator();
                    int booksCounter = 0;
                    while (iterator.hasNext()) {
                        Books book = iterator.next();
                        if (book.Category.equals(category)) {
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