import java.net.*;
import java.io.*;

class TCPServer implements Runnable {
	// passive
	private ServerSocket ss;
	// active
	private Socket s;
	private InetSocketAddress isA;

	// the passive and active sockets
	// the address
	/** The main method for threading. */
	TCPServer() {
		ss = null;
		s = null;
		isA = new InetSocketAddress("localhost", 8085);
	}

	/** The main method for threading. */
	public void run() {
		try {
			System.out.println("TCPServer launched ...");
			ss = new ServerSocket(isA.getPort());
			s = ss.accept();
			System.out.println("Hello, the server accepts");
			s.close();
			ss.close();
		} catch (IOException e) {
			System.out.println("IOException TCPServer");
		}
	}
}

class TCPServerBuilder extends TCPFile {
	// passive
	protected ServerSocket ss;
	// active
	protected Socket s;
	private InetSocketAddress isA;

	// the passive and active sockets
	// the address
	/** The main method for threading. */
	TCPServerBuilder() {
		ss = null;
		s = null;
		isA = null;
	}

	protected void setSocket() throws IOException {
		isA = new InetSocketAddress("localhost", 8085);
		ss = new ServerSocket(isA.getPort());
		setStreamBuffer(ss.getReceiveBufferSize());
	}
}

class TCPServerHello extends TCPServerBuilder implements Runnable {
	public void run() {
		try {
			System.out.println("TCPServer launched ...");
			setSocket();
			s = ss.accept();
			System.out.println("Hello, the server accepts");
			s.close();
			ss.close();
		} catch (IOException e) {
			System.out.println("IOException TCPServer");
		}
	}
}

class TCPServerInfo extends TCPServerBuilder implements Runnable {
	public void run() {
		try {
			setSocket();
			ssInfo("The server sets the passive socket", ss);
			s = ss.accept();
			sInfo("The server accepts the connexion", s);
			s.close();
			sInfo("The server closes a connexion", s);
			ss.close();
			ssInfo("The server closes the passive socket", ss);
		} catch (IOException e) {
			System.out.println("IOException TCPServerInfo");
		}
	}
}

class TCPServerMessage extends TCPServerBuilder implements Runnable {
	public void run() {
		try {
			setSocket();
			ssInfo("The server sets the passive socket", ss);
			s = ss.accept();
			sInfo("The server accepts the connexion", s);

			// Message
			InputStream in = s.getInputStream();
			System.out.println(readMessage(in));
			in.close();

			s.close();
			sInfo("The server closes a connexion", s);
			ss.close();
			ssInfo("The server closes the passive socket", ss);
		} catch (IOException e) {
			System.out.println("IOException TCPServerInfo");
		}
	}
}

class TCPServerTimeout extends TCPServerBuilder implements Runnable {
	public void run() {
		try {
			setSocket();
			ss.setSoTimeout(5000);
			while (true) {
				s = ss.accept();
				s.setSoTimeout(1000);

				// Message
				InputStream in = s.getInputStream();
				System.out.println(readMessage(in));
				in.close();

				s.close();
			}
		} catch (IOException e) {
			try {
				s.close();
				ss.close();
			} catch (IOException e1) {
				System.out.println("IOException TCPServerTimeout - fail close");
			}
			System.out.println("IOException TCPServerTimeout");
		}
	}
}

class TCPServerLMessage extends TCPServerBuilder implements Runnable {
	public void run() {
		try {
			setSocket();
			ss.setSoTimeout(5000);
			while (true) {
				s = ss.accept();
				s.setSoTimeout(1000);

				// Message
				InputStream in = s.getInputStream();
				System.out.println(readMessage(in));
				in.close();

				s.close();
			}
		} catch (IOException e) {
			try {
				s.close();
				ss.close();
			} catch (IOException e1) {
				System.out.println("IOException TCPServerLMessage - fail close");
			}
			System.out.println("IOException TCPServerTimeout");
		}
	}
}

class TCPServerFile extends TCPServerBuilder implements Runnable {
	public void run() {
		try {
			setSocket();
			// ss.setSoTimeout(5000);

			while (true) {
				s = ss.accept();
				s.setSoTimeout(1000);

				new Thread(new ServerFile(ss, s)).start();
			}
		} catch (IOException e) {
			try {
				s.close();
				ss.close();
			} catch (IOException e1) {
				System.out.println("IOException TCPServerFile - fail close");
			}
			System.out.println("IOException TCPServerTimeout");
		}
	}
}

class ServerFile extends TCPServerBuilder implements Runnable {
	ServerFile(ServerSocket ss, Socket s) {
		this.ss = ss;
		this.s = s;
	}

	public void run() {
		try {
			TFTransfertFile(s.getOutputStream(), "test.txt");
			// TFTransfertFile(s.getOutputStream(), "file92Kio.txt");
			// TFTransfertFile(s.getOutputStream(), "db-small.xml");

			s.close();
			System.out.println("File sent succesfully !");
		} catch (IOException e) {
			System.out.println("IOException ServerFile - fail close");
		}

	}
}
