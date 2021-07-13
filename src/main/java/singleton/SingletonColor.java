package singleton;


import javafx.scene.paint.Color;

public class SingletonColor {

    public static SingletonColor instance = new SingletonColor();

    private String background, backgroundTitle, line, button;

    private SingletonColor () {
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setBackgroundTitle(String backgroundTitle) {
        this.backgroundTitle = backgroundTitle;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setButton(String button) {
        this.button = button;
    }


    public String getBackground() {
        return "#" + Color.web(background).toString().substring(2);
    }

    public String getLine() {
        return "#" + Color.web(line).toString().substring(2);
    }

    public String getButton() {
        return "#" + Color.web(button).toString().substring(2);
    }

    public String getBackgroundTitle () {
        return "#" + Color.web(backgroundTitle).toString().substring(2);
    }
}
