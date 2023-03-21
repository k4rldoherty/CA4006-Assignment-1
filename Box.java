import java.util.*;

public class Box implements Runnable {
    public static List<Book> boxBooks = new ArrayList<Book>();
    static Box box = new Box();


    public static List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        synchronized (boxBooks) {
            while (boxBooks.isEmpty()) {
                try {
                    boxBooks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int size = Math.min(boxBooks.size(), 10);
            books.addAll(boxBooks.subList(0, size));
            boxBooks.subList(0, size).clear();
        }
        return books;
    }

    public List<Book> addToBox(List<Book> DeliveryList) {
        for (Book book : DeliveryList) {
            boxBooks.add(book);
        }
        return boxBooks;
    }

    public int size() {
        int Size = boxBooks.size();
        return Size;
    }

    @Override
    public String toString() {
        return boxBooks.toString();
    }

    @Override
    public void run() {
        List<Book> delivery = Delivery.getNewDelivery();
        box.addToBox(new ArrayList<>(delivery));
    }
}
