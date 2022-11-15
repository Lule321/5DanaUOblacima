package repository;

import org.springframework.data.repository.CrudRepository;

import entities.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {

}
//translated into a bean with name orderRepository