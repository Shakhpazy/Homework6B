package app;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class SpreadsheetUI extends JFrame {
    // UI Components
    private JTable table;
    private JToolBar toolbar;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    
    // Data
    private Spreadsheet spreadsheet;
    
    public SpreadsheetUI(Spreadsheet theSpreadsheet) {
        this.spreadsheet = theSpreadsheet;
        spreadsheet.setUI(this);
        initializeUI();
    }

    private void initializeUI() {
        setupFrame();
        setupTable();
        setupToolbar();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Excel Clone");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupToolbar() {
        toolbar = new JToolBar();
        toolbar.setPreferredSize(new Dimension(800, 30));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
        toolbar.setBackground(Color.LIGHT_GRAY);
        
        JButton saveButton = createSaveButton();
        toolbar.add(saveButton);

        JButton clearButton = createClearButton();
        toolbar.add(clearButton);

        
        add(toolbar, BorderLayout.NORTH);
    }

    private JButton createClearButton() {
        JButton clearButton = new JButton("Clear");
        Dimension buttonSize = new Dimension(80, 50);
        clearButton.setPreferredSize(buttonSize);
        clearButton.setMaximumSize(buttonSize);
        clearButton.setMinimumSize(buttonSize);
        clearButton.setBackground(Color.WHITE);
        clearButton.setForeground(Color.BLACK);
        clearButton.addActionListener(e -> {
            System.out.println("Clearing spreadsheet");
            spreadsheet.clear();
        });
        return clearButton;
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        Dimension buttonSize = new Dimension(80, 50);
        saveButton.setPreferredSize(buttonSize);
        saveButton.setMaximumSize(buttonSize);
        saveButton.setMinimumSize(buttonSize);
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);
        saveButton.addActionListener(e -> {
            System.out.println("Saving spreadsheet");
            spreadsheet.save();
        });
        return saveButton;
    }

    private void setupTable() {
        createTableModel();
        createTable();
        createScrollPane();
        createRowHeader();
    }

    private void createTableModel() {
        String[] columns = createColumnHeaders();
        model = new DefaultTableModel(columns, spreadsheet.getNumberOfRows()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
            
            @Override
            public void setValueAt(Object value, int row, int col) {
                String input = ((String)value).trim().toUpperCase();
                String current = spreadsheet.getCellFormula(row, col);
                
                if (!input.equals(current)) {
                    // Always treat input as potential formula
                    spreadsheet.setCellFormula(row, col, input);
                }
            }

            @Override
            public Object getValueAt(int row, int col) {
                Cell cell = spreadsheet.getCell(row, col);
                return cell.getFormula().isEmpty() ? "" : String.valueOf(cell.getValue());
            }
        };
    }

    private String[] createColumnHeaders() {
        String[] columns = new String[spreadsheet.getNumberOfColumns()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = getColumnName(i);
        }
        return columns;
    }

    private String getColumnName(int index) {
        StringBuilder sb = new StringBuilder();
        while (index >= 0) {
            sb.insert(0, (char)('A' + (index % 26)));
            index = index / 26 - 1;
        }
        return sb.toString();
    }

    private void createTable() {
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setReorderingAllowed(false);
    }

    private void createScrollPane() {
        scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    private void createRowHeader() {
        JList<String> rowHeader = new JList<>(new AbstractListModel<String>() {
            public int getSize() { return spreadsheet.getNumberOfRows(); }
            public String getElementAt(int index) { return String.valueOf(index + 1); }
        });
        
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setBackground(Color.WHITE);
        rowHeader.setFont(rowHeader.getFont().deriveFont(Font.BOLD));
        
        rowHeader.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, 
                        isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
                return label;
            }
        });
        
        scrollPane.setRowHeaderView(rowHeader);
    }

    public void refreshTable() {
        if (model != null) {
            model.fireTableDataChanged();
        }
    }
}