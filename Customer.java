import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

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

    // Declaring a function to generate randomness for determining a genre the
    // customer wishes to buy
    public Customer() {
        // this.bookstore = bookstore;
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
    // Probability of 1/10 or 10% is achieved by adding 9 counts of false to a List and one count of True
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

    // introducing Functionality so a Customer can take a book from a shelf and
    // purchase it
    public static void takeBook(String genre) {
        if (genre == "fiction") {
            Shelve.FictionShelf.remove(0);
        }
        if (genre == "horror") {
            Shelve.HorrorShelf.remove(0);

        }
        if (genre == "romance") {
            Shelve.RomanceShelf.remove(0);
        }
        if (genre == "fantasy") {
            Shelve.FantasyShelf.remove(0);

        }
        if (genre == "crime") {
            Shelve.CrimeShelf.remove(0);

        }
        if (genre == "sport") {
            Shelve.SportShelf.remove(0);

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
                        // First of three possible events when a custoemr comes this one that the Shelve isn't empty but the Waiting Line is.
                        if (!Shelve.FictionShelf.isEmpty() && Shelve.FictionWaitingLine.isEmpty()) {
                            // Take the Book from the shelf
                            takeBook(genre);
                            // Add the finishing time to a String along with the start time and customer name
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            // Split this string into parts
                            String[] parts = customer_and_end_time.split(":");
                            // Setting a String to contain Arrival Time and Departure Time
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            // Sending the strings to a function to calculate the wait time
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            // Adding the customer wait time to a list to be used to calculate average wait time for the day
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section." + "The Customer spent " + WaitTime
                                    + " shopping");
                        } // This is the second possible event a customer arrives and the Shelf isn't empty byt the line isn't empty either 
                        else if (!Shelve.FictionShelf.isEmpty() && !Shelve.FictionWaitingLine.isEmpty()) {
                            // Adding the current customer to the Waiting Queue
                            Shelve.CustomerWaitingLine(Shelve.FictionWaitingLine, customer_and_start_time);
                            // Get the customer who is first in the Line
                            String customer_in_queue = Shelve.FictionWaitingLine.remove();
                            // Split this string into parts
                            String[] parts = customer_in_queue.split(":");
                            // Setting a String to contain Arrival Time and Departure Time
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            //Take the Book
                            takeBook(genre);
                            // Get Departure Times as Integers
                            String DepartureTime = Integer.toString(Main.tickCount);
                            // Calculate the Wait Time of the customer
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            // Add this wait time to a list for later calculation
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section." + " The Customer spent " + WaitTime + " shopping.");
                        } // This is the third case where the Shelf is empty
                        else if (Shelve.FictionShelf.isEmpty()) {
                            // The Customer is added to the appropriate waiting line
                            Shelve.CustomerWaitingLine(Shelve.FictionWaitingLine, customer_and_start_time);
                            System.out
                                    .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                                            + " joined the waiting line for "
                                            + genre);
                        }

                        break;
                    // These are the same as above just for different genres
                    case "fantasy":
                        if (!Shelve.FantasyShelf.isEmpty() && Shelve.FantasyWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section." + " The Customer spent " + WaitTime
                                    + " shopping.");

                        } else if (!Shelve.FantasyShelf.isEmpty() && !Shelve.FantasyWaitingLine.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.FantasyWaitingLine, customer_and_start_time);
                            String customer_in_queue = Shelve.FantasyWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section." + " The Customer spent " + WaitTime + " shopping");
                        } else if (Shelve.FantasyShelf.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.FantasyWaitingLine, customer_and_start_time);
                            System.out
                            .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                            + " joined the waiting line for "
                            + genre);
                        }

                        break;

                    case "crime":
                        if (!Shelve.CrimeShelf.isEmpty() && Shelve.CrimeWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section." + "The Customer spent " + WaitTime
                                    + " shopping");
                        } else if (!Shelve.CrimeShelf.isEmpty() && !Shelve.CrimeWaitingLine.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.CrimeWaitingLine, customer_and_start_time);
                            String customer_in_queue = Shelve.CrimeWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section." + " The Customer spent " + WaitTime + " shopping.");
                        } else if (Shelve.CrimeShelf.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.CrimeWaitingLine, customer_and_start_time);
                            System.out
                            .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                            + " joined the waiting line for "
                            + genre);
                        }

                        break;

                    case "romance":
                        if (!Shelve.RomanceShelf.isEmpty() && Shelve.RomanceWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section." + "The Customer spent " + WaitTime
                                    + " shopping");
                        } else if (!Shelve.RomanceShelf.isEmpty() && !Shelve.RomanceWaitingLine.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.RomanceWaitingLine, customer_and_start_time);
                            String customer_in_queue = Shelve.RomanceWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section." + " The Customer spent " + WaitTime + " shopping.");
                        } else if (Shelve.RomanceShelf.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.RomanceWaitingLine, customer_and_start_time);
                            System.out
                            .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                            + " joined the waiting line for "
                            + genre);
                        }

                        break;

                    case "horror":
                        if (!Shelve.HorrorShelf.isEmpty() && Shelve.HorrorWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section." + "The Customer spent " + WaitTime
                                    + " shopping");
                        } else if (!Shelve.HorrorShelf.isEmpty() && !Shelve.HorrorWaitingLine.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.HorrorWaitingLine, customer_and_start_time);
                            String customer_in_queue = Shelve.HorrorWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section." + " The Customer spent " + WaitTime + " shopping.");
                        } else if (Shelve.HorrorShelf.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.HorrorWaitingLine, customer_and_start_time);
                            System.out
                            .println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer
                            + " joined the waiting line for "
                            + genre);
                        }

                        break;

                    case "sport":
                        if (!Shelve.SportShelf.isEmpty() && Shelve.SportWaitingLine.isEmpty()) {
                            takeBook(genre);
                            String customer_and_end_time = customer_and_start_time + ":" + Main.tickCount;
                            String[] parts = customer_and_end_time.split(":");
                            String ArrivalTime = parts[1];
                            String DepartureTime = parts[2];
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer +
                                    " bought a book from " + genre + " section." + "The Customer spent " + WaitTime
                                    + " shopping");
                        } else if (!Shelve.SportShelf.isEmpty() && Shelve.SportWaitingLine.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.SportWaitingLine, customer_and_start_time);
                            String customer_in_queue = Shelve.SportWaitingLine.remove();
                            String[] parts = customer_in_queue.split(":");
                            String customer_to_serve = parts[0];
                            String ArrivalTime = parts[1];
                            takeBook(genre);
                            String DepartureTime = Integer.toString(Main.tickCount);
                            int WaitTime = WaitTime(ArrivalTime, DepartureTime);
                            WaitTimeList(WaitTime);
                            System.out.println("<" + Main.tickCount + ">" + "<" + threadId + ">" + customer_to_serve
                                    + " bought a book from "
                                    + genre + " section." + " The Customer spent " + WaitTime + " shopping.");
                        } else if (Shelve.SportShelf.isEmpty()) {
                            Shelve.CustomerWaitingLine(Shelve.SportWaitingLine, customer_and_start_time);
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