package server;

import Display.Display;
import web.SocketsThread;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Server implements Runnable{

    private Display display;
    private BufferStrategy bs;
    private Graphics g;

    private SocketsThread st;

    private String title;
    private int width, height;

    public Server(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        display = new Display(title, width, height);
        st = new SocketsThread();
    }

    public void tick() {
        String s = st.getMessage();
        draw = s.equals("hi");
    }
    private boolean draw = false;

    public void runPyFile() {
        try {
            Runtime.getRuntime().exec(command);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public final String filename = "";
    public final String filepath = "";
    public final String command = "sudo python " + filepath + " " + filename;

    public void render() {
        bs = display.getCanvas().getBufferStrategy();
        if(bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        g.clearRect(0,0,width, height);

        if(draw) {
            g.setColor(Color.BLACK);
            g.drawRect(50, 50, 50,50);
            draw = false;
        }


        bs.show();
        g.dispose();
    }

    private void init() {

    }


    @Override
    public void run() {
        init();

        System.out.println("init done");

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while(running) {
            // Math for limiting frames
            now = System.nanoTime();
            delta += (now-lastTime) / timePerTick;
            timer += now-lastTime;;
            lastTime = now;
            if(delta>=1) {
                tick();
                render();
                ticks++;
                delta--;
            }

            if(timer >= 1000000000) {
                // Message for frames can be put here
                // use ticks for amount of frames per second

            }
        }
        stop();
    }

    public boolean running;
    private Thread thread;

    public synchronized void start() {
        if(running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
        // Start other threads here
        st.start();
    }
    public synchronized void stop() {
        if(!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
