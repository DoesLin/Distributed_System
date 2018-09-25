import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

class UDPClient implements Runnable {
	InetSocketAddress isA; // the remote address
	DatagramSocket s; // the socket object
	DatagramPacket req, rep; // to prepare the request and reply messages
	private final int size = 2048; // the default size for the buffer array

	/** The builder. */
	UDPClient() {
		isA = new InetSocketAddress("10.172.5.196", 8080);
		// isA = new InetSocketAddress("localhost", 8080);
		s = null;
		req = rep = null;
	}

	/** The main run method for threading. */
	public void run() {
		try {
			s = new DatagramSocket();
			req = new DatagramPacket(new byte[size], 0, size, isA.getAddress(),
					isA.getPort());
			s.send(req);
			System.out.println("request sent");
			rep = new DatagramPacket(new byte[size], size);
			s.receive(rep);
			System.out.println("reply received");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPClient");
		}
	}
}

class UDPClientBuilder extends UDPRWTime {
	InetSocketAddress isA;
	DatagramSocket s;
	DatagramPacket req, rep;
	final int size = 2048;

	UDPClientBuilder() {
		isA = null;
		s = null;
		req = rep = null;
	}

	protected void setConnection() throws IOException {
		s = new DatagramSocket();

		// Set timeout here
		s.setSoTimeout(5000);

		// isA = new InetSocketAddress("10.172.5.196", 8080);
		isA = new InetSocketAddress("localhost", 8080);
		/** we can include more setting, later ... */
	}
}

class UDPClientHello extends UDPClientBuilder implements Runnable {
	public void run() {
		try {
			setConnection();
			req = new DatagramPacket(new byte[size], 0, size, isA.getAddress(),
					isA.getPort());
			s.send(req);
			System.out.println("request sent");
			rep = new DatagramPacket(new byte[size], size);
			s.receive(rep);
			System.out.println("reply received");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPClient");
		}
	}
}

class UDPClientTimeout extends UDPClientBuilder implements Runnable {
	public void run() {
		try {
			setConnection();
			// req = new DatagramPacket(new byte[size], 0, size,
			// isA.getAddress(),
			// isA.getPort());
			// s.send(req);
			System.out.println("request not sent");
			rep = new DatagramPacket(new byte[size], size);
			s.receive(rep);
			System.out.println("reply received");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPClient : Timeout");
		}
	}
}

class UDPClientMsg extends UDPClientBuilder implements Runnable {
	public void run() {
		try {
			setConnection();

			String myMsg = "Does : Hellow world !";
			req = getTextSendingPacket(isA, myMsg, size);

			s.send(req);
			System.out.println("request sent");

			rep = new DatagramPacket(new byte[size], size);
			s.receive(rep);
			System.out.println("reply received");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPClient : Timeout");
			s.close();
		}
	}
}

class UDPClientChat extends UDPClientBuilder implements Runnable {
	private Scanner sc;

	public void run() {
		try {
			setConnection();

			sc = new Scanner(System.in);
			String myMsg = sc.nextLine();

			while (myMsg.compareTo("end") != 0) {
				req = getTextSendingPacket(isA, "Does : " + myMsg, size);
				s.send(req);
				System.out.println("request sent");

				rep = new DatagramPacket(new byte[size], size);
				s.receive(rep);
				System.out.println("reply received");

				myMsg = sc.nextLine();
			}

			sc.close();
			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPClient : Timeout");
			sc.close();
			s.close();
		}
	}
}

class UDPClientTime extends UDPClientBuilder implements Runnable {
	private void syncWithServer() {
		long testDiff = 0;
		long diffTimeStamp = getTimeStamp(rep) - getTimeStamp() + testDiff;
		try {
			Thread.sleep((diffTimeStamp > 0) ? diffTimeStamp : 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			setConnection();

			req = new DatagramPacket(new byte[size], 0, size, isA.getAddress(),
					isA.getPort());
			s.send(req);
			System.out.println("request sent");

			rep = new DatagramPacket(new byte[size], size);
			s.receive(rep);
			System.out.println("reply received : server timestamp - "
					+ getTimeStamp(rep));
			System.out.println("client timestamp before - " + getTimeStamp());

			syncWithServer();
			System.out.println("client timestamp after - " + getTimeStamp());

			s.close();
		} catch (IOException e) {
			System.out.println("IOException UDPClient : Timeout");
			s.close();
		}
	}
}

class UDPClientNTP extends UDPClientBuilder implements Runnable {
	private long T1, T2, T3, T4, k;
	private int count = 0;
	
	private void commNTP() throws IOException {
		setConnection();

		req = new DatagramPacket(new byte[size], 0, size, isA.getAddress(),
				isA.getPort());

		T1 = getLocalTime();

		s.send(req);
		System.out.println("request sent");

		rep = new DatagramPacket(new byte[size], size);
		s.receive(rep);
		
		T3 = getTimeStamp(rep);
		T4 = getLocalTime();

		System.out.println("reply received");
		
		System.out.println("T1 (nanoTime) : " + T1);
		System.out.println("T2 (timeStamp) : " + T2);
		System.out.println("T3 (timeStamp) : " + T3);
		System.out.println("T4 (nanoTime) : " + T4);
		
		k = (T4 - T1) / 2;
		
		System.out.println("k: " + k);

		s.close();
	}
	
	private void commInPeriod(long lPeriod) throws IOException {
		while(count++ != 100) {
			try {
				Thread.sleep(lPeriod);
				commNTP();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			commInPeriod(250);
		} catch (IOException e) {
			System.out.println("IOException UDPClient : Timeout");
			s.close();
		}
	}
}
