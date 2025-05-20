package app;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;

public class SpreadsheetUI extends JFrame {

    private JTable table;
    private Spreadsheet spreadsheet;
    private JToolBar jbar;
    
    public SpreadsheetUI(Spreadsheet theSpreadsheet) {
        spreadsheet = theSpreadsheet;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Excel Clone");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        setTable();
        setJbar();
        setVisible(true);
    }

    public void setJbar() {
        jbar = new JToolBar();
        jbar.setPreferredSize(new Dimension(800, 30));
        jbar.setBackground(Color.black);
        jbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
        add(jbar, BorderLayout.NORTH);
    }

    /** table model found on stackoverflow and modified to fit the needs of the project */
    public void setTable() {
        String[] columns = new String[spreadsheet.getNumberOfColumns()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = String.valueOf((char)('A' + i));
        }
        
        // Create custom table model that connects to Spreadsheet
        DefaultTableModel model = new DefaultTableModel(columns, spreadsheet.getNumberOfRows()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
            
            @Override
            public void setValueAt(Object value, int row, int col) {
                spreadsheet.setCellFormula(row, col, (String)value);
                super.setValueAt(value, row, col);
            }
            
            @Override
            public Object getValueAt(int row, int col) {
                return spreadsheet.getCellFormula(row, col);
            }
        };
        
        // Initialize all cells with their default values
        for (int row = 0; row < spreadsheet.getNumberOfRows(); row++) {
            for (int col = 0; col < spreadsheet.getNumberOfColumns(); col++) {
                model.setValueAt(spreadsheet.getCell(row, col).getFormula(), row, col);
            }
        }
        
        // Add listener for cell changes
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    // Get the value from the spreadsheet directly
                    Object newValue = spreadsheet.getCellFormula(row, col);
                    System.out.println("Cell " + (char)('A' + col) + (row + 1) + " changed to: " + newValue);
                    // Add your custom handling here
                }
            }
        });
        
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(25);
        
        // Style the table
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Style the header
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setReorderingAllowed(false);
        
        // Add row numbers
        JList<String> rowHeader = new JList<>(new AbstractListModel<String>() {
            public int getSize() { return spreadsheet.getNumberOfRows(); }
            public String getElementAt(int index) { return String.valueOf(index + 1); }
        });
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setBackground(Color.WHITE);
        rowHeader.setFont(rowHeader.getFont().deriveFont(Font.BOLD));

        // Center the row numbers
        rowHeader.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeader);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }
}