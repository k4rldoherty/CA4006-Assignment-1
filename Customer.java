import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Customer implements Runnable {
    private final String[] genres = { "FICTION", "HORROR", "ROMANCE", "FANTASY", "SPORT", "CRIME" };
    static int customerCount;
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
                Queue<String> queue = new LinkedList<>();
    
                if (genre == "FICTION"){
                    section = Section.fictionSection;
                    queue = Section.fictionQueue;
                } else if (genre == "CRIME"){
                    section = Section.crimeSection;
                    queue = Section.crimeQueue;
                } else if (genre == "HORROR"){
                    section = Section.horrorSection;
                    queue = Section.horrorQueue;
                } else if (genre == "ROMANCE"){
                    section = Section.romanceSection;
                    queue = Section.romanceQueue;
                } else if (genre == "SPORT"){
                    section = Section.sportSection;
                    queue = Section.sportQueue;
                } else if (genre == "FANTASY"){
                    section = Section.fantasySection;
                    queue = Section.fantasyQueue;
                }
                if (!section.isEmpty() && queue.isEmpty()) {
                    section.remove(0);
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer + " bought a book from " + genre + " section.");
                } else if (section.isEmpty() || !queue.isEmpty()) {
                    queue.add(customer);
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer + " joined the waiting line for " + genre);
                }
            }
            try {
                Thread.sleep(4 * Main.TICK_TIME_SIZE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Book> section = new ArrayList<Book>();
            Queue<String> queue = new LinkedList<>();
            for (String genre : genres){
                if (genre == "FICTION"){
                    section = Section.fictionSection;
                    queue = Section.fictionQueue;
                } else if (genre == "CRIME"){
                    section = Section.crimeSection;
                    queue = Section.crimeQueue;
                } else if (genre == "HORROR"){
                    section = Section.horrorSection;
                    queue = Section.horrorQueue;
                } else if (genre == "ROMANCE"){
                    section = Section.romanceSection;
                    queue = Section.romanceQueue;
                } else if (genre == "SPORT"){
                    section = Section.sportSection;
                    queue = Section.sportQueue;
                } else if (genre == "FANTASY"){
                    section = Section.fantasySection;
                    queue = Section.fantasyQueue;
                }
                if (!queue.isEmpty() && !section.isEmpty()){
                    customer = queue.remove();
                    section.remove(0);
                    long threadId = Thread.currentThread().getId();
                    System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer + " was waiting but has now bought from " + genre + " section.");
                }
            }
        }
    }
}