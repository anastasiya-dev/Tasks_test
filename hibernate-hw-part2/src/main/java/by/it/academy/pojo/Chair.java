package by.it.academy.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "chair")
public class Chair {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;
    @Column(name = "produced_date")
    private Date producedDate;
    @Column(name = "manufacturer")
    private String manufacturer;
    @Column(name = "furniture_class")
    private char furnitureClass;
}
