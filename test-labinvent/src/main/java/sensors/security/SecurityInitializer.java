package sensors.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    protected SecurityInitializer() {
        super(SecurityConfiguration.class);
    }
}
