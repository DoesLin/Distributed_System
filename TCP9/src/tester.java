public class tester {

	public static void main(String[] args) {

		// Question 1
		// new Thread(new TCPServer()).start();
		// new Thread(new TCPClient()).start();

		// Question 2
		// new Thread(new TCPServerHello()).start();
		// new Thread(new TCPClientHello()).start();

		// Question 3
		// new Thread(new TCPServerInfo()).start();
		// new Thread(new TCPClientInfo()).start();

		// Question 4
		// new Thread(new TCPServerMessage()).start();
		// new Thread(new TCPClientMessage()).start();

		// Question 6
		// new Thread(new TCPServerTimeout()).start();
		// new Thread(new TCPClientTimeout()).start();

		// Question 7
		// new Thread(new TCPServerLMessage()).start();
		// new Thread(new TCPClientLMessage()).start();

		// Question 8 - 9
		new Thread(new TCPServerFile()).start();
		new Thread(new TCPClientFile()).start();
	}

}
