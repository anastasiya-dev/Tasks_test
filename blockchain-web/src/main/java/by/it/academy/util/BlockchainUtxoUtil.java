package by.it.academy.util;

import by.it.academy.pojo.BlockchainUtxo;
import by.it.academy.pojo.TransactionOutput;

import java.security.PublicKey;

public class BlockchainUtxoUtil {
    public static BlockchainUtxo createBcUtxo(String id, TransactionOutput transactionOutput) {
        BlockchainUtxo blockchainUtxo = new BlockchainUtxo();
        blockchainUtxo.setBlockchainUtxoId(id);
        blockchainUtxo.setRecipient(transactionOutput.recipient);
        blockchainUtxo.setTransactionInputId(transactionOutput.transactionInput.transactionOutputId);
        blockchainUtxo.setParentTransactionId(transactionOutput.getTransaction().transactionId);
        blockchainUtxo.setValue(transactionOutput.value);
        return blockchainUtxo;
    }

    //Check if coin belongs to you
    public static boolean isMine(BlockchainUtxo blockchainUtxo, PublicKey publicKey) {
        return (publicKey.equals(blockchainUtxo.getRecipient()));
    }
}
