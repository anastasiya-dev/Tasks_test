package by.it.academy.service;

import by.it.academy.pojo.TransactionPackage;
import by.it.academy.repository.TransactionPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionPackageService {

    @Autowired
    TransactionPackageRepository transactionPackageRepository;
    @Autowired
    TransactionPackage transactionPackage;

    public TransactionPackage createTransactionPackage(String blockId, String transactionId) {
        transactionPackage.setBlockId(blockId);
        transactionPackage.setTransactionId(transactionId);

        TransactionPackage saved = transactionPackageRepository.save(transactionPackage);
//        logger.info("Creating block: " + block);
        return saved;
    }

    public ArrayList<TransactionPackage> findAllTransactionPackagesByBlockId(String blockId) {
//        logger.info("Extracting all tr");
        ArrayList<TransactionPackage> allPackages = findAll();
        ArrayList<TransactionPackage> packagesByBlockId = new ArrayList<>();
        for(TransactionPackage transactionPackage: allPackages){
            if(transactionPackage.getBlockId().equals(blockId)){
                packagesByBlockId.add(transactionPackage);
            }
        }
        return packagesByBlockId;
    }

    public ArrayList<TransactionPackage> findAll() {
//        logger.info("Extracting all tr");
        return (ArrayList<TransactionPackage>) transactionPackageRepository.findAll();
    }
}
