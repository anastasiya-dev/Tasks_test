package by.it.academy.util;

import by.it.academy.pojo.Blockchain;
import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionInput;
import by.it.academy.pojo.TransactionOutput;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionUtil {

    private static float minimumTransaction = 0.1f;

    // Constructor:
    public static Transaction createTransaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        Transaction transaction = new Transaction();
        transaction.setSender(from);
        transaction.setReciepient(to);
        transaction.setValue(value);
        transaction.setInputs(inputs);
        return transaction;
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private static String calulateHash(Transaction transaction) {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(transaction.getSender()) +
                        StringUtil.getStringFromKey(transaction.getReciepient()) +
                        Float.toString(transaction.getValue()) + transaction.getTransactionId()
        );
    }

    //Signs all the data we don't wish to be tampered with.
    public static void generateSignature(Transaction transaction, PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getReciepient()) + Float.toString(transaction.getValue());
        transaction.setSignature(StringUtil.applyECDSASig(privateKey, data));
        transaction.setTransactionDateTime(LocalDateTime.now());
    }

    //Verifies the data we signed hasn't been tampered with
    public static boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getReciepient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    //Returns true if new transaction could be created.
    public static boolean processTransaction(Transaction transaction) {

        if (verifySignature(transaction) == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for (TransactionInput i : transaction.inputs) {
            i.transactionOutput = Blockchain.getUTXOs().get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if (getInputsValue(transaction) < minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue(transaction));
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue(transaction) - transaction.value; //get value of inputs then the left over change:
//        transactionId = calulateHash(transaction);
        transaction.outputs.add(TransactionOutputUtil.createTransactionOutput(transaction.getReciepient(), transaction.getValue(), transaction)); //send value to recipient
        transaction.outputs.add(TransactionOutputUtil.createTransactionOutput(transaction.getSender(), leftOver, transaction)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for (TransactionOutput o : transaction.outputs) {
            Blockchain.getUTXOs().put(o.id, o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for (TransactionInput i : transaction.inputs) {
            if (i.transactionOutput == null) continue; //if Transaction can't be found skip it
            Blockchain.getUTXOs().remove(i.transactionOutput.id);
        }

        return true;
    }

    //returns sum of inputs(UTXOs) values
    public static float getInputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionInput i : transaction.inputs) {
            if (i.transactionOutput == null) continue; //if Transaction can't be found skip it
            total += i.transactionOutput.value;
        }
        return total;
    }

    //returns sum of outputs:
    public static float getOutputsValue(Transaction transaction) {
        float total = 0;
        for (TransactionOutput o : transaction.outputs) {
            total += o.value;
        }
        return total;
    }
}
