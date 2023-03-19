import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Introducing a customer class to handle the functionality of a customer on a thread
public class Customer implements Runnable {
    // Introducing variables to be accessed by other classes and the Customer class
    // itself
    private final String[] genres = { "fiction", "horror", "romance", "fantasy", "sport", "crime" };
    private final Random random;
    static int customerCount;
    static int customerServedCount;
    private String customer;
    static List<Integer> CustomerWaitTimes = new ArrayList<>();

    public Customer() {
        this.random = new Random();
    }

    // Introducing a function which takes two strings the Arrival Time and Departure
    // Time of a customer and changing them to ints to calculate Wait Time
    public static int WaitTime(String ArrivalTime, String DepartureTime) {
        Integer Arrival = Integer.parseInt(ArrivalTime);

        Integer Departure = Integer.parseInt(DepartureTime);

        int WaitTime = Departure - Arrival;

        return WaitTime;
    }

    // Introducing a function which takes an integer and adds the Intger to a list
    // of Integers which it returns, the list it is added to is a global class
    // variable
    public static List<Integer> WaitTimeList(int Time) {
        CustomerWaitTimes.add(Time);

        return CustomerWaitTimes;
    }

    // Introducing functionality to clear the global List, which is passed to the
    // function
    // This is needed to ensure the List only contains the customers that left
    // during that particular day
    public static List<Integer> ClearWaitTime(List<Integer> WaitTime) {
        WaitTime.clear();

        return WaitTime;
    }

    // A function to claculate the Average Wait Time of customers, it does this by
    // summing the values in the List and dividing it by the size of the List
    public static int WaitTimeAverage(List<Integer> WaitTimes) {
        int i = 0;
        int sum_of_wait_time = 0;

        while (i < WaitTimes.size()) {
            sum_of_wait_time += WaitTimes.get(i);

            i++;
        }

        // Introducing an Error Boundary to prevent Average being attemped if the list
        // is empty
        if (WaitTimes.size() != 0) {

            int AverageWaitTime = sum_of_wait_time / WaitTimes.size();

            return AverageWaitTime;
        }

        else {
            return 0;
        }
    }

    // Introducing Functionality to determine whether a customer comes after a tick
    // Probability of 1/10 or 10% is achieved by adding 9 counts of false to a List
    // and one count of True
    public static String CustomerProbability() {
        List<String> ProbabilityOfCustomer = new ArrayList<String>();
        var i = 0;
        while (i < 10) {
            if (i != 9) {
                ProbabilityOfCustomer.add("False");
            } else {
                ProbabilityOfCustomer.add("True");
            }
            i++;
        }

        int UpperRange = ProbabilityOfCustomer.size();

        Random rand = new Random();
        int Index = rand.nextInt(UpperRange);

        String IsDelivery = ProbabilityOfCustomer.get(Index);

        return IsDelivery;
    }

    // introducing Functionality so a Customer can take a book from a section and
    // purchase it
    public static void takeBook(String genre) {
        if (genre == "fiction") {
            Section.FictionSection.remove(0);
        }
        if (genre == "horror") {
            Section.HorrorSection.remove(0);

        }
        if (genre == "romance") {
            Section.RomanceSection.remove(0);
        }
        if (genre == "fantasy") {
            Section.FantasySection.remove(0);

        }
        if (genre == "crime") {
            Section.CrimeSection.remove(0);

        }
        if (genre == "sport") {
            Section.SportSection.remove(0);

        }

        customerServedCount++;
    }

