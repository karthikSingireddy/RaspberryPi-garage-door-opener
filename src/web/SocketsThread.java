package web;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketsThread extends Thread {

    private ServerSocket ss;
    private Socket s;

    private DataInputStream di;


    public SocketsThread() {
        try {
            this.ss = new ServerSocket(45643);
            this.s = this.ss.accept();
            this.di = new DataInputStream(this.s.getInputStream());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void tick() {
        try {
            this.message = di.readUTF();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(this.message);
    }

    private String message;

    public String getMessage() {
        return this.message;
    }


    public void run() {
        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while(true) {
            now = System.nanoTime();
            delta += (now-lastTime) / timePerTick;
            timer += now-lastTime;
            lastTime = now;
            if(delta>=1) {
                synchronized (this) {
                    tick();
                }
                ticks++;
                delta--;
            }
        }
    }

}
