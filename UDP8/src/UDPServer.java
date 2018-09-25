import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

class UDPServer implements Runnable {
	private InetSocketAddress isA; // the address
	private DatagramSocket s; // the socket object
	private DatagramPacket req, rep; // to prepare the request and reply
										// messages
	private final int size = 2048; // the default size for the buffer array

	/** The builder. */
	UDPServer() {
		isA = new InetSocketAddress("localhost", 8080);
		s = null;
		req = rep = null;
	}

	/** The main run method for threading. */
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());
			req = new DatagramPacket(new byte[size], size);
			s.receive(req);
			System.out.println("request received");
			rep = new DatagramPacket(new byte[size], 0, size,
					req.getSocketAddress());
			s.send(rep);
			System.out.println("reply sent");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPServer");
		}
	}
}

class UDPServerBuilder extends UDPRWTime {
	InetSocketAddress isA;
	DatagramSocket s;
	DatagramPacket req, rep;
	final int size = 2048;

	UDPServerBuilder() {
		isA = new InetSocketAddress("localhost", 8080);
		s = null;
		req = rep = null;
	}
}

class UDPServerHello extends UDPServerBuilder implements Runnable {
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());
			req = new DatagramPacket(new byte[size], size);
			s.receive(req);
			System.out.println("request received");
			rep = new DatagramPacket(new byte[size], 0, size,
					req.getSocketAddress());
			s.send(rep);
			System.out.println("reply sent");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPServer");
		}
	}
}

class UDPServerTimeout extends UDPServerBuilder implements Runnable {
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());

			// Set timeout here
			s.setSoTimeout(5000);

			req = new DatagramPacket(new byte[size], size);
			s.receive(req);
			System.out.println("request received");
			rep = new DatagramPacket(new byte[size], 0, size,
					req.getSocketAddress());
			s.send(rep);
			System.out.println("reply sent");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPServer : Timeout");
		}
	}
}

class UDPServerMsg extends UDPServerBuilder implements Runnable {
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());

			// Set timeout here
			s.setSoTimeout(5000);

			req = new DatagramPacket(new byte[size], size);
			s.receive(req);
			System.out.println("request received : " + getMsg(req));

			rep = new DatagramPacket(new byte[size], 0, size,
					req.getSocketAddress());
			s.send(rep);
			System.out.println("reply sent");

			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPServer : Timeout");
			s.close();
		}
	}
}

class UDPServerChat extends UDPServerBuilder implements Runnable {
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());
			s.setSoTimeout(5000);

			while (true) {
				req = new DatagramPacket(new byte[size], size);
				s.receive(req);
				System.out.println("request received : " + getMsg(req));

				rep = new DatagramPacket(new byte[size], 0, size,
						req.getSocketAddress());
				s.send(rep);
				System.out.println("reply sent");
			}
		} catch (IOException e) {
			System.out.println("IOException UDPServer : Timeout");
			s.close();
		}
	}
}

class UDPServerTime extends UDPServerBuilder implements Runnable {
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());
			s.setSoTimeout(5000);

			req = new DatagramPacket(new byte[size], size);
			s.receive(req);
			System.out.println("request received");

			rep = new DatagramPacket(new byte[size], 0, size,
					req.getSocketAddress());
			setTimeStamp(rep);
			s.send(rep);
			System.out.println("reply sent");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPServer : Timeout");
			s.close();
		}
	}
}

class UDPServerNTP extends UDPServerBuilder implements Runnable {
	public void run() {
		try {
			s = new DatagramSocket(isA.getPort());
			s.setSoTimeout(5000);

			while (true) {
				req = new DatagramPacket(new byte[size], size);
				s.receive(req);
				System.out.println("request received");

				rep = new DatagramPacket(new byte[size], 0, size,
						req.getSocketAddress());
				setTimeStamp(rep);
				s.send(rep);
				System.out.println("reply sent");
			}
		} catch (IOException e) {
			System.out.println("IOException UDPServer : Timeout");
			s.close();
		}
	}
}
