import java.io.*;

public class RedirectOutput {
    public static void main(String[] args) {
        PrintStream originalOut = System.out;  // Save original System.out

        try {
            PrintStream fileOut = new PrintStream("./output.txt");  // Open file for writing
            System.setOut(fileOut);  // Redirect System.out to file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // ... your code here ...

        System.out.flush();  // Make sure all output is written to file
        System.setOut(originalOut);  // Restore original System.out
    }
}