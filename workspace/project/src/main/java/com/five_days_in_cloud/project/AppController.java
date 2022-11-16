package com.five_days_in_cloud.project;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.five_days_in_cloud.project.utilities.OrderRequest;

import entities.OrderBookElement;
import entities.OrderBookEntity;
import entities.OrderEntity;
import entities.TradeEntity;
import entities.utilities.Status;
import entities.utilities.Type;
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
	 	
		String error = OrderRequest.validate(order, orderRepository);
		if(!error.equals("")) return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		
		OrderRequest.initOrderEntity(order);
		
		order = orderRepository.save(order);
		
		order = OrderRequest.processRequest(order, orderBookRepository, orderRepository);
		
		JSONObject jo = OrderRequest.orderEntityAsJSONObject(order);
		
		return new ResponseEntity<>(jo.toString(), HttpStatus.CREATED);
	}
	
	@GetMapping("/order/{id}")
	public ResponseEntity<String> orderById(@PathVariable Long id)
	{
		Optional<OrderEntity> optional = orderRepository.findById(id);	
		OrderEntity order = null;
		if(optional.isPresent()) order = optional.get();
		else return new ResponseEntity<>("Order with this id doesn't not exist!", HttpStatus.BAD_REQUEST);
		
		JSONObject jo = OrderRequest.orderEntityAsJSONObject(order);
		
		return new ResponseEntity<>(jo.toString(), HttpStatus.OK);
		
	}
	
	@GetMapping("/orderbook")
	public ResponseEntity<OrderBookEntity> orderBook()
	{
		JSONObject jo = new JSONObject();
		
		Long id = (long) 1;
		OrderBookEntity ob = null;
		if(orderBookRepository.findById(id).isEmpty())
		{
			ob = new OrderBookEntity();
			orderBookRepository.save(ob);
		}
		else ob = orderBookRepository.findById(id).get();
		
		return new ResponseEntity<>(ob, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete()
	{
		tradeRepository.deleteAll();
		orderRepository.deleteAll();
		
		Long id = (long) 1;
		
		OrderBookEntity ob = orderBookRepository.findById(id).get();
		
		ob.getBuyOrders().clear();
		ob.getSellOrders().clear();
		
		orderBookRepository.save(ob);
		
		
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	/*
	@GetMapping("/test/{price}")
	public ResponseEntity<List<OrderEntity>> test(@PathVariable Double price)
	{
		List<OrderEntity> list = orderRepository.findOrderEntitiesByPrice(price);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	*/
}

