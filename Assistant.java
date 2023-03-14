import java.util.List;

public class Assistant implements Runnable {
    private List<BookSection> sections;
    private Box box;

    public Assistant(List<BookSection> sections, Box box) {
        this.sections = sections;
        this.box = box;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Book book = getRandomBook();
                BookSection section = getRandomSection();
                if (book != null) {
                    System.out.println("Assistant found book: " + book.getId() + " in section: " + section.getId());
                    box.addDelivery(new Delivery(book));
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Book getRandomBook() {
        BookSection section = getRandomSection();
        if (section != null) {
            return section.getRandomBook();
        }
        return null;
    }

    private BookSection getRandomSection() {
        int sectionIndex = (int) (Math.random() * sections.size());
        return sections.get(sectionIndex);
    }
}