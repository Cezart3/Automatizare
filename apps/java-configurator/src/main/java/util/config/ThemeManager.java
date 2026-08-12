package util.config;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    public static void applyTheme() {
        FlatDarkLaf.setup();

        // colțuri rotunjite
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 16);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ComboBox.arc", 8);

        // focus
        UIManager.put("Component.focusWidth", 2);
        UIManager.put("Component.focusColor", new Color(0x29B6F6));

        // butoane
        UIManager.put("Button.background", new Color(0x44475A)); // gri închis
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", new Color(0x6272A4)); // buton activ

        // panouri
        UIManager.put("Panel.background", new Color(0x282A36)); // fundal general întunecat

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Eroare aplicare tema FlatLaf: " + ex.getMessage());
        }
    }
}
