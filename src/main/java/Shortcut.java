import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import singleton.SingletonShortcut;

import java.io.File;
import java.io.IOException;

public class Shortcut extends Application {

    public AnchorPane anchorShortcut;

    private double screenHeight = Screen.getPrimary().getBounds().getHeight();
    public TranslateTransition transition;

    public Stage stage = new Stage();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) {
        try {
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

    private void setStage (Stage primaryStage) throws IOException {
        anchorShortcut = new FXMLLoader(getClass().getResource("shortcut.fxml")).load();
        Scene scene = new Scene(anchorShortcut);

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setOpacity(0);
        primaryStage.show();

        this.stage.setTitle("Barre de recherche");
        this.stage.initOwner(primaryStage);
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setIconified(false);
        this.stage.setAlwaysOnTop(true);
        this.stage.setResizable(false);
        this.stage.setX(0);
        this.stage.setY(0);
        this.stage.setHeight(0.05);
        this.stage.setWidth(0.05);

        anchorShortcut.translateYProperty().addListener((observable, oldValue, newValue) -> {
            stage.setHeight(stage.getHeight() + newValue.longValue());
            anchorShortcut.setLayoutY(0);
            anchorShortcut.setTranslateY(0);
        });

        if (SingletonShortcut.shortcutInternetController.listShortcut.isEmpty()) {
            setTransitionWhenEmpty();
        } else {
            setFirstTransition();
        }

        scene.setFill(Color.TRANSPARENT);

        this.stage.setScene(scene);
        this.stage.show();

        scene.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, evt ->  {
            if (!SingletonShortcut.freezeApp) {
                transition.stop();
                if (SingletonShortcut.shortcutInternetController.listShortcut.size() < 4) {
                    setTransitionWhenEmpty();
                    SingletonShortcut.shortcutController.setViewAccessible();
                } else {
                    setFirstTransition();
                    Platform.runLater(() -> {
                        SingletonShortcut.shortcutController.setViewInaccessible();
                        SingletonShortcut.shortcutMostUsed.showListOfShortcutInternetMostUsed();
                    });
                }

                stage.setHeight(0.05);
                stage.setWidth(441);
                transition.play();
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_EXITED, evt ->  {
            if (!SingletonShortcut.freezeApp) {
                transition.stop();
                SingletonShortcut.shortcutController.setViewInaccessible();
                stage.setHeight(0.05);
                stage.setWidth(0.05);
                setInitializedTransition();
                transition.play();
            }
        });

        SingletonShortcut.shortcutMostUsed.swipeDown.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            transition.stop();
            setSecondTransition();
            SingletonShortcut.shortcutController.setViewAccessible();
            transition.play();
        });
    }

    public void setInitializedTransition (){
        transition = new TranslateTransition();
        transition.setFromY(screenHeight);
        transition.setToY(0.05);

        transition.setDuration(Duration.seconds(0.0001));
        transition.setNode(anchorShortcut);
    }

    public void setFirstTransition () {
        transition = new TranslateTransition();
        transition.setFromY(0.05);
        transition.setToY(130);

        transition.setDuration(Duration.seconds(0.0001));
        transition.setNode(anchorShortcut);
    }

    public void setSecondTransition () {
        transition = new TranslateTransition();
        transition.setFromY(130);
        transition.setToY(screenHeight);

        transition.setDuration(Duration.seconds(0.0001));
        transition.setNode(anchorShortcut);
    }

    public void setTransitionWhenEmpty () {
        transition = new TranslateTransition();
        transition.setFromY(0.05);
        transition.setToY(screenHeight);

        transition.setDuration(Duration.seconds(0.0001));
        transition.setNode(anchorShortcut);
    }
}
