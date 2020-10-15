package sensors.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class AuthenticationController {

    @RequestMapping(value = "/unauthorized")
    public ModelAndView unauthorizedResponse(ModelAndView modelAndView) {
        log.info("Authentication failed");
        modelAndView.setViewName("unauthorized");
        return modelAndView;
    }

    @RequestMapping(value = "/")
    public String startPage(ModelAndView modelAndView) {
        return "redirect: /test-labinvent/incorrect-input";
    }
}
