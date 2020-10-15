package sensors.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sensors.pojo.Sensor;
import sensors.repository.SensorRepository;
import sensors.support.SensorStatus;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SensorService {

    @Autowired
    SensorRepository sensorRepository;
    @Autowired
    Sensor sensor;

    public List<Sensor> getAll(SensorStatus sensorStatus) {
        return sensorRepository.findBySensorStatus(sensorStatus);
    }

    public Sensor saveSensor(Sensor sensorInput) {
        Sensor sensorInDatabase;
        if (sensorInput.getSensorId() == null ||
                findByIdAndStatus(sensorInput.getSensorId(), SensorStatus.ACTIVE) == null) {
            sensorInDatabase = sensor;
            sensorInDatabase.setSensorStatus(SensorStatus.ACTIVE);
        } else {
            sensorInDatabase = findByIdAndStatus(sensorInput.getSensorId(), SensorStatus.ACTIVE);
        }
        sensorInDatabase.setName(sensorInput.getName());
        sensorInDatabase.setModel(sensorInput.getModel());
        sensorInDatabase.setRangeFrom(sensorInput.getRangeFrom());
        sensorInDatabase.setRangeTo(sensorInput.getRangeTo());
        sensorInDatabase.setType(sensorInput.getType());
        sensorInDatabase.setUnit(sensorInput.getUnit());
        sensorInDatabase.setLocation(sensorInput.getLocation());
        sensorInDatabase.setDescription(sensorInput.getDescription());
        sensorInDatabase.setSensorId(sensorInput.getSensorId());
        Sensor sensorSaved = sensorRepository.save(sensorInDatabase);
        log.info("After saving: " + sensorSaved);
        return findByIdAndStatus(sensorSaved.getSensorId(), sensorSaved.getSensorStatus());
    }

    public Sensor findByIdAndStatus(String sensorId, SensorStatus sensorStatus) {
        try {
            return sensorRepository.findBySensorIdAndSensorStatus(sensorId, sensorStatus).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void delete(String sensorId) {
        Sensor sensorInDatabase = findByIdAndStatus(sensorId, SensorStatus.ACTIVE);
        sensorInDatabase.setSensorStatus(SensorStatus.DELETED);
        sensorRepository.save(sensorInDatabase);
    }
}
