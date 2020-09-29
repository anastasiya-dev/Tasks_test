package by.it.academy.repository;

import by.it.academy.pojo.BlockTemporary;
import org.springframework.data.repository.CrudRepository;

public interface BlockTemporaryRepository extends CrudRepository<BlockTemporary, String> {
    BlockTemporary findByMiningSessionId(String miningSessionId);
}
