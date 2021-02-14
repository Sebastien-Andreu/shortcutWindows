package shortcut;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

public class ShortcutController {

    @FXML
    public AnchorPane viewMostUsed, viewShortcutInternet, viewShortcutFolder, viewShortcutApplication, allAppShortcut;


    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutController = this;
            setColor();
            if (SingletonShortcut.shortcutInternetController.listShortcut.size() < 4) {
                setViewAccessible();
            } else {
                setViewInaccessible();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setViewAccessible () {
        viewMostUsed.setVisible(false);
        viewShortcutInternet.setVisible(true);
        viewShortcutFolder.setVisible(true);
        viewShortcutApplication.setVisible(true);
    }

    public void setViewInaccessible () {
        viewMostUsed.setVisible(true);
        viewShortcutInternet.setVisible(false);
        viewShortcutFolder.setVisible(false);
        viewShortcutApplication.setVisible(false);
    }

    public void setColor () {
        allAppShortcut.setStyle("-fx-background-color:"+ SingletonColor.singletonColor.getBackground() + ";");
    }
}
