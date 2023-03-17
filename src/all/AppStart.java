package all;

import all.controlers.Controller3D;
import all.view.Window;

import javax.swing.*;


public class AppStart {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            new Controller3D(window.getPanel());
            window.setVisible(true);
        });

    }
}

