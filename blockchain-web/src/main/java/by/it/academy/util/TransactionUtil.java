package by.it.academy.util;

import by.it.academy.pojo.Transaction;
import by.it.academy.pojo.TransactionInput;
import by.it.academy.pojo.TransactionOutput;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class TransactionUtil {

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
    }

    //Verifies the data we signed hasn't been tampered with
    public static boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getReciepient()) + Float.toString(transaction.getValue());
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

//    //Returns true if new transaction could be created.
//    public static boolean processTransaction() {
//
//        if (verifySignature() == false) {
//            System.out.println("#Transaction Signature failed to verify");
//            return false;
//        }
//
//        //gather transaction inputs (Make sure they are unspent):
//        for (TransactionInput i : inputs) {
//            i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
//        }
//
//        //check if transaction is valid:
//        if (getInputsValue() < NoobChain.minimumTransaction) {
//            System.out.println("#Transaction Inputs to small: " + getInputsValue());
//            return false;
//        }
//
//        //generate transaction outputs:
//        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
//        transactionId = calulateHash();
//        outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); //send value to recipient
//        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender
//
//        //add outputs to Unspent list
//        for (TransactionOutput o : outputs) {
//            NoobChain.UTXOs.put(o.id, o);
//        }
//
//        //remove transaction inputs from UTXO lists as spent:
//        for (TransactionInput i : inputs) {
//            if (i.UTXO == null) continue; //if Transaction can't be found skip it
//            NoobChain.UTXOs.remove(i.UTXO.id);
//        }
//
//        return true;
//    }
//
//    //returns sum of inputs(UTXOs) values
//    public static float getInputsValue() {
//        float total = 0;
//        for (TransactionInput i : inputs) {
//            if (i.UTXO == null) continue; //if Transaction can't be found skip it
//            total += i.UTXO.value;
//        }
//        return total;
//    }
//
//    //returns sum of outputs:
//    public static float getOutputsValue() {
//        float total = 0;
//        for (TransactionOutput o : outputs) {
//            total += o.value;
//        }
//        return total;
//    }
}
