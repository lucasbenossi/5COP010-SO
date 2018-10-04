package so.rmi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

public class Client {
	public static String host = "192.168.0.3";
	public static List<Container> partesCinza = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		List<Thread> threads = new ArrayList<>();
		File file = new File(args[0]);
		
		List<Host> hosts = new ArrayList<>(args.length - 1);
		for(int i = 1; i < args.length; i++) {
			hosts.add(new Host(args[i]));
		}
		
		BufferedImage imagem = ImageIO.read(file);

		BufferedImage vetorImagens[] = cortarImagem(imagem, 1, hosts.size());

		long timeStart = System.currentTimeMillis();

		int i = 0;
		for (BufferedImage img : vetorImagens) {
			int width = img.getWidth();
			int height = img.getHeight();
			int arrayRGB[] = new int[width * height];
			
			img.getRGB(0, 0, width, height, arrayRGB, 0, width);

			Thread t = new Thread(new ClientThread(arrayRGB, height, width, i, hosts.get(i)));
			t.start();

			threads.add(t);

			i++;
		}
		
		for (Thread thread : threads) {
			thread.join();
		}
		
		Comparator<Container> comparator = new Comparator<Container>() {
			@Override
			public int compare(Container c1, Container c2) {
				return c1.index - c2.index;
			}
		};
		Collections.sort(partesCinza, comparator);
		
		for(i = 0; i < hosts.size(); i++) {
			BufferedImage img = vetorImagens[i];
			int width = img.getWidth();
			int height = img.getHeight();
			
			img.setRGB(0, 0, width, height, partesCinza.get(i).arrayRGB, 0, width);
		}
		
		imagem = juntarImagem(vetorImagens, 1, hosts.size());
		
		System.out.print("Salvando... ");
		ImageIO.write(imagem, "jpg", new File("saida.jpg"));
		System.out.println("Ok.");
		
		Tempo.printTime("", System.currentTimeMillis() - timeStart);
	}
	
	private static BufferedImage[] cortarImagem(BufferedImage image, int rows, int cols) {
		int chunks = rows * cols;

		int chunkWidth = image.getWidth() / cols;
		int chunkHeight = image.getHeight() / rows;
		int count = 0;
		BufferedImage vetorImagens[] = new BufferedImage[chunks];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				vetorImagens[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
				Graphics2D gr = vetorImagens[count].createGraphics();
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				gr.dispose();
				
				count++;
			}
		}

		return vetorImagens;
	}
	
	private static BufferedImage juntarImagem(BufferedImage imagens[], int rows, int cols) {

		int chunkWidth = imagens[0].getWidth();
		int chunkHeight = imagens[0].getHeight();
		int tipo = imagens[0].getType();

		BufferedImage imagem = new BufferedImage(chunkWidth * cols, chunkHeight * rows, tipo);

		int num = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				imagem.createGraphics().drawImage(imagens[num], chunkWidth * j, chunkHeight * i, null);
				num++;
			}
		}
		return imagem;
	}

}
