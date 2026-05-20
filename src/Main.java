import ui.Menu;
import ui.UberGUI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=====================================");
        System.out.println("  UBER MANAGEMENT SYSTEM LAUNCHER");
        System.out.println("=====================================");
        System.out.println("Select Interface Mode:");
        System.out.println("1. Text-Based Interface (CLI)");
        System.out.println("2. Graphical User Interface (GUI)");
        System.out.print("Enter choice (1 or 2): ");
        
        String choice = sc.nextLine().trim();
        
        if (choice.equals("2")) {
            System.out.println("Launching GUI...");
            // Run GUI on the Event Dispatch Thread
            javax.swing.SwingUtilities.invokeLater(() -> {
                UberGUI gui = new UberGUI();
                gui.setVisible(true);
            });
        } else {
            System.out.println("Launching Text Interface...");
            new Menu().start();
        }
    }
}