package lmbenossi.Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	public static void main(String[] args) {
		ServerSocket listener = null;
        try {
        	listener = new ServerSocket(2000);
        	
        	System.out.println("Server started");
            
            try {
            	while(true) {
                	new ClientHandler(listener.accept()).start();
                }
            } catch (Exception e) {}
            
            listener.close();
        } catch(Exception e) {
        	System.out.println("ERRO: "+e);
        } finally {
        	try {
        		listener.close();
        	} catch(Exception e) {}
        }
    }
	
	private static class ClientHandler extends Thread {
		private Socket socket;
		PrintStream stream;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			try {
				this.stream = new PrintStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			try {
				Scanner scan = new Scanner(socket.getInputStream());
				while(scan.hasNextLine()) {
					String line = scan.nextLine();
					System.out.println(line);
					stream.println(scan.nextLine());
				}
				scan.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
