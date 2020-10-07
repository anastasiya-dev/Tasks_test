package by.it.academy.repository;

import by.it.academy.pojo.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction,String> {

}
