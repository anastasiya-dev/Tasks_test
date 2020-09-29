package by.it.academy.service;

import by.it.academy.pojo.TransactionPackage;
import by.it.academy.repository.TransactionPackageRepository;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class TransactionPackageService {

    @Autowired
    TransactionPackageRepository transactionPackageRepository;
    @Autowired
    TransactionPackage transactionPackage;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(TransactionPackageService.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public TransactionPackage createTransactionPackage(String blockAttemptedId, String transactionId, String miningSessionId) {
        transactionPackage.setTransactionPackageId(blockAttemptedId + " / " + transactionId + " / " + miningSessionId);
        transactionPackage.setBlockAttemptedId(blockAttemptedId);
        transactionPackage.setTransactionId(transactionId);
        transactionPackage.setMiningSessionId(miningSessionId);
        TransactionPackage saved = transactionPackageRepository.save(transactionPackage);
        logger.info("Creating transaction package: " + transactionPackage);
        return saved;
    }

    public ArrayList<TransactionPackage> findAllTransactionPackagesByBlockId(String blockId) {
        logger.info("Extracting all transaction packages for block: " + blockId);
        ArrayList<TransactionPackage> allPackages = findAll();
        ArrayList<TransactionPackage> packagesByBlockId = new ArrayList<>();
        for (TransactionPackage transactionPackageIterating : allPackages) {
            if (transactionPackageIterating.getBlockAttemptedId().equals(blockId)) {
                packagesByBlockId.add(transactionPackageIterating);
            }
        }
        return packagesByBlockId;
    }

    public ArrayList<TransactionPackage> findAll() {
        logger.info("Extracting all transaction packages");
        return (ArrayList<TransactionPackage>) transactionPackageRepository.findAll();
    }
}
