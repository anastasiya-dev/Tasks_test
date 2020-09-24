package by.it.academy;

import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebSecurity
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter implements CommandLineRunner {

    @Autowired
    Genesis genesis;
    @Autowired
    BlockGenerator blockGenerator;
    @Autowired
    Consistency consistency;
    @Autowired
    BalanceTest balanceTest;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
                .antMatchers("/**").hasRole("USER")
                .antMatchers("/swagger-ui").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .and()
                .formLogin().disable()
                .csrf().disable()
        ;

        http.authorizeRequests().antMatchers("/register").permitAll();
        super.configure(http);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class);
    }

    int difficulty = 3;
    float threshold = 100_000f;

    @Override
    public void run(String... args) throws Exception {
        Logger logger = LoggerUtil.startLogging(ApplicationConfiguration.class.getName());
        this.genesis.genesis(difficulty);
        while (balanceTest.balance() < threshold) {
            this.blockGenerator.generateBlockchain(difficulty);
            this.consistency.isChainValid(difficulty);
            logger.info("Starting 2 minute sleep in main");
            Thread.sleep(1000 * 60 * 2);
            logger.info("Ending 2 minute sleep in main");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
