package myproject.starting;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class CustomAspect {
    private static Logger logger = Logger.getLogger(CustomAspect.class.getName());

    @Pointcut("execution(* FirstBean.testAutowired(..))")
    public void performance() {
    }

    @Before("performance()")
    public void beforeCustomAspect() {
        System.out.println("Execution before CustomAspect");
    }

    @AfterReturning("performance()")
    public void afterCustomAspect() {
        System.out.println("Execution after CustomAspect");
    }
}
