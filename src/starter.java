import server.Server;

public class starter {
    public static void main(String[] args) {
        Server s = new Server("hi", 500, 500);
        s.start();
    }
}
