// gpl 3 or greater

// TODO system tray functionality

import java.awt.*;
import static org.junit.Assert.*;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;


import java.security.MessageDigest;

public class ClipboardHistory {

    public static void main(String[] args) {
        // TODO code application logic here
        
        Tray t = new Tray();

        int i = 1000;
        String old = "";
        do {
            String cb = getClipboardContents();

            if (!cb.equals(old)) {
                System.out.println("\nText: " + cb
                        + "\nHash: " + getHex(hash(cb))
                        + "\nTick: " + i);
            }
            try {
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
            }
            old = cb;
        } while (i-- > 0);
    }

    // returns a clumsy byte array
    public static byte[] hash(String s) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(s.getBytes());
            return messageDigest.digest();
        } catch (Exception e) {
            System.out.println("ERR");
        }
        byte b[] = {0, 1, 0, 1};
        return b;
    }

    // byte array to hex string
    public static String getHex(byte[] raw) {
        String HEXES = "0123456789ABCDEF";
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    /*
     * <h1> is this big? </h1>
     *
     */
    public static String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                //highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    @org.junit.Test
    public void emptyTest() {
        assertTrue(2 + 2 == 4);
    }
}

class Tray {
    Tray(){
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(
                Toolkit.getDefaultToolkit().createImage(
                    "icon.gif"
                //, "tray icon")
                ));
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}