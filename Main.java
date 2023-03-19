import java.util.*;
import javax.swing.*;

public class Main {
    public static int TICKS_PER_DAY = 1000;
    public static int TICK_TIME_SIZE = 1000;

    public static int tickCount = 0;
    public static Box box = new Box();
    private static List<Books> booksInHands = new ArrayList<Books>();
    private static int booksCounter = 0;

    private final static List<Assistant> assistants = new ArrayList<>();

    public static void main(String[] args) {
        // introducing popup GUI boxes for configurable parameters
        // JFrame gui = new JFrame();
        // String getAssistants = JOptionPane.showInputDialog(gui,
        // "How many assistants would you like to work in the Bookstore?");

        // String getTicks = JOptionPane.showInputDialog(gui,
        // "Select ticks to Seconds Correlation. (Higher = Slower, Lower = Faster)");
        // int ticks = Integer.parseInt(getTicks);
        // int assistantsAmount = Integer.parseInt(getAssistants);

        System.out.println("How many assistants work at the book store?");
        Scanner scanner = new Scanner(System.in);
        String assistantCount = scanner.nextLine();
        System.out.println(assistantCount + " assistants are working");
        int assistantsAmount = Integer.parseInt(assistantCount);

        System.out.println("How many ticks will pass every second in the simulation?");
        String ticksCount = scanner.nextLine();
        System.out.println(ticksCount + " ticks will pass every second");
        int ticks = Integer.parseInt(ticksCount);
        scanner.close();

        // Ensuring the input by user is valid and if it is not defaulting to
        // alternative values
        if (assistantsAmount == 0) {
            System.out.println("You didnt enter a valid whole number by default there will only be one assistant.");
            assistantsAmount = 1;
        }
        if (ticks == 0) {
            System.out.println("ticks cannot be 0, ticks will default to one every second");
        } else {
            TICK_TIME_SIZE = ticks;
        }

        // Adding the threads to a list of threads
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < assistantsAmount; i++) {
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