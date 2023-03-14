public class Book {
    private int id;
    private int sectionId;

    public Book(int id, int sectionId) {
        this.id = id;
        this.sectionId = sectionId;
    }

    public int getId() {
        return id;
    }

    public int getSectionId() {
        return sectionId;
    }
}