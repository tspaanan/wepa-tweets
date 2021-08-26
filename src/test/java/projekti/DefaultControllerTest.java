package projekti;

import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author tspaanan
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultControllerTest {
    
    public DefaultControllerTest() {
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
    }
    
    @After
    public void tearDown() {
        //this.wepaTweetterRepository.deleteAll();
        this.wepaTweetterRepository.delete(newTweetter);
        this.wepaTweetterRepository.delete(otherTweetter);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    private WepaTweetter newTweetter = new WepaTweetter();
    private WepaTweetter otherTweetter = new WepaTweetter();
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Test
    @WithMockUser
    //for some reason @WithMockUser doesn't work with SecurityConfiguration,
    //unless all the requests are allowed, making it useless
    public void userCanRegisterViaRegisterPost() throws Exception {
        this.mockMvc.perform(post("/register")
                    .param("username", "postUser")
                    .param("password", "password")
                    .param("realname", "realName")
                    .param("random", "string11"))
                    .andExpect(status().is3xxRedirection());
        WepaTweetter registered = this.wepaTweetterRepository.findByUsername("postUser");
        assertEquals("realName", registered.getRealname());
        assertEquals("string11", registered.getRandom());
        assertNotEquals("password", registered.getPassword());
        //password should be hashed
    }
    
    @Test
    public void userCanBeFoundViaSearch() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/search")
                    .param("searchterm", "testUser"))
                    .andReturn();
        //List results = Arrays.asList(result.getResponse().getContentAsString());
        //TODO: viimeistele tämä testimetodi
    }
    
    //@Test
    //@WithMockUser(username="testUser", password="password", roles="USER")
    //perhaps this annotation doesn't work as expected?
    //maybe https://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-mockmvc
    //public void userCanFollowOthers() throws Exception {
    //    this.mockMvc.perform(get("/follow")
    //                .param("follow", "otherUser")
    //                .with(user(userDetailsService.loadUserByUsername("testUser"))));
    //    List<WepaTweetter> followers = 
    //            this.wepaTweetterRepository.findByUsername("testUser").getFollowing();
    //    assertEquals("otherUser", followers.get(0).getUsername());
    //}
}
