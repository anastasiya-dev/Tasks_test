package by.it.academy.repository;

import by.it.academy.pojo.Utxo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtxoRepository extends JpaRepository<Utxo, String> {
}
