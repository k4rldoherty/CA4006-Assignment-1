import java.util.ArrayList;
import java.util.List;

public class Assistant extends Thread {

    private Bookstore bookstore;
    private List<Book> books;
    private int ticksPerStep = 1; // default value

    public Assistant(Bookstore bookstore) {
        this.bookstore = bookstore;
        books = new ArrayList<>();
    }

    public void setTicksPerStep(int ticksPerStep) {
        this.ticksPerStep = ticksPerStep;
    }

    public void run() {
        while (true) {
            // Check if there are any books in the box
            Box box = bookstore.getBox();
            if (box.isEmpty()) {
                try {
                    sleep(ticksPerStep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            // Take up to 10 books from the box
            int numBooks = Math.min(10, box.getNumBooks());
            books = box.getBooks(numBooks);

            // Find a section to stock the books in
            for (Book book : books) {
                Section section = bookstore.getSection(book.getGenre());
                section.addBook(book);
            }

            // Wait for the appropriate amount of time
            int ticksToWait = 10 + books.size();
            try {
                sleep(ticksToWait * ticksPerStep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}