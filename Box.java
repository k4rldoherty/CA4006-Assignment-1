import java.util.*;

public class Box implements Runnable {
    public static List<Book> BooksInBox = new ArrayList<Book>();
    static Box box = new Box();


    public static List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        synchronized (BooksInBox) {
            while (BooksInBox.isEmpty()) {
                try {
                    BooksInBox.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Iterator<Book> iterator = BooksInBox.iterator();
            int count = 0;
            while (iterator.hasNext() && count < 10) {
                Book book = iterator.next();
                iterator.remove();
                books.add(book);
                count++;
            }
        }
        return books;
    }

    public List<Book> FillBox(List<Book> DeliveryList) {
        for (Book book : DeliveryList) {
            BooksInBox.add(book);
        }
        return BooksInBox;
    }

    public int size() {
        int Size = BooksInBox.size();
        return Size;
    }

    @Override
    public String toString() {
        return BooksInBox.toString();
    }

    @Override
    public void run() {
        List<Book> delivery = Delivery.getNewDelivery();
        box.FillBox(new ArrayList<>(delivery));
    }
}
