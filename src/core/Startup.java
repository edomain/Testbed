package core;

import screens.LineScreen;
import screens.ModelScreen;
import screens.TransformsScreen;
import util.LoggingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * 12/30/15
 * TODO(stephen):
 * make switching screens safe
 * ability to have a master screen controller where we can easily switch between multiple screens at run time
 Float*
 **/
public class Startup extends JPanel implements Runnable {
    private static Startup startupinstance;
    int width, height;
    static boolean startupflag = false;
    private volatile Thread gameThread;
    //NOTE: one bitmap instance shared among screens. could be multiple though
    private Bitmap bitmap;
    private Input input;

    private volatile Screen currentScreen;

    volatile int[] bitmapImageData;
    volatile BufferedImage bitmapImage;
    volatile boolean running;

    private Startup(int width, int height) {
        this.width = width;
        this.height = height;
        bitmapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bitmapImageData = ((DataBufferInt) bitmapImage.getRaster().getDataBuffer()).getData();
        bitmap = new Bitmap(width, height);
        input = new Input();

        Dimension dim = new Dimension(width, height);
        setPreferredSize(dim);
        setFocusable(true);
        addKeyListener(input);
        addMouseListener(input);
        requestFocus();
        setVisible(true);
    }

    public synchronized void stop() {
        running = false;
        //TODO(Stephen): Figure out a way to exit thread safe
        System.exit(0);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameThread = new Thread(this, "gameThread");
        Screen startScreen = new ModelScreen();// create new screen
        setCurrentScreen(startScreen);
        running = true;
        currentScreen = startScreen;
        gameThread.start();
    }

    @Override
    public void run() {
        final int MAX_YIELDS_PER_THREAD = 5;
        final int MAX_FRAMES_SKIPS = 5;
        final int PERIOD = Math.abs(1000 / 60);

        float delta = 1000.0f / 60.0f;

        long startTime, endTime, afterTime, sleepTime;
        long oversleepTime = 0L;
        int count = 0;
        long excess = 0;

        int fps = 0;

        long timed = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        while (running) {
            currentScreen.updateAndRender();
            drawToScreen();
            fps++;

            if (LoggingUtil.FPS && System.currentTimeMillis() - timed >= 1000) {
                frame.setTitle("FPS: " + fps + "fps :-: TARGET: 60.0fps");
//                System.out.println();
                timed += 1000;
                fps = 0;
            }

            endTime = System.currentTimeMillis();
            afterTime = endTime - startTime;
            sleepTime = PERIOD - afterTime - oversleepTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                }
                oversleepTime = (System.currentTimeMillis() - endTime) - sleepTime;
            } else {
                oversleepTime = 0L;
                excess -= sleepTime;

                if (count++ > MAX_YIELDS_PER_THREAD) {
                    count = 0;
                    Thread.yield();
                }

                int skips = 0;
                while (excess >= PERIOD && skips < MAX_FRAMES_SKIPS) {
                    excess -= PERIOD;
                    currentScreen.updateAndRender();
                    skips++;
                }
            }

            startTime = System.currentTimeMillis();
        }
    }

    public void drawToScreen() {
        Graphics g = null;
        try {
            g = this.getGraphics();
            bitmap.copyBuffers(bitmapImageData);
            if (g != null) {
                g.drawImage(bitmapImage, 0, 0, width, height, null);
            }
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } catch (Exception ex) {
        }
    }

    private static JFrame frame;
    public static void main(String[] args) {
        frame = new JFrame("2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(Startup.getStartupInstance(800, 700));
        frame.pack();
        frame.setVisible(true);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public synchronized void setCurrentScreen(Screen currentScreen) {
        this.currentScreen = currentScreen;
        currentScreen.init();
        input.setCurrentScreen(currentScreen);
    }

    private static Startup getStartupInstance(int width, int height) {
        if (!startupflag) {
            startupinstance = new Startup(width, height);
            startupflag = true;
        }
        return startupinstance;
    }

    public static Startup getStartupinstance() {
        if(startupflag) {
            return startupinstance;
        }
        return null;
    }

    static Startup getStartupInstance() {
        return startupinstance;
    }
}
