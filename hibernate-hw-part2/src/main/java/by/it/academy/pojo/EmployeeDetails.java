package by.it.academy.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode
public class EmployeeDetails implements Serializable {

    private static final long serialVersionUID = 4L;

    @Id
    @GenericGenerator(name = "gen", strategy = "foreign",
            parameters = @Parameter(name = "property", value = "employee"))
    @GeneratedValue(generator = "gen")
    private long employeeId;

    @Column(name = "pension_plan")
    private String pensionPlan;

    private String hobbies;

    @OneToOne
    @PrimaryKeyJoinColumn
    @EqualsAndHashCode.Exclude
    private Employee employee;
}
