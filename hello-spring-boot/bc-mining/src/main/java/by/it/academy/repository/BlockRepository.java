package by.it.academy.repository;

import by.it.academy.pojo.Block;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface BlockRepository extends CrudRepository<Block, String> {
}
