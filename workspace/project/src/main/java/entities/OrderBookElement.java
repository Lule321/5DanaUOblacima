package entities;

import javax.persistence.Embeddable;

@Embeddable
public class OrderBookElement {
	
	private Double price;
	private Double quantity;
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public OrderBookElement(Double price, Double quantity) {
		super();
		this.price = price;
		this.quantity = quantity;
	}
	
	public OrderBookElement()
	{
		super();
	}

}