    // The main runner code that runs when the Thread is started
    @Override
    public void run() {
        // Infinite Loop
        while (true) {
            // Try to sleep the thread for 10 ticks
            try {
                Thread.sleep(1 * Main.TICK_TIME_SIZE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String isCustomer = Customer.CustomerProbability();
            if (isCustomer == "True") {
                // Setting the thread Id to a variable
                long threadId = Thread.currentThread().getId();
                // Introducing a Customer count to ensure customers are uniquly identifiable.
                customerCount++;
                customer = "Customer-" + customerCount;
                // Appending the customer name along with arrival time to a string
                String customer_and_start_time = customer + ":" + Main.tickCount;
                // Getting the next customers genre wanted
                String genre = genres[random.nextInt(genres.length)];

                // Introduciong a switch based off the customers genre
                switch (genre) {
                    // If the genre is fiction
                    case "fiction":
                        // First of three possible events when a custoemr comes this one that the Section
                        // isn't empty but the Waiting Line is.
                        if (!Section.FictionSection.isEmpty() && Section.FictionWaitingLine.isEmpty()) {
                            // Take the Book from the section
                            takeBook(genre);
                            // Add the finishing time to a String along with the start time and customer
                            // name
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            // Split this string into parts
                            String[] parts = customer_and_end_time.split(":");
                            // Setting a String to contain Arrival Time and Departure Time
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            // Sending the strings to a function to calculate the wait time
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            // Adding the customer wait time to a list to be used to calculate average wait
                            // time for the day
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section.");
                        } // This is the second possible event a customer arrives and the Section isn't
                          // empty by the line isn't empty either
                        else if (!Section.FictionSection.isEmpty() && !Section.FictionWaitingLine.isEmpty()) {
                            // Adding the current customer to the Waiting Queue
                            Section.CustomerWaitingLine(Section.FictionWaitingLine, customer_and_start_time);
                            // Get the customer who is first in the Line
                            String customer_in_queue = Section.FictionWaitingLine.remove();
                            // Split this string into parts
                            String[] parts = customer_in_queue.split(":");
                            // Setting a String to contain Arrival Time and Departure Time
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            // Take the Book
                            takeBook(genre);
                            // Get Departure Times as Integers
                            String DepartureTime = Integer.toString(Main.tickCount);
                            // Calculate the Wait Time of the customer
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            // Add this wait time to a list for later calculation
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section.");
                        } // This is the third case where the Section is empty
                        else if (Section.FictionSection.isEmpty()) {
                            // The Customer is added to the appropriate waiting line
                            Section.CustomerWaitingLine(Section.FictionWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;
                    // These are the same as above just for different genres
                    case "fantasy":
                        if (!Section.FantasySection.isEmpty() && Section.FantasyWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section.");

                        } else if (!Section.FantasySection.isEmpty() && !Section.FantasyWaitingLine.isEmpty()) {
                            Section.CustomerWaitingLine(Section.FantasyWaitingLine, customer_and_start_time);
                            String customer_in_queue = Section.FantasyWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section.");
                        } else if (Section.FantasySection.isEmpty()) {
                            Section.CustomerWaitingLine(Section.FantasyWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;

                    case "crime":
                        if (!Section.CrimeSection.isEmpty() && Section.CrimeWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section.");
                        } else if (!Section.CrimeSection.isEmpty() && !Section.CrimeWaitingLine.isEmpty()) {
                            Section.CustomerWaitingLine(Section.CrimeWaitingLine, customer_and_start_time);
                            String customer_in_queue = Section.CrimeWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section.");
                        } else if (Section.CrimeSection.isEmpty()) {
                            Section.CustomerWaitingLine(Section.CrimeWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;

                    case "romance":
                        if (!Section.RomanceSection.isEmpty() && Section.RomanceWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section.");
                        } else if (!Section.RomanceSection.isEmpty() && !Section.RomanceWaitingLine.isEmpty()) {
                            Section.CustomerWaitingLine(Section.RomanceWaitingLine, customer_and_start_time);
                            String customer_in_queue = Section.RomanceWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section.");
                        } else if (Section.RomanceSection.isEmpty()) {
                            Section.CustomerWaitingLine(Section.RomanceWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;

                    case "horror":
                        if (!Section.HorrorSection.isEmpty() && Section.HorrorWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section.");
                        } else if (!Section.HorrorSection.isEmpty() && !Section.HorrorWaitingLine.isEmpty()) {
                            Section.CustomerWaitingLine(Section.HorrorWaitingLine, customer_and_start_time);
                            String customer_in_queue = Section.HorrorWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section.");
                        } else if (Section.HorrorSection.isEmpty()) {
                            Section.CustomerWaitingLine(Section.HorrorWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;

                    case "sport":
                        if (!Section.SportSection.isEmpty() && Section.SportWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section.");
                        } else if (!Section.SportSection.isEmpty() && Section.SportWaitingLine.isEmpty()) {
                            Section.CustomerWaitingLine(Section.SportWaitingLine, customer_and_start_time);
                            String customer_in_queue = Section.SportWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section.");
                        } else if (Section.SportSection.isEmpty()) {
                            Section.CustomerWaitingLine(Section.SportWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;
                }
            }
        }
    }
}