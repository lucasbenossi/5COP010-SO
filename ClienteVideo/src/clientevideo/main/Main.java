package clientevideo.main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		if(args.length > 0) {
			Globals.servidor = args[0];
		}
		
		try (Scanner scan = new Scanner(System.in);) {
			Client client = null;
			while(scan.hasNextLine()) {
				String message = scan.nextLine().trim();
				
				switch (message.substring(0, 2)) {
				case "10":
					client = new Client(Globals.servidor, message.substring(2, 3));
					break;
				case "12":
					if(client != null) {
						client.stop();
					}
					break;
				case "11":
				case "13":
					try (Socket socket = new Socket(Globals.servidor, Globals.PORTA_SERVER);
							PrintStream out = new PrintStream(socket.getOutputStream());
							Scanner in = new Scanner(socket.getInputStream());) {
						out.println(message);
						out.flush();
						System.out.println(in.nextLine().trim());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		}
	}


}
