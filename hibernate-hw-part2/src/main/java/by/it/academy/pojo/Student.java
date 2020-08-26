package by.it.academy.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Student")
//for table per class hierarchy strategy
//@DiscriminatorValue("S")

//for table per subclass strategy
//@PrimaryKeyJoinColumn(name = "PERSON_ID")

//for table per class strategy - no annotation

public class Student extends Person {
    private static final long serialVersionUID = 2L;
    private String faculty;
    private Double mark;
}
