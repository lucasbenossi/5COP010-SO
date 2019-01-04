package clientevideo.main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		try (Scanner scan = new Scanner(System.in);) {
			openServerSocket();
			
			while(scan.hasNextLine()) {
				String message = scan.nextLine();
				System.out.println("Enviado mensagem " + message.trim());
				
				try (Socket socket = new Socket(Globals.SERVER, Globals.PORTA_SERVER);
						PrintStream out = new PrintStream(socket.getOutputStream());
						Scanner in = new Scanner(socket.getInputStream());) {
					
					out.print(message);
					out.flush();
					
					switch (message.substring(0, 2)) {
					case "10":
						new Client();
						break;
					case "12":
						openServerSocket();
						break;
					case "11":
					case "13":
						System.out.println(in.nextLine());
						break;
					default:
						break;
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void openServerSocket() throws IOException {
		if (Globals.listener != null ) {
			Globals.listener.close();
		}
		Globals.listener = new ServerSocket(Globals.PORTA_CLIENT);
	}
	
}
