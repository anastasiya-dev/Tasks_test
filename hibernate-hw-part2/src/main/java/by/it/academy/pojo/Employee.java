package by.it.academy.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Employee")
//for table per class hierarchy strategy
//@DiscriminatorValue("E")

//for table per subclass strategy
//@PrimaryKeyJoinColumn(name = "PERSON_ID")

//for table per class strategy - no annotation
@NamedQueries(@NamedQuery(
        name = "get_employee_by_id",
        query = "Select E from Employee E where E.id=:id"
)
)
public class Employee extends Person {

    private static final long serialVersionUID = 3L;

    private String company;
    private Double salary;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private EmployeeDetails employeeDetails;

    @ManyToOne
    @JoinColumn(name = "FK_DepartmentDepartmentId")
    @EqualsAndHashCode.Exclude
    private Department department;

    @ManyToMany(mappedBy = "employees")
    @EqualsAndHashCode.Exclude
    private Set<Meeting> meetings = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "EMPLOYEES_TASKS", joinColumns = {@JoinColumn(name = "employee_id")}, inverseJoinColumns = {@JoinColumn(name = "task_id")})
    private List<Task> tasks = new ArrayList<>();

    @Override
    public int hashCode() {
        final int hashCode = 17 * getName().hashCode();
        return hashCode;
    }
}
