import java.net.*;
import java.io.*;

class TCPClient implements Runnable {
	private Socket s;
	private InetSocketAddress isA;

	// the client socket
	// the remote address
	/** The builder. */
	TCPClient() {
		s = null;
		isA = new InetSocketAddress("localhost", 8085);
	}

	/** The main method for threading. */
	public void run() {
		try {
			System.out.println("TCPClient launched ...");
			s = new Socket(isA.getHostName(), isA.getPort());
			System.out.println("Hello, the client is connected");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException TCPClient");
		}
	}
}

class TCPClientBuilder extends TCPFile {
	Socket s;
	InetSocketAddress isA;

	TCPClientBuilder() {
		s = null;
		isA = null;
	}

	protected void setSocket() throws IOException {
		isA = new InetSocketAddress("localhost", 8085);
		s = new Socket(isA.getHostName(), isA.getPort());
		setStreamBuffer(2024);
	}
}

class TCPClientHello extends TCPClientBuilder implements Runnable {
	public void run() {
		try {
			System.out.println("TCPClientHello launched ...");
			setSocket();
			System.out.println("Hello, the client is connected");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException TCPClientHello");
		}
	}
}

class TCPClientInfo extends TCPClientBuilder implements Runnable {
	public void run() {
		try {
			System.out.println("TCPClientHello launched ...");
			setSocket();
			System.out.println("Hello, the client is connected");
			s.close();
		} catch (IOException e) {
			System.out.println("IOException TCPClientHello");
		}
	}
}

class TCPClientMessage extends TCPClientBuilder implements Runnable {
	public void run() {
		try {
			System.out.println("TCPClientMessage launched ...");
			setSocket();
			System.out.println("Hello, the client is connected");

			// Message
			String msOut = "Aujourd'hui, TP ASR Java.";
			OutputStream out = s.getOutputStream();
			writeMessage(out, msOut);
			out.close();

			s.close();
		} catch (IOException e) {
			System.out.println("IOException TCPClientMessage");
		}
	}
}

class TCPClientTimeout extends TCPClientBuilder implements Runnable {
	public void run() {
		try {
			int count = 0;
			while (count++ < 5) {
				System.out.println("TCPClientMessage launched ...");
				setSocket();
				System.out.println("Hello, the client is connected");

				// Message
				String msOut = "Aujourd'hui, TP ASR Java. - " + count;
				OutputStream out = s.getOutputStream();
				writeMessage(out, msOut);
				out.close();

				s.close();
			}
		} catch (IOException e) {
			System.out.println("IOException TCPClientTimeout");
		}
	}
}

class TCPClientLMessage extends TCPClientBuilder implements Runnable {
	public void run() {
		try {
			long count = 0;
			while (count < Long.MAX_VALUE / 10 * 8) {
				System.out.println("TCPClientMessage launched ...");
				setSocket();
				System.out.println("Hello, the client is connected");

				// Message
				String msOut = "Testing large message - " + count;
				OutputStream out = s.getOutputStream();
				writeMessage(out, msOut);
				out.close();
				if (count < Long.MAX_VALUE / 10 * 9) {
					count += Long.MAX_VALUE / 10;
				}

				s.close();
			}
		} catch (IOException e) {
			System.out.println("IOException TCPClientTimeout");
		}
	}
}

class TCPClientFile extends TCPClientBuilder implements Runnable {

	public void run() {
		try {
			System.out.println("TCPClientMessage launched ...");
			setSocket();
			System.out.println("Hello, the client is connected");

			int fileRdSize = 0;
			FileOutputStream fileFos = new FileOutputStream("outputFile.txt");
			BufferedOutputStream fileBos = new BufferedOutputStream(fileFos);

			while ((fileRdSize = getReadSize(s.getInputStream())) != -1) {
				TFAfficherFile(fileRdSize);
				TFEcrireFile(fileRdSize, fileBos);
			}
			
			fileBos.close();
			fileFos.close();
			s.close();

			System.out.println("File received successfully !");

		} catch (IOException e) {
			System.out.println("IOException TCPClientTimeout");
		}
	}
}
