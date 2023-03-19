import java.util.*;

public class Section {
    static int capacity = 100;
    static List<Books> FictionSection = new ArrayList<Books>();
    static List<Books> HorrorSection = new ArrayList<Books>();
    static List<Books> SportSection = new ArrayList<Books>();
    static List<Books> FantasySection = new ArrayList<Books>();
    static List<Books> RomanceSection = new ArrayList<Books>();
    static List<Books> CrimeSection = new ArrayList<Books>();
    static Queue<String> FictionWaitingLine = new LinkedList<>();
    static Queue<String> HorrorWaitingLine = new LinkedList<>();
    static Queue<String> SportWaitingLine = new LinkedList<>();
    static Queue<String> FantasyWaitingLine = new LinkedList<>();
    static Queue<String> RomanceWaitingLine = new LinkedList<>();
    static Queue<String> CrimeWaitingLine = new LinkedList<>();

    public static void AddBooksToShelves(Books book) {

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