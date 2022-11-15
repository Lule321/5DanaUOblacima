package com.five_days_in_cloud.project.utilities;

import org.json.JSONObject;

import entities.OrderEntity;

public class OrderRequest {

	public static JSONObject orderEntityAsJSONObject(OrderEntity order)
	{
		String dateString = order.getCreatedDateTime().toString();
		JSONObject jo = new JSONObject();
		jo.put("id", order.getId());
		jo.put("currentDateTime", dateString);
		jo.put("currencyPair", order.getCurrencyPair());
		jo.put("type", order.getType().toString());
		jo.put("price", order.getPrice());
		jo.put("quantity", order.getQuantity());
		jo.put("filledQuantity", order.getFilledQuantity());
		jo.put("orderStatus", order.getStatus().toString());
		
		
		
		jo.put("trades", "");
		
		return jo;
	}
	
}
