package singleton;

import shortcut.*;

public class SingletonShortcut {
    public static ShortcutInternetController shortcutInternetController;
    public static ShortcutFolderController shortcutFolderController;
    public static ShortcutAppController shortcutAppController;
    public static ShortcutMostUsed shortcutMostUsed;
    public static ShortcutController shortcutController;
    public static ShortcutParameterController shortcutParameterController;

    public static String saveFolder;

    public static boolean freezeApp = false;

    private SingletonShortcut () {}

    public static void updateColor() {
        shortcutMostUsed.setColor();
        shortcutInternetController.setColor();
        shortcutFolderController.setColor();
        shortcutAppController.setColor();
        shortcutMostUsed.setColor();
        shortcutController.setColor();
    }
}
