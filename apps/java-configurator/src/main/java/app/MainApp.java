package app;

import gui.layout.ConfiguratorFrame;
import util.config.ThemeManager;

import javax.swing.*;

import org.apache.commons.cli.*;

public class MainApp {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "headless", false, "Run in headless mode");
        options.addOption("j", "json", true, "Path to JSON file with order details");
        options.addOption("o", "output", true, "File path for the generated PDF");
        
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                HeadlessRunner.run(cmd);
                return;
            }
        } catch (ParseException e) {
            System.err.println("Failed to parse command line properties: " + e.getMessage());
        }

        ThemeManager.applyTheme();
        SwingUtilities.invokeLater(ConfiguratorFrame::new);
    }
}
