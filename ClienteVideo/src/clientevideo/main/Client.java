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
			
			String message = scan.nextLine();
			
			switch (message.substring(0, 2)) {
			case "00":
				System.out.println("Canal cheio, tentando outro cliente.");
				//TODO tentar conectar outro cliente
				break;
			case "10":
				System.out.println("Conectado so servidor, iniciando Thread.");
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
		while(true) {
			try (Socket socket = Globals.listener.accept();
					FileOutputStream file = new FileOutputStream("video")) {
				
				InputStream input = socket.getInputStream();
				while(input.available() > 0) {
					byte[] data = new byte[Globals.BUFFER_SIZE];
					input.read(data);
					file.write(data);
				}
				
			} catch (IOException e) {
				System.out.println("Finalizando Thread.");
				break;
			}
			System.out.println("Vídeo recebido");
		}
	}
}
