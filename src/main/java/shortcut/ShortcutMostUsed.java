package shortcut;

import database.Database;
import element.ShortcutElement;
import element.ShortcutElementMostUsed;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import singleton.SingletonShortcut;

import java.util.List;

public class ShortcutMostUsed {

    @FXML
    public FlowPane showMostUsedShortcutInternet;

    @FXML
    public AnchorPane viewMostUsed;

    @FXML
    public HBox swipeDown;

    public ObservableList<ShortcutElementMostUsed> listShortcutInternet = FXCollections.observableArrayList();

    public boolean refreshView = true;

    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutMostUsed = this;
            this.listShortcutInternet.addListener(this::eventListenerShortcut);
            showListOfShortcutInternetMostUsed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutElementMostUsed> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                this.showMostUsedShortcutInternet.getChildren().add(listShortcutInternet.get(listShortcutInternet.size() - 1).getView());
            }
            if (change.wasRemoved()) {
//                if (!listShortcutInternet.isEmpty()){
//                    if (!refreshView){
//                        this.showMostUsedShortcutInternet.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
//                    }
//                } else {
//                    this.showMostUsedShortcutInternet.getChildren().clear();
//                }
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
}
