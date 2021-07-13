package shortcut;

import database.Database;
import element.ShortcutElementMostUsed;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

import java.util.List;

public class ShortcutMostUsed {

    @FXML
    public FlowPane showMostUsedShortcutInternet;

    @FXML
    public AnchorPane viewMostUsed, anchorElement, anchorAllElement;

    @FXML
    public HBox swipeDown, hBoxViewMostUsed;

    @FXML
    public ImageView param;


    public ObservableList<ShortcutElementMostUsed> listShortcutInternet = FXCollections.observableArrayList();

    public boolean refreshView = true;

    @FXML
    public void initialize () {
        try {
            setColor();
            SingletonShortcut.shortcutMostUsed = this;
            this.listShortcutInternet.addListener(this::eventListenerShortcut);
            showListOfShortcutInternetMostUsed();
            anchorAllElement.requestLayout();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutElementMostUsed> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                this.showMostUsedShortcutInternet.getChildren().add(listShortcutInternet.get(listShortcutInternet.size() - 1).getView());
            }
        }
    }

    public void showListOfShortcutInternetMostUsed () {
        Platform.runLater(() -> {
            if (listShortcutInternet.size() < 4) {
                refreshView = true;
            }

            if (refreshView) {
                Database database = new Database();
                List<ShortcutElementMostUsed> list = database.getListOfShortcutMostUsed();
                this.showMostUsedShortcutInternet.getChildren().clear();
                listShortcutInternet.clear();

                if (list.size() >= 4) {
                    for ( int i = 0; i < 4; ++i) {
                        listShortcutInternet.add(list.get(i));
                    }
                }
                refreshView = false;
            }
        });
    }

    public void resetColorOfItem () {
        listShortcutInternet.forEach(ShortcutElementMostUsed::resetColor);
    }

    public void setColor () {
        resetColorOfItem();
        hBoxViewMostUsed.setStyle("-fx-border-color:"+ SingletonColor.instance.getLine() + "; -fx-border-width: 0 0 3 0;");
    }
}
