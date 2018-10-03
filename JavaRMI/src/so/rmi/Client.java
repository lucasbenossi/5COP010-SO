package so.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

	public static void main(String[] args) {
		try {
            Registry registry = LocateRegistry.getRegistry("localhost", 2001);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello(1);
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

	}

}
