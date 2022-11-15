package repository;

import org.springframework.data.repository.CrudRepository;

import entities.OrderBookEntity;

public interface OrderBookRepository extends CrudRepository<OrderBookEntity, Integer> {

}
