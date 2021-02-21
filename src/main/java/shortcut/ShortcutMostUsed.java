package shortcut;

import database.Database;
import element.ShortcutElementMostUsed;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import singleton.SingletonColor;
import singleton.SingletonPosScreen;
import singleton.SingletonShortcut;

import java.util.List;
import java.util.Objects;

public class ShortcutMostUsed {

    @FXML
    public FlowPane showMostUsedShortcutInternet;

    @FXML
    public AnchorPane viewMostUsed, anchorElement, anchorParameter, anchorAllElement, anchorColorViewTitle, anchorColorViewData;

    @FXML
    public HBox swipeDown, hBoxViewMostUsed, hBoxAnchorParameter, hBoxAnchorColor, hBoxColorViewBorder, hBoxAnchorPosition;

    @FXML
    public ImageView param, quitParameter;

    @FXML
    public Button buttonColorView, buttonResetFont, buttonResetLine, buttonResetButton, buttonResetTitle;

    @FXML
    public ColorPicker colorBackground, colorLine, colorButton, colorTitle;

    @FXML
    public ToggleGroup groupPosScreen;

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

            invisibleButtonResetColor();
            showPictureButton(buttonResetFont, "picture/cancel.png");
            showPictureButton(buttonResetButton, "picture/cancel.png");
            showPictureButton(buttonResetLine, "picture/cancel.png");
            showPictureButton(buttonResetTitle, "picture/cancel.png");

            groupPosScreen.getToggles().get(0).setUserData(0);
            groupPosScreen.getToggles().get(1).setUserData(1);

            SingletonPosScreen.SingletonPosScreen.updatePos();

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

    private void showPictureButton (Button btn, String str) {
        btn.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(str)).toString())));
    }

    public void resetColorOfItem () {
        listShortcutInternet.forEach(ShortcutElementMostUsed::resetColor);
    }

    public void setColor () {
        resetColorOfItem();
        hBoxViewMostUsed.setStyle("-fx-border-color:"+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0;");
        anchorParameter.setStyle("-fx-background-color:"+ SingletonColor.singletonColor.getBackground() + ";");
        hBoxAnchorParameter.setStyle("-fx-border-color: "+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ SingletonColor.singletonColor.getBackgroundTitle() + ";");

        hBoxAnchorColor.setStyle("-fx-border-color: "+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ SingletonColor.singletonColor.getBackgroundTitle() + ";");
        hBoxAnchorPosition.setStyle("-fx-border-color: "+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 3 0 3 0; -fx-background-color:"+ SingletonColor.singletonColor.getBackgroundTitle() + ";");

        anchorColorViewTitle.setStyle("-fx-border-color: white; -fx-border-width: 1.5 1.5 1.5 1.5;");

        buttonResetFont.setStyle("-fx-background-color:" + SingletonColor.singletonColor.getButton() + "; -fx-background-radius: 20;");
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
        buttonResetFont.setVisible(true);
        anchorColorViewData.setStyle("-fx-background-color:"+ "#" + colorBackground.getValue().toString().substring(2) + ";"); // title
    }

    public void onUserChooseLineColor(ActionEvent event) {
        buttonResetLine.setVisible(true);
        hBoxColorViewBorder.setStyle("-fx-border-color: "+ "#" + colorLine.getValue().toString().substring(2) + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ "#" + colorTitle.getValue().toString().substring(2) + ";"); // line
    }

    public void onUserChooseButtonColor(ActionEvent event) {
        buttonResetButton.setVisible(true);
        buttonColorView.setStyle("-fx-background-color:"+ "#" + colorButton.getValue().toString().substring(2) + "; -fx-background-radius: 20;"); // button
    }

    private void invisibleButtonResetColor () {
        buttonResetFont.setVisible(false);
        buttonResetButton.setVisible(false);
        buttonResetLine.setVisible(false);
        buttonResetTitle.setVisible(false);
    }

    public void onUserChooseTitleBackgroundColor(ActionEvent event) {
        buttonResetTitle.setVisible(true);
        onUserChooseLineColor(null);
    }

    public void onUserWantToUpdateColor(ActionEvent event) {
        database.updateColor(colorBackground.getValue(), colorTitle.getValue(), colorLine.getValue(), colorButton.getValue());
        setColorOfColorPicker();
        SingletonShortcut.updateColor();

        invisibleButtonResetColor();
    }

    public void onUserWantToResetColor(ActionEvent event) {
        database.getDefaultColor();
        setColorOfColorPicker();
        SingletonShortcut.updateColor();

        invisibleButtonResetColor();
    }

    public void onUserWantToResetColorFont(ActionEvent event) {
        colorBackground.setValue(Color.valueOf(SingletonColor.singletonColor.getBackground()));
        onUserChooseBackgroundColor(null);
        buttonResetFont.setVisible(false);
    }

    public void onUserWantToResetColorLine(ActionEvent event) {
        colorLine.setValue(Color.valueOf(SingletonColor.singletonColor.getLine()));
        onUserChooseLineColor(null);
        buttonResetLine.setVisible(false);
    }

    public void onUserWantToResetColorButton(ActionEvent event) {
        colorButton.setValue(Color.valueOf(SingletonColor.singletonColor.getButton()));
        onUserChooseButtonColor(null);
        buttonResetButton.setVisible(false);
    }

    public void onUserWantToResetColorTitle(ActionEvent event) {
        colorTitle.setValue(Color.valueOf(SingletonColor.singletonColor.getBackgroundTitle()));
        onUserChooseTitleBackgroundColor(null);
        buttonResetTitle.setVisible(false);
    }
}
