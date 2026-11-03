package main;

import javax.swing.SwingUtilities;
import view.MainForm;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}