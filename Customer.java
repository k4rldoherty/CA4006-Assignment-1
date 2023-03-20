import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Customer implements Runnable {
    private final String[] genres = { "FICTION", "HORROR", "ROMANCE", "FANTASY", "SPORT", "CRIME" };
    static int customerCount;
    static int customerServedCount;
    private String customer;
    static List<Integer> CustomerWaitTimes = new ArrayList<>();

    // The main runner code that runs when the Thread is started
    @Override
    public void run() {
        while (true) {
            Main.tickCount++;
            try {
                Thread.sleep(1 * Main.TICK_TIME_SIZE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean newCustomer = (new Random().nextInt(10) == 1);
            if (newCustomer) {
                long threadId = Thread.currentThread().getId();
                customerCount++;
                customer = "Customer-" + customerCount;
                Random random = new Random();
                String genre = genres[random.nextInt(genres.length)];
                List<Book> section = new ArrayList<Book>();
                Queue<String> waitline = new LinkedList<>();
    
                if (genre == "FICTION"){
                    section = Section.FictionSection;
                    waitline = Section.FictionWaitingLine;
                } else if (genre == "CRIME"){
                    section = Section.CrimeSection;
                    waitline = Section.CrimeWaitingLine;
                } else if (genre == "HORROR"){
                    section = Section.HorrorSection;
                    waitline = Section.HorrorWaitingLine;
                } else if (genre == "ROMANCE"){
                    section = Section.RomanceSection;
                    waitline = Section.RomanceWaitingLine;
                } else if (genre == "SPORT"){
                    section = Section.SportSection;
                    waitline = Section.SportWaitingLine;
                } else if (genre == "FANTASY"){
                    section = Section.FantasySection;
                    waitline = Section.FantasyWaitingLine;
                }
                if (!section.isEmpty() && waitline.isEmpty()) {
                    section.remove(0);
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer + " bought a book from " + genre + " section.");
                } else if (section.isEmpty() || !waitline.isEmpty()) {
                    waitline.add(customer);
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer + " joined the waiting line for " + genre + waitline.toString());
                }
            }
            try {
                Thread.sleep(4 * Main.TICK_TIME_SIZE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Book> section = new ArrayList<Book>();
            Queue<String> waitline = new LinkedList<>();
            for (String genre : genres){
                if (genre == "FICTION"){
                    section = Section.FictionSection;
                    waitline = Section.FictionWaitingLine;
                } else if (genre == "CRIME"){
                    section = Section.CrimeSection;
                    waitline = Section.CrimeWaitingLine;
                } else if (genre == "HORROR"){
                    section = Section.HorrorSection;
                    waitline = Section.HorrorWaitingLine;
                } else if (genre == "ROMANCE"){
                    section = Section.RomanceSection;
                    waitline = Section.RomanceWaitingLine;
                } else if (genre == "SPORT"){
                    section = Section.SportSection;
                    waitline = Section.SportWaitingLine;
                } else if (genre == "FANTASY"){
                    section = Section.FantasySection;
                    waitline = Section.FantasyWaitingLine;
                }
                if (!waitline.isEmpty() && !section.isEmpty()){
                    customer = waitline.remove();
                    section.remove(0);
                    long threadId = Thread.currentThread().getId();
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer + " was waiting but has now bought from " + genre + " section.");
                }
            }
        }
    }
}