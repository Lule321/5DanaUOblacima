package com.five_days_in_cloud.project.utilities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import entities.OrderBookElement;
import entities.OrderBookEntity;
import entities.OrderEntity;
import entities.TradeEntity;
import entities.utilities.Status;
import entities.utilities.Type;
import repository.OrderBookRepository;
import repository.OrderRepository;

public class OrderRequest {

	public static JSONObject orderEntityAsJSONObject(OrderEntity order)
	{
		String dateString = order.getCreatedDateTime().toString();
		JSONObject jo = new JSONObject();
		jo.put("id", order.getId());
		jo.put("createdDateTime", dateString);
		jo.put("currencyPair", order.getCurrencyPair());
		jo.put("type", order.getType().toString());
		jo.put("price", order.getPrice());
		jo.put("quantity", order.getQuantity());
		jo.put("filledQuantity", order.getFilledQuantity());
		jo.put("orderStatus", order.getStatus().toString());
		
		List<TradeEntity> tradesList = null;
		
		
		if(order.getType().equals(entities.utilities.Type.BUY)) tradesList = order.getBuyTrades();
		else tradesList = order.getSellTrades();
		if(tradesList != null)
		{
			
			JSONObject[] tradesArray = new JSONObject[tradesList.size()];
			
			int i = 0;
			for(TradeEntity t : tradesList)
			{
				tradesArray[i++] = tradeEntityAsJSONObject(t);
			}
			
			
			jo.put("trades", tradesArray);
		}
		else {
			jo.put("trades", new JSONObject[0]);
		}
		
		return jo;
	}
	
	public static void initOrderEntity(OrderEntity order)
	{
		long miliseconds = new Date().getTime();
		order.setCreatedDateTime(new Timestamp(miliseconds));
		order.setFilledQuantity(0.0);
		order.setStatus(Status.OPEN);
	}
	
	private static JSONObject tradeEntityAsJSONObject(TradeEntity trade)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("id", trade.getId());
		jo.put("buyOrderId", trade.getBuyOrder().getId());
		jo.put("sellOrderId", trade.getSellOrder().getId());
		jo.put("timestamp", trade.getCreatedDateTime().getTime());
		jo.put("price", trade.getPrice());
		jo.put("quantity", trade.getQuantity());
		
