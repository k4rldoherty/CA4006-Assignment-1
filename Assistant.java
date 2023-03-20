import java.util.*;
import java.util.concurrent.Semaphore;

public class Assistant implements Runnable {
    // Introducing a customer class to handle the functionality of a customer on a
    // thread
    private final Object lock = new Object();
    private static Books book;
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
    List<Queue<String>> waitingLists = new ArrayList<>();

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
        List<Books> booksToTake = new ArrayList<>();
    
        int i = 0;
        while (i < priorityType.size() && booksToTake.size() < carrySpace) {
            for (Iterator<Books> iter = books.iterator(); iter.hasNext();) {
                Books book = iter.next();
                if (book.toString().equals(priorityType.get(i))) {
                    booksToTake.add(book);
                    iter.remove();
                }
            }
            i++;
        }
        while (booksToTake.size() < carrySpace && !books.isEmpty()) {
            booksToTake.add(books.remove(0));
        }
        return booksToTake.isEmpty() ? null : booksToTake;
    }

    // Checks to see if a customer is waiting at a particular section
    public boolean isWaiting(Queue<String> Section) {
        boolean isWaiting = false;
        if (!Section.isEmpty()) {
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
                    if (!Section.CrimeWaitingLine.isEmpty()) {
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
            if (!booksInHands.isEmpty()) {
                synchronized (lock) {
                    // create an iterator which iterates through the books in hands
                    Iterator<Books> iterator = booksInHands.iterator();
                    if (booksInHands.toString().contains("Fiction")) { // if the booksInHands has a fiction book
                        while (iterator.hasNext()) { // while there is still a book in the iterator list
                            book = iterator.next();
                            if (book.toString().equals("Fiction")) { // if said book is a fiction book
                                Section.AddBooksToShelves(book); // stack the fiction book onto the section
                                iterator.remove(); // remove the book from booksInHands
                                booksCounter++; // increment a book counter to count how many fiction books were stacked
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) { // stay at the Fiction section until all Fiction books are stacked
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name
                                        + " began stocking FICTION section with " + booksCounter + " books");
                                booksCounter = 0; // reset counter to 0 so we can reuse it
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE); // sleep for one
                                                                                                    // tick for every
                                                                                                    // book left in hand
                                                                                                    // + 10 ticks
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction section
                    if (booksInHands.toString().contains("Sport")) {
                        iterator = booksInHands.iterator();
                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Sport")) {
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
                                        + " began stocking SPORT section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction section
                    if (booksInHands.toString().contains("Fantasy")) {
                        iterator = booksInHands.iterator();
                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Fantasy")) {
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
                                        + " began stocking FANTASY section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction section
                    if (booksInHands.toString().contains("Horror")) {
                        iterator = booksInHands.iterator();

                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Horror")) {
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
                                        + " began stocking HORROR section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction section
                    if (booksInHands.toString().contains("Crime")) {
                        iterator = booksInHands.iterator();

                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Crime")) {
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
                                        + " began stocking CRIME section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction section
                    if (booksInHands.toString().contains("Romance")) {
                        iterator = booksInHands.iterator();
                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Romance")) {
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
                                        + " began stocking ROMANCE section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
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