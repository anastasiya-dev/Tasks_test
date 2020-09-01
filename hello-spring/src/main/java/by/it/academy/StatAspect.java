package by.it.academy;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class StatAspect {

    private static AtomicInteger counter = new AtomicInteger(0);

    @Before("execution(* by.it.academy.service.NotificationCommandExecutor.execute(..))")
    public void countExecution() {
        int updatedCount = counter.incrementAndGet();
        System.out.println("Call execution = " + updatedCount);
    }
}
