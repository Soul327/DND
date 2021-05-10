package Main;

public class Main {
	static Server server;
	static Client client;
	static Window window;
	
	public static int port = 3434;

	public static void main(String args[]) throws InterruptedException {
		new Main();
	}

	public Main() throws InterruptedException {
		server = new Server();
		client = new Client();
		server.start();
		Thread.sleep(100);
		client.start();

		window= new Window();
		window.start();
	}
}
