package entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TradeEntity {

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(optional = false)
	private OrderEntity buyOrder;
	
	public TradeEntity(OrderEntity buyOrder, OrderEntity sellOrder, Double price, Double quantity) {
		super();
		this.buyOrder = buyOrder;
		this.sellOrder = sellOrder;
		this.price = price;
		this.quantity = quantity;
	}
	
	public TradeEntity()
	{
		super();
	}
	@ManyToOne(optional = false)
	private OrderEntity sellOrder;
	
	@Column(nullable = false)
	private Timestamp createdDateTime;
	
	@Column(nullable = false, columnDefinition = "Decimal(15,2)")
	private Double price;
	
	@Column(nullable = false, columnDefinition = "Decimal(15,2)")
	private Double quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderEntity getBuyOrder() {
		return buyOrder;
	}

	public void setBuyOrder(OrderEntity buyOrder) {
		this.buyOrder = buyOrder;
	}

	public OrderEntity getSellOrder() {
		return sellOrder;
	}

	public void setSellOrder(OrderEntity sellOrder) {
		this.sellOrder = sellOrder;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

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
	
	
	
}
