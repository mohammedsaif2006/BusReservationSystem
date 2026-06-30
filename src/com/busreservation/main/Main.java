package com.busreservation.main;

import javax.swing.SwingUtilities;
import com.busreservation.ui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });

    }

}