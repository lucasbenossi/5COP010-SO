package clientevideo.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

public class Canal implements Runnable {
	private LinkedList<String> clients = new LinkedList<>();
	
	public Canal() {
		System.out.println("Canal criado");
	}

	@Override
	public void run() {
		for(String client : clients) {
			try (Socket socket = new Socket(client, Globals.PORTA_CLIENT);
					FileInputStream file = new FileInputStream("video");
					OutputStream out = socket.getOutputStream();) {
				byte[] buffer = new byte[Globals.BUFFER_SIZE];
				while(file.read(buffer) > 0) {
					out.write(buffer);
				}
				System.out.println("Video enviado para " + client);
				
			} catch (IOException e) {
				e.printStackTrace();
				removeClient(client);
			}
		}
	}
	
	public void addClient(String client) {
		try (Socket socket = new Socket(client, Globals.PORTA_CLIENT);
				PrintStream out = new PrintStream(socket.getOutputStream());) {
			synchronized (this.clients) {
				if (this.clients.size() < Globals.MAX_CLIENTES && !this.clients.contains(client)) {
					this.clients.add(client);
					out.println("10");
					out.flush();
					System.out.println("Cliente " + client + " adicionado");
				} else {
					out.println("00");
					out.flush();
					System.out.println("Cliente " + client + " não adicionado, máximo de clientes atingido");
				}
			}
		} catch (IOException e) {
		}
	}
	
	public void removeClient(String client) {
		synchronized (this.clients) {
			if(this.clients.contains(client)) {
				this.clients.remove(client);	
				System.out.println("Cliente " + client + " removido");
			}
		}
	}
	
	public String getClients() {
		String[] clients = this.clients.toArray(new String[this.clients.size()]);
		String string = "[";
		
		if(clients.length > 0) {
			string += "'" + clients[0] + "'";
			for(int i = 1; i < clients.length; i++) {
				string += ", '" + clients[i] + "'";
			}
		}
		
		string += "]";
		return string;
	}
	
	public String getNClients() {
		return Integer.toString(this.clients.size());
	}
}