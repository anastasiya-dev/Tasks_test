package by.it.academy.service;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionInput;
import by.it.academy.pojo.TransactionOutput;
import by.it.academy.repository.BlockchainUtxoDao;
import by.it.academy.repository.TransactionDao;
import by.it.academy.repository.TransactionInputDao;
import by.it.academy.repository.TransactionOutputDao;
import by.it.academy.util.StringUtil;
import by.it.academy.util.TransactionOutputUtil;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

import static by.it.academy.Validation.factory;

//@Service
public class TransactionService {

    //    @Autowired
//    @Value("#{transactionDao}")
    TransactionDao transactionDao = new TransactionDao();

    //    @Autowired
//    @Value("#{blockchainUtxoDao}")
    BlockchainUtxoDao blockchainUtxoDao = new BlockchainUtxoDao();

    TransactionInputDao transactionInputDao = new TransactionInputDao();

    TransactionOutputDao transactionOutputDao = new TransactionOutputDao();

    private float minimumTransaction = 0.1f;

////    @Autowired
//    WalletService walletService;

    public boolean createNewTransaction(SessionFactory sessionFactory, Transaction transaction) {
        transactionDao.create(sessionFactory, transaction);
        return true;
    }

    public Transaction findTransactionById(SessionFactory sessionFactory, String id) {
        return (Transaction) transactionDao.findById(sessionFactory, id);
    }

    public ArrayList<Transaction> findAllTransactions(SessionFactory factory) {
        return (ArrayList<Transaction>) transactionDao.findAll(factory, "");
    }

//    public boolean deleteTransaction(Transaction transaction) {
//        return transactionDao.delete(transaction);
//    }
//
//    public Transaction updateTransaction(Transaction transaction) {
//        return (Transaction) transactionDao.update(transaction);
//    }


//    public ArrayList<Transaction> getAllForWallet(String walletId) {
//        ArrayList<Transaction> transactionsAll = (ArrayList<Transaction>) transactionDao.findAll("");
//        Wallet wallet = walletService.findWalletById(walletId);
//        String publicKey = StringUtil.getStringFromKey(wallet.getPublicKey());
//        ArrayList<Transaction> transactionsForWallet = new ArrayList<>();
//        for (Transaction transaction : transactionsAll) {
//            if (StringUtil.getStringFromKey(transaction.getRecipient())
//                    .equals(StringUtil.getStringFromKey(transaction.getSender()))) {
//                transactionsForWallet.add(transaction);
//                transaction.setValue(transaction.getValue() * Float.valueOf(-1));
//                transactionsForWallet.add(transaction);
//            } else if (StringUtil.getStringFromKey(transaction.getSender()).equals(publicKey)) {
//                transaction.setValue(transaction.getValue() * Float.valueOf(-1));
//                transactionsForWallet.add(transaction);
//            } else if (StringUtil.getStringFromKey(transaction.getRecipient()).equals(publicKey)) {
//                transactionsForWallet.add(transaction);
//            }
//        }
//        return transactionsForWallet;
//    }

    //Returns true if new transaction could be created.
    public boolean processTransaction(Transaction transaction) {

//        if (verifySignature(transaction) == false) {
//            System.out.println("#Transaction Signature failed to verify");
//            return false;
//        }

        //gather transaction inputs (Make sure they are unspent):
        for (TransactionInput i : transaction.inputs) {
            BlockchainUtxo UTXO = blockchainUtxoDao.findById(factory, i.transactionOutputId);
            TransactionOutput transactionOutput =
                    TransactionOutputUtil.createTransactionOutput(
                            UTXO.recipient,UTXO.value,transaction
                    );
            i.setTransactionOutput(transactionOutput);
////            i.transactionOutput.setTransactionInput((TransactionInput) transactionInputDao.findById(factory, UTXO.getTransactionInputId()));
//            i.transactionOutput.setTransaction((Transaction) transactionDao.findById(factory, UTXO.getParentTransactionId()));
//            i.transactionOutput.setValue(UTXO.getValue());
//            i.transactionOutput.setRecipient(UTXO.recipient);
            transactionInputDao.create(factory, i);
            transactionOutputDao.create(factory,transactionOutput);
//                    Blockchain.getUTXOs().get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if (getInputsValue(transaction) < minimumTransaction) {
            System.out.println("inputs for checking: " + getInputsValue(transaction));
            System.out.println("#Transaction Inputs to small: " + getInputsValue(transaction));
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue(transaction) - transaction.value; //get value of inputs then the left over change:
//        transactionId = calulateHash(transaction);
        transaction.outputs.add(TransactionOutputUtil.createTransactionOutput(transaction.getRecipient(), transaction.getValue(), transaction)); //send value to recipient
        transaction.outputs.add(TransactionOutputUtil.createTransactionOutput(transaction.getSender(), leftOver, transaction)); //send the left over 'change' back to sender

        createNewTransaction(factory, transaction);

        System.out.println(findTransactionById(factory, transaction.transactionId));

        //add outputs to Unspent list
        for (TransactionOutput o : transaction.outputs) {
            BlockchainUtxo blockchainUtxo = new BlockchainUtxo();
            blockchainUtxo.setBlockchainUtxoId(o.id);
            blockchainUtxo.setRecipient(o.recipient);
            blockchainUtxo.setTransactionInputId(o.transactionInput.transactionOutputId);
            blockchainUtxo.setParentTransactionId(o.getTransaction().transactionId);
            blockchainUtxo.setValue(o.value);
            blockchainUtxoDao.create(factory, blockchainUtxo);
//            Blockchain.getUTXOs().put(o.id, o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for (TransactionInput i : transaction.inputs) {
            if (i.transactionOutput == null) continue; //if Transaction can't be found skip it
            BlockchainUtxo blockchainUtxoDel = new BlockchainUtxo();
            blockchainUtxoDel.setBlockchainUtxoId(i.transactionOutput.id);
//            blockchainUtxoDel.setTransactionOutput(i.transactionOutput);

            blockchainUtxoDel.setRecipient(i.transactionOutput.recipient);
            blockchainUtxoDel.setTransactionInputId(i.transactionOutput.transactionInput.transactionOutputId);
            blockchainUtxoDel.setParentTransactionId(i.transactionOutput.getTransaction().transactionId);
            blockchainUtxoDel.setValue(i.transactionOutput.value);

            blockchainUtxoDao.delete(factory, blockchainUtxoDel);
//            Blockchain.getUTXOs().remove(i.transactionOutput.id);
        }

        return true;
    }

    //Verifies the data we signed hasn't been tampered with
    public boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    //returns sum of inputs(UTXOs) values
    public float getInputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionInput i : transaction.inputs) {
            if (i.transactionOutput == null) continue; //if Transaction can't be found skip it
            total += i.transactionOutput.value;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionOutput o : transaction.outputs) {
            total += o.value;
        }
        return total;
    }

}
