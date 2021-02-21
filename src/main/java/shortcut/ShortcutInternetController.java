package shortcut;

import element.ShortcutElement;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import database.Database;
import javafx.scene.layout.HBox;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

import java.net.URI;
import java.net.URISyntaxException;
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
    public HBox hboxTitle;

    @FXML
    public Button buttonAddShortcut, buttonDelShortcut, buttonEditShortcut, buttonValidAddShortcut, buttonValidEditShortcut;


    public ShortcutElement shortcutElementSelected = null;
    public boolean updateList = false;
    private boolean shortcutAddClicked = false;

    public Database database;

    public ObservableList<ShortcutElement> listShortcut = FXCollections.observableArrayList();


    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutInternetController = this;
            setColor();
            this.listShortcut.addListener(this::eventListenerShortcut);

            database = new Database();

            showPictureButton(buttonEditShortcut, "picture/edit.png");
            showPictureButton(buttonAddShortcut, "picture/add.png");
            showPictureButton(buttonDelShortcut, "picture/delete.png");

            initializeListOfShortCut();

            inputAddUrl.textProperty().addListener(e -> {
                if (isURL(inputAddUrl.getText())) {
                    try {
                        inputAddText.setText(getDomainName(inputAddUrl.getText()));
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String name = uri.getHost();
        name = name.startsWith("www.") ? name.substring(4) : name;
        name = name.substring(0,name.lastIndexOf('.'));
        return (name.substring(0,1).toUpperCase()) +  (name.substring(1,name.length()));
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutElement> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                this.showShortcut.getChildren().add(listShortcut.get(listShortcut.size()-1).getView());
            }
            if (change.wasRemoved()) {
                if (!listShortcut.isEmpty()){
                    if (!updateList) {
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
                database.deleteShortcutElement(shortcutElementSelected, listShortcut);
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

    public void updateView (int start, int stop) {
        if (start < stop) {
            for (int i = start; i < listShortcut.size(); ++i) {
                if (i != stop) {
                    listShortcut.get(i).view.toFront();
                }
            }
        } else {
            for (int i = start; i >= 0; --i) {
                if (i != stop) {
                    listShortcut.get(i).view.toBack();
                }
            }
        }
    }

    public void setColor () {
        resetColorOfItem();
        hboxTitle.setStyle("-fx-background-color: "+ SingletonColor.singletonColor.getBackgroundTitle() + "; -fx-border-color: "+ SingletonColor.singletonColor.getLine() + "; -fx-border-width: 0 0 3 0;");
        buttonAddShortcut.setStyle("-fx-background-radius: 20; -fx-background-color: "+ SingletonColor.singletonColor.getButton() + ";");
        buttonDelShortcut.setStyle("-fx-background-radius: 20; -fx-background-color: "+ SingletonColor.singletonColor.getButton() + ";");
        buttonEditShortcut.setStyle("-fx-background-radius: 20; -fx-background-color: "+ SingletonColor.singletonColor.getButton() + ";");

        scrollPaneViewShortcut.setStyle("-fx-background-color: transparent; -fx-border-color: "+ SingletonColor.singletonColor.getBackground() + "; -fx-border-width: 0 0 3 0;");
        showShortcut.setStyle("-fx-background-color: "+ SingletonColor.singletonColor.getBackground() + "; -fx-hgap: 20; -fx-vgap: 12;");

        inputAddText.setStyle("-fx-background-color: " + SingletonColor.singletonColor.getBackgroundTitle() + "; -fx-text-inner-color: #ffffff;");
        inputAddUrl.setStyle("-fx-background-color: " + SingletonColor.singletonColor.getBackgroundTitle() +"; -fx-text-inner-color: #ffffff;");
        buttonValidAddShortcut.setStyle("-fx-background-color: " + SingletonColor.singletonColor.getButton() +";");
        buttonValidEditShortcut.setStyle("-fx-background-color: " + SingletonColor.singletonColor.getButton() +";");
    }
}
