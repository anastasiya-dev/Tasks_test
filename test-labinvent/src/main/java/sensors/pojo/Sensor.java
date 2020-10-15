package sensors.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
import sensors.support.RangeCheck;
import sensors.support.SensorStatus;
import sensors.support.SensorType;
import sensors.support.SensorUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@RangeCheck(message = "From value should be less than To value in the range")
public class Sensor {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String sensorId;
    @NotNull
    @Size(
            max = 30,
            message = "The sensor name length '${validatedValue}' must not exceed {max} characters"
    )
    private String name;
    @NotNull
    @Size(
            max = 15,
            message = "The sensor model length '${validatedValue}' must not exceed {max} characters"
    )
    private String model;
    private Integer rangeFrom;
    private Integer rangeTo;
    @NotNull
    private SensorType type;
    @NotNull
    private SensorUnit unit;
    @Transient
    private String unitValue;
    @Size(
            max = 40,
            message = "The sensor location length '${validatedValue}' must not exceed {max} characters"
    )
    private String location;
    @Size(
            max = 200,
            message = "The sensor description length '${validatedValue}' must not exceed {max} characters"
    )
    private String description;
    private SensorStatus sensorStatus;

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId='" + sensorId + '\'' +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", rangeFrom=" + rangeFrom +
                ", rangeTo=" + rangeTo +
                ", type=" + type +
                ", unit=" + unit +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", sensorStatus=" + sensorStatus +
                '}';
    }
}
