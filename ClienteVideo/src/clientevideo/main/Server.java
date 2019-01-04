package clientevideo.main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Server implements Runnable {
	private ServerSocket listener = null;
	private Canal canal = null;
	
	public Server() {
		try {
			this.listener = new ServerSocket(Globals.PORTA_SERVER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.canal = new Canal();
		
		new Thread(this).start();
		
		System.out.println("Server escutando");
	}
	
	public void sendVideo() {
		try {
			Thread thread = new Thread(canal);
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Socket socket = listener.accept();
				ClientHandler handler = new ClientHandler(socket);
				new Thread(handler).start();
			} catch (SocketException e) {
				System.out.println("Parando server");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		try {
			this.listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class ClientHandler implements Runnable {
		private Socket socket = null;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try (Scanner scan = new Scanner(this.socket.getInputStream());
					PrintStream out = new PrintStream(this.socket.getOutputStream());) {
				String message = scan.nextLine().trim();
				String client = socket.getInetAddress().getHostAddress();
				
				switch (message.substring(0, 2)) {
				case "10":
					canal.addClient(client);
					break;
				case "12":
					canal.removeClient(client);
					break;
				case "11":
					out.println(canal.getClients());
					out.flush();
					System.out.println("Enviado lista de clientes para " + client);
					break;
				case "13":
					out.println(canal.getNClients());
					out.flush();
					System.out.println("Enviado quantidade de clientes para " + client);
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
