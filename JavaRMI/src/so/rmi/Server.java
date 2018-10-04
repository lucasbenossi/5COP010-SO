package so.rmi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Conversor {

	@Override
	public int[] converter(int[] image, int width, int height) throws RemoteException {
		long timeStart = System.currentTimeMillis();
		System.out.print("Convertendo... ");
		
		BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imagem.setRGB(0, 0, width, height, image, 0, width);
		for (int i = 0; i < imagem.getWidth(); i++) {
			for (int j = 0; j < imagem.getHeight(); j++) {
				Color rgb = new Color(imagem.getRGB(i, j));
				int media = (rgb.getRed() + rgb.getGreen() + rgb.getBlue()) / 3;
				Color mediaRGB = new Color(media, media, media);
				imagem.setRGB(i, j, mediaRGB.getRGB());
			}
		}
		imagem.getRGB(0, 0, imagem.getWidth(), imagem.getHeight(), image, 0, imagem.getWidth());
		
		System.out.println("Ok.");
		Tempo.printTime("", System.currentTimeMillis() - timeStart);

		return image;
	}

	public static void main(String args[]) {
		try {
			Server server = new Server();
			Conversor stub = (Conversor) UnicastRemoteObject.exportObject(server, 0);
			
			Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
			registry.bind("converter", stub);
			
			System.err.println("Servidor rodando na porta " + args[0]);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
