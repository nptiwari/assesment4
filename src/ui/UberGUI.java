package ui;

import model.Driver;
import model.Rider;
import model.Trip;
import service.DriverService;
import service.RiderService;
import service.TripService;
import service.StatsService;
import util.AlgorithmUtils;
import util.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class UberGUI extends JFrame {
    
    private static final String DATA_FILE = "data/users.csv";
    private ArrayList<Driver> drivers = new ArrayList<>();
    private ArrayList<Rider> riders = new ArrayList<>();
    private ArrayList<Trip> trips = new ArrayList<>(); // In-memory trips
    
    private DriverService driverService;
    private RiderService riderService;
    private TripService tripService;
    
    // UI Components for Tables
    private JTable driverTable;
    private DefaultTableModel driverTableModel;
    private JTable riderTable;
    private DefaultTableModel riderTableModel;
    private JTextArea statsArea;

    public UberGUI() {
        // 1. Initialize Backend Services and Load Data
        FileHandler.loadData(DATA_FILE, drivers, riders);
        driverService = new DriverService(drivers);
        riderService = new RiderService(riders);
        tripService = new TripService();

        // 2. Setup Main Window
        setTitle("Uber Management System - Advanced GUI");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // 3. Create Tabs for all Assessment 3 Features
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🚗 Driver Management", createDriverPanel());
        tabbedPane.addTab("👤 Rider Management", createRiderPanel());
        tabbedPane.addTab("🗺️ Trips & Statistics", createTripPanel());
        
        add(tabbedPane);
    }

    // ==========================================
    // TAB 1: DRIVER MANAGEMENT (Includes Assesment 4 Algorithms)
    // ==========================================
    private JPanel createDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // // Top: Sorting & Searching Algorithms
        // JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // JButton btnSort = new JButton("Sort by ID (QuickSort)");
        // JTextField txtSearch = new JTextField(12);
        // JButton btnSearch = new JButton("Search ID (Binary)");
        // topPanel.add(btnSort);
        // topPanel.add(new JLabel(" | Search ID:"));
        // topPanel.add(txtSearch);
        // topPanel.add(btnSearch);
        
        // Top: Sorting & Searching Algorithms
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create a Dropdown for sorting options
        String[] sortOptions = {"Sort by ID", "Sort by Name", "Sort by Rating (Highest)"};
        JComboBox<String> cbSort = new JComboBox<>(sortOptions);
        JButton btnApplySort = new JButton("Apply Sort");
        
        JTextField txtSearch = new JTextField(10);
        JButton btnSearch = new JButton("Search ID");
        
        topPanel.add(new JLabel("Sort Drivers: "));
        topPanel.add(cbSort);
        topPanel.add(btnApplySort);
        topPanel.add(new JLabel("  | Search ID:"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        // Center: Data Table
        String[] columns = {"ID", "Name", "Email", "Vehicle", "Rating", "Status"};
        driverTableModel = new DefaultTableModel(columns, 0);
        driverTable = new JTable(driverTableModel);
        refreshDriverTable();
        
        // Bottom: CRUD Operations
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = new JButton("Add Driver");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnSave = new JButton("Save All Data");
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnSave);

        // Action Listeners for Algorithms
        // btnSort.addActionListener(e -> {
        //     AlgorithmUtils.sortDriversById(drivers, 0, drivers.size() - 1);
        //     refreshDriverTable();
        //     JOptionPane.showMessageDialog(this, "Drivers sorted alphabetically by ID!");
        // });

        // Action Listener for the new Dropdown Sort
        btnApplySort.addActionListener(e -> {
            int selectedIndex = cbSort.getSelectedIndex();
            
            if (selectedIndex == 0) {
                AlgorithmUtils.sortDriversById(drivers, 0, drivers.size() - 1);
            } else if (selectedIndex == 1) {
                AlgorithmUtils.sortDriversByName(drivers, 0, drivers.size() - 1);
            } else if (selectedIndex == 2) {
                AlgorithmUtils.sortDriversByRating(drivers, 0, drivers.size() - 1);
            }
            
            refreshDriverTable();
            JOptionPane.showMessageDialog(this, "Drivers sorted successfully by " + cbSort.getSelectedItem().toString().replace("Sort by ", "") + "!");
        });

        btnSearch.addActionListener(e -> {
            String target = txtSearch.getText().trim();
            AlgorithmUtils.sortDriversById(drivers, 0, drivers.size() - 1);
            Driver found = AlgorithmUtils.binarySearchDriverById(drivers, target);
            if (found != null) {
                JOptionPane.showMessageDialog(this, "Driver Found:\nName: " + found.getName() + "\nStatus: " + found.getStatus());
            } else {
                JOptionPane.showMessageDialog(this, "Driver not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Listeners for CRUD
        btnDelete.addActionListener(e -> {
            int row = driverTable.getSelectedRow();
            if (row != -1) {
                String id = (String) driverTableModel.getValueAt(row, 0);
                drivers.removeIf(d -> d.getId().equals(id));
                refreshDriverTable();
            } else {
                JOptionPane.showMessageDialog(this, "Select a driver first.");
            }
        });

        // btnAdd.addActionListener(e -> {
        //     String id = JOptionPane.showInputDialog(this, "Enter New Driver ID (e.g., D015):");
        //     if (id == null || id.trim().isEmpty()) return; // User clicked cancel
            
        //     String name = JOptionPane.showInputDialog(this, "Enter Full Name:");
        //     String email = JOptionPane.showInputDialog(this, "Enter Email Address:");
            
        //     // Adding a default vehicle setup to keep the GUI form simple
        //     Driver newDriver = new Driver(id.trim(), name, email, "0400000000", "LIC-NEW", 
        //                                   5.0, 0.0, 0, "Car", "Toyota", "Camry", "NEW123", 2024, "AVAILABLE");
        //     drivers.add(newDriver);
        //     refreshDriverTable();
        //     JOptionPane.showMessageDialog(this, "Driver " + name + " added successfully!\n(Don't forget to click 'Save All Data' to keep changes).");
        // });

        btnAdd.addActionListener(e -> {
            // Create text fields and dropdowns for the form
            JTextField txtId = new JTextField(10);
            JTextField txtName = new JTextField(10);
            JTextField txtEmail = new JTextField(10);
            JTextField txtPhone = new JTextField(10);
            JTextField txtLicense = new JTextField(10);
            JComboBox<String> cbType = new JComboBox<>(new String[]{"Car", "SUV", "Van"});
            JTextField txtMake = new JTextField(10);
            JTextField txtModel = new JTextField(10);
            JTextField txtPlate = new JTextField(10);
            JTextField txtYear = new JTextField("2024", 10);
            JTextField txtRating = new JTextField("5.0", 10);
            JComboBox<String> cbStatus = new JComboBox<>(new String[]{"AVAILABLE", "ON_TRIP", "SUSPENDED", "OFFLINE"});

            // Organize them neatly in a grid layout (12 rows, 2 columns)
            JPanel formPanel = new JPanel(new GridLayout(12, 2, 5, 5));
            formPanel.add(new JLabel("Driver ID:")); formPanel.add(txtId);
            formPanel.add(new JLabel("Name:")); formPanel.add(txtName);
            formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);
            formPanel.add(new JLabel("Phone Number:")); formPanel.add(txtPhone);
            formPanel.add(new JLabel("License Number:")); formPanel.add(txtLicense);
            formPanel.add(new JLabel("Vehicle Type:")); formPanel.add(cbType);
            formPanel.add(new JLabel("Vehicle Make:")); formPanel.add(txtMake);
            formPanel.add(new JLabel("Vehicle Model:")); formPanel.add(txtModel);
            formPanel.add(new JLabel("Vehicle Plate:")); formPanel.add(txtPlate);
            formPanel.add(new JLabel("Vehicle Year:")); formPanel.add(txtYear);
            formPanel.add(new JLabel("Driver Rating (0.0 - 5.0):")); formPanel.add(txtRating);
            formPanel.add(new JLabel("Current Status:")); formPanel.add(cbStatus);

            // Show the single, combined form inside a popup
            int result = JOptionPane.showConfirmDialog(this, formPanel, "Register New Driver", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            // If the user clicks "OK"
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String id = txtId.getText().trim();
                    if (id.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Error: Driver ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Convert the number fields from text to double/int
                    double rating = Double.parseDouble(txtRating.getText().trim());
                    int year = Integer.parseInt(txtYear.getText().trim());
                    
                    // Create the driver with the EXACT data they typed into the form
                    Driver newDriver = new Driver(
                        id, 
                        txtName.getText().trim(), 
                        txtEmail.getText().trim(), 
                        txtPhone.getText().trim(), 
                        txtLicense.getText().trim(), 
                        rating, 
                        0.0, // Wallet balance starts at $0
                        0,   // Total trips starts at 0
                        cbType.getSelectedItem().toString(), 
                        txtMake.getText().trim(), 
                        txtModel.getText().trim(), 
                        txtPlate.getText().trim(), 
                        year, 
                        cbStatus.getSelectedItem().toString()
                    );
                    
                    // Add to list and update table
                    drivers.add(newDriver);
                    refreshDriverTable();
                    JOptionPane.showMessageDialog(this, "Driver " + txtName.getText().trim() + " registered successfully!");
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Make sure 'Year' is a whole number and 'Rating' is a decimal (e.g. 5.0).", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSave.addActionListener(e -> {
            FileHandler.saveData(DATA_FILE, drivers, riders);
            JOptionPane.showMessageDialog(this, "Data successfully saved to users.csv!");
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(driverTable), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ==========================================
    // TAB 2: RIDER MANAGEMENT
    // ==========================================
    private JPanel createRiderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Rider ID", "Name", "Email", "Phone", "Payment Method"};
        riderTableModel = new DefaultTableModel(columns, 0);
        riderTable = new JTable(riderTableModel);
        refreshRiderTable();

        // JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // JButton btnDelete = new JButton("Delete Selected Rider");
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = new JButton("Add Rider");
        JButton btnDelete = new JButton("Delete Selected Rider");

        btnAdd.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Enter New Rider ID (e.g., R010):");
            if (id == null || id.trim().isEmpty()) return; // User clicked cancel
            
            String name = JOptionPane.showInputDialog(this, "Enter Full Name:");
            String email = JOptionPane.showInputDialog(this, "Enter Email Address:");
            String phone = JOptionPane.showInputDialog(this, "Enter Phone Number:");
            
// Adding default values: 5.0 rating, "Credit Card", "No Address", and 0.0 spent
Rider newRider = new Rider(id.trim(), name, email, phone, 5.0, "Credit Card", "No Address", 0.0);            riders.add(newRider);
            refreshRiderTable();

            FileHandler.saveData(DATA_FILE, drivers, riders);
            JOptionPane.showMessageDialog(this, "Rider " + name + " added successfully!");
        });
        
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnDelete);
        
        btnDelete.addActionListener(e -> {
            int row = riderTable.getSelectedRow();
            if (row != -1) {
                String id = (String) riderTableModel.getValueAt(row, 0);
                riders.removeIf(r -> r.getId().equals(id));
                FileHandler.saveData(DATA_FILE, drivers, riders);
                refreshRiderTable();
            } else {
                JOptionPane.showMessageDialog(this, "Select a rider first.");
            }
        });

        bottomPanel.add(btnDelete);
        panel.add(new JLabel("Registered Riders:"), BorderLayout.NORTH);
        panel.add(new JScrollPane(riderTable), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ==========================================
    // TAB 3: TRIPS & STATISTICS
    // ==========================================
    private JPanel createTripPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Center: Stats Text Area
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        updateStatsView();

        // Bottom: Trip Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnRequestTrip = new JButton("Simulate New Trip Request");
        JButton btnRefreshStats = new JButton("Refresh Statistics");

        btnRequestTrip.addActionListener(e -> {
            if (riders.isEmpty() || drivers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Need at least one rider and driver to simulate a trip.");
                return;
            }
            
            Rider currentRider = riders.get(0);
            String destination = JOptionPane.showInputDialog(this, "Enter Destination for " + currentRider.getName() + ":");
            
            if (destination != null && !destination.trim().isEmpty()) {
                // Use the new GUI-friendly method! (Assuming 15.5km and NOT peak hour for the simulation)
                tripService.requestTripFromGUI(currentRider.getId(), "Current Location", destination, 15.5, false);
                
                JOptionPane.showMessageDialog(this, "Trip Requested!\nThere are now " + tripService.getQueueSize() + " trips in the queue.");
                updateStatsView();
            }
        });

        btnRefreshStats.addActionListener(e -> updateStatsView());

        bottomPanel.add(btnRequestTrip);
        bottomPanel.add(btnRefreshStats);

        panel.add(new JLabel("System Statistics & Trip Management"), BorderLayout.NORTH);
        panel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ==========================================
    // HELPER METHODS (UI Refreshers)
    // ==========================================
    private void refreshDriverTable() {
        driverTableModel.setRowCount(0); 
        for (Driver d : drivers) {
            driverTableModel.addRow(new Object[]{
                d.getId(), d.getName(), d.getEmail(), d.getVehicleMake(), d.getRating(), d.getStatus()
            });
        }
    }

    private void refreshRiderTable() {
        riderTableModel.setRowCount(0); 
        for (Rider r : riders) {
            riderTableModel.addRow(new Object[]{
                r.getId(), r.getName(), r.getEmail(), r.getPhone(), r.getPaymentMethod()
            });
        }
    }

    private void updateStatsView() {
        // Utilizing your existing StatsService architecture
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================\n");
        sb.append("       UBER PLATFORM STATISTICS      \n");
        sb.append("=====================================\n\n");
        
        sb.append("Total Registered Drivers: ").append(drivers.size()).append("\n");
        long availableDrivers = drivers.stream().filter(d -> d.getStatus() == Driver.Status.AVAILABLE).count();
        sb.append("Currently Available Drivers: ").append(availableDrivers).append("\n\n");
        
        sb.append("Total Registered Riders: ").append(riders.size()).append("\n");
        // sb.append("Total Trips Requested/Completed: ").append(trips.size()).append("\n\n");
        
        if (!drivers.isEmpty()) {
            sb.append("--- Highest Rated Driver ---\n");
            Driver top = drivers.get(0);
            for (Driver d : drivers) {
                if (d.getRating() > top.getRating()) top = d;
            }
            sb.append("Name: ").append(top.getName()).append("\n");
            sb.append("Rating: ").append(top.getRating()).append(" / 5.0\n");
        }
        
        statsArea.setText(sb.toString());
    }
}