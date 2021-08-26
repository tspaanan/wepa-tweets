package projekti;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author tspaanan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WepaFollowerTest {
    
    public WepaFollowerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        newTweetter.setUsername("testUser");
        newTweetter.setPassword("password");
        newTweetter.setRealname("realName");
        newTweetter.setRandom("string");
        this.wepaTweetterRepository.save(newTweetter);
    }
    
    @After
    public void tearDown() {
        //this.wepaTweetterRepository.deleteAll();
        //monimutkaisemmissa tietokannoissa taulut täytyy purkaa
        //oikeassa järjestyksessä, muuten foreign key constraint estää purun
        this.wepaTweetterRepository.delete(newTweetter);
    }

    private WepaTweetter newTweetter = new WepaTweetter();

    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;

    @Test
    public void wepaFollowerEncapsulatesWepaTweetter() {
        this.wepaFollowerRepository.save(new WepaFollower(
                this.newTweetter,new WepaTweetter(),LocalDateTime.now()));
        List<WepaFollower> testFollower = this.wepaFollowerRepository.findByFollowed(this.newTweetter);
        this.wepaFollowerRepository.delete(testFollower.get(0));
        assertEquals("testUser", testFollower.get(0).getFollowed().getUsername());
    }
}
