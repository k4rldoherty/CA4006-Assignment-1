import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Main {
    public static int TICKS_PER_DAY = 1000;
    public static int TICK_TIME_SIZE = 1000;

    public static int tickCount = 0;
    private static List<Book> carriedBooks = new ArrayList<Book>();
    private static int bookCount = 0;



    public static void main(String[] args) throws FileNotFoundException {

        // Configurable Assistant Count
        System.out.println("How many assistants work at the book store?");
        Scanner scanner = new Scanner(System.in);
        String assistantCount = scanner.nextLine();
        System.out.println(assistantCount + " assistants are working");
        int assistantsAmount = Integer.parseInt(assistantCount);

        // Ensuring the input by user is valid and if it is not defaulting to
        // alternative values
        if (assistantsAmount <= 0) {
            System.out.println("You didnt enter a valid whole number by default there will only be one assistant.");
            assistantsAmount = 1;
        }

        // Configurable Ticks per Second
        System.out.println("Choose mapping of ticks to seconds to determine how fast / slow the simulation will run");
        String ticksCount = scanner.nextLine();
        System.out.println("You have chosen " + ticksCount + "mapped to seconds");
        int ticks = Integer.parseInt(ticksCount);
        scanner.close();

        // Ensuring the input by user is valid and if it is not defaulting to
        // alternative values
        if (ticks <= 0) {
            System.out.println("Ticks must be a valid whole number, ticks will default to 1000 every second");
        } else {
            TICK_TIME_SIZE = ticks;
        }

        File file = new File("output.txt");
        //Instantiating the PrintStream class
        PrintStream stream = new PrintStream(file);
        System.out.println("From now on "+file.getAbsolutePath()+" will be where you can see your output");
        System.setOut(stream);

        
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new Delivery()));
        for (int i = 0; i < assistantsAmount; i++) {
            threads.add(new Thread(new Assistant("<<Assistant-" + i + ">>", carriedBooks, bookCount)));
        }
        threads.add(new Thread(new Customer()));

        for (Thread thread : threads) {
            thread.start();
        }

    }
}