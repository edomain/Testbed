package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 1/2/16
 **/
public class Input implements KeyListener, MouseListener {
    //TODO(Stephen): Why does moving the mouse in a swing application
    // cause us to drop frames
    private Screen currentScreen;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentScreen.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setCurrentScreen(Screen screen) {
        currentScreen = screen;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