		return jo;
		
		
	}

	public static String validate(OrderEntity order, OrderRepository orderRepo)
	{
		if(!order.getCurrencyPair().equals(OrderEntity.fixedCurrencyPair)) return OrderValidationValues.INVALID_CURRENCY.getError();
		if(order.getPrice() <=  0) return OrderValidationValues.INVALID_PRICE.getError();
		if(order.getQuantity() <= 0) return OrderValidationValues.INVALID_QUANTITY.getError();
		
		if(!orderRepo.findById(order.getId()).isEmpty()) return OrderValidationValues.INVALID_ID.getError();
		else return "";
		
	}
	
	public static OrderEntity processRequest(OrderEntity order, OrderBookRepository orderBookRepository, OrderRepository orderRepository)
	{
		OrderBookEntity orderBook = orderBookRepository.findById((long) 1).get();
		
		//allOrdersToBeUpdated.add(order);
		
		if(order.getType() == Type.BUY)
		{
			
			processBuy(order, orderBook, orderRepository);
			
		}
		else
		{
			processSell(order, orderBook, orderRepository);
		}
		orderBookRepository.save(orderBook);
		order = orderRepository.save(order);
		return order;
	}
	
	private static void processBuy(OrderEntity order, OrderBookEntity orderBook, OrderRepository orderRepository)
	{
		List<OrderEntity> allOrdersToBeUpdated = new ArrayList<>();
		List<OrderBookElement> orderBookElementList = orderBook.getSellOrders();

		while(order.getQuantity() > 0.005 && !orderBookElementList.isEmpty())
		{
			OrderBookElement orderBookElement = orderBookElementList.get(0);
			if(orderBookElement.getPrice() <= order.getPrice() + 0.005)
			{
			
			List<OrderEntity> orderEntitiesList = orderRepository.findOrderEntitiesByPriceAndType(orderBookElement.getPrice(), Type.SELL);
			Double givenSum = 0.0;
			for(OrderEntity o : orderEntitiesList)
			{
				
				TradeEntity te = tradeIncomingBuyOrder(order, o);
				order.getBuyTrades().add(te);
				allOrdersToBeUpdated.add(o);
				givenSum += te.getQuantity();
				if(order.getQuantity() < 0.005) break;
			}
				orderBookElement.setQuantity(orderBookElement.getQuantity() - givenSum);
				if(orderBookElement.getQuantity() < 0.005) orderBookElementList.remove(0);
			
			}
			else break;
		}
		
		if(order.getQuantity() > 0.005)
		{
			List<OrderBookElement> orderBookBuyElementList = orderBook.getBuyOrders();
			addBuyOrderToOrderBook(order, orderBookBuyElementList);
		}
		
		orderRepository.saveAll(allOrdersToBeUpdated);
	}
	
	private static TradeEntity tradeIncomingBuyOrder(OrderEntity buyOrder, OrderEntity sellOrder)
	{
		Double price = sellOrder.getPrice();
		Double quantity = buyOrder.getQuantity() > sellOrder.getQuantity() ? sellOrder.getQuantity() : buyOrder.getQuantity();
		Timestamp createdDateTime = new Timestamp(new Date().getTime());
		
		buyOrder.decreaseQuantity(quantity);
		sellOrder.decreaseQuantity(quantity);
		
		if(buyOrder.getQuantity() < 0.005) buyOrder.setStatus(Status.CLOSED);
		if(sellOrder.getQuantity() < 0.005) sellOrder.setStatus(Status.CLOSED);
		
		TradeEntity te = new TradeEntity(buyOrder, sellOrder, price, quantity);
		te.setCreatedDateTime(createdDateTime);
		
		
		return te;
	}
	
	private static void addBuyOrderToOrderBook(OrderEntity order, List<OrderBookElement> orderBookElementList)
	{
		int i = 0;
		for(; i < orderBookElementList.size(); i++)
		{

			if(orderBookElementList.get(i).getPrice() + 0.005 > order.getPrice() && orderBookElementList.get(i).getPrice() - 0.005 < order.getPrice())
			{
				orderBookElementList.get(i).setQuantity(orderBookElementList.get(i).getQuantity() + order.getQuantity());
				break;
			}
			
			else if(orderBookElementList.get(i).getPrice() < order.getPrice())
			{
				OrderBookElement newOrderBookElement = new OrderBookElement(order.getPrice(), order.getQuantity());
				orderBookElementList.add(i, newOrderBookElement);
				break;
			}
		}
		
		if(i == orderBookElementList.size())
		{
			OrderBookElement newOrderBookElement = new OrderBookElement(order.getPrice(), order.getQuantity());
			orderBookElementList.add(newOrderBookElement);
		}
	}

	private static void processSell(OrderEntity order, OrderBookEntity orderBook, OrderRepository orderRepository)
	{
		List<OrderEntity> allOrdersToBeUpdated = new ArrayList<>();
		List<OrderBookElement> orderBookElementList = orderBook.getBuyOrders();

		while(order.getQuantity() > 0.005 && !orderBookElementList.isEmpty())
		{
			OrderBookElement orderBookElement = orderBookElementList.get(0);
			if(orderBookElement.getPrice() + 0.005 >= order.getPrice())
			{
			
			List<OrderEntity> orderEntitiesList = orderRepository.findOrderEntitiesByPriceAndType(orderBookElement.getPrice(), Type.BUY);
			Double givenSum = 0.0;
			for(OrderEntity o : orderEntitiesList)
			{
				
				TradeEntity te = tradeIncomingSellOrder(order, o);
				givenSum += te.getQuantity();
				order.getSellTrades().add(te);
				allOrdersToBeUpdated.add(o);
				if(order.getQuantity() < 0.005) break;
			}
				orderBookElement.setQuantity(orderBookElement.getQuantity() - givenSum);
				if(orderBookElement.getQuantity() < 0.005) orderBookElementList.remove(0);
				//System.out.println(orderBookElement.getQuantity());
			}
			else break;
		}
		
		if(order.getQuantity() > 0.005)
		{
			List<OrderBookElement> orderBookSellElementList = orderBook.getSellOrders();
			addSellOrderToOrderBook(order, orderBookSellElementList);
		}
		
		orderRepository.saveAll(allOrdersToBeUpdated);
	}

	private static TradeEntity tradeIncomingSellOrder(OrderEntity sellOrder, OrderEntity buyOrder)
	{
		Double price = buyOrder.getPrice();
		Double quantity = buyOrder.getQuantity() > sellOrder.getQuantity() ? sellOrder.getQuantity() : buyOrder.getQuantity();
		Timestamp createdDateTime = new Timestamp(new Date().getTime());
		
		buyOrder.decreaseQuantity(quantity);
		sellOrder.decreaseQuantity(quantity);
		
		if(buyOrder.getQuantity() < 0.005) buyOrder.setStatus(Status.CLOSED);
		if(sellOrder.getQuantity() < 0.005) sellOrder.setStatus(Status.CLOSED);
		
		TradeEntity te = new TradeEntity(buyOrder, sellOrder, price, quantity);
		te.setCreatedDateTime(createdDateTime);
		
		
		return te;
	}
	
	private static void addSellOrderToOrderBook(OrderEntity order, List<OrderBookElement> orderBookElementList)
	{
		int i = 0;
		for(; i < orderBookElementList.size(); i++)
		{

			if(orderBookElementList.get(i).getPrice() + 0.005 > order.getPrice() && orderBookElementList.get(i).getPrice() - 0.005 < order.getPrice())
			{
				orderBookElementList.get(i).setQuantity(orderBookElementList.get(i).getQuantity() + order.getQuantity());
				break;
			}
			else if(orderBookElementList.get(i).getPrice() > order.getPrice())
			{
				OrderBookElement newOrderBookElement = new OrderBookElement(order.getPrice(), order.getQuantity());
				orderBookElementList.add(i, newOrderBookElement);
				break;
			}
		}
		
		if(i == orderBookElementList.size())
		{
			OrderBookElement newOrderBookElement = new OrderBookElement(order.getPrice(), order.getQuantity());
			orderBookElementList.add(newOrderBookElement);
		}
	}
	
}
