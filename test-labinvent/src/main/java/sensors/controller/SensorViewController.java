package sensors.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import sensors.pojo.Sensor;
import sensors.service.SensorService;
import sensors.support.FilterInput;
import sensors.support.SensorStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/sensors-table")
public class SensorViewController {

    @Autowired
    SensorService sensorService;

    private FilterInput savedFilterInput = null;

    @RequestMapping(value = {"", "/{page}"}, method = RequestMethod.GET)
    public ModelAndView viewAllSensors(
            @ModelAttribute FilterInput filterInput,
            @PathVariable(required = false, name = "page") String page,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("Received filter requirement " + filterInput);

        if (page == null) {
            savedFilterInput = filterInput;
        } else {
            filterInput = savedFilterInput;
        }

        ModelAndView modelAndView = new ModelAndView();

        List<Sensor> sensors = sensorService.getAll(SensorStatus.ACTIVE);
        ArrayList<Sensor> sensorsFiltered = new ArrayList<>();
        String viewerInput = filterInput.getViewerInput();

        for (Sensor sensor : sensors) {

            if (filterInput.getViewerInput() == null
                    || sensor.getModel().contains(viewerInput)
                    || (sensor.getRangeFrom() != null && sensor.getRangeFrom().toString().contains(viewerInput))
                    || (sensor.getRangeTo() != null && sensor.getRangeTo().toString().contains(viewerInput))
                    || sensor.getType().toString().contains(viewerInput)
                    || sensor.getUnit().toString().contains(viewerInput)
                    || (sensor.getLocation() != null && sensor.getLocation().contains(viewerInput))
                    || (sensor.getDescription() != null && sensor.getDescription().contains(viewerInput))) {
                sensor.setUnitValue(sensor.getUnit().toString());
                sensorsFiltered.add(sensor);
            }
        }
        modelAndView.setViewName("sensors-table");
        pagination(request, response, page, sensorsFiltered);
        return modelAndView;
    }

    private void pagination(HttpServletRequest request,
                            HttpServletResponse response,
                            String page,
                            ArrayList<Sensor> sensorFiltered) {
        PagedListHolder<Sensor> sensorList;
        if (page == null) {
            sensorList = new PagedListHolder<>();
            sensorList.setSource(sensorFiltered);
            sensorList.setPageSize(4);
            request.getSession().setAttribute("sensorList", sensorList);
        } else if (page.equals("prev")) {
            sensorList = (PagedListHolder<Sensor>) request.getSession().getAttribute("sensorList");
            sensorList.previousPage();
        } else if (page.equals("next")) {
            sensorList = (PagedListHolder<Sensor>) request.getSession().getAttribute("sensorList");
            sensorList.nextPage();
        } else {
            int pageNum = Integer.parseInt(page);
            sensorList = (PagedListHolder<Sensor>) request.getSession().getAttribute("sensorList");
            sensorList.setPage(pageNum - 1);
        }
    }
}
