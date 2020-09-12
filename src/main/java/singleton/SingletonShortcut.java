package singleton;

import shortcut.ShortcutAppController;
import shortcut.ShortcutInternetController;
import shortcut.ShortcutFolderController;

public class SingletonShortcut {

    public static ShortcutInternetController shortcutInternetController;
    public static ShortcutFolderController shortcutFolderController;
    public static ShortcutAppController shortcutAppController;
    private SingletonShortcut () {}
}
