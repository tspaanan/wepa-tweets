package projekti;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("production")
public class ProductionSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity sec) throws Exception {
        sec.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().permitAll().and()
                .logout().permitAll();
    }
}
