package sensors.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sensors.pojo.Sensor;
import sensors.service.SensorService;
import sensors.support.SensorStatus;

@Slf4j
@Controller
@Secured("ROLE_Administrator")
public class SensorEditController {

    @Autowired
    SensorService sensorService;

    private String sensorsTableRedirect = "redirect:/sensors-table";

    @GetMapping(value = "/add-sensor")
    public ModelAndView sensorPage(ModelAndView modelAndView) {
        log.info("Add sensor page");
        modelAndView.setViewName("add-sensor");
        return modelAndView;
    }

    @PostMapping(value = "/add-sensor")
    public String createSensor(ModelAndView modelAndView,
                               @ModelAttribute Sensor sensorInput,
                               RedirectAttributes redirectAttributes
    ) {
        log.info("Received sensor input: " + sensorInput);
        try {
            Sensor sensorSaved = sensorService.saveSensor(sensorInput);
            modelAndView.setViewName("add-sensor");
            redirectAttributes.addAttribute("sensorId", sensorSaved.getSensorId());
            log.info("Sensor saved");
            return "redirect: /test-labinvent/edit-sensor/{sensorId}";
        } catch (Exception e) {
            return "redirect:/incorrect-input";
        }
    }

    @GetMapping(value = "/edit-sensor/{sensorId}")
    public ModelAndView editSensor(ModelAndView modelAndView,
                                   @PathVariable String sensorId) {
        log.info("Edit sensor page");
        modelAndView.setViewName("add-sensor");
        modelAndView.addObject("sensor", sensorService.findByIdAndStatus(sensorId, SensorStatus.ACTIVE));
        return modelAndView;
    }

    @GetMapping(value = "/incorrect-input")
    public ModelAndView incorrectInput(ModelAndView modelAndView) {
        log.warn("Incorrect user input for sensor info");
        modelAndView.setViewName("incorrect-input");
        return modelAndView;
    }

    @GetMapping(value = "/delete-sensor/{sensorId}")
    public String deleteSensor(ModelAndView modelAndView,
                               @PathVariable String sensorId) {
        log.info("Deleting sensor: " + sensorId);
        sensorService.delete(sensorId);
        return sensorsTableRedirect;
    }

    @GetMapping(value = "/add-sensor/cancel")
    public String cancel(ModelAndView modelAndView) {
        log.info("Edit sensor cancelled");
        return sensorsTableRedirect;
    }
}
