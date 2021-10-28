package projekti;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
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
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FunctionalTest { //extends org.fluentlenium.adapter.junit.FluentTest {
    
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
        //newTweetter.setUsername("testUser");
        //newTweetter.setPassword("password");
        //newTweetter.setRealname("realName");
        //newTweetter.setRandom("string");
        //this.wepaTweetterRepository.save(newTweetter);
        //otherTweetter.setUsername("otherUser");
        //this.wepaTweetterRepository.save(otherTweetter);
        //thirdTweetter.setUsername("thirdUser");
        //this.wepaTweetterRepository.save(thirdTweetter);
        //wepaMessage.setMessageContent("message");
        //wepaMessage.setTweetter(newTweetter);
        //wepaMessageRepository.save(wepaMessage);
        //wepaComment.setCommentContent("comment");
        //wepaComment.setMessage(wepaMessage);
        //wepaComment.setTweetter(newTweetter);
        //wepaCommentRepository.save(wepaComment);
        //wepaMessageRepository.save(wepaMessage);
        newUser.setName("testToFollow");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @After
    public void tearDown() {
        //this.wepaTweetterRepository.deleteAll();
    }
    
    //@LocalServerPort
    //private Integer port;
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private WepaMessageRepository wepaMessageRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;
    
    @Autowired
    private WepaCommentRepository wepaCommentRepository;
    
    @Autowired
    private PublicImageObjectRepository publicImageObjectRepository;
    
    @Autowired
    private DefaultController defaultController;
    
    @Autowired DefaultService defaultService;
    
    @Autowired
    private MockMvc mockMvc;
    
    //private WepaTweetter newTweetter = new WepaTweetter();
    //private WepaTweetter otherTweetter = new WepaTweetter();
    //private WepaTweetter thirdTweetter = new WepaTweetter();
    //private WepaMessage wepaMessage = new WepaMessage();
    //private WepaComment wepaComment = new WepaComment();
    private SecurityProperties.User newUser = new SecurityProperties.User();
    private Authentication auth = new UsernamePasswordAuthenticationToken(newUser,null);
    
    //@Test
    //public void indexHasMessages() throws Exception {
    //    mockMvc.perform(get("/")).andExpect(model().attributeExists("messages"));
    //}
    
    @Test
    public void newUserIsRedirectedUponRegister() throws Exception {
        mockMvc.perform(post("/register")
                .param("username","username9")
                .param("password", "password")
                .param("realname", "realname")
                .param("random", "random"))
                .andExpect(status().is3xxRedirection());
    }
    
    //@Test
    //public void newUserCanRegister() throws Exception {
    //    mockMvc.perform(post("/register")
    //            .param("username","username10")
    //            .param("password", "password")
    //            .param("realname", "realname")
    //            .param("random", "random"))
    //            .andReturn();
    //    assertEquals("realname", wepaTweetterRepository.findByUsername("username10").getRealname());
    //}
    
    //@Test
    //@WithMockUser
    //public void albumViewHasAllImages() throws Exception {
    //    mockMvc.perform(get("/wepa-tweetter/random/album"))
    //            .andExpect(status().is2xxSuccessful());
    //}
    
    //@Test
    //public void imageReturnedHasHttpHeaders() {
    //    ResponseEntity<byte[]> response = defaultController.returnImage(1L);
    //    assertTrue(response.getHeaders().containsKey(""));
    //}
    
    @Test
    public void searchReturnsNullUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/search")
                .param("searchterm", "fourthUser"))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("null"));
    }
    
    @Test
    public void searchReturnsUser() throws Exception {
        WepaTweetter searchUser = new WepaTweetter();
        searchUser.setUsername("searchUser");
        wepaTweetterRepository.save(searchUser);
        MvcResult result = mockMvc.perform(get("/search")
                .param("searchterm", "searchUser"))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("searchUser"));
    }
    
    //@Test
    //@WithMockUser(username="testToFollow")
    //public void userCanFollow() throws Exception {
    //    WepaTweetter testToFollow = new WepaTweetter();
    //    testToFollow.setUsername("testToFollow");
    //    wepaTweetterRepository.save(testToFollow);
    //    WepaTweetter testFollower = new WepaTweetter();
    //    testFollower.setUsername("testFollower");
    //    wepaTweetterRepository.save(testFollower);
    //    MvcResult result = mockMvc.perform(get("/follow")
    //            .param("follow", "testFollower"))
    //            .andReturn();
    //    assertTrue(result.getResponse().getContentAsString().contains("testToFollow"));
    //}
    
    //@Test
    //@WithMockUser
    //public void userCanSendMessageLeadesToRedirect() throws Exception {
    //    mockMvc.perform(post("/newmessage")
    //            .param("message","message"))
    //            .andExpect(status().is3xxRedirection());
    //}
    
    //@Test
    //public void userCanLeaveComment() throws Exception {
    //    WepaMessage message = new WepaMessage();
    //    message.setMessageContent("content");
    //    wepaMessageRepository.save(message);
    //    MvcResult result = mockMvc.perform(get("/newcomment")
    //            .param("newComment", "newComment")
    //            .param("commentMessageId", Long.toString(wepaMessageRepository.findAll().get(0).getId())))
    //            .andReturn();
    //    assertTrue(result.getResponse().getContentAsString().contains("newComment"));
    //}
    
    @Test
    public void userCanLeaveCommentToImage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("mockFile", new byte[1]);
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username13");
        wepaTweetterRepository.save(newTweetter);
        defaultService.addImage(newTweetter, mockFile, "description");
        MvcResult result = mockMvc.perform(get("/newimagecomment")
                .param("newComment", "newComment")
                .param("commentImageId", Long.toString(publicImageObjectRepository.findAll().get(0).getId())))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("newComment"));
    }
    
    @Test
    public void userCanSetProfileImage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("mockFile", new byte[1]);
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username12");
        wepaTweetterRepository.save(newTweetter);
        defaultService.addImage(newTweetter, mockFile, "description");
        WepaTweetter testUser2 = new WepaTweetter();
        testUser2.setUsername("testUser2");
        wepaTweetterRepository.save(testUser2);
        mockMvc.perform(post("/setprofile")
                .param("profileOwner","testUser2")
                .param("profileId", Long.toString(publicImageObjectRepository.findAll().get(0).getId())))
                .andExpect(status().is3xxRedirection());
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
