package entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import entities.utilities.Status;
import entities.utilities.Type;



@Entity
public class OrderEntity {
	
	public static final String fixedCurrencyPair = "BTCUSD";
	
	public OrderEntity()
	{
		super();
	}
	
	public Long getId() {
		return id;
	}

	public OrderEntity(Long id, String currencyPair, Type type, Double price, Double quantity) {
		super();
		this.id = id;
		this.currencyPair = currencyPair;
		this.type = type;
		this.price = price;
		this.quantity = quantity;
	}
	
	public OrderEntity(Long id, Type type, Double price, Double quantity) {
		super();
		this.id = id;
		this.currencyPair = "BTCUSD";
		this.type = type;
		this.price = price;
		this.quantity = quantity;
	}
	
	@Id
	private Long id;
	
	@Column(nullable=false, columnDefinition = "varchar(255) default 'BTCUSD'")
	private String currencyPair;

	@Column(nullable=false)
	private Timestamp createdDateTime;
	
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Type type;
	
	@Column(nullable = false, columnDefinition = "Decimal(15,2)")
	private Double price;
	
	@Column(nullable = false, columnDefinition = "Decimal(15,2)")
	private Double quantity;
	
	@Column(nullable = false, columnDefinition = "Decimal(15,2)")
	private Double filledQuantity;
	
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	
	@OneToMany(mappedBy = "buyOrder")
	private List<TradeEntity> buyTrades;
	
	@OneToMany(mappedBy = "sellOrder")
	private List<TradeEntity> sellTrades;
	
	public List<TradeEntity> getBuyTrades() {
		return buyTrades;
	}

	public void setBuyTrades(List<TradeEntity> buyTrades) {
		this.buyTrades = buyTrades;
	}

	public List<TradeEntity> getSellTrades() {
		return sellTrades;
	}

	public void setSellTrades(List<TradeEntity> sellTrades) {
		this.sellTrades = sellTrades;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public Double getFilledQuantity() {
		return filledQuantity;
	}

	public void setFilledQuantity(Double filledQuantity) {
		this.filledQuantity = filledQuantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}

}
