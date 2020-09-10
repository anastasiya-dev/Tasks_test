package by.it.academy.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;

@Entity
public class Blockchain {

    @Id
    int id = 0;

    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public static HashMap<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
}
