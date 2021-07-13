package singleton;

public class SingletonPosScreen {
    public static SingletonPosScreen instance = new SingletonPosScreen();

    private int value;
    private int screen = 0;

    private SingletonPosScreen () { }

    public int getValue () {
        return this.value;
    }

    public void setValue ( int value ) {
        this.value = value;
    }

    public void updatePos () {
        SingletonShortcut.shortcutParameterController.groupPosScreen.getToggles().get(value).setSelected(true);
    }

    public int getScreen () { return this.screen; }

    public void updateScreen ( int screen ) {
        this.screen = screen;
    }
}
