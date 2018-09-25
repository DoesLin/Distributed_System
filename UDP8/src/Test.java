
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new Thread(new UDPServer()).start();
		// new Thread(new UDPClient()).start();
		
		// new Thread(new UDPServerHello()).start();
		// new Thread(new UDPClientHello()).start();
		
		// new Thread(new UDPServerTimeout()).start();
		// new Thread(new UDPClientTimeout()).start();
		
		// new Thread(new UDPServerMsg()).start();
		// new Thread(new UDPClientMsg()).start();
		
		// new Thread(new UDPServerChat()).start();
		// new Thread(new UDPClientChat()).start();
		
		// new Thread(new UDPServerTime()).start();
		// new Thread(new UDPClientTime()).start();
		
		new Thread(new UDPServerNTP()).start();
		new Thread(new UDPClientNTP()).start();
	}

}