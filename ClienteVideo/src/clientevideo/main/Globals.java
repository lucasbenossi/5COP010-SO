package clientevideo.main;

import java.net.ServerSocket;

public class Globals {
	public static final String SERVER = "localhost";
	public static final int PORTA_SERVER = 6060;
	public static final int PORTA_CLIENT = 9091;
	public static final int BUFFER_SIZE = 1024;
	
	public static ServerSocket listener = null;
}