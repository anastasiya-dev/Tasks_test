package by.it.academy;

import by.it.academy.pojo.MiningSession;
import by.it.academy.service.MiningSessionService;
import by.it.academy.support.MiningSessionStatus;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebSecurity
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter implements CommandLineRunner {

    @Autowired
    Genesis genesis;
    @Autowired
    BalanceTest balanceTest;
    @Autowired
    MiningSessionService miningSessionService;
    //    @Autowired
//    ApplicationContext applicationContext;
    @Autowired
    MiningHead miningHead;

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(ApplicationConfiguration.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("Genesis1")
                .password(new BCryptPasswordEncoder().encode("111"))
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
//                .antMatchers("/**").hasRole("USER")
//                .antMatchers("/swagger-ui").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .and()
//                .formLogin().disable()
//                .csrf().disable()
                .antMatchers("/*").permitAll()
//.antMatchers(HttpMethod.POST).permitAll()
                .and().csrf().disable()
        ;

        super.configure(http);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class);
    }

    public static int difficulty = 5;
    static float threshold = 1_000_000;

    @Override
    public void run(String... args) throws Exception {
        this.genesis.genesis(difficulty);

        while (balanceTest.balance() < threshold) {
            logger.info("Checking mining session pool");
            try {
                ArrayList<MiningSession> allMiningSessionsByStatus
                        = miningSessionService.findAllMiningSessionsByStatus(MiningSessionStatus.IN_PROCESS);
                miningHead.miningRunner(allMiningSessionsByStatus);
            } catch (NullPointerException e) {
                logger.info("Mining session pool empty");
            }
            logger.info("Letting mining session pool to be filled");
            Thread.sleep(1000 * 2 * 60);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
