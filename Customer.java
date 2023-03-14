import java.util.*;

public class Customer implements Runnable {
    private BookSection[] sections;
    private int[] sectionBookCounts;
    private Random rand;

    public Customer(List<BookSection> sections) {
        this.sections = sections.toArray(new BookSection[0]);
        this.sectionBookCounts = new int[sections.size()];
        this.rand = new Random();
    }

    // @Override
    // public void run() {
    // while (true) {
    // try {
    // Thread.sleep(getRandomTime(10));
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // int sectionIndex = rand.nextInt(sections.length);
    // synchronized (sections[sectionIndex]) {
    // while (sectionBookCounts[sectionIndex] == 0) {
    // try {
    // sections[sectionIndex].wait();
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // sectionBookCounts[sectionIndex]--;
    // System.out.println(Thread.currentThread().getName() + " bought a book from "
    // + sections[sectionIndex].getId());
    // }
    // }
    // }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(getRandomTime(10)); // simulate browsing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int sectionIndex = rand.nextInt(sections.length);
            synchronized (sections[sectionIndex]) {
                while (sectionBookCounts[sectionIndex] == 0) {
                    try {
                        sections[sectionIndex].wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // simulate selecting a book
                int bookIndex = rand.nextInt(sectionBookCounts[sectionIndex]);
                sectionBookCounts[sectionIndex]--;
                System.out.println(
                        Thread.currentThread().getName() + " bought a book from " + sections[sectionIndex].getId());

                // simulate purchasing the book
                try {
                    Thread.sleep(getRandomTime(5)); // simulate checkout time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " checked out a book from "
                        + sections[sectionIndex].getId());
            }
        }
    }

    private int getRandomTime(int averageTime) {
        int time = (int) (-Math.log(1 - rand.nextDouble()) * averageTime);
        return time;
    }

    public void updateBookCount(int sectionIndex, int numBooks) {
        sectionBookCounts[sectionIndex] += numBooks;
        sections[sectionIndex].notifyAll();
    }
}
