package shortcut;

import element.ShortcutElement;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import database.Database;
import singleton.SingletonShortcut;

import java.net.URL;
import java.util.*;


public class ShortcutInternetController {

    @FXML
    public AnchorPane viewShortcutInternet, anchorPaneViewAddShortcut;

    @FXML
    ScrollPane scrollPaneViewShortcut;

    @FXML
    public FlowPane showShortcut;

    @FXML
    TextField inputAddText, inputAddUrl;

    @FXML
    public Button buttonAddShortcut, buttonDelShortcut, buttonEditShortcut, buttonValidAddShortcut, buttonValidEditShortcut;


    public ShortcutElement shortcutElementSelected = null;
    public boolean setInFirstPlace = false;
    private boolean shortcutAddClicked = false;

    public Database database;

    public ObservableList<ShortcutElement> listShortcut = FXCollections.observableArrayList();


    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutInternetController = this;

            this.listShortcut.addListener(this::eventListenerShortcut);

            database = new Database();

            showPictureButton(buttonEditShortcut, "picture/edit.png");
            showPictureButton(buttonAddShortcut, "picture/add.png");
            showPictureButton(buttonDelShortcut, "picture/delete.png");

            initializeListOfShortCut();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutElement> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                final Node[] toFirstPlace = new Node[1];
                toFirstPlace[0] = null;
                if (setInFirstPlace){
                    showShortcut.getChildren().forEach(e -> {
                        if (e.equals(shortcutElementSelected.view)){
                            toFirstPlace[0] = e;
                        }
                    });
                    toFirstPlace[0].toBack();
                } else {
                    this.showShortcut.getChildren().add(listShortcut.get(listShortcut.size()-1).getView());
                }
            }
            if (change.wasRemoved()) {
                if (!listShortcut.isEmpty()){
                    if (!setInFirstPlace) {
                        this.showShortcut.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                } else {
                    this.showShortcut.getChildren().clear();
                }
            }
        }
    }


    private void showPictureButton (Button btn, String str) {
        btn.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(str)).toString())));
    }


    public void initializeListOfShortCut () {
        showShortcut.getChildren().clear();
        for (ShortcutElement element: database.getListOfShortcut()) {
            listShortcut.add(element);
        }
    }

    public void resetColorOfItem () {
        listShortcut.forEach(ShortcutElement::resetColor);
    }

    private boolean verifyIfEntryAddShortcutIsNotEmpty () {
        boolean result = true;
        if (inputAddText.getText().isEmpty()) {
            result = false;
            inputAddText.setStyle("-fx-background-color: red; -fx-text-inner-color: #ffffff;");
        } else {
            inputAddText.setStyle("-fx-background-color: #23272a; -fx-text-inner-color: #ffffff;");
        }
        if (inputAddUrl.getText().isEmpty()) {
            result = false;
            inputAddUrl.setStyle("-fx-background-color: red; -fx-text-inner-color: #ffffff;");
        } else {
            inputAddUrl.setStyle("-fx-background-color: #23272a; -fx-text-inner-color: #ffffff;");
        }
        return result;
    }

    private boolean urlIsValid () {
        try {
            URL url = new URL(inputAddUrl.getText());
            url.toURI();
            return true;
        } catch (Exception exception) {
            inputAddUrl.setStyle("-fx-background-color: red; -fx-text-inner-color: #ffffff;");
            return false;
        }
    }

    public void onUserWantToAddShortcut (ActionEvent event) {
        if (verifyIfEntryAddShortcutIsNotEmpty() && urlIsValid()) {
            resetColorOfItem();
            shortcutAddClicked = false;
            if (listShortcut.isEmpty()) {
                database.addNewShortcut(0, inputAddText.getText(), inputAddUrl.getText());
            } else {
                database.addNewShortcut(listShortcut.get(listShortcut.size()-1).pos + 1, inputAddText.getText(), inputAddUrl.getText());
            }

            listShortcut.add(database.getLastShortcutElement());

            showPictureButton(buttonAddShortcut, "picture/add.png");
            shortcutAddClicked = false;

            anchorPaneViewAddShortcut.setVisible(false);
            scrollPaneViewShortcut.setVisible(true);
            SingletonShortcut.freezeApp = false;
        }
    }

    public void onUserClickToShowAddShortcut (ActionEvent event) {
        if (!shortcutAddClicked) {
            SingletonShortcut.freezeApp = true;
            inputAddText.clear();
            inputAddUrl.clear();

            anchorPaneViewAddShortcut.setVisible(true);
            buttonValidAddShortcut.setVisible(true);
            buttonValidEditShortcut.setVisible(false);
            scrollPaneViewShortcut.setVisible(false);
            shortcutAddClicked = true;
            showPictureButton(buttonAddShortcut, "picture/cancel.png");
        } else {
            SingletonShortcut.freezeApp = false;
            resetColorOfItem();
            anchorPaneViewAddShortcut.setVisible(false);
            scrollPaneViewShortcut.setVisible(true);
            shortcutAddClicked = false;
            showPictureButton(buttonAddShortcut, "picture/add.png");
        }
    }

    public void onUserClickToDelShortcut (ActionEvent event) {
        Platform.runLater(() -> {
            if (shortcutElementSelected != null) {
                database.deleteShortcutElement(shortcutElementSelected);
                listShortcut.removeIf(shortcutElement -> shortcutElement.equals(shortcutElementSelected));
                shortcutElementSelected = null;
            }
        });

        buttonDelShortcut.setVisible(false);
        buttonEditShortcut.setVisible(false);
    }

    public void onMouseClickedHead (MouseEvent event) {
        buttonDelShortcut.setVisible(false);
        buttonEditShortcut.setVisible(false);
        resetColorOfItem();
    }

    public void onUserWantToShowEditShortcut (ActionEvent event) {
        buttonEditShortcut.setVisible(false);
        buttonDelShortcut.setVisible(false);

        inputAddText.setText(shortcutElementSelected.text);
        inputAddUrl.setText(shortcutElementSelected.url);

        anchorPaneViewAddShortcut.setVisible(true);
        buttonValidEditShortcut.setVisible(true);
        buttonValidAddShortcut.setVisible(false);
        scrollPaneViewShortcut.setVisible(false);
        shortcutAddClicked = true;
        showPictureButton(buttonAddShortcut, "picture/cancel.png");
        resetColorOfItem();
    }

    public void onUserWantToEditItems (ActionEvent event) {
        if (verifyIfEntryAddShortcutIsNotEmpty()) {
            shortcutAddClicked = false;
            showPictureButton(buttonAddShortcut, "picture/add.png");

            database.updateShortcutElement(shortcutElementSelected, inputAddText.getText(), inputAddUrl.getText());

            listShortcut.forEach(e -> {
                if (e.id == shortcutElementSelected.id) {
                    e.text = inputAddText.getText();
                    e.url = inputAddUrl.getText();
                    e.setValue(inputAddText.getText());
                }
            });

            inputAddText.clear();
            inputAddUrl.clear();
            anchorPaneViewAddShortcut.setVisible(false);
            scrollPaneViewShortcut.setVisible(true);
        }
    }

    public void setToFirstPlace (ShortcutElement shortcutElement) {
        shortcutElementSelected = shortcutElement;
        setInFirstPlace = true;
        listShortcut.remove(shortcutElement);
        listShortcut.add(0,shortcutElement);
        setInFirstPlace = false;
    }
}
