package com.five_days_in_cloud.project;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.five_days_in_cloud.project.utilities.OrderRequest;

import entities.OrderEntity;
import entities.utilities.Status;
import repository.OrderBookRepository;
import repository.OrderRepository;
import repository.TradeRepository;

@Controller
@RequestMapping(path="/app")
public class AppController {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private TradeRepository tradeRepository;
	
	@Autowired
	private OrderBookRepository orderBookRepository;
	
	@GetMapping("/")
	public @ResponseBody String index()
	{
		return "Greetings from Spring Boot Controller!";
	}
	
	
	@PostMapping(
		value = "/order",
		consumes = {MediaType.APPLICATION_JSON_VALUE}
		)
	public ResponseEntity<String> order(@RequestBody OrderEntity order)
	{
		long miliseconds = new Date().getTime();
		order.setCreatedDateTime(new Timestamp(miliseconds));
		order.setFilledQuantity(0.0);
		order.setStatus(Status.OPEN);
		OrderEntity persistedOrder = orderRepository.save(order);
		
		JSONObject jo = OrderRequest.orderEntityAsJSONObject(persistedOrder);
		
		

		
		return new ResponseEntity<>(jo.toString(), HttpStatus.CREATED);
	}
	
	
}

