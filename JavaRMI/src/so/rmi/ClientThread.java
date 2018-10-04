package so.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientThread implements Runnable {
	private int[] imagem;
	private int width;
	private int height;
	private int index;
	private Conversor stub;
	
	public ClientThread(int[] imagem, int height, int width, int index, Host host) {
		this.imagem = imagem;
		this.width = width;
		this.height = height;
		this.index = index;
		
		try {
            Registry registry = LocateRegistry.getRegistry(host.address, host.port);
            this.stub = (Conversor) registry.lookup("converter");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void run() {
		long timeStart = System.currentTimeMillis();
		
		try {
			Container imagemCinza = new Container(this.index);

			imagemCinza.arrayRGB = stub.converter(this.imagem, this.height, this.width);
			Client.partesCinza.add(imagemCinza);
			
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}
		
		Tempo.printTime("Thread " + this.index + " ", System.currentTimeMillis() - timeStart);
	}

}
