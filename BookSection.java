import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookSection {
    private String id;
    private List<Book> books;

    public BookSection(String id) {
        this.id = id;
        books = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public synchronized boolean hasBook() {
        return !books.isEmpty();
    }

    public synchronized void addBook(Book book) {
        books.add(book);
    }

    public synchronized Book takeBook() {
        if (books.isEmpty()) {
            return null;
        }
        return books.remove(0);
    }

    public Book getRandomBook() {
        if (books.isEmpty()) {
            return null;
        }
        int index = new Random().nextInt(books.size());
        return books.get(index);
    }

    public synchronized void buyBook() {
        while (books.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        books.remove(0);
        System.out.println("Customer bought a book from section " + id + ", total stock: " + books.size());
        notifyAll();
    }
}