import java.util.*;

public class Box implements Runnable {
    static int space = 200;
    public static List<Books> BooksInBox = new ArrayList<Books>();
    static Box Box_1;
    static Box box = CreateNewBox();
    static Delivery delivery = new Delivery();

    // Need Function to print contents of Box
    public static List<Books> getBooks() {
        List<Books> books = new ArrayList<>();

        synchronized (BooksInBox) {
            // Wait until the box has books
            while (BooksInBox.isEmpty()) {
                try {
                    BooksInBox.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Take up to 10 books from the box
            Iterator<Books> iterator = BooksInBox.iterator();
            int count = 0;
            while (iterator.hasNext() && count < 10) {
                Books book = iterator.next();
                iterator.remove();
                books.add(book);
                count++;
            }
        }

        return books;
    }

    public List<Books> FillBox(List<Books> DeliveryList) {
        int i = 0;
        if (DeliveryList.size() != 0) {
            while (i < DeliveryList.size()) {
                if (BooksInBox.size() == space) {
                    return BooksInBox;
                }
                Books x = DeliveryList.get(i);

                BooksInBox.add(x);

                i++;
            }
        }

        return BooksInBox;
    }

    public int size() {
        int Size = BooksInBox.size();
        return Size;
    }

    public static Box CreateNewBox() {
        Box box = new Box();
        return box;
    }

    @Override
    public String toString() {
        return "" + BooksInBox;
    }

    public static void main(String[] args) {
        Box box = CreateNewBox();
        Delivery Delivery = new Delivery();
        List<Books> delivery_1 = Delivery.GenerateDelivery();
        box.FillBox(delivery_1);
    }

    @Override
    public void run() {
        List<Books> deliveredContents1 = Delivery.GenerateDelivery();
        box.FillBox(new ArrayList<>(deliveredContents1));
        deliveredContents1.clear();
    }

}
