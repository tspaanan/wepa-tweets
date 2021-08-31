package projekti;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 *
 * @author tspaanan
 */
@Component
@Profile("development")
public class TestDataComponent implements ApplicationRunner {
    private WepaTweetterRepository wepaTweetterRepository;
    private WepaFollowerRepository wepaFollowerRepository;
    private PublicImageObjectRepository publicImageObjectRepository;
    private WepaMessageRepository wepaMessageRepository;
    
    @Autowired
    public TestDataComponent(WepaTweetterRepository wepaTweetterRepository,
            WepaFollowerRepository wepaFollowerRepository,
            PublicImageObjectRepository publicImageObjectRepository,
            WepaMessageRepository wepaMessageRepository) {
        this.wepaTweetterRepository = wepaTweetterRepository;
        this.wepaFollowerRepository = wepaFollowerRepository;
        this.publicImageObjectRepository = publicImageObjectRepository;
        this.wepaMessageRepository = wepaMessageRepository;
    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    //@Transactional //add later
    public void run(ApplicationArguments args) throws Exception {
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
        WepaFollower follower1 = new WepaFollower(this.wepaTweetterRepository.findByUsername("user2"),
                this.wepaTweetterRepository.findByUsername("user1"),LocalDateTime.now());
        this.wepaFollowerRepository.save(follower1);
        WepaFollower follower2 = new WepaFollower(this.wepaTweetterRepository.findByUsername("user1"),
                this.wepaTweetterRepository.findByUsername("user2"),LocalDateTime.now());
        this.wepaFollowerRepository.save(follower2);
        WepaFollower follower3 = new WepaFollower(this.wepaTweetterRepository.findByUsername("user3"),
                this.wepaTweetterRepository.findByUsername("user2"),LocalDateTime.now());
        this.wepaFollowerRepository.save(follower3);
        WepaFollower follower4 = new WepaFollower(this.wepaTweetterRepository.findByUsername("user4"),
                this.wepaTweetterRepository.findByUsername("user1"),LocalDateTime.now());
        this.wepaFollowerRepository.save(follower4);
        
        //add test image
        Path imagePath = Paths.get("../../mooc-wepa-21/osa04-Osa04_01.GifBin/src/main/resources/public/img/bananas.gif");
        byte[] imageBytes = Files.readAllBytes(imagePath);
        PublicImageObject publicImageObject = new PublicImageObject();
        publicImageObject.setMediaType("image/gif");
        publicImageObject.setDescription("banana");
        publicImageObject.setImageContent(imageBytes);
        WepaTweetter user1 = this.wepaTweetterRepository.findByUsername("user1");
        publicImageObject.setOwner(user1);
        this.publicImageObjectRepository.save(publicImageObject);
        
        //add test messages
        WepaMessage message1 = new WepaMessage(user1,LocalDateTime.now(),"testiviesti #1");
        this.wepaMessageRepository.save(message1);
        WepaMessage message2 = new WepaMessage(user1,LocalDateTime.now(),"testiviesti #2");
        this.wepaMessageRepository.save(message2);
        WepaMessage message3 = new WepaMessage(user1,LocalDateTime.now(),"testiviesti #3");
        this.wepaMessageRepository.save(message3);
        WepaMessage message4 = new WepaMessage(user1,LocalDateTime.now(),"testiviesti #4");
        this.wepaMessageRepository.save(message4);
    }

}
