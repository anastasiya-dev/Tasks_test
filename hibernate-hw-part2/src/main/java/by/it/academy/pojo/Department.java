package by.it.academy.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
public class Department implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private String departmentId;

    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private String departmentName;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Employee> employees;
}
