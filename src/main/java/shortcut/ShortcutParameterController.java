package shortcut;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import singleton.SingletonColor;
import singleton.SingletonPosScreen;
import singleton.SingletonShortcut;

import java.util.Objects;

public class ShortcutParameterController {

    @FXML
    public AnchorPane anchorParameter, anchorColorViewTitle, anchorColorViewData, anchorContentColor;

    @FXML
    public HBox hBoxAnchorParameter, hBoxAnchorColor, hBoxColorViewBorder, hBoxAnchorPosition, hBoxAnchorScreen;

    @FXML
    public ImageView quitParameter;

    @FXML
    public Button buttonColorView, buttonResetFont, buttonResetLine, buttonResetButton, buttonResetTitle;

    @FXML
    public ColorPicker colorBackground, colorLine, colorButton, colorTitle;

    @FXML
    public ToggleGroup groupPosScreen;

    @FXML
    public ChoiceBox<String> screenSelector;

    private Database database;

    public Scene scene = null;
    public Stage stage;
    @FXML
    public void initialize () {
        SingletonShortcut.shortcutParameterController = this;
        SingletonPosScreen.instance.updatePos();

        ObservableList<String> observableList = FXCollections.observableArrayList();

        for (int i = 0; i < Screen.getScreens().size(); i++) {
            observableList.add("Ecran " + (i+1));
        }
        screenSelector.setItems(observableList);
        screenSelector.getSelectionModel().select(SingletonPosScreen.instance.getScreen());

        screenSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            database.updateScreen(screenSelector.getSelectionModel().selectedIndexProperty().intValue());
            SingletonPosScreen.instance.updateScreen(screenSelector.getSelectionModel().selectedIndexProperty().intValue());
            if (SingletonPosScreen.instance.getValue() == 0) {
                this.stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMinX());
            } else {
                this.stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxX() - 442);
            }
        });


        database = new Database();

        this.quitParameter.setOnMouseClicked( e -> {
            if (scene != null) {
                this.stage.setScene(scene);
                this.stage.setHeight(130);
                SingletonShortcut.freezeApp = false;
            }
        });

        setColor();
        setColorOfColorPicker();
        invisibleButtonResetColor();

        groupPosScreen.getToggles().get(0).setUserData(0);
        groupPosScreen.getToggles().get(1).setUserData(1);

        groupPosScreen.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            database.updatePosScreen(Integer.parseInt(groupPosScreen.getSelectedToggle().getUserData().toString()));
            SingletonPosScreen.instance.setValue(Integer.parseInt(groupPosScreen.getSelectedToggle().getUserData().toString()));
            System.out.println(SingletonPosScreen.instance.getScreen());
            if (SingletonPosScreen.instance.getValue() == 0) {
                this.stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMinX());
            } else {
                this.stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxX() - 442);
            }
        });

        showPictureButton(buttonResetFont, "picture/cancel.png");
        showPictureButton(buttonResetButton, "picture/cancel.png");
        showPictureButton(buttonResetLine, "picture/cancel.png");
        showPictureButton(buttonResetTitle, "picture/cancel.png");

        anchorContentColor.setMinHeight(0);
        anchorContentColor.setMaxHeight(0);
        anchorContentColor.setPrefHeight(0);
    }

    public void setStage ( Stage stage ) {
        this.stage = stage;
    }

    public void setColor () {
        anchorParameter.setStyle("-fx-background-color:"+ SingletonColor.instance.getBackground() + ";");
        hBoxAnchorParameter.setStyle("-fx-border-color: "+ SingletonColor.instance.getLine() + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ SingletonColor.instance.getBackgroundTitle() + ";");

        hBoxAnchorColor.setStyle("-fx-border-color: "+ SingletonColor.instance.getLine() + "; -fx-border-width: 0 0 3 0; -fx-background-color:"+ SingletonColor.instance.getBackgroundTitle() + ";");
        hBoxAnchorPosition.setStyle("-fx-border-color: "+ SingletonColor.instance.getLine() + "; -fx-border-width: 3 0 3 0; -fx-background-color:"+ SingletonColor.instance.getBackgroundTitle() + ";");
        hBoxAnchorScreen.setStyle("-fx-border-color: "+ SingletonColor.instance.getLine() + "; -fx-border-width: 3 0 3 0; -fx-background-color:"+ SingletonColor.instance.getBackgroundTitle() + ";");

        anchorColorViewTitle.setStyle("-fx-border-color: white; -fx-border-width: 1.5 1.5 1.5 1.5;");

        buttonResetFont.setStyle("-fx-background-color:" + SingletonColor.instance.getButton() + "; -fx-background-radius: 20;");
    }

    private void setColorOfColorPicker () {
        colorBackground.setValue(Color.valueOf(SingletonColor.instance.getBackground()));
        colorLine.setValue(Color.valueOf(SingletonColor.instance.getLine()));
        colorButton.setValue(Color.valueOf(SingletonColor.instance.getButton()));
        colorTitle.setValue(Color.valueOf(SingletonColor.instance.getBackgroundTitle()));

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
        buttonResetLine.setVisible(false);
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
        colorBackground.setValue(Color.valueOf(SingletonColor.instance.getBackground()));
        onUserChooseBackgroundColor(null);
        buttonResetFont.setVisible(false);
    }

    public void onUserWantToResetColorLine(ActionEvent event) {
        colorLine.setValue(Color.valueOf(SingletonColor.instance.getLine()));
        onUserChooseLineColor(null);
        buttonResetLine.setVisible(false);
    }

    public void onUserWantToResetColorButton(ActionEvent event) {
        colorButton.setValue(Color.valueOf(SingletonColor.instance.getButton()));
        onUserChooseButtonColor(null);
        buttonResetButton.setVisible(false);
    }

    public void onUserWantToResetColorTitle(ActionEvent event) {
        colorTitle.setValue(Color.valueOf(SingletonColor.instance.getBackgroundTitle()));
        onUserChooseTitleBackgroundColor(null);
        buttonResetTitle.setVisible(false);
    }

    private void showPictureButton (Button btn, String str) {
        btn.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(str)).toString())));
    }
}
