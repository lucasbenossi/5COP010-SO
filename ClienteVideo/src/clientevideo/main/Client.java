package clientevideo.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
	private Thread thread = null;
	
	public Client() {
		try (Socket socket = Globals.listener.accept();
				Scanner scan = new Scanner(socket.getInputStream());) {
			
			String message = scan.nextLine().trim();
			
			switch (message.substring(0, 2)) {
			case "00":
				System.out.println("Canal cheio, tentando outro cliente");
				//TODO tentar conectar outro cliente
				break;
			case "10":
				System.out.println("Conectado so servidor, iniciando Thread");
				thread = new Thread(this);
				thread.start();
				break;
			default:
				break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		Server server = new Server();
		
		while(true) {
			try (Socket socket = Globals.listener.accept();
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
