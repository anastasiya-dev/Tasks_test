package by.it.academy.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Meeting {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private String subject;

    @ManyToMany
    @JoinTable(name = "meeting_employee",
            joinColumns = {
                    @JoinColumn(name = "meeting_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "employee_id")
            })
    private Set<Employee> employees = new HashSet<>();

    @Override
    public int hashCode() {
        final int hashCode = 29 * getSubject().hashCode();
        return hashCode;
    }
}
