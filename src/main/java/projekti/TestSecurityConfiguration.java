package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("test")
public class TestSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity sec) throws Exception {
        //sec.authorizeRequests()
        //        .antMatchers(HttpMethod.GET,"/").permitAll()
        //        .antMatchers("/register").permitAll()
        //        //.anyRequest().authenticated()
        //        .anyRequest().permitAll()
        //        .and()
        //        .formLogin().permitAll().and()
        //        .logout().permitAll();
        //nothing above permits any test to work, only the free-for-all below works
        sec.ignoring().antMatchers("/**");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
