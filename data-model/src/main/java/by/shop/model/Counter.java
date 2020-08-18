package by.shop.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data

public class Counter {

    //1000 transactions, each reads the state of the counter and makes commit
    //need to get the correct value of the counter

    @Id
    private int id;
    private long count;

}
