import java.util.*;
import java.util.concurrent.Semaphore;

public class Assistant implements Runnable {
    // Introducing a customer class to handle the functionality of a customer on a thread
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

    // Declaring new instance of booksInHands and name for each assistant
    public Assistant(String name, List<Books> booksInHands, int booksCounter) {
        this.booksInHands = booksInHands;
        this.name = name;
        this.booksCounter = booksCounter;
    }
    // Take books from the box for each assistant with new BooksToTake variable each time, only called if no one is waiting in a queue
    public List<Books> takeBooksFromBox() {
        List<Books> books = Box.getBooks();
        List<Books> booksToTake = new ArrayList<Books>();

        if (!books.isEmpty()) {
            while (booksToTake.size() < carrySpace) {
                for (Books book : books) {
                    booksToTake.add(book);
                }
            }
            books.removeAll(booksToTake);
            return booksToTake;
        } else {
            return null;
        }
    }
    // Take books from the box for each assistant with new BooksToTake variable each time, only called if someone is waiting in a queue
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
    
    // Checks to see if a customer is waiting at a particular shelf
    public boolean IsWaiting(Queue<String> Shelf) {
        boolean IsWaiting = false;
        if (Shelf.size() != 0) {
            IsWaiting = true;
        }

        return IsWaiting;
    }

    @Override
    public void run() {
        // continues to run while thread is alive
        while (true) {
            long threadId = Thread.currentThread().getId(); // get current threadID
            try {

                Thread.sleep(1 * Main.TICK_TIME_SIZE); // sleep for 1 tick
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            assistantTicks++;

            try {
                assistant.acquire(); // semaphore used to to ensure two assistants aren't going to box at the same time
                if (!Box.BooksInBox.isEmpty()) {
                    if (IsWaiting(Shelve.CrimeWaitingLine)) {
                        priorityType.add("Crime");
                    }
                    if (IsWaiting(Shelve.HorrorWaitingLine)) {
                        priorityType.add("Horror");
                    }
                    if (IsWaiting(Shelve.RomanceWaitingLine)) {
                        priorityType.add("Romance");
                    }
                    if (IsWaiting(Shelve.FantasyWaitingLine)) {
                        priorityType.add("Fantasy");
                    }
                    if (IsWaiting(Shelve.FictionWaitingLine)) {
                        priorityType.add("Fiction");
                    }
                    if (IsWaiting(Shelve.SportWaitingLine)) {
                        priorityType.add("Sport");
                    }
                    try {
                        // as long as assitant has no books in hand and there is no one waiting in a queue, take books from the box
                        if (booksInHands.size() == 0 && priorityType.size() == 0) { 
                            try {
                                Thread.sleep(10 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            booksInHands = takeBooksFromBox();
                        } // as long as assitant has no books in hand and there is somone waiting in queues, take books from the box
                        else if (booksInHands.size() == 0 && priorityType.size() != 0) {
                            try {
                                Thread.sleep(10 * Main.TICK_TIME_SIZE);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            booksInHands = takePriorityBooksFromBox(priorityType);
                        }
                        System.out.println("<"+Main.tickCount+">"+"<"+threadId+">"+name+" collected 10 books from the box, the books are: " + booksInHands);
                    } finally {

                    }
                    
                    try {
                        Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE); // sleep for 10 ticks
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                assistant.release(); // release the semaphore so another assistant can perform this action

            }
            // as long as books in hands doesn't equal 0, so assistant has books in hands
            if (booksInHands.size() != 0) {
                synchronized (lock) { 
                    // create an iterator which iterates through the books in hands
                    Iterator<Books> iterator = booksInHands.iterator();
                    if (booksInHands.toString().contains("Fiction")) { // if the booksInHands has a fiction book
                        while (iterator.hasNext()) { // while there is still a book in the iterator list
                            book = iterator.next();
                            if (book.toString().equals("Fiction")) { //if said book is a fiction book
                                Shelve.AddBooksToShelves(book); // stack the fiction book onto the shelf
                                iterator.remove(); // remove the book from booksInHands
                                booksCounter++; // increment a book counter to count how many fiction books were stacked
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) { // stay at the Fiction shelf until all Fiction books are stacked
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " began stocking FICTION section with " + booksCounter + " books");
                                booksCounter = 0; // reset counter to 0 so we can reuse it
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE); // sleep for one tick for every book left in hand + 10 ticks
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction shelf
                    if (booksInHands.toString().contains("Sport")) {
                        iterator = booksInHands.iterator();
                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Sport")) {
                                Shelve.AddBooksToShelves(book);
                                iterator.remove(); 
                                booksCounter++;
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) { 
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " began stocking SPORT section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction shelf
                    if (booksInHands.toString().contains("Fantasy")) {
                        iterator = booksInHands.iterator();
                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Fantasy")) {
                                Shelve.AddBooksToShelves(book);
                                iterator.remove();
                                booksCounter++;
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) {
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " began stocking FANTASY section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction shelf
                    if (booksInHands.toString().contains("Horror")) {
                        iterator = booksInHands.iterator();

                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Horror")) {
                                Shelve.AddBooksToShelves(book);
                                iterator.remove();
                                booksCounter++;
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) {
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " began stocking HORROR section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction shelf
                    if (booksInHands.toString().contains("Crime")) {
                        iterator = booksInHands.iterator();

                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Crime")) {
                                Shelve.AddBooksToShelves(book);
                                iterator.remove();
                                booksCounter++;
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) {
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " began stocking CRIME section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // operates the same as fiction shelf
                    if (booksInHands.toString().contains("Romance")) {
                        iterator = booksInHands.iterator();
                        while (iterator.hasNext()) {
                            book = iterator.next();
                            if (book.toString().equals("Romance")) {
                                Shelve.AddBooksToShelves(book);
                                iterator.remove();
                                booksCounter++;
                                try {
                                    Thread.sleep(1 * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (!iterator.hasNext()) {
                                System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + name + " began stocking ROMANCE section with " + booksCounter + " books");
                                booksCounter = 0;
                                try {
                                    Thread.sleep((booksInHands.size() + 10) * Main.TICK_TIME_SIZE);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            // if the assistantsTicks modulo the random number between 200-300
            if(assistantTicks % randomNumber == 0)
            {
                try {
                    assistantBreak.acquire(); // semaphore acquire so only one assitant can take a break at a time
                    System.out.println("<"+Main.tickCount+">"+"<"+threadId+">"+ name + " is on their break.");
                    Thread.sleep(150 * Main.TICK_TIME_SIZE); // sleep for 150 ticks
                    randomNumber = rand.nextInt(101) + 200 + assistantTicks; // generate another random number between 200-300 and add the current assistant ticks to it
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally{
                    assistantBreak.release(); // release the sempahore so another assistant can take a break
                    System.out.println("<"+Main.tickCount+">"+"<"+threadId+">"+ name + " is back from their break.");

                }
            }
        }
    }

    public static void main(String[] args) {
    }
}