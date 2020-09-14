package by.it.academy;

import by.it.academy.pojo.Block;
import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.Transaction;
import by.it.academy.repository.BlockDao;
import by.it.academy.service.*;
import by.it.academy.util.BlockUtil;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Consistency {

    public static SessionFactory factory = null;
    public static int difficulty = 3;

    static BlockDao blockDao = new BlockDao();
    static BlockService blockService = new BlockService();
    static TransactionService transactionService = new TransactionService();
    static UserService userService = new UserService();
    static WalletService walletService = new WalletService();
    static BlockchainUtxoService blockchainUtxoService = new BlockchainUtxoService();

    public static void main(String[] args) {
        factory = start();

        System.out.println(isChainValid());

        finish(factory);
    }

    private static SessionFactory start() {
        StandardServiceRegistry registry = startRegistry();
        SessionFactory factory = null;

        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return factory;
    }

    private static StandardServiceRegistry startRegistry() {
        return new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
    }

    private static void finish(SessionFactory factory) {
        if (!factory.isClosed()) {
            factory.close();
            factory = null;
        }
    }

    private static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        List<BlockchainUtxo> tempUTXOs = blockchainUtxoService.findAllUTXOs(factory); //a temporary working list of unspent transactions at a given block state.
//        tempUTXOs.add(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        ArrayList<Block> blockchain = (ArrayList<Block>) blockDao.findAll(factory, "");
        System.out.println("before sort:");
        for (Block block : blockchain) {
            System.out.println(block.hash);
        }
        blockchain.sort(Comparator.comparingLong(b -> b.getTimeStamp()));
        System.out.println("after sort:");
        for (Block block : blockchain) {
            System.out.println(block.hash);
        }

        //loop through blockchain to check hashes:
        for (
                int i = 1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(BlockUtil.calculateHash(currentBlock))) {
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
//            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if (!transactionService.verifySignature(currentTransaction)) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if (transactionService.getInputsValue(factory, currentTransaction) !=
                        transactionService.getOutputsValue(factory, currentTransaction)) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for (BlockchainUtxo blockchainUtxo : tempUTXOs) {
                    System.out.println("transaction: " + currentTransaction.transactionId);
                    System.out.println("UTXO: " + blockchainUtxo.getBlockchainUtxoId());
                    float leftOver;
                    if (blockchainUtxo.getOutputTransactionId() != null) {
                        if (blockchainUtxo.getOutputTransactionId().equals(currentTransaction.transactionId)) {
                            if (blockchainUtxo.getRecipient().equals(currentTransaction.getRecipient())) {
                                System.out.println("recipient check");
                                System.out.println("blockchain: " + blockchainUtxo.value);
                                System.out.println("transaction: " + currentTransaction.value);
                                return blockchainUtxo.value == currentTransaction.value;
                            }
                        }
                    }
                }


//                for (TransactionInput input : currentTransaction.inputs) {
//                    tempOutput = tempUTXOs.get(input.transactionOutputId);
//
//                    if (tempOutput == null) {
//                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
//                        return false;
//                    }
//
//                    if (input.transactionOutput.value != tempOutput.value) {
//                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
//                        return false;
//                    }
//
//                    tempUTXOs.remove(input.transactionOutputId);
//                }

//                for (TransactionOutput output : currentTransaction.outputs) {
//                    tempUTXOs.put(output.id, output);
//                }
//
//                if (currentTransaction.outputs.get(0).getRecipient() != currentTransaction.getRecipient()) {
//                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
//                    return false;
//                }
//                if (currentTransaction.outputs.get(1).getRecipient() != currentTransaction.sender) {
//                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
//                    return false;
//                }
//
            }
        }
        System.out.println("Blockchain is valid");
        return true;
    }
}
