package by.it.academy;

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

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebSecurity
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter implements CommandLineRunner {

    @Autowired
    Genesis genesis;

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

        http.authorizeRequests().antMatchers("/register").permitAll();
        super.configure(http);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class);
    }

    int difficulty = 3;

    @Override
    public void run(String... args) throws Exception {
        this.genesis.genesis(difficulty);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
