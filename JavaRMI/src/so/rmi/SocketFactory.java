package so.rmi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

//implementado mas não usado

public class SocketFactory extends RMISocketFactory {

	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		return new ServerSocket(port, 5, InetAddress.getByName("192.168.56.101"));
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		return new Socket(host, port);
	}
}
