package shortcut;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import singleton.SingletonShortcut;

public class ShortcutController {

    public AnchorPane root;

    @FXML
    public AnchorPane viewMostUsed, viewShortcutInternet, viewShortcutFolder, viewShortcutApplication;

    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutController = this;
            setViewInaccessible();

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

}
