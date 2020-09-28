package by.it.academy.repository;

import by.it.academy.pojo.MiningSession;
import by.it.academy.support.MiningSessionStatus;
import org.springframework.data.repository.CrudRepository;

public interface MiningSessionRepository extends CrudRepository<MiningSession, String> {
}
