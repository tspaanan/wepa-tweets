package projekti;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class FunctionalTest {
    
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
        newUser.setName("testToFollow");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @After
    public void tearDown() {
    }
    
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
    
    private SecurityProperties.User newUser = new SecurityProperties.User();
    private Authentication auth = new UsernamePasswordAuthenticationToken(newUser,null);
    
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
}
