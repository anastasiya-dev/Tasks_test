package sensors.support;

import sensors.pojo.Sensor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<RangeCheck, Sensor> {

    @Override
    public void initialize(RangeCheck constraintAnnotation) {

    }

    @Override
    public boolean isValid(Sensor sensor, ConstraintValidatorContext constraintValidatorContext) {
        if (sensor.getRangeFrom() == null || sensor.getRangeTo() == null) {
            return true;
        }
        return sensor.getRangeFrom() < sensor.getRangeTo();
    }
}
