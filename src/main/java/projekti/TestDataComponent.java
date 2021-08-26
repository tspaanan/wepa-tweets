package projekti;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author tspaanan
 */
@Component
@Profile("developmentNOT")
public class TestDataComponent implements ApplicationRunner {
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    public TestDataComponent(WepaTweetterRepository wepaTweetterRepository) {
        this.wepaTweetterRepository = wepaTweetterRepository;
    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    //@Transactional //add later
    public void run(ApplicationArguments args) {
        //this.wepaTweetterRepository.deleteAll();
        //deleteAll() ei toimi, koska CASCADE-optiot ei aktivoidu
        System.out.println(System.getProperty("user.dir"));
        //File oldDb = new File("database.mv.db");
        //oldDb.delete();
        for (int i = 1; i < 10; i++) {
            WepaTweetter newTweetter = new WepaTweetter();
            newTweetter.setUsername("user" + i);
            newTweetter.setPassword("pass" + i);
            newTweetter.setRealname("name" + i);
            newTweetter.setRandom("string" + i);
            newTweetter.setPassword(this.passwordEncoder.encode(newTweetter.getPassword()));
            this.wepaTweetterRepository.save(newTweetter);
        }
    }

}
