package projekti;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author tspaanan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FunctionalTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    public FunctionalTest() {
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
        otherTweetter.setUsername("otherUser");
        this.wepaTweetterRepository.save(otherTweetter);
        thirdTweetter.setUsername("thirdUser");
        this.wepaTweetterRepository.save(thirdTweetter);
    }
    
    @After
    public void tearDown() {
        this.wepaTweetterRepository.delete(newTweetter);
        this.wepaTweetterRepository.delete(otherTweetter);
    }
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;
    
    @Autowired
    private DefaultController defaultController;
    
    private WepaTweetter newTweetter = new WepaTweetter();
    private WepaTweetter otherTweetter = new WepaTweetter();
    private WepaTweetter thirdTweetter = new WepaTweetter();
    
    @Test
    public void noTestWithoutAuthentication() {
        
    }
    
    //this test needs authentication, MockMvc or something
    //@Test
    //public void allFollowersAreDisplayed() {
    //    WepaFollower follower1 = new WepaFollower(this.wepaTweetterRepository.findByUsername("otherUser"),
    //            this.wepaTweetterRepository.findByUsername("testUser"),LocalDateTime.now());
    //    this.wepaFollowerRepository.save(follower1);
    //    WepaFollower follower2 = new WepaFollower(this.wepaTweetterRepository.findByUsername("thirdUser"),
    //            this.wepaTweetterRepository.findByUsername("testUser"),LocalDateTime.now());
    //    this.wepaFollowerRepository.save(follower2);
    //    goTo("http://localhost:" + port + "/wepa-tweetter/string");
    //    this.wepaFollowerRepository.delete(follower1);
    //    this.wepaFollowerRepository.delete(follower2);
    //    assertTrue(pageSource().contains("otherUser"));
    //    assertTrue(pageSource().contains("thirdUser"));        
    //}
        
    //this test needs either
    //1) MockMvc authentication to work, or
    //2) override of Spring's own login form with custom "/login" method
    //@Test
    //public void userCanFollowOtherUser() throws Exception {
    //    this.wepaTweetterRepository.deleteAll();
    //    WepaTweetter user1 = new WepaTweetter();
    //    user1.setUsername("user1");
    //    user1.setRandom("string1");
    //    //this.wepaTweetterRepository.save(user1);
    //    user1.setPassword("pass1");
    //    defaultController.registerPost(user1);
    //    //registering user1 bc/ BCrypt hashing
    //    WepaTweetter user2 = new WepaTweetter();
    //    user2.setUsername("user2");
    //    user2.setRandom("string2");
    //    this.wepaTweetterRepository.save(user2);
    //    //goTo("http://localhost:" + port + "/login");
    //    goTo("http://localhost:" + port);
    //    //find("#username").fill().with("user1");
    //    System.out.println(pageSource());
    //    find("input").first().fill().with("user1");
    //    find("#password").fill().with("pass1");
    //    find("button").first().submit();
    //    goTo("http://localhost:" + port);
    //    find("#searchterm").fill().with("user2");
    //    find("#submitSearch").click();
    //    //javascript creates element w/ id="user2", but I can't get
    //    //fluentlenium to wait for its creation
    //    //this loop goes around a few hundred times before javascript is loaded
    //    int i = 0;
    //    while (true) {
    //        try {
    //            find("#user2").click();
    //            break;
    //        } catch (Exception e) {
    //            i++;
    //            continue;
    //        }
    //    }
    //    System.out.println("loop ran this many times: " + i);
    //    goTo("http://localhost:" + port + "/wepa-tweetter/string1");
    //    System.out.println(pageSource());
    //    assertTrue(pageSource().contains("user2"));
    //}
    
}
