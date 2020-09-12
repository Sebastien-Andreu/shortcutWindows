package shortcut;

import database.Database;
import element.ShortcutElement;
import element.ShortcutElementMostUsed;
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


    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutMostUsed = this;
            this.listShortcutInternet.addListener(this::eventListenerShortcut);
            showListOfShortcutInternetMostUsed();

//            swipeDown.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, evt -> {
//                SingletonShortcut.shortcutController.transition.stop();
//                SingletonShortcut.shortcutController.setSecondTransition();
//                SingletonShortcut.shortcutController.setViewAccessible();
//                SingletonShortcut.shortcutController.transition.play();
//            });
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
                if (!listShortcutInternet.isEmpty()){
                    this.showMostUsedShortcutInternet.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                } else {
                    this.showMostUsedShortcutInternet.getChildren().clear();
                }
            }
        }
    }

    public void showListOfShortcutInternetMostUsed () {
        listShortcutInternet.clear();
        Database database = new Database();
        List<ShortcutElement> list = database.getListOfShortcut();
        for ( int i = 0; i < 4; ++i) {
            listShortcutInternet.add(new ShortcutElementMostUsed(list.get(i).text, list.get(i).url));
        }
    }

    public void resetColorOfItem () {
        listShortcutInternet.forEach(ShortcutElementMostUsed::resetColor);
    }

    public void onUserWantToShowAllShortcut(MouseEvent event) {

    }
}
