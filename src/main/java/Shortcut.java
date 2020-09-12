import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shortcut.ShortcutInternetController;

import java.io.File;

public class Shortcut extends Application {

    public ShortcutInternetController shortcutInternetController;

    public static void main (String[] args) {
        try {
            launch(args);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void start (Stage shortcut) throws Exception {
        try {
            Scene scene = new Scene(new FXMLLoader(getClass().getResource("shortcut.fxml")).load());
            shortcut.setScene(scene);
            shortcut.setResizable(false);

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

            shortcut.show();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
