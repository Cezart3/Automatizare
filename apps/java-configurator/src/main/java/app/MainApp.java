package app;

import gui.layout.ConfiguratorFrame;
import util.config.ThemeManager;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        ThemeManager.applyTheme();
        SwingUtilities.invokeLater(ConfiguratorFrame::new);
    }
}
