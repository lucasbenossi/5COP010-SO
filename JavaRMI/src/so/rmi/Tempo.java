package so.rmi;

public abstract class Tempo {

	public static void printTime(String prefix, long time) {
		System.out.print(prefix + "Tempo: ");
		if (time >= 1000) {
			System.out.print(time / 1000 + "." + time % 1000);

		} else {
			System.out.print("0." + time % 1000);
		}
		System.out.println("s");
	}
}
