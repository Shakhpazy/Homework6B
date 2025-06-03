package app;
import javax.swing.SwingUtilities;

/**
 * Initializes and runs the spreadsheet application.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class Main {
    public static void main(String[] args) {

        Spreadsheet spreadsheet = new Spreadsheet(52,52);
        // Launch the UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new SpreadsheetUI(spreadsheet);
        });
        
    }
} 