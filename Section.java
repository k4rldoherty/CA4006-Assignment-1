import java.util.*;

public class Section {
    static int capacity = 30;
    static List<Book> FictionSection = new ArrayList<Book>();
    static List<Book> HorrorSection = new ArrayList<Book>();
    static List<Book> SportSection = new ArrayList<Book>();
    static List<Book> FantasySection = new ArrayList<Book>();
    static List<Book> RomanceSection = new ArrayList<Book>();
    static List<Book> CrimeSection = new ArrayList<Book>();
    static Queue<String> FictionWaitingLine = new LinkedList<>();
    static Queue<String> HorrorWaitingLine = new LinkedList<>();
    static Queue<String> SportWaitingLine = new LinkedList<>();
    static Queue<String> FantasyWaitingLine = new LinkedList<>();
    static Queue<String> RomanceWaitingLine = new LinkedList<>();
    static Queue<String> CrimeWaitingLine = new LinkedList<>();

    public static void AddBooksToShelves(Book book) {

        if (book.toString().equals("Fiction")) {
            if (FictionSection.size() < capacity) {
                FictionSection.add(book);
            } else {
                System.out.println("Fiction Capacity Reached, Returning to stock box");
                Box.BooksInBox.add(book);
            }
        } else if (book.toString().equals("Horror")) {
            if (HorrorSection.size() < capacity) {
                HorrorSection.add(book);
            } else {
                System.out.println("Horror Capacity Reached, Returning to stock box");
                Box.BooksInBox.add(book);
            }
        } else if (book.toString().equals("Sport")) {
            if (SportSection.size() < capacity) {
                SportSection.add(book);
            } else {
                System.out.println("Sport Capacity Reached, Returning to stock box");
                Box.BooksInBox.add(book);
            }
        } else if (book.toString().equals("Fantasy")) {
            if (FantasySection.size() < capacity) {
                FantasySection.add(book);
            } else {
                System.out.println("FantasyCapacity Reached, Returning to stock box");
                Box.BooksInBox.add(book);
            }
        } else if (book.toString().equals("Romance")) {
            if (RomanceSection.size() < capacity) {
                RomanceSection.add(book);
            } else {
                System.out.println("Capacity Reached, Returning to stock box");
                Box.BooksInBox.add(book);
            }
        } else if (book.toString().equals("Crime")) {
            if (CrimeSection.size() < capacity) {
                CrimeSection.add(book);
            } else {
                System.out.println("Capacity Reached, Returning to stock box");
                Box.BooksInBox.add(book);
            }
        }
    }

    public static Queue<String> CustomerWaitingLine(Queue<String> CustomerWaitingLine, String Customer) {

        CustomerWaitingLine.add(Customer);

        return CustomerWaitingLine;
    }

    public static boolean LineEmpty(Queue<String> CustomerWaitingLine) {
        if (CustomerWaitingLine.size() == 0) {
            return true;
        } else {
            return false;
        }

    }
}