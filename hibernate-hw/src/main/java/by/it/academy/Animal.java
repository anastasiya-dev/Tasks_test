package by.it.academy;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(generator = "system-uuid",strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    String id;

    @Column(name = "date_of_birth")
    Date dateOfBirth;

    @Column
    String name;

    @Column(name = "number_of_legs")
    int numberOfLegs;

    @Column
    char sex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return numberOfLegs == animal.numberOfLegs &&
                sex == animal.sex &&
                id.equals(animal.id) &&
                dateOfBirth.equals(animal.dateOfBirth) &&
                name.equals(animal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfBirth, name, numberOfLegs, sex);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id='" + id + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", name='" + name + '\'' +
                ", numberOfLegs=" + numberOfLegs +
                ", sex=" + sex +
                '}';
    }
}

