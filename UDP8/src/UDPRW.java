import java.io.IOException;
import java.net.*;

class UDPRWEmpty extends UDPInfo {
	/** The buffer array. */
	// private byte[] sB;

	/** To prepare a sending packet at a given size. */
	protected DatagramPacket getSendingPacket(InetSocketAddress isAR, int size)
			throws IOException {
		return new DatagramPacket(new byte[size], 0, size, isAR.getAddress(),
				isAR.getPort());
	}

	/** To prepare a receiving packet at a given size. */
	protected DatagramPacket getReceivingPacket(int size) throws IOException {
		return new DatagramPacket(new byte[size], size);
	}

}

class UDPRWText extends UDPRWEmpty {
	/** The buffer array. */
	private byte[] sB;

	/** To create a sending packet send with a txt message. */
	protected DatagramPacket getTextSendingPacket(InetSocketAddress isA,
			String msg, int size) throws IOException {
		sB = toBytes(msg, new byte[size]);
		return new DatagramPacket(sB, 0, sB.length, isA.getAddress(),
				isA.getPort());
	}

	/** To set the Msg to a parametter packet. */
	protected void setMsg(DatagramPacket dP, String msg) throws IOException {
		toBytes(msg, dP.getData());
	}

	private byte[] array;

	private byte[] toBytes(String msg, byte[] lbuf) {
		array = msg.getBytes();
		if (array.length < lbuf.length)
			for (int i = 0; i < array.length; i++)
				lbuf[i] = array[i];
		return lbuf;
	}

	private int p;

	/** To extract the txt message from a packet. */
	protected String getMsg(DatagramPacket dP) {
		sB = dP.getData();
		for (int i = 0; i < sB.length; i++) {
			if (sB[i] == 0) {
				p = i;
				i = sB.length;
			}
		}
		return new String(dP.getData(), 0, p);
	}

}

class UDPRWTime extends UDPRWText {
	private byte[] sB;
	private long tstamp;

	/** The buffer array. */
	/** The timestamp. */
	/** To get the local time. */
	protected long getLocalTime() {
		return System.nanoTime();
	}

	/** To get the timestamp. */
	protected long getTimeStamp() {
		return System.currentTimeMillis();
	}

	/** To get a sending packet with a timestamp. */
	protected DatagramPacket getTimeSendingPacket(InetSocketAddress isA,
			int size) throws IOException {
		tstamp = getTimeStamp();
		sB = toBytes(tstamp, new byte[size]);
		return new DatagramPacket(sB, 0, sB.length, isA.getAddress(),
				isA.getPort());
	}

	/** To set the timestamp to a parametter packet. */
	protected void setTimeStamp(DatagramPacket dP) {
		tstamp = getTimeStamp();
		sB = toBytes(tstamp, dP.getData());
	}

	private byte[] toBytes(long data, byte[] lbuf) {
		for (int i = 0; i < 8; i++)
			lbuf[i] = (byte) ((data >> (7 - i) * 8) & 0xff);
		return lbuf;
	}

	/** To extract timestamp from a receiving packet. */
	protected long getTimeStamp(DatagramPacket dP) {
		return getLong(dP.getData());
	}

	private long getLong(byte[] by) {
		value = 0;
		for (int i = 0; i < 8; i++) {
			value = (value << 8) + (by[i] & 0xff);
		}
		return value;
	}

	private long value;
}
