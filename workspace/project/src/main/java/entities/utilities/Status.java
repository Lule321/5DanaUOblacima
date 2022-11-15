package entities.utilities;

public enum Status {
	OPEN, CLOSED;
	
	public String toString()
	{
		if(this.equals(OPEN)) return "OPEN";
		else return "CLOSED";
	}
}
