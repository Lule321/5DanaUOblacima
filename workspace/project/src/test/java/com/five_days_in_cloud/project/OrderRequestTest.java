package com.five_days_in_cloud.project;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import com.five_days_in_cloud.project.utilities.OrderRequest;

import entities.OrderBookEntity;
import entities.OrderEntity;
import entities.utilities.Status;
import entities.utilities.Type;
import entities.TradeEntity;
import repository.OrderRepository;
import repository.OrderBookRepository;
import java.util.Optional;

public class OrderRequestTest {
	
	@BeforeAll
	public static void setUp()
	{
		Long id = (long) 1;
		when(orderRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(null));
		when(orderBookRepositoryMock.findById(id)).thenReturn(Optional.of(orderBookEntityTest));
		Long id2 = (long) 2;
		when(orderRepositoryMock.findById(id2)).thenReturn(Optional.of(new OrderEntity()));
	}
	private static OrderBookRepository orderBookRepositoryMock = Mockito.mock(OrderBookRepository.class);
	private static OrderRepository orderRepositoryMock = Mockito.mock(OrderRepository.class);
	private static OrderBookEntity orderBookEntityTest = new OrderBookEntity(); 
	@Test	
	public void orderEntityAsJSONObjectTestWithoutTrades()
	{
		OrderEntity order = new OrderEntity();
		Long id = (long) 1;
		order.setId(id);
		Timestamp createdDateTime = new Timestamp(1);
		order.setCreatedDateTime(createdDateTime);
		String currencyPair = "Dummy";
		order.setCurrencyPair(currencyPair);
		Double filledQuantity = 0.0;
		order.setFilledQuantity(filledQuantity);
		Double quantity = 1.0;
		order.setQuantity(quantity);
		Double price = 22.0;
		order.setPrice(price);
		
		Status status = Status.CLOSED;
		order.setStatus(Status.CLOSED);
		
		Type type = Type.SELL;
		order.setType(type);
		
		JSONObject[] trades = new JSONObject[0];
		
		JSONObject jo = OrderRequest.orderEntityAsJSONObject(order);
		
		Assert.isTrue(id == jo.get("id"), id + " is not equal to " + jo.get("id"));
		Assert.isTrue(createdDateTime.toString().equals(jo.get("createdDateTime")), createdDateTime + " is not equal to " + jo.get("createdDateTime"));
		Assert.isTrue(currencyPair.equals(jo.get("currencyPair")), currencyPair + " not equal to " + jo.get("currencyPair"));
		Assert.isTrue(filledQuantity == jo.get("filledQuantity"), filledQuantity + " not equal to " + jo.get("filledQuantity"));
		Assert.isTrue(quantity == jo.get("quantity"), quantity + " not equal to " + jo.get("quantity"));
		Assert.isTrue(price == jo.get("price"), price + " not equal to " + jo.get("price"));
		Assert.isTrue(status.toString().equals(jo.get("orderStatus")), status + " not equal to " + jo.get("orderStatus"));
		Assert.isTrue(type.toString().equals(jo.get("type")), type + " not equal to " + jo.get("type"));
		Assert.isTrue(((JSONObject[]) jo.get("trades")).length == 0, "Trades length not zero!");
		
	}
	
	@Test
	public void orderEntityAsJSONObjectTestWithTrades()
	{
		OrderEntity order = new OrderEntity();
		Long id = (long) 1;
		order.setId(id);
		Timestamp createdDateTime = new Timestamp(1);
		order.setCreatedDateTime(createdDateTime);
		String currencyPair = "Dummy";
		order.setCurrencyPair(currencyPair);
		Double filledQuantity = 0.0;
		order.setFilledQuantity(filledQuantity);
		Double quantity = 1.0;
		order.setQuantity(quantity);
		Double price = 22.0;
		order.setPrice(price);
		
		Status status = Status.CLOSED;
		order.setStatus(Status.CLOSED);
		
		Type type = Type.SELL;
		order.setType(type);
		
		ArrayList<TradeEntity> trades = new ArrayList<>();
		TradeEntity te = new TradeEntity(order, order, price, quantity);
		te.setCreatedDateTime(createdDateTime);
		te.setId((long) 1);
		trades.add(te);
		
		te = new TradeEntity(order, order, price + 1, quantity + 1);
		te.setCreatedDateTime(createdDateTime);
		te.setId((long) 2);
		trades.add(te);
		
		order.setSellTrades(trades);
		
		
		JSONObject jo = OrderRequest.orderEntityAsJSONObject(order);
		
		
		Assert.isTrue(id == jo.get("id"), id + " is not equal to " + jo.get("id"));
		Assert.isTrue(createdDateTime.toString().equals(jo.get("createdDateTime")), createdDateTime + " is not equal to " + jo.get("createdDateTime"));
		Assert.isTrue(currencyPair.equals(jo.get("currencyPair")), currencyPair + " not equal to " + jo.get("currencyPair"));
		Assert.isTrue(filledQuantity == jo.get("filledQuantity"), filledQuantity + " not equal to " + jo.get("filledQuantity"));
		Assert.isTrue(quantity == jo.get("quantity"), quantity + " not equal to " + jo.get("quantity"));
		Assert.isTrue(price == jo.get("price"), price + " not equal to " + jo.get("price"));
		Assert.isTrue(status.toString().equals(jo.get("orderStatus")), status + " not equal to " + jo.get("orderStatus"));
		Assert.isTrue(type.toString().equals(jo.get("type")), type + " not equal to " + jo.get("type"));
		
		JSONObject[] resultTrades = (JSONObject[]) (jo.get("trades"));
		int i = 0;
		//System.out.println(resultTrades[0].toString());
		for(TradeEntity t : trades)
		{
			Assert.isTrue(t.getId() == resultTrades[i].get("id"), "Ids not good in tradeEntity!");
			Assert.isTrue(t.getBuyOrder().getId() == resultTrades[i].get("buyOrderId"), "BuyOrder not good in tradeEntity!");
			Assert.isTrue(t.getSellOrder().getId() == resultTrades[i].get("sellOrderId"), "SellOrder not good in tradeEntity!");
			Assert.isTrue(t.getCreatedDateTime().getTime() == (Long) resultTrades[i].get("timestamp"), "Timestamp not good in tradeEntity!");
			Assert.isTrue(t.getPrice() == resultTrades[i].get("price"), "Price not good in tradeEntity!");
			Assert.isTrue(t.getQuantity() == resultTrades[i].get("quantity"), "Quantity not good in tradeEntity!");
			i++;
		}
		
	}
	

	
	@Test
	public void validateTestValidData()
	{
		Long id = (long) 1;
		
		OrderEntity order = new OrderEntity();
		
		order.setCurrencyPair("BTCUSD");
		order.setId(id);
		order.setQuantity(1.0);
		order.setPrice(2.0);
		
		String resultString = OrderRequest.validate(order, orderRepositoryMock);
		Assert.isTrue(resultString.equals(""), "Returns not valid for valid test { " + resultString + "}");
		
	}
	
	@Test
	public void validateTestInvalidId()
	{
		Long id = (long) 2;
		OrderEntity order = new OrderEntity();
		order.setCurrencyPair("BTCUSD");
		order.setId(id);
		order.setQuantity(1.0);
		order.setPrice(2.0);
		
		String resultString = OrderRequest.validate(order, orderRepositoryMock);
		Assert.isTrue(resultString.equals("Id negative or already exists!"), "Returns: { " + resultString + "}");
		
	}

	@Test
	public void validateTestInvalidCurrencyPair()
	{
		Long id = (long) 1;
		
		OrderEntity order = new OrderEntity();
		
		order.setCurrencyPair("BTCUS");
		order.setId(id);
		order.setQuantity(1.0);
		order.setPrice(2.0);
		
		String resultString = OrderRequest.validate(order, orderRepositoryMock);
		Assert.isTrue(resultString.equals("Currency not equal to BTCUSD!"), "Returns not valid for valid test { " + resultString + "}");
	}
	
	@Test
	public void validateTestInvalidPrice()
	{
		Long id = (long) 1;
		
		OrderEntity order = new OrderEntity();
		
		order.setCurrencyPair("BTCUSD");
		order.setId(id);
		order.setQuantity(1.0);
		order.setPrice(0.0);
		
		String resultString = OrderRequest.validate(order, orderRepositoryMock);
		Assert.isTrue(resultString.equals("Price must be positive!"), "Returns not valid for valid test { " + resultString + "}");
	}

	@Test
	public void validateTestInvalidQuantity()
	{
		Long id = (long) 1;
		
		OrderEntity order = new OrderEntity();
		
		order.setCurrencyPair("BTCUSD");
		order.setId(id);
		order.setQuantity(-1.0);
		order.setPrice(1.0);
		
		String resultString = OrderRequest.validate(order, orderRepositoryMock);
		Assert.isTrue(resultString.equals("Quantity must be positive!"), "Returns not valid for valid test { " + resultString + "}");
	}

	public static OrderEntity createTestOrderEntity(int id, int timestamp, double quantity, double price, Type type)
	{
		OrderEntity order = new OrderEntity();
		order.setId((long) id);
		Timestamp createdDateTime = new Timestamp(timestamp);
		order.setCreatedDateTime(createdDateTime);
		order.setCurrencyPair("BTCUSD");
		order.setFilledQuantity(0.0);
		order.setQuantity(quantity);
		order.setPrice(price);
		order.setStatus(Status.OPEN);
		order.setType(type);
		
		return order;
	}
	
	@Test
	public void processRequestTestBuyOrderBookEntitySellEmptyBuyEmpty()
	{
		OrderEntity order = createTestOrderEntity(1, 1, 3.0, 3.5, Type.BUY);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 1, "Buy order size not equal to 1!");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() == order.getPrice(), "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getQuantity() == order.getQuantity(), "Quantity not equal to order's quantity!");
		Assert.isTrue(order.getBuyTrades().size() == 0, "Order has buy trades!");
		
		orderBookEntityTest.getBuyOrders().clear();
		
	}
	
	@Test public void processRequestTestSellOrderBookEntitySellEmptyBuyEmpty()
	{
		OrderEntity order = createTestOrderEntity(1, 1, 3.0, 3.5, Type.SELL);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Buy order size not equal to 1!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() == order.getPrice(), "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getQuantity() == order.getQuantity(), "Quantity not equal to order's quantity!");
		Assert.isTrue(order.getSellTrades().size() == 0, "Order has buy trades!");
		
		orderBookEntityTest.getSellOrders().clear();
	}
	

}
