package projekti;

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
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author tspaanan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {
    
    public IntegrationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        //WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("testUser");
        newTweetter.setPassword("password");
        newTweetter.setRealname("realName");
        newTweetter.setRandom("string");
        this.wepaTweetterRepository.save(newTweetter);
        otherTweetter.setUsername("otherUser");
        this.wepaTweetterRepository.save(otherTweetter);
    }
    
    @After
    public void tearDown() {
        this.wepaTweetterRepository.deleteAll();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    private WepaTweetter newTweetter = new WepaTweetter();
    private WepaTweetter otherTweetter = new WepaTweetter();
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private DefaultController defaultController;
    
    @Test
    public void userCanRegister() {
        this.wepaTweetterRepository.deleteAll();
        defaultController.registerPost(newTweetter);
        WepaTweetter registered = this.wepaTweetterRepository.findByUsername("testUser");
        assertEquals("realName", registered.getRealname());
        assertEquals("string", registered.getRandom());
        assertNotEquals("password", registered.getPassword()); //asserting NotEqual, bc/
        //password should have been hashed via BCrypt
    }
    
    //@Test
    //public void userCanBeSearchedFor() {
    //    List<WepaTweetter> searchedForList = defaultController.search(newTweetter.getUsername());
    //    WepaTweetter searchedFor = searchedForList.get(0);
    //    assertEquals("testUser", searchedFor.getUsername());
    //    assertEquals("realName", searchedFor.getRealname());
    //    assertEquals("string", searchedFor.getRandom());
    //}
    //selvitä tämä LazyInitializationException
    //https://thorben-janssen.com/lazyinitializationexception/
    
    @Test
    public void userCanFollowOtherUsers() {
        //without SecurityContext present this test cannot be written at this level
    }
}
