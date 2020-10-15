package sensors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sensors.pojo.Sensor;
import sensors.support.SensorStatus;

import java.util.ArrayList;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    ArrayList<Sensor> findBySensorIdAndSensorStatus(String sensorId, SensorStatus sensorStatus);

    ArrayList<Sensor> findBySensorStatus(SensorStatus sensorStatus);
}
