package singleton;

public class SingletonPosScreen {
    public static SingletonPosScreen SingletonPosScreen = new SingletonPosScreen();

    private int value;

    private SingletonPosScreen () {

    }

    public int getValue () {
        return this.value;
    }

    public void setValue ( int value ) {
        this.value = value;
    }

    public void updatePos () {
        SingletonShortcut.shortcutMostUsed.groupPosScreen.getToggles().get(value).setSelected(true);
    }
}
