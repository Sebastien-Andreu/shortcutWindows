import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import singleton.SingletonPosScreen;
import singleton.SingletonShortcut;

import java.io.File;
import java.io.IOException;

public class Shortcut extends Application {

    public AnchorPane anchorShortcut;
    Database database = new Database();

    private final double appWidth = 441;
    private final double appMinSize = 0.05;
    private final double heightMostUsed = 130;

    public Stage stage = new Stage();
    public Scene scene;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) {
        try {
            setSaveFolder();
            database.addFirstColor();
            database.addFirstScreenValue();
            database.getPosScreen();
            database.getScreen();
            database.getColor();
            setStage(primaryStage);

            File folder = new File("shortcut");
            if (!folder.exists()) {
                try {
                    folder.mkdir();

                    File folder1 = new File("shortcut\\application");
                    if (!folder1.exists()) {
                        try {
                            folder1.mkdir();
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                    }

                    File folder2 = new File("shortcut\\folder");
                    if (!folder2.exists()) {
                        try {
                            folder2.mkdir();
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                        }
                    }
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setSaveFolder () {
        SingletonShortcut.saveFolder = System.getProperty("user.dir");
    }

    private void setStage (Stage primaryStage) throws IOException {
        anchorShortcut = new FXMLLoader(getClass().getResource("shortcut.fxml")).load();
        scene = new Scene(anchorShortcut);

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setOpacity(0);
        primaryStage.show();

        this.stage.setTitle("Barre de recherche");
        this.stage.initOwner(primaryStage);
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setIconified(false);
        this.stage.setAlwaysOnTop(true);
        this.stage.setResizable(false);

        if (SingletonPosScreen.instance.getValue() == 0) {
            this.stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMinX());
        } else {
            this.stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxX() - 1);
        }
        this.stage.setY(0);
        this.stage.setHeight(appMinSize);
        this.stage.setWidth(appMinSize);


        anchorShortcut.translateYProperty().addListener((observable, oldValue, newValue) -> {
            stage.setHeight(stage.getHeight() + newValue.longValue());
            anchorShortcut.setLayoutY(0);
            anchorShortcut.setTranslateY(0);
        });

        scene.setFill(Color.TRANSPARENT);
        this.stage.setScene(scene);
        this.stage.show();

        scene.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, evt -> {
            if (!SingletonShortcut.freezeApp) {
                if (SingletonShortcut.shortcutInternetController.listShortcut.size() < 4) {
                    this.stage.setHeight(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxY());
                    SingletonShortcut.shortcutController.setViewAccessible();
                } else {
                    this.stage.setHeight(heightMostUsed);
                    SingletonShortcut.shortcutController.setViewInaccessible();
                    SingletonShortcut.shortcutMostUsed.showListOfShortcutInternetMostUsed();
                }

                if (SingletonPosScreen.instance.getValue() == 1) {
                    stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxX() - appWidth);
                }
                stage.setWidth(appWidth);
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_EXITED, evt -> {
            if (!SingletonShortcut.freezeApp) {
                SingletonShortcut.shortcutController.setViewInaccessible();
                if (SingletonPosScreen.instance.getValue() == 1) {
                    stage.setX(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxX() - 1);
                }
                stage.setHeight(appMinSize);
                stage.setWidth(appMinSize);

                SingletonShortcut.shortcutInternetController.resetColorOfItem();
                SingletonShortcut.shortcutAppController.resetColorOfItem();
                SingletonShortcut.shortcutFolderController.resetColorOfItem();
            }
        });

        SingletonShortcut.shortcutMostUsed.swipeDown.setOnMouseClicked(e -> {
            this.stage.setHeight(Screen.getScreens().get(SingletonPosScreen.instance.getScreen()).getBounds().getMaxY());
            SingletonShortcut.shortcutController.setViewAccessible();
        });

        SingletonShortcut.shortcutMostUsed.param.setOnMouseClicked(e -> {
            SingletonShortcut.freezeApp = true;
            try {
                AnchorPane anchorParameter = new FXMLLoader(getClass().getResource("shortcutParameter.fxml")).load();
                SingletonShortcut.shortcutParameterController.setStage(this.stage);
                SingletonShortcut.shortcutParameterController.scene = this.scene;
                this.stage.setScene(new Scene(anchorParameter));
                this.stage.setHeight(anchorParameter.getPrefHeight());
                System.out.println(anchorParameter.getPrefHeight());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
