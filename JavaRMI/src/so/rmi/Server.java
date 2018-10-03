package so.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {

	@Override
	public String sayHello(int i) throws RemoteException {
		return "Hello " + i;
	}
	
	public static void main(String args[]) {
		try {
			Server server = new Server();
			Hello stub = (Hello) UnicastRemoteObject.exportObject(server, 2001);
			Registry registry = LocateRegistry.getRegistry("localhost", 2001);
			registry.bind("Hello", stub);
			
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server error");
			e.printStackTrace(System.err);
		}
	}

}
