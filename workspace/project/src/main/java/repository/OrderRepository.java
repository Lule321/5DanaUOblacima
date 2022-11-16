package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import entities.OrderEntity;
import entities.utilities.Type;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	
	@Query("SELECT o FROM OrderEntity o WHERE o.price = :price AND o.type = :type ORDER BY o.createdDateTime ASC")
	List<OrderEntity> findOrderEntitiesByPriceAndType(@Param("price") Double price, @Param("type") Type type);
}
//translated into a bean with name orderRepository