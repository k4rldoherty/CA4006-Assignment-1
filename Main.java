import java.util.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

// Our Main class is used to run the overall project and all the classes
public class Main {
    public static int TICKS_PER_DAY = 1000;
    public static int TICK_TIME_SIZE = 1000;

    public static int tickCount = 0;
    public static Box box = new Box();
    private static List<Books> booksInHands = new ArrayList<Books>();
    private static int booksCounter = 0;

    private final static List<Assistant> assistants = new ArrayList<>();

    // Function to ensure that shelves all have one book when the bookshop first opens
    public static void StartShelves() {
        List<String> CategoriesUsed = new ArrayList<String>();

        while (CategoriesUsed.size() < 6) {
            Books book = Books.GenerateBook();

            if (!CategoriesUsed.contains(book.toString())) {

                String book_category = book.toString();

                if (book_category.equals("Horror")) {
                    CategoriesUsed.add("Horror");

                    Shelve.HorrorShelf.add(book);
                }

                if (book_category.equals("Sport")) {
                    CategoriesUsed.add("Sport");

                    Shelve.SportShelf.add(book);
                }

                if (book_category.equals("Fiction")) {
                    CategoriesUsed.add("Fiction");

                    Shelve.FictionShelf.add(book);
                }

                if (book_category.equals("Fantasy")) {
                    CategoriesUsed.add("Fantasy");

                    Shelve.FantasyShelf.add(book);
                }

                if (book_category.equals("Romance")) {
                    CategoriesUsed.add("Romance");

                    Shelve.RomanceShelf.add(book);
                }

                if (book_category.equals("Crime")) {
                    CategoriesUsed.add("Crime");

                    Shelve.CrimeShelf.add(book);
                }
            }
        }

        System.out.println("Each Shelf contains 1 book");

    }

    // Our Main function which contains the runner code for the entire project
    public static void main(String[] args) {
        // introducing popup GUI boxes for configurable parameters
        JFrame jFrame = new JFrame();
        String getAssistants = JOptionPane.showInputDialog(jFrame, "How many assistants would you like to work in the Bookstore?");
            
        JOptionPane.showMessageDialog(jFrame, "You Have Selected This Many Assistant: " + getAssistants);

        String getTicks = JOptionPane.showInputDialog(jFrame, "What would you like the Ticks to seconds mapping to be like? (1000 is one tick a second a lower value is faster and a bigger value is slower)");
            
        JOptionPane.showMessageDialog(jFrame, "You Have Selected This Mapping: " + getTicks);

        int Ticks = Integer.parseInt(getTicks);
        int Assistants_Amount = Integer.parseInt(getAssistants);
        
        // Ensuring the input by user is valid and if it is not defaulting to alternative values
        if (Assistants_Amount == 0) {
            System.out.println("You didnt enter a valid whole number by default there will only be one assistant.");
            Assistants_Amount = 1;
        }
        if (Ticks == 0) 
        {
            System.out.println("You Didnt enter a valid whole number, Ticks will default to one every second");
        }
        else {
            TICK_TIME_SIZE = Ticks;
        }
        
        StartShelves();

        // Adding the threads to a list of threads
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Assistants_Amount; i++) {
            assistants.add(new Assistant("Assistant-" + i, booksInHands, booksCounter));
            threads.add(new Thread(new Assistant("Assistant-" + i, booksInHands, booksCounter)));
        }
        threads.add(new Thread(new Customer()));
        threads.add(new Thread(new Tick(box)));

        // Starting all the threads contained in the list
        for (Thread thread : threads) {
            thread.start();
        }

    }
}