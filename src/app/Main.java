package app;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        Spreadsheet spreadsheet = new Spreadsheet(52,52);
        // Launch the UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new SpreadsheetUI(spreadsheet);
        });
        
    }
} 