package so.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Conversor extends Remote {
	int[] converter(int[] imagem, int width, int height) throws RemoteException;
}
