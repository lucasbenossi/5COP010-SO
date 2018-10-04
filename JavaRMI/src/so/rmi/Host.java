package so.rmi;

public class Host {
	public String address;
	public int port;
	
	public Host(String arg) {
		this.address = arg.split(":")[0];
		this.port = Integer.parseInt(arg.split(":")[1]);
	}
}
