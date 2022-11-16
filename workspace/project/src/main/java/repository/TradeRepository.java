package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import entities.TradeEntity;

public interface TradeRepository extends JpaRepository<TradeEntity, Long> {

}
