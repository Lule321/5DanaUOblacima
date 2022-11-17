package com.five_days_in_cloud.project;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import com.five_days_in_cloud.project.utilities.OrderRequest;

import entities.OrderBookElement;
import entities.OrderBookEntity;
import entities.OrderEntity;
import entities.utilities.Status;
import entities.utilities.Type;
import entities.TradeEntity;
import repository.OrderRepository;
import repository.OrderBookRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Timeout;

public class OrderRequestTest {
	
	
	
	@BeforeAll
	public static void setUp()
	{
		Long id = (long) 1;
		when(orderRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(null));
		when(orderBookRepositoryMock.findById(id)).thenReturn(Optional.of(orderBookEntityTest));
		Long id2 = (long) 2;
		when(orderRepositoryMock.findById(id2)).thenReturn(Optional.of(new OrderEntity()));
		
		when(orderRepositoryMock.findOrderEntitiesByPriceAndType(50.0, Type.BUY)).thenReturn(orderEntityBuyList);
		when(orderRepositoryMock.findOrderEntitiesByPriceAndType(50.0, Type.SELL)).thenReturn(orderEntitySellList);
	
		when(orderRepositoryMock.findOrderEntitiesByPriceAndType(60.0, Type.BUY)).thenReturn(orderEntityBuyList2);
		when(orderRepositoryMock.findOrderEntitiesByPriceAndType(60.0, Type.SELL)).thenReturn(orderEntitySellList2);
	}
	private static OrderBookRepository orderBookRepositoryMock = Mockito.mock(OrderBookRepository.class);
	private static OrderRepository orderRepositoryMock = Mockito.mock(OrderRepository.class);
	private static OrderBookEntity orderBookEntityTest = new OrderBookEntity(); 
	private static ArrayList<OrderEntity> orderEntityBuyList = new ArrayList<>();
	private static ArrayList<OrderEntity> orderEntitySellList = new ArrayList<>();
	private static ArrayList<OrderEntity> orderEntityBuyList2 = new ArrayList<>();
	private static ArrayList<OrderEntity> orderEntitySellList2 = new ArrayList<>();
	
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
		
		
	}
	
	@Test public void processRequestTestSellOrderBookEntitySellEmptyBuyEmpty()
	{
		OrderEntity order = createTestOrderEntity(1, 1, 3.0, 3.5, Type.SELL);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Buy order size not equal to 1!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() < order.getPrice() + 0.005, "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() > order.getPrice() - 0.005, "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getQuantity() < order.getQuantity() + 0.005, "Quantity not equal to order's quantity!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getQuantity() > order.getQuantity() - 0.005, "Quantity not equal to order's quantity!");
		Assert.isTrue(order.getSellTrades().size() == 0, "Order has buy trades!");
		
	}
	
	@Test
	// BookEntity has one sell order for selling, the sell order will be fullfilled, same goes for the buy order
	public void processRequestTestBuyOrderBookEntitySellHasOneBuyEmpty1()
	{
		OrderEntity order = createTestOrderEntity(1, 2, 3.0, 60.0, Type.BUY);
	
		OrderEntity sellOrder = createTestOrderEntity(2, 1, 3.0, 50.0, Type.SELL);
		orderEntitySellList.add(sellOrder);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 3.0);
		
		orderBookEntityTest.getSellOrders().add(obElem);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 0, "Buy order size not equal to 0!");
		Assert.isTrue(sellOrder.getStatus().equals(Status.CLOSED), "Sell order not closed");
		Assert.isTrue(sellOrder.getQuantity() < 0.005, "Sell order Quantity not zero");
		Assert.isTrue(order.getStatus().equals(Status.CLOSED), "Order not closed");
		Assert.isTrue(order.getQuantity() < 0.005, "Order quantity not zero");
		Assert.isTrue(order.getBuyTrades().size() == 1, "Order hasnt got one buy trades!");
		
		Assert.isTrue(order.getBuyTrades().get(0).getPrice() < sellOrder.getPrice() + 0.005, "Trade price not good!");
		Assert.isTrue(order.getBuyTrades().get(0).getPrice() > sellOrder.getPrice() - 0.005, "Trade price not good!");
		
		Assert.isTrue(order.getBuyTrades().get(0).getQuantity() < sellOrder.getFilledQuantity() + 0.005, "Trade quantity not good!");
		Assert.isTrue(order.getBuyTrades().get(0).getQuantity() > sellOrder.getFilledQuantity() - 0.005, "Trade quantity not good!");
		
		
	}
	
	@Test
	// BookEntity has one buy order for buying, the buy order will be fullfilled, same goes for the sell order
	public void processRequestTestSellOrderBookEntitySellEmptyBuyHasOne1()
	{
		OrderEntity order = createTestOrderEntity(1, 2, 3.0, 45.0, Type.SELL);
	
		OrderEntity buyOrder = createTestOrderEntity(2, 1, 3.0, 50.0, Type.BUY);
		orderEntityBuyList.add(buyOrder);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 3.0);
		
		orderBookEntityTest.getBuyOrders().add(obElem);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 0, "Sell order size not equal to 0!");
		Assert.isTrue(buyOrder.getStatus().equals(Status.CLOSED), "Buy order not closed");
		Assert.isTrue(buyOrder.getQuantity() < 0.005, "Buy order Quantity not zero");
		Assert.isTrue(order.getStatus().equals(Status.CLOSED), "Order not closed");
		Assert.isTrue(order.getQuantity() < 0.005, "Order quantity not zero");
		Assert.isTrue(order.getSellTrades().size() == 1, "Order hasnt got one buy trades!");
		
		Assert.isTrue(order.getSellTrades().get(0).getPrice() < buyOrder.getPrice() + 0.005, "Trade price not good!");
		Assert.isTrue(order.getSellTrades().get(0).getPrice() > buyOrder.getPrice() - 0.005, "Trade price not good!");
		
		Assert.isTrue(order.getSellTrades().get(0).getQuantity() < buyOrder.getFilledQuantity() + 0.005, "Trade quantity not good!");
		Assert.isTrue(order.getSellTrades().get(0).getQuantity() > buyOrder.getFilledQuantity() - 0.005, "Trade quantity not good!");
		
	}
	
	@Test
	// BookEntity has one sell order for selling, the sell order won't be fullfilled
	public void processRequestTestBuyOrderBookEntitySellHasOneBuyEmpty2()
	{
		OrderEntity order = createTestOrderEntity(1, 2, 3.0, 45.0, Type.BUY);
		
		OrderEntity sellOrder = createTestOrderEntity(2, 1, 3.0, 50.0, Type.SELL);
		orderEntitySellList.add(sellOrder);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 3.0);
		
		orderBookEntityTest.getSellOrders().add(obElem);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 1, "Buy order size not equal to 0!");
		Assert.isTrue(sellOrder.getStatus().equals(Status.OPEN), "Sell order not open");
		Assert.isTrue(order.getStatus().equals(Status.OPEN), "Order not open");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() == order.getPrice(), "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getQuantity() == order.getQuantity(), "Quantity not equal to order's quantity!");
		Assert.isTrue(order.getBuyTrades().size() == 0, "Order has buy trades!");
		
	}

	@Test
	// BookEntity has one buy order for buying, the buy order won't be fullfilled
	public void processRequestTestSellOrderBookEntitySellEmptyBuyHasOne2()
	{
		OrderEntity order = createTestOrderEntity(1, 2, 3.0, 60.0, Type.SELL);
	
		OrderEntity buyOrder = createTestOrderEntity(2, 1, 3.0, 50.0, Type.BUY);
		orderEntityBuyList.add(buyOrder);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 3.0);
		
		orderBookEntityTest.getBuyOrders().add(obElem);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Sell order size not equal to 0!");
		Assert.isTrue(buyOrder.getStatus().equals(Status.OPEN), "Buy order not open");
		Assert.isTrue(order.getStatus().equals(Status.OPEN), "Order not open");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() < order.getPrice() + 0.005, "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() > order.getPrice() - 0.005, "Price not equal to order's price!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getQuantity() < order.getQuantity() + 0.005, "Quantity not equal to order's quantity!");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getQuantity() > order.getQuantity() - 0.005, "Quantity not equal to order's quantity!");
		
		
	}
	
	@Test
	// BookEntity has one sell order for selling, the sell order will be filled but the buy one won't
	public void processRequestTestBuyOrderBookEntitySellHasOneBuyEmpty3()
		{
			OrderEntity order = createTestOrderEntity(1, 2, 4.0, 55.0, Type.BUY);
			
			OrderEntity sellOrder = createTestOrderEntity(2, 1, 3.0, 50.0, Type.SELL);
			orderEntitySellList.add(sellOrder);
			
			OrderBookElement obElem = new OrderBookElement(50.0, 3.0);
			
			orderBookEntityTest.getSellOrders().add(obElem);
			
			OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
			
			Assert.isTrue(sellOrder.getStatus().equals(Status.CLOSED), "Sell order not closed");
			Assert.isTrue(sellOrder.getQuantity() < 0.005, "Sell order Quantity not zero");
			Assert.isTrue(order.getStatus().equals(Status.OPEN), "Order not closed");
			Assert.isTrue(order.getBuyTrades().size() == 1, "Order hasnt got one buy trades!");
			
			Assert.isTrue(order.getBuyTrades().get(0).getPrice() < sellOrder.getPrice() + 0.005, "Trade price not good!");
			Assert.isTrue(order.getBuyTrades().get(0).getPrice() > sellOrder.getPrice() - 0.005, "Trade price not good!");
			
			Assert.isTrue(order.getBuyTrades().get(0).getQuantity() < sellOrder.getFilledQuantity() + 0.005, "Trade quantity not good!");
			Assert.isTrue(order.getBuyTrades().get(0).getQuantity() > sellOrder.getFilledQuantity() - 0.005, "Trade quantity not good!");
			
			
			Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 1, "Buy order size not equal to 1!");
			Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() == order.getPrice(), "Price not equal to order's price!");
			Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getQuantity() == order.getQuantity(), "Quantity not equal to order's quantity!");
			
		}
	
	
	@Test
	// BookEntity has one buy order for selling, the buy order will be filled but the sell one won't
	public void processRequestTestSellOrderBookEntityBuyEmptySellHasOne3()
		{
			OrderEntity order = createTestOrderEntity(1, 2, 4.0, 45.0, Type.SELL);
			
			OrderEntity buyOrder = createTestOrderEntity(2, 1, 3.0, 50.0, Type.BUY);
			orderEntityBuyList.add(buyOrder);
			
			OrderBookElement obElem = new OrderBookElement(50.0, 3.0);
			
			orderBookEntityTest.getBuyOrders().add(obElem);
			
			OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
			
			Assert.isTrue(buyOrder.getStatus().equals(Status.CLOSED), "Sell order not closed");
			Assert.isTrue(buyOrder.getQuantity() < 0.005, "Sell order Quantity not zero");
			Assert.isTrue(order.getStatus().equals(Status.OPEN), "Order not closed");
			Assert.isTrue(order.getSellTrades().size() == 1, "Order hasnt got one sell trades!");
			
			Assert.isTrue(order.getSellTrades().get(0).getPrice() < buyOrder.getPrice() + 0.005, "Trade price not good!");
			Assert.isTrue(order.getSellTrades().get(0).getPrice() > buyOrder.getPrice() - 0.005, "Trade price not good!");
			
			Assert.isTrue(order.getSellTrades().get(0).getQuantity() < buyOrder.getFilledQuantity() + 0.005, "Trade quantity not good!");
			Assert.isTrue(order.getSellTrades().get(0).getQuantity() > buyOrder.getFilledQuantity() - 0.005, "Trade quantity not good!");
			
			
			Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Sell order size not equal to 1!");
			Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() == order.getPrice(), "Price not equal to order's price!");
			Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getQuantity() == order.getQuantity(), "Quantity not equal to order's quantity!");
			
		}
	
	@Test
	//Buy order will be completed
	public void processRequestTestBuyOrderBookEntitySellHasMultipleBuyHasMultiple1()
	{
		OrderEntity order = createTestOrderEntity(1, 10, 15.0, 60.0, Type.BUY);
		
		OrderEntity sellOrder1 = createTestOrderEntity(2, 1, 3.0, 50.0, Type.SELL);
		OrderEntity sellOrder2 = createTestOrderEntity(2, 2, 2.0, 50.0, Type.SELL);
		orderEntitySellList.add(sellOrder1);
		orderEntitySellList.add(sellOrder2);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 5.0);
		
		orderBookEntityTest.getSellOrders().add(obElem);
		
		OrderEntity sellOrder3 = createTestOrderEntity(2, 3, 7.0, 60.0, Type.SELL);
		OrderEntity sellOrder4 = createTestOrderEntity(2, 4, 5.0, 60.0, Type.SELL);
		orderEntitySellList2.add(sellOrder3);
		orderEntitySellList2.add(sellOrder4);
		OrderBookElement obElem2 = new OrderBookElement(60.0, 12.0);
		orderBookEntityTest.getSellOrders().add(obElem2);
		
		OrderBookElement obElem3 = new OrderBookElement(70.0, 5.0); //Not possible but for test purposes
		OrderBookElement obElem4 = new OrderBookElement(45.0, 5.0); 
		orderBookEntityTest.getBuyOrders().add(obElem3);
		orderBookEntityTest.getBuyOrders().add(obElem4);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(order.getBuyTrades().size() == 4, "Buy trade size not valid!");
		Assert.isTrue(sellOrder1.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder2.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder3.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder4.getStatus() == Status.OPEN, "Status of sell order not open");
		
		Assert.isTrue(order.getStatus() == Status.CLOSED, "Status of order not closed");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Order book sell orders number not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() < obElem2.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() > obElem2.getPrice() - 0.005, "Price in order book not valid");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 2, "Size of buy orders should be unchanged");
		
	}
	
	
	@Test
	//Buy order wont be completed, no buy orders with this price in book
	public void processRequestTestBuyOrderBookEntitySellHasMultipleBuyHasMultiple2()
	{
		OrderEntity order = createTestOrderEntity(1, 10, 15.0, 55.0, Type.BUY);
		
		OrderEntity sellOrder1 = createTestOrderEntity(2, 1, 3.0, 50.0, Type.SELL);
		OrderEntity sellOrder2 = createTestOrderEntity(2, 2, 2.0, 50.0, Type.SELL);
		orderEntitySellList.add(sellOrder1);
		orderEntitySellList.add(sellOrder2);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 5.0);
		
		orderBookEntityTest.getSellOrders().add(obElem);
		
		OrderEntity sellOrder3 = createTestOrderEntity(2, 3, 7.0, 60.0, Type.SELL);
		OrderEntity sellOrder4 = createTestOrderEntity(2, 4, 5.0, 60.0, Type.SELL);
		orderEntitySellList2.add(sellOrder3);
		orderEntitySellList2.add(sellOrder4);
		OrderBookElement obElem2 = new OrderBookElement(60.0, 12.0);
		orderBookEntityTest.getSellOrders().add(obElem2);
		
		OrderBookElement obElem3 = new OrderBookElement(70.0, 5.0); //Not possible but for test purposes
		OrderBookElement obElem4 = new OrderBookElement(45.0, 5.0); 
		orderBookEntityTest.getBuyOrders().add(obElem3);
		orderBookEntityTest.getBuyOrders().add(obElem4);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(order.getBuyTrades().size() == 2, "Buy trade size not valid!");
		Assert.isTrue(sellOrder1.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder2.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder3.getStatus() == Status.OPEN, "Status of sell order not closed");
		Assert.isTrue(sellOrder4.getStatus() == Status.OPEN, "Status of sell order not open");
		
		Assert.isTrue(order.getStatus() == Status.OPEN, "Status of order not closed");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Order book sell orders number not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() < obElem2.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() > obElem2.getPrice() - 0.005, "Price in order book not valid");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 3, "Size of buy orders should be 3");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getPrice() < order.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getPrice() > order.getPrice() - 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getQuantity() < order.getQuantity() + 0.005, "Quantity in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getQuantity() > order.getQuantity() - 0.005, "Quantity in order book not valid");
	
	}
	
	
	@Test
	//Buy order wont be completed, there is a buy order with this price in book
	public void processRequestTestBuyOrderBookEntitySellHasMultipleBuyHasMultiple3()
	{
		OrderEntity order = createTestOrderEntity(1, 10, 15.0, 55.0, Type.BUY);
		
		OrderEntity sellOrder1 = createTestOrderEntity(2, 1, 3.0, 50.0, Type.SELL);
		OrderEntity sellOrder2 = createTestOrderEntity(2, 2, 2.0, 50.0, Type.SELL);
		orderEntitySellList.add(sellOrder1);
		orderEntitySellList.add(sellOrder2);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 5.0);
		
		orderBookEntityTest.getSellOrders().add(obElem);
		
		OrderEntity sellOrder3 = createTestOrderEntity(2, 3, 7.0, 60.0, Type.SELL);
		OrderEntity sellOrder4 = createTestOrderEntity(2, 4, 5.0, 60.0, Type.SELL);
		orderEntitySellList2.add(sellOrder3);
		orderEntitySellList2.add(sellOrder4);
		OrderBookElement obElem2 = new OrderBookElement(60.0, 12.0);
		orderBookEntityTest.getSellOrders().add(obElem2);
		
		OrderBookElement obElem3 = new OrderBookElement(70.0, 5.0); //Not possible but for test purposes
		OrderBookElement obElem4 = new OrderBookElement(55.0, 5.0); 
		orderBookEntityTest.getBuyOrders().add(obElem3);
		orderBookEntityTest.getBuyOrders().add(obElem4);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(order.getBuyTrades().size() == 2, "Buy trade size not valid!");
		Assert.isTrue(sellOrder1.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder2.getStatus() == Status.CLOSED, "Status of sell order not closed");
		Assert.isTrue(sellOrder3.getStatus() == Status.OPEN, "Status of sell order not closed");
		Assert.isTrue(sellOrder4.getStatus() == Status.OPEN, "Status of sell order not open");
		
		Assert.isTrue(order.getStatus() == Status.OPEN, "Status of order not closed");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 1, "Order book sell orders number not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() < obElem2.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(0).getPrice() > obElem2.getPrice() - 0.005, "Price in order book not valid");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 2, "Size of buy orders should be 3");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getPrice() < order.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getPrice() > order.getPrice() - 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(1).getQuantity() > order.getQuantity() + 0.005, "Quantity in order book not valid");
	
	}
	
	@Test
	//Sell order will be completed
	public void processRequestTestSellOrderBookEntitySellHasMultipleBuyHasMultiple1()
	{
		OrderEntity order = createTestOrderEntity(1, 10, 15.0, 40.0, Type.SELL);
		
		OrderEntity buyOrder3 = createTestOrderEntity(2, 3, 7.0, 60.0, Type.BUY);
		OrderEntity buyOrder4 = createTestOrderEntity(2, 4, 5.0, 60.0, Type.BUY);
		orderEntityBuyList2.add(buyOrder3);
		orderEntityBuyList2.add(buyOrder4);
		
		
		OrderEntity buyOrder1 = createTestOrderEntity(2, 1, 3.0, 50.0, Type.BUY);
		OrderEntity buyOrder2 = createTestOrderEntity(2, 2, 2.0, 50.0, Type.BUY);
		orderEntityBuyList.add(buyOrder1);
		orderEntityBuyList.add(buyOrder2);
		
		OrderBookElement obElem2 = new OrderBookElement(60.0, 12.0);
		orderBookEntityTest.getBuyOrders().add(obElem2);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 5.0);
		
		orderBookEntityTest.getBuyOrders().add(obElem);
		


		OrderBookElement obElem4 = new OrderBookElement(35.0, 5.0); 
		OrderBookElement obElem3 = new OrderBookElement(70.0, 5.0);
		orderBookEntityTest.getSellOrders().add(obElem3);
		orderBookEntityTest.getSellOrders().add(obElem4);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		Assert.isTrue(order.getSellTrades().size() == 3, "Buy trade size not valid!");
		Assert.isTrue(buyOrder4.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder3.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder1.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder2.getStatus() == Status.OPEN, "Status of buy order not open");
		
		Assert.isTrue(order.getStatus() == Status.CLOSED, "Status of order not closed");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 1, "Order book buy orders number not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() < obElem.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() > obElem.getPrice() - 0.005, "Price in order book not valid");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 2, "Size of buy orders should be unchanged");
		
	}
	
	
	@Test
	//Sell order won't be completed, no sell orders with this price in the sell book
	public void processRequestTestSellOrderBookEntitySellHasMultipleBuyHasMultiple2()
	{
		OrderEntity order = createTestOrderEntity(1, 10, 15.0, 55.0, Type.SELL);
		
		OrderEntity buyOrder3 = createTestOrderEntity(2, 3, 7.0, 60.0, Type.BUY);
		OrderEntity buyOrder4 = createTestOrderEntity(2, 4, 5.0, 60.0, Type.BUY);
		orderEntityBuyList2.add(buyOrder3);
		orderEntityBuyList2.add(buyOrder4);
		
		
		OrderEntity buyOrder1 = createTestOrderEntity(2, 1, 3.0, 50.0, Type.BUY);
		OrderEntity buyOrder2 = createTestOrderEntity(2, 2, 2.0, 50.0, Type.BUY);
		orderEntityBuyList.add(buyOrder1);
		orderEntityBuyList.add(buyOrder2);
		
		OrderBookElement obElem2 = new OrderBookElement(60.0, 12.0);
		orderBookEntityTest.getBuyOrders().add(obElem2);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 5.0);
		
		orderBookEntityTest.getBuyOrders().add(obElem);
		


		OrderBookElement obElem4 = new OrderBookElement(35.0, 5.0); 
		OrderBookElement obElem3 = new OrderBookElement(70.0, 5.0);
		orderBookEntityTest.getSellOrders().add(obElem4);
		orderBookEntityTest.getSellOrders().add(obElem3);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		
		
		Assert.isTrue(order.getSellTrades().size() == 2, "Sell trade size not valid!");
		Assert.isTrue(buyOrder4.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder3.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder1.getStatus() == Status.OPEN, "Status of buy order not open");
		Assert.isTrue(buyOrder2.getStatus() == Status.OPEN, "Status of buy order not open");
		
		Assert.isTrue(order.getStatus() == Status.OPEN, "Status of order not closed");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 1, "Order book buy orders number not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() < obElem.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() > obElem.getPrice() - 0.005, "Price in order book not valid");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 3, "Size of buy orders should be unchanged");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getPrice() < order.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getPrice() > order.getPrice() - 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getQuantity() < order.getQuantity() + 0.005, "Quantity in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getQuantity() > order.getQuantity() - 0.005, "Quantity in order book not valid");
		
		
	}
	
	
	@Test
	//Sell order won't be completed, there are sell orders with this price in the sell book
	public void processRequestTestSellOrderBookEntitySellHasMultipleBuyHasMultiple3()
	{
		OrderEntity order = createTestOrderEntity(1, 10, 15.0, 55.0, Type.SELL);
		
		OrderEntity buyOrder3 = createTestOrderEntity(2, 3, 7.0, 60.0, Type.BUY);
		OrderEntity buyOrder4 = createTestOrderEntity(2, 4, 5.0, 60.0, Type.BUY);
		orderEntityBuyList2.add(buyOrder3);
		orderEntityBuyList2.add(buyOrder4);
		
		
		OrderEntity buyOrder1 = createTestOrderEntity(2, 1, 3.0, 50.0, Type.BUY);
		OrderEntity buyOrder2 = createTestOrderEntity(2, 2, 2.0, 50.0, Type.BUY);
		orderEntityBuyList.add(buyOrder1);
		orderEntityBuyList.add(buyOrder2);
		
		OrderBookElement obElem2 = new OrderBookElement(60.0, 12.0);
		orderBookEntityTest.getBuyOrders().add(obElem2);
		
		OrderBookElement obElem = new OrderBookElement(50.0, 5.0);
		
		orderBookEntityTest.getBuyOrders().add(obElem);
		


		OrderBookElement obElem4 = new OrderBookElement(35.0, 5.0); 
		OrderBookElement obElem3 = new OrderBookElement(55.0, 5.0);
		orderBookEntityTest.getSellOrders().add(obElem4);
		orderBookEntityTest.getSellOrders().add(obElem3);
		
		OrderRequest.processRequest(order, orderBookRepositoryMock, orderRepositoryMock);
		
		
		
		Assert.isTrue(order.getSellTrades().size() == 2, "Sell trade size not valid!");
		Assert.isTrue(buyOrder4.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder3.getStatus() == Status.CLOSED, "Status of buy order not closed");
		Assert.isTrue(buyOrder1.getStatus() == Status.OPEN, "Status of buy order not open");
		Assert.isTrue(buyOrder2.getStatus() == Status.OPEN, "Status of buy order not open");
		
		Assert.isTrue(order.getStatus() == Status.OPEN, "Status of order not closed");
		
		Assert.isTrue(orderBookEntityTest.getBuyOrders().size() == 1, "Order book buy orders number not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() < obElem.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getBuyOrders().get(0).getPrice() > obElem.getPrice() - 0.005, "Price in order book not valid");
		
		Assert.isTrue(orderBookEntityTest.getSellOrders().size() == 2, "Size of buy orders should be unchanged");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getPrice() < order.getPrice() + 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getPrice() > order.getPrice() - 0.005, "Price in order book not valid");
		Assert.isTrue(orderBookEntityTest.getSellOrders().get(1).getQuantity() > order.getQuantity() + 0.005, "Quantity in order book not valid");
		
		
	}
	
	
	
	@AfterEach
	public void after()
	{
		orderBookEntityTest.getBuyOrders().clear();
		orderBookEntityTest.getSellOrders().clear();
		orderEntitySellList.clear();
		orderEntityBuyList.clear();
		orderEntitySellList2.clear();
		orderEntityBuyList2.clear();
	}
}
