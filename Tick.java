
// Creating a Tick Class to manage the passing of Time and Statistics for the EOD in the Bookstore
public class Tick implements Runnable {
    private Box box;
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
                e.printStackTrace();
            }
            long threadId = Thread.currentThread().getId(); // get current threadID

            String isDelivery = Delivery.NextDeliveryTime(); // run NextDeliveryTime in delivery file that returns
                                                             // either True or False
            if (isDelivery == "True") { // if what is returned is True
                Thread boxThread = new Thread(box); // create a new thread for the box
                System.out.println("<" + Main.tickCount + "><" + threadId + ">" + "New Delivery!");
                boxThread.run(); // run the box thread and have it stop when it is finished
                System.out.println("<" + Main.tickCount + "><" + threadId + ">" + "Stock Box: " + Box.box);
                DeliveryCount++; // increment delivery counter
            }

            Main.tickCount++; // increment the tickCount

            if (Main.tickCount % Main.TICKS_PER_DAY == 0) { // if the tickCount modulo ticks per day (1000) == 0, so
                                                            // every 1000 ticks
                int AverageWaitTime = Customer.WaitTimeAverage(Customer.CustomerWaitTimes); // get the average wait time
                                                                                            // of the customers
                int DeliveryAmount = Tick.DeliveryCount; // get the aount of deliverys done that day
                int TotalCustomers = Customer.customerCount; // get the total customers today
                int TotalCustomersServed = Customer.customerServedCount; // get the total amount of customers served in
                                                                         // a day
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

                System.out.println("End of day stats: ");
                System.out.println("Total Customers: " + CustomersInDay);
                System.out.println("Customers Served: " + CustomersServedInDay);
                System.out.println("Deliveries: " + DeliveryAmount);
                System.out.println("Average wait time: " + AverageWaitTime);

                Customer.ClearWaitTime(Customer.CustomerWaitTimes); // reset the wait time
                Tick.DeliveryCount = 0; // reset the delivery count
            }

        }
    }

    public static void main(String[] args) {

    }
}