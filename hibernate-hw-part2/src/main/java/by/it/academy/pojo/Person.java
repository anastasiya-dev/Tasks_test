package by.it.academy.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

import static lombok.EqualsAndHashCode.*;

@Data
@Entity
@Table(name = "PERSON")
//for table per class hierarchy strategy
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "Person_Type", discriminatorType =DiscriminatorType.CHAR)
//@DiscriminatorValue("P")

//for table per subclass strategy
//@Inheritance(strategy = InheritanceType.JOINED)

//for table per class strategy
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private Integer age;

    @Column
    private String name;

    @Column
    private String surname;

    @Embedded
    private Address address;
}
