package entities.utilities;

public enum Type {
	BUY, SELL;
	
	public String toString()
	{
		if(this.equals(BUY)) return "BUY";
		else return "SELL";
	}
	
}
