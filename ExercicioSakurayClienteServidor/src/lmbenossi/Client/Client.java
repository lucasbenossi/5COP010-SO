package lmbenossi.Client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 2000);
			
			OutputHandle output = new OutputHandle(socket);
			InputHandle input = new InputHandle(socket);
			
			output.start();
			input.start();
			
			try {
				output.join();
			} catch (Exception e) {
			} finally {
				input.interrupt();
			}
			
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class OutputHandle extends Thread {
		private Socket socket;
		
		public OutputHandle(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				PrintStream stream = new PrintStream(socket.getOutputStream());
				
				for(int i = 0; i < 10000; i++) {
					stream.println(i);
					Thread.sleep(300);
				}
			} catch(Exception e) {
				System.out.println("ERRO: "+e);
			}
		}
	}
	
	private static class InputHandle extends Thread {
		private Socket socket;
		
		public InputHandle(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			Scanner scan;
			try {
				scan = new Scanner(socket.getInputStream());
				while(scan.hasNextLine()) {
					System.out.println(scan.nextLine());
				}
				scan.close();
			} catch (Exception e) {
				System.out.println("ERRO: "+e);
			}
		}
	}
}
