import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

/** The TCPInfo to display the socket's info. */
class TCPInfo {
	/** The internal class/structure to trace the socket parametters. */
	class SocketInfo {
		String lA, rA, tC;
		int lP, rP, sbS, rbS, tO, soLinger;
		boolean bounded, connected, closed, isIPV6, noDelay;

		SocketInfo() {
			lA = rA = tC = null;
			lP = rP = sbS = rbS = tO = soLinger = -1;
			bounded = connected = closed = isIPV6 = noDelay = false;
		}
	}

	/** To print the passive sever socket's parameters. */
	void ssInfo(String event, ServerSocket ss) throws SocketException, IOException {
		ssI = new SocketInfo();
		ssI.isIPV6 = isIPV6(ss.getInetAddress());
		ssI.lA = getAdressName(ss.getInetAddress());
		ssI.lP = ss.getLocalPort();
		ssI.bounded = ss.isBound();
		ssI.closed = ss.isClosed();
		if (!ssI.closed) {
			ssI.tO = ss.getSoTimeout();
			ssI.rbS = ss.getReceiveBufferSize();
		}
		print(event, ssI);
	}

	private SocketInfo ssI;

	/** To print the active server socket's parameters. */
	void sInfo(String event, Socket s) throws SocketException, IOException {
		sI = new SocketInfo();
		sI.isIPV6 = isIPV6(s.getInetAddress());
		sI.lA = getAdressName(s.getLocalAddress());
		sI.lP = s.getLocalPort();
		sI.rA = getAdressName(s.getInetAddress());
		sI.rP = s.getPort();
		sI.bounded = s.isBound();
		sI.connected = s.isConnected();
		sI.closed = s.isClosed();
		if (!sI.closed) {
			sI.tO = s.getSoTimeout();
			sI.soLinger = s.getSoLinger();
			sI.sbS = s.getSendBufferSize();
			sI.rbS = s.getReceiveBufferSize();
			// sI.noDelay = s.getTcpNoDelay();
			// sI.tC = Integer.toHexString(s.getTrafficClass());
		}
		print(event, sI);
	}

	private SocketInfo sI;

	private static String getAdressName(InetAddress iA) {
		if (iA != null)
			return iA.toString();
		return null;
	}

	private static boolean isIPV6(InetAddress iA) {
		if (iA instanceof Inet6Address)
			return true;
		return false;
	}

	private void print(String event, SocketInfo sI) {
		System.out.println(event + ":\n" + "IPV6: " + sI.isIPV6 + "\n" + "local \tadress:" + sI.lA + "\t port:" + sI.lP
				+ "\n" + "remote \tadress:" + sI.rA + "\t port:" + sI.rP + "\n" + "bounded: " + sI.bounded + "\n"
				+ "connected: " + sI.connected + "\n" + "closed: " + sI.closed + "\n" + "timeout: " + sI.tO
				+ "\tso linger: " + sI.soLinger + "\n" + "buffer \tsend:" + sI.sbS + "\treceive:" + sI.rbS + "\n");
	}
}

class TCPBuffer extends TCPInfo {
	protected byte[] buffer;
	private final int size = 8192;

	/** The set method for the buffer. */
	void setStreamBuffer(int size) {
		if (size > 0)
			buffer = new byte[size];
		else
			buffer = new byte[this.size];
	}
}

class TCPMessage extends TCPBuffer {
	/** The (simple) text write method. */
	void writeMessage(OutputStream out, String msOut) throws IOException {
		if ((out != null) & (msOut != null)) {
			fillChar(msOut);
			out.write(buffer);
			out.flush();
			clearBuffer();
		}
	}

	private void fillChar(String msOut) {
		if (msOut != null)
			if (msOut.length() < buffer.length)
				for (int i = 0; i < msOut.length(); i++)
					buffer[i] = (byte) msOut.charAt(i);
	}

	void clearBuffer() {
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0;
	}

	/** The (simple) text read method. */
	String readMessage(InputStream in) throws IOException {
		if (in != null) {
			in.read(buffer);
			count = count();
			if (count > 0)
				return new String(buffer, 0, count);
		}
		return null;
	}

	private int count;

	protected int count() {
		for (int i = 0; i < buffer.length; i++)
			if (buffer[i] == 0)
				return i;
		return buffer.length;
	}

	void loopWriteMessage(OutputStream out, int loop) throws IOException {
		for (int i = 0; i < loop; i++) {
			fillAtRandom(buffer);
			out.write(buffer);
			out.flush();
		}
	}

	private void fillAtRandom(byte[] buffer) {
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = (byte) r.nextInt(256);
	}

	private Random r = new Random();
}

class TCPFile extends TCPMessage {

	void TFTransfertFile(OutputStream out, String inputFile) throws IOException {
		writeInputStream(out, new FileInputStream(new File(inputFile)), new File(inputFile).length());

		out.flush();
		out.close();
	}

	void TFEcrireFile(int fileRdSize, BufferedOutputStream fileBos) throws IOException {
		fileBos.write(buffer, 0, fileRdSize);
		fileBos.flush();
	}

	void TFAfficherFile(int fileRdSize) throws IOException {
		System.out.print(getReadString(fileRdSize));
	}

	int getReadSize(InputStream in) throws IOException {
		return in.read(buffer);
	}

	private void writeInputStream(OutputStream out, FileInputStream fileFis, long fileLength) throws IOException {
		BufferedInputStream fileBis = new BufferedInputStream(fileFis);
		long fileCur = 0;

		while (fileCur != fileLength) {
			int fileRdSize = 1024;
			if (fileLength - fileCur >= fileRdSize)
				fileCur += fileRdSize;
			else {
				fileRdSize = (int) (fileLength - fileCur);
				fileCur = fileLength;
			}

			setStreamBuffer(fileRdSize);
			fileBis.read(buffer, 0, fileRdSize);
			out.write(buffer);
			// System.out.println("Sending file - " + (fileCur * 100) / fileLength + "%
			// complete !");
		}

		fileFis.close();
		fileBis.close();
	}

	private String getReadString(int readSize) {
		return new String(buffer, 0, readSize);
	}
}
