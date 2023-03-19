import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Creating a Tick Class to manage the passing of Time and Statistics for the EOD in the Bookstore
public class Tick implements Runnable {
    private Box box;
    private Random random = new Random();
    public static boolean deliveryRecieved = false;
    public static int DeliveryCount = 0;
    public static int CustomersPrior = 0;
    public static int CustomersServedPrior = 0;

    public Tick(Box box) {
        this.box = box;
    }

    // Runner Code for the Thread
    @Override
    public void run() {

        while (true) {
                try {
                    Thread.sleep(1 * Main.TICK_TIME_SIZE); // sleep for one tick
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long threadId = Thread.currentThread().getId(); // get current threadID

                String isDelivery = Delivery.NextDeliveryTime(); // run NextDeliveryTime in delivery file that returns either True or False
                if (isDelivery == "True") { // if what is returned is True
                    Thread boxThread = new Thread(box); // create a new thread for the box
                    System.out.println("<" + Main.tickCount + "> <" + threadId + ">" + "Recieved a delivery!");
                    boxThread.run(); // run the box thread and have it stop when it is finished
                    System.out.println("<" + Main.tickCount + "> <" + threadId + ">" + " Box: " + Box.box); 
                    DeliveryCount++; // increment delivery counter
                }
                
                Main.tickCount++; // increment the tickCount of the program

                if (Main.tickCount % Main.TICKS_PER_DAY == 0) { // if the tickCount modulo ticks per day (1000) == 0, so every 1000 ticks
                    int AverageWaitTime = Customer.WaitTimeAverage(Customer.CustomerWaitTimes); // get the average wait time of the customers
                    int DeliveryAmount = Tick.DeliveryCount; // get the aount of deliverys done that day
                    int TotalCustomers = Customer.customerCount; // get the total customers today
                    int TotalCustomersServed = Customer.customerServedCount; // get the total amount of customers served in a day
                    int CustomersInDay = 0;
                    int CustomersServedInDay = 0;


                    if (Main.tickCount < Main.TICKS_PER_DAY) { // gets the customers from the first day
                        CustomersInDay = TotalCustomers; 
                        CustomersServedInDay = TotalCustomersServed;
                    }  

                    else { // gets the customers from every other day after the first day
                        CustomersInDay = TotalCustomers - CustomersPrior;
                        CustomersServedInDay = TotalCustomersServed - CustomersServedPrior;
                    }

                    CustomersPrior = Customer.customerCount;
                    CustomersServedPrior = Customer.customerServedCount;

                    System.out.println("It is the End of the Day here are the statistics for the Day: ");
                    System.out.println("There was this many customers visting today: " + CustomersInDay);
                    System.out.println("There was this many customers served today: " + CustomersServedInDay);
                    System.out.println("There was this many deliverys: " + DeliveryAmount);
                    System.out.println("The Average Wait Time of Customers was: " + AverageWaitTime);
        
                    Customer.ClearWaitTime(Customer.CustomerWaitTimes); // reset the wait time
                    Tick.DeliveryCount = 0; // reset the delivery count
                }
        
        }
    }

    public static void main(String[] args) {

    }
}