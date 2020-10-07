package by.it.academy.repository;

import by.it.academy.pojo.Utxo;
import org.springframework.data.repository.CrudRepository;

public interface UtxoRepository extends CrudRepository<Utxo, String> {
}
