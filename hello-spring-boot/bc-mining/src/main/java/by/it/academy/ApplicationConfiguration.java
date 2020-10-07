package by.it.academy;

import by.it.academy.management.TransactionManagement;
import by.it.academy.multithreading.MiningLauncher;
import by.it.academy.pojo.MiningSession;
import by.it.academy.service.BlockTemporaryService;
import by.it.academy.service.MiningSessionService;
import by.it.academy.service.TransactionService;
import by.it.academy.support.MiningSessionStatus;
import by.it.academy.support.TransactionStatus;
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
import java.time.format.DateTimeFormatter;
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
    @Autowired
    MiningLauncher miningLauncher;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionManagement transactionManagement;

    public static int DIFFICULTY = 5;
    public static float THRESHOLD = 1_000_000;
    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
    public static int MAX_QUANTITY = 3;
    public static float MINER_REWARD = 0.05f;
    public static float SENDER_REWARD = 0.01f;
    public static int CHECK_FLAG = 100_000;

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
                .and().csrf().disable()
        ;

        super.configure(http);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class);
    }

    @Override
    public void run(String... args) throws Exception {
        this.genesis.genesis(DIFFICULTY);

        while (balanceTest.balance() < THRESHOLD) {
            logger.info("Checking mining session pool");
            try {
                ArrayList<MiningSession> allMiningSessionsByStatus
                        = miningSessionService.findAllMiningSessionsByStatus(MiningSessionStatus.IN_PROCESS);
                if (!transactionService.findAllTransactionsByStatus(TransactionStatus.CONFIRMED).isEmpty()) {
                    logger.info("Starting mining multithreading");
                    for (MiningSession miningSession : allMiningSessionsByStatus) {
                        transactionManagement.formTransactionsSetForSession(miningSession);
                    }
                    miningLauncher.launch(allMiningSessionsByStatus);
                }
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
