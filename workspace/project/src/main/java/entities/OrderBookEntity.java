package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class OrderBookEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(targetEntity = OrderEntity.class)
	private List<OrderEntity> buyOrders;
	
	@OneToMany(targetEntity = OrderEntity.class)
	private List<OrderEntity> sellOrders;

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

	public List<OrderEntity> getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(List<OrderEntity> buyOrders) {
		this.buyOrders = buyOrders;
	}

	public List<OrderEntity> getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(List<OrderEntity> sellOrders) {
		this.sellOrders = sellOrders;
	}
	
	

}
