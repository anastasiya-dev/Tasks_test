package myproject.extending;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExtendedTaskIntroducer {
    @DeclareParents(value = "myproject.extending.DoTask+",
            defaultImpl = ExtendedTaskImpl.class)
    public static DoExtendedTask extendedTask;
}
