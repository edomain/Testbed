package core;

import java.awt.event.KeyEvent;
import java.util.Scanner;

/**
 * 12/30/15
 **/
public abstract class Screen {
    protected Bitmap bitmap;

    public Screen() {
        bitmap = Startup.getStartupInstance().getBitmap();
    }

    public abstract void init();

    public abstract void updateAndRender();

    public abstract void keyPressed(KeyEvent e);

    public void setCurrentScreen(Screen currentScreen) {
        Startup.getStartupInstance().setCurrentScreen(currentScreen);
    }
}
