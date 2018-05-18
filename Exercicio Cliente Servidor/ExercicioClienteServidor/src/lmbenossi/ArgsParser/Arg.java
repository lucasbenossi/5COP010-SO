package lmbenossi.ArgsParser;

public enum Arg {
	SERVER("-server", Type.BOOLEAN),
	CLIENT("-client", Type.STRING),
	PORT("-port", Type.INTEGER);
	
	private enum Type{
		BOOLEAN, STRING, INTEGER;
	}
	
	private String arg;
	private Type type;
	private boolean set = false;
	private String string = null;
	private int integer = 0;
	
	private Arg(String arg, Type type) {
		this.arg = arg;
		this.type = type;
	}
	
	public void set() {
		this.set = true;
	}
	public void set(String string) {
		if(this.isInteger()) {
			try {
				this.integer = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				this.integer = 0;
				return;
			}
		}
		set();
		this.string = string;
	}

	@Override
	public String toString() {
		return this.arg;
	}
	
	public boolean isBoolean() {
		return this.type.equals(Type.BOOLEAN);
	}
	public boolean isString() {
		return this.type.equals(Type.STRING);
	}
	public boolean isInteger() {
		return this.type.equals(Type.INTEGER);
	}
	
	public boolean isSet(){
		return this.set;
	}
	public String getString() {
		return this.string;
	}
	public int getInteger() {
		return this.integer;
	}
}
