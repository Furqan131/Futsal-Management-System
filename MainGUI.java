import javax.swing.*;

public class MainGUI {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}

