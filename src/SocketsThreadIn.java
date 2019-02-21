import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketsThreadIn extends Thread {
	
	public final int port = 32765;
	
	public final String filename = "lights.py";
	public final String filepath = "/home/pi/Desktop/";
	public final String command = "sudo python " + filepath + filename;
	
	public static void main(String[] args) throws Exception {
	SocketsThreadIn sti = new SocketsThreadIn();
	sti.run();

//		ServerSocket ss = new ServerSocket(32714 - 1);
//		Socket s = ss.accept();
//		DataInputStream dis = new DataInputStream(s.getInputStream());
//		System.out.println(dis.readUTF());

	}

	public SocketsThreadIn() {
		try {
			System.out.println("in try ");
			this.ss = new ServerSocket(32714-1);
			System.out.println("serversocket created");
			this.s = ss.accept();
			System.out.println("connected");
			this.dis = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("not connected");
		}
	}
	
	public void runPyFile() {
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void tick() {
		try {
			this.message = dis.readUTF();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("read: " + this.message);
	}

	@Override
	public void run() {

		final int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (true) {
			// Math for limiting frames
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			;
			lastTime = now;
			if (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}

			if (timer >= 1000000000) {
				// Message for frames can be put here
				// use ticks for amount of frames per second
			}
		}
	}

	private ServerSocket ss;
	private Socket s;
	private DataInputStream dis;
	private String message;
}
