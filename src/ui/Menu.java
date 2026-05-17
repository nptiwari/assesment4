package ui;

import model.Driver;
import model.Rider;
import service.DriverService;
import service.RiderService;
import service.StatsService;
import service.TripService;
import util.Display;
import util.FileHandler;
import util.InputHelper;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu - main CLI controller for the Uber Management System.
 * Routes user input to the appropriate service layer.
 */
public class Menu {

    private static final String DATA_FILE = "data/users.csv";

    private final ArrayList<Driver> drivers;
    private final ArrayList<Rider>  riders;
    private final DriverService     driverService;
    private final RiderService      riderService;
    private final TripService       tripService;
    private final Scanner           sc;

    public Menu() {
        this.drivers       = new ArrayList<>();
        this.riders        = new ArrayList<>();
        this.driverService = new DriverService(drivers);
        this.riderService  = new RiderService(riders);
        this.tripService   = new TripService();
        this.sc            = new Scanner(System.in);
    }

    public void start() {
        Display.banner("  UBER MANAGEMENT SYSTEM  |  ICT711");
        Display.blank();
        FileHandler.loadData(DATA_FILE, drivers, riders);
        Display.blank();

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = InputHelper.readChoice(sc);
            switch (choice) {
                case "1":
                    driverMenu();
                    break;
                case "2":
                    riderMenu();
                    break;
                case "3":
                    tripMenu();
                    break;
                case "4":
                    driverService.runEvaluations();
                    break;
                case "5":
                    StatsService.showDashboard(drivers, riders, tripService);
                    break;
                case "6":
                    FileHandler.saveData(DATA_FILE, drivers, riders);
                    break;
                case "0":
                    if (InputHelper.confirm(sc, "Save data before exiting?")) {
                        FileHandler.saveData(DATA_FILE, drivers, riders);
                    }
                    Display.banner("  Thank you for using Uber Management System");
                    running = false;
                    break;
                default:
                    Display.warn("Invalid option. Please try again.");
            }
        }
        sc.close();
    }

    // ── DRIVER MENU ──────────────────────────────────────────────────────────
    private void driverMenu() {
        boolean back = false;
        while (!back) {
            Display.section("Driver Management");
            Display.menuItem("1", "Add Driver");
            Display.menuItem("2", "View All Drivers");
            Display.menuItem("3", "Search Driver");
            Display.menuItem("4", "Update Driver");
            Display.menuItem("5", "Delete Driver");
            Display.menuItem("0", "Back to Main Menu");
            switch (InputHelper.readChoice(sc)) {
                case "1": driverService.addDriver(sc); break;
                case "2": driverService.viewAll();     break;
                case "3": driverService.query(sc);     break;
                case "4": driverService.update(sc);    break;
                case "5": driverService.delete(sc);    break;
                case "0": back = true;                 break;
                default:  Display.warn("Invalid option.");
            }
        }
    }

    // ── RIDER MENU ───────────────────────────────────────────────────────────
    private void riderMenu() {
        boolean back = false;
        while (!back) {
            Display.section("Rider Management");
            Display.menuItem("1", "Add Rider");
            Display.menuItem("2", "View All Riders");
            Display.menuItem("3", "Search Rider");
            Display.menuItem("4", "Update Rider");
            Display.menuItem("5", "Delete Rider");
            Display.menuItem("0", "Back to Main Menu");
            switch (InputHelper.readChoice(sc)) {
                case "1": riderService.addRider(sc); break;
                case "2": riderService.viewAll();    break;
                case "3": riderService.query(sc);    break;
                case "4": riderService.update(sc);   break;
                case "5": riderService.delete(sc);   break;
                case "0": back = true;               break;
                default:  Display.warn("Invalid option.");
            }
        }
    }

    // ── TRIP MENU ────────────────────────────────────────────────────────────
    private void tripMenu() {
        boolean back = false;
        while (!back) {
            Display.section("Trip Management");
            Display.menuItem("1", "Request a Trip");
            Display.menuItem("2", "Assign and Complete Next Trip");
            Display.menuItem("3", "Cancel a Queued Trip");
            Display.menuItem("4", "View Trip Queue");
            Display.menuItem("5", "View Completed Trips");
            Display.menuItem("0", "Back to Main Menu");
            switch (InputHelper.readChoice(sc)) {
                case "1": tripService.requestTrip(sc, riders);             break;
                case "2": tripService.assignNextTrip(sc, drivers, riders); break;
                case "3": tripService.cancelQueuedTrip(sc);                break;
                case "4": tripService.viewQueue();                         break;
                case "5": tripService.viewCompleted();                     break;
                case "0": back = true;                                     break;
                default:  Display.warn("Invalid option.");
            }
        }
    }

    // ── MAIN MENU ────────────────────────────────────────────────────────────
    private void printMainMenu() {
        Display.blank();
        Display.divider();
        Display.menuItem("1", "Driver Management");
        Display.menuItem("2", "Rider Management");
        Display.menuItem("3", "Trip Management");
        Display.menuItem("4", "Run Driver Evaluations");
        Display.menuItem("5", "System Dashboard");
        Display.menuItem("6", "Save Data");
        Display.menuItem("0", "Exit");
        Display.divider();
    }
}