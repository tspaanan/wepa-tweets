package projekti;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author tspaanan
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WepaTweetter tweetter = this.wepaTweetterRepository.findByUsername(username);
        if (tweetter == null) {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return new User(tweetter.getUsername(), tweetter.getPassword(),
                        Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
