package repository;

import org.springframework.data.repository.CrudRepository;

import entities.TradeEntity;

public interface TradeRepository extends CrudRepository<TradeEntity, Integer> {

}
