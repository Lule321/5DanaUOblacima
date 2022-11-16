package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import entities.OrderBookEntity;

public interface OrderBookRepository extends JpaRepository<OrderBookEntity, Long> {

}
