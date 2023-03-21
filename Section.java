import java.util.*;

public class Section {
    static int capacity = 30;
    static List<Book> fictionSection = new ArrayList<Book>();
    static List<Book> horrorSection = new ArrayList<Book>();
    static List<Book> sportSection = new ArrayList<Book>();
    static List<Book> fantasySection = new ArrayList<Book>();
    static List<Book> romanceSection = new ArrayList<Book>();
    static List<Book> crimeSection = new ArrayList<Book>();
    static Queue<String> fictionQueue = new LinkedList<>();
    static Queue<String> horrorQueue = new LinkedList<>();
    static Queue<String> sportQueue = new LinkedList<>();
    static Queue<String> fantasyQueue = new LinkedList<>();
    static Queue<String> romanceQueue = new LinkedList<>();
    static Queue<String> crimeQueue = new LinkedList<>();

    public static void AddBooksToShelves(Book book) {

        if (book.toString().equals("Fiction")) {
            if (fictionSection.size() < capacity) {
                fictionSection.add(book);
            } else {
                System.out.println("Fiction Capacity Reached, Returning to stock box");
                Box.boxBooks.add(book);
            }
        } else if (book.toString().equals("Horror")) {
            if (horrorSection.size() < capacity) {
                horrorSection.add(book);
            } else {
                System.out.println("Horror Capacity Reached, Returning to stock box");
                Box.boxBooks.add(book);
            }
        } else if (book.toString().equals("Sport")) {
            if (sportSection.size() < capacity) {
                sportSection.add(book);
            } else {
                System.out.println("Sport Capacity Reached, Returning to stock box");
                Box.boxBooks.add(book);
            }
        } else if (book.toString().equals("Fantasy")) {
            if (fantasySection.size() < capacity) {
                fantasySection.add(book);
            } else {
                System.out.println("FantasyCapacity Reached, Returning to stock box");
                Box.boxBooks.add(book);
            }
        } else if (book.toString().equals("Romance")) {
            if (romanceSection.size() < capacity) {
                romanceSection.add(book);
            } else {
                System.out.println("Capacity Reached, Returning to stock box");
                Box.boxBooks.add(book);
            }
        } else if (book.toString().equals("Crime")) {
            if (crimeSection.size() < capacity) {
                crimeSection.add(book);
            } else {
                System.out.println("Capacity Reached, Returning to stock box");
                Box.boxBooks.add(book);
            }
        }
    }
}