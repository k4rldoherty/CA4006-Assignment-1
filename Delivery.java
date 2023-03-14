import java.util.ArrayList;
import java.util.List;

public class Delivery {
    private List<BookSection> sections;
    private Book book;

    
    public Delivery(List<BookSection> sections) {
        this.sections = sections;
    }

    public Delivery(Book book) {
        this.book = book;
        this.sections = null;
    }
    
    public List<BookSection> getSections() {
        return sections;
    }
}
