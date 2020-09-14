//package by.it.academy;
//
//import by.it.academy.pojo.Block;
//import by.it.academy.pojo.Transaction;
//import by.it.academy.pojo.Wallet;
//import by.it.academy.repository.BlockDao;
//import by.it.academy.service.BlockService;
//import by.it.academy.service.TransactionService;
//import by.it.academy.util.BlockUtil;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//
//import static by.it.academy.Start.walletService;
//
//public class Validation {
//    public static int difficulty = 3;
//    public static SessionFactory factory = null;
//    public static Transaction genesisTransaction;
//
//    static BlockDao blockDao = new BlockDao();
//    static BlockService blockService = new BlockService();
//    static TransactionService transactionService = new TransactionService();
//
//    public static void main(String[] args) {
//
//        factory = start();
//        String genericBlockHash = "0000f9ff930d31ba20357d6bd187da8a034da031708df0f4b2e4a4cd1dc500a1";
//
//        Block block1 = BlockUtil.createBlock(genericBlockHash);
//        String firstActualWalletNumber = "4054c7bf-347f-480e-b86f-69a0b3011319";
//        Wallet firstActualWallet = walletService.findWalletById(factory, firstActualWalletNumber);
//
//        System.out.println("\nWalletA's balance is: " + walletService.getBalance(factory,firstActualWallet));
//        System.out.println("\nWalletA is Attempting to send funds (400) to WalletB...");
////        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 400f));
////        addBlock(block1);
////        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
////        System.out.println("WalletB's balance is: " + walletB.getBalance());
//
//        finish(factory);
//
//    }
//
//    private static SessionFactory start() {
//        StandardServiceRegistry registry = startRegistry();
//        SessionFactory factory = null;
//
//        try {
//            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//        } catch (Exception e) {
//            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
//            // so destroy it manually.
//            e.printStackTrace();
//            StandardServiceRegistryBuilder.destroy(registry);
//        }
//        return factory;
//    }
//
//    private static StandardServiceRegistry startRegistry() {
//        return new StandardServiceRegistryBuilder()
//                .configure("hibernate.cfg.xml")
//                .build();
//    }
//
//    private static void finish(SessionFactory factory) {
//        if (!factory.isClosed()) {
//            factory.close();
//            factory = null;
//        }
//    }
//
////    private static Boolean isChainValid() {
////        Block currentBlock;
////        Block previousBlock;
////        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
////        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
////        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
////
////        ArrayList <Block> blockchain = (ArrayList<Block>) blockDao.findAll(factory,"");
////        //loop through blockchain to check hashes:
////        for (int i = 1; i < blockchain.size(); i++) {
////
////            currentBlock = blockchain.get(i);
////            previousBlock = blockchain.get(i - 1);
////            //compare registered hash and calculated hash:
////            if (!currentBlock.hash.equals(BlockUtil.calculateHash(currentBlock))) {
////                System.out.println("#Current Hashes not equal");
////                return false;
////            }
////            //compare previous hash and registered previous hash
////            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
////                System.out.println("#Previous Hashes not equal");
////                return false;
////            }
////            //check if hash is solved
////            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
////                System.out.println("#This block hasn't been mined");
////                return false;
////            }
////
////            //loop thru blockchains transactions:
////            TransactionOutput tempOutput;
////            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
////                Transaction currentTransaction = currentBlock.getTransactions().get(t);
////
////                if (!transactionService.verifySignature(currentTransaction)) {
////                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
////                    return false;
////                }
////                if (transactionService.getInputsValue(currentTransaction) != transactionService.getOutputsValue(currentTransaction)) {
////                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
////                    return false;
////                }
////
////                for (TransactionInput input : currentTransaction.inputs) {
////                    tempOutput = tempUTXOs.get(input.transactionOutputId);
////
////                    if (tempOutput == null) {
////                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
////                        return false;
////                    }
////
////                    if (input.transactionOutput.value != tempOutput.value) {
////                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
////                        return false;
////                    }
////
////                    tempUTXOs.remove(input.transactionOutputId);
////                }
////
////                for (TransactionOutput output : currentTransaction.outputs) {
////                    tempUTXOs.put(output.id, output);
////                }
////
////                if (currentTransaction.outputs.get(0).getRecipient() != currentTransaction.getRecipient()) {
////                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
////                    return false;
////                }
////                if (currentTransaction.outputs.get(1).getRecipient() != currentTransaction.sender) {
////                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
////                    return false;
////                }
////
////            }
////        }
////        System.out.println("Blockchain is valid");
////        return true;
////    }
//
//}
