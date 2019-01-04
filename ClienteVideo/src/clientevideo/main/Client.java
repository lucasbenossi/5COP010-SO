package clientevideo.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Client implements Runnable {
	private Thread thread = null;
	private ServerSocket listener = null;
	private String servidor = null;
	private String channel = null;
	private LinkedList<String> serverQueue = new LinkedList<String>();
	
	public Client(String servidor, String channel) {
		this.channel = channel;
		
		try {
			listener = new ServerSocket(Globals.PORTA_CLIENT);
			
			serverQueue.push(servidor);
			tentarServidor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void tentarServidor() throws IOException {
		while(!serverQueue.isEmpty()) {
			String servidor = serverQueue.pop();
			System.out.println("Tentando servidor " + servidor);
			
			try (Socket socket = new Socket(servidor, Globals.PORTA_SERVER);
					PrintStream out = new PrintStream(socket.getOutputStream());) {
				out.println("10" + channel);
				out.flush();
			}
			
			try (Socket socket = this.listener.accept();
					Scanner scan = new Scanner(socket.getInputStream());) {
				
				String message = scan.nextLine().trim();
				
				switch (message.substring(0, 2)) {
				case "00":
					System.out.println("Canal cheio, tentando outro servidor");
					serverQueue.addAll(getClientesServidor(servidor));
					break;
				case "10":
					this.servidor = servidor;
					System.out.println("Conectado so servidor, iniciando Thread");
					thread = new Thread(this);
					thread.start();
					return;
				default:
					break;
				}
				
			}
		}
		System.out.println("Não existe servidor disponível, desistindo");
		this.listener.close();
	}
	
	public void stop() {
		try (Socket socket = new Socket(this.servidor, Globals.PORTA_SERVER);
				PrintStream out = new PrintStream(socket.getOutputStream());) {
			out.println("12" + this.channel);
			out.flush();
			
			this.listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getClientesServidor(String servidor) throws IOException {
		ArrayList<String> strings = new ArrayList<String>();
		try (Socket socket = new Socket(servidor, Globals.PORTA_SERVER);
				PrintStream out = new PrintStream(socket.getOutputStream());
				Scanner in = new Scanner(socket.getInputStream());) {
			out.println("11" + channel);
			out.flush();
			
			String response = in.nextLine().trim().replaceAll("[\\[\\] ']", "");
			for(String string : response.split(",")) {
				if(string.length() > 0) {
					strings.add(string);
				}
			}
		} 
		return strings;
	}

	@Override
	public void run() {
		Server server = new Server();
		
		while(true) {
			try (Socket socket = this.listener.accept();
					FileOutputStream file = new FileOutputStream("video")) {
				
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[Globals.BUFFER_SIZE];
				while(in.read(buffer) > 0) {
					file.write(buffer);
				}
				
			} catch (IOException e) {
				System.out.println("Finalizando Thread");
				server.stop();
				break;
			}
			System.out.println("Vídeo recebido");
			
			server.sendVideo();
		}
	}
}
