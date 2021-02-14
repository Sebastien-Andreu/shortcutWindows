package shortcut;

import database.Database;
import element.ShortcutElementMostUsed;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

import java.util.List;

public class ShortcutMostUsed {

    @FXML
    public FlowPane showMostUsedShortcutInternet;

    @FXML
    public AnchorPane viewMostUsed, anchorElement, anchorParameter, anchorAllElement, anchorColorViewTitle, anchorColorViewData;

    @FXML
    public HBox swipeDown, hBoxViewMostUsed, hBoxAnchorParameter, hBoxAnchorColor, hBoxColorViewBorder;

    @FXML
    public ImageView param, quitParameter;

    @FXML
    public Button buttonColorView;

    @FXML
    public ColorPicker colorBackground, colorLine, colorButton, colorTitle;

    public ObservableList<ShortcutElementMostUsed> listShortcutInternet = FXCollections.observableArrayList();
    Database database = new Database();

    public boolean refreshView = true;

    @FXML
    public void initialize () {
        try {
            setColor();
            setColorOfColorPicker();
            SingletonShortcut.shortcutMostUsed = this;
            this.listShortcutInternet.addListener(this::eventListenerShortcut);
            this.onUserWantToQuitParameter();
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

    public void onUserWantToShowParameter() {
        this.anchorElement.setVisible(false);
        this.anchorParameter.setVisible(true);
    }

    public void onUserWantToQuitParameter() {
        this.anchorElement.setVisible(true);
        this.anchorParameter.setVisible(false);
    }

    public void resetColorOfItem () {
        listShortcutInternet.forEach(ShortcutElementMostUsed::resetColor);
    }

    public void setColor () {
        resetColorOfItem();
        hBoxViewMostUsed.setStyle("-fx-border-color:"+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0;");
        anchorParameter.setStyle("-fx-background-color:"+ SingletonColor.singletonColor.getBackground() + ";");
        hBoxAnchorParameter.setStyle("-fx-border-color: "+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ SingletonColor.singletonColor.getBackgroundTitle() + ";");
        hBoxAnchorColor.setStyle("-fx-border-color: "+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0;");

        anchorColorViewTitle.setStyle("-fx-border-color: white; -fx-border-width: 1.5 1.5 1.5 1.5;");
    }

    private void setColorOfColorPicker () {
        colorBackground.setValue(Color.valueOf(SingletonColor.singletonColor.getBackground()));
        colorLine.setValue(Color.valueOf(SingletonColor.singletonColor.getLine()));
        colorButton.setValue(Color.valueOf(SingletonColor.singletonColor.getButton()));
        colorTitle.setValue(Color.valueOf(SingletonColor.singletonColor.getBackgroundTitle()));

        onUserChooseBackgroundColor(null);
        onUserChooseLineColor(null);
        onUserChooseButtonColor(null);

    }

    public void onUserChooseBackgroundColor(ActionEvent event) {
        anchorColorViewData.setStyle("-fx-background-color:"+ "#" + colorBackground.getValue().toString().substring(2) + ";"); // title
    }

    public void onUserChooseLineColor(ActionEvent event) {
        hBoxColorViewBorder.setStyle("-fx-border-color: "+ "#" + colorLine.getValue().toString().substring(2) + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ "#" + colorTitle.getValue().toString().substring(2) + ";"); // line
    }

    public void onUserChooseButtonColor(ActionEvent event) {
        buttonColorView.setStyle("-fx-background-color:"+ "#" + colorButton.getValue().toString().substring(2) + "; -fx-background-radius: 20;"); // button
    }

    public void onUserChooseTitleBackgroundColor(ActionEvent event) {
        onUserChooseLineColor(null);
    }

    public void onUserWantToUpdateColor(ActionEvent event) {
        database.updateColor(colorBackground.getValue(), colorTitle.getValue(), colorLine.getValue(), colorButton.getValue());
        setColorOfColorPicker();
        SingletonShortcut.updateColor();
    }

    public void onUserWantToResetColor(ActionEvent event) {
        database.getDefaultColor();
        setColorOfColorPicker();
        SingletonShortcut.updateColor();
    }
}
