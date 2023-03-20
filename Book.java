import java.util.Random;

public class Book {
    private static final String[] CATEGORIES = {"Fiction", "Crime", "Fantasy", "Romance", "Horror", "Sport"};
    private static final Random RANDOM = new Random();
    private String category;

    public Book() {
        this.category = CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];
    }

    @Override
    public String toString() {
        return category;
    }
}