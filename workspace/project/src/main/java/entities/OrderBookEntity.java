package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class OrderBookEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ElementCollection
	@OrderBy("price DESC")
	private List<OrderBookElement> buyOrders = new ArrayList<>();
	
	@ElementCollection
	@OrderBy("price ASC")
	private List<OrderBookElement> sellOrders = new ArrayList<>();

	public OrderBookEntity()
	{
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrderBookElement> getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(List<OrderBookElement> buyOrders) {
		this.buyOrders = buyOrders;
	}

	public List<OrderBookElement> getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(List<OrderBookElement> sellOrders) {
		this.sellOrders = sellOrders;
	}
	
	

}
