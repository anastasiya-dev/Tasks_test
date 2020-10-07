package by.it.academy.repository;

import by.it.academy.pojo.MiningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiningSessionRepository extends JpaRepository<MiningSession, String> {
}
