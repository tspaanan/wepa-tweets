package projekti;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        newUser.setName("testUser");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    @After
    public void tearDown() {
    }

    private MockMultipartFile mockFile = new MockMultipartFile("mockFile", new byte[1]);
    private User newUser = new User();
    private Authentication auth = new UsernamePasswordAuthenticationToken(newUser,null);
    
    @Autowired
    private PublicImageObjectRepository publicImageObjectRepository;
    
    @Autowired
    private WepaCommentRepository wepaCommentRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;
    
    @Autowired
    private WepaMessageRepository wepaMessageRepository;
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private DefaultService defaultService;
    
    @Test
    public void imageCanBeAdded() throws IOException {
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username1");
        wepaTweetterRepository.save(newTweetter);
        defaultService.addImage(newTweetter, mockFile, "description");
        PublicImageObject mockImage = publicImageObjectRepository.findByOwner(newTweetter).get(0);
        assertEquals("username1", mockImage.getOwner().getUsername());
        assertEquals("description", mockImage.getDescription());
        assertEquals(1, mockImage.getImageContent().length);
    }
    
    @Test
    public void imageCommentCanBeAdded() throws IOException {
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username2");
        wepaTweetterRepository.save(newTweetter);
        defaultService.addImage(newTweetter, mockFile, "description");
        WepaComment testComment = defaultService.addImageComment("comment", Long.toString(publicImageObjectRepository.findByOwner(newTweetter).get(0).getId()));
        assertEquals("comment", testComment.getCommentContent());
    }
    
    //@Test
    //public void imageCanBeLiked() throws IOException {
    //    defaultService.addImage(newTweetter, mockFile, "description");
    //    defaultService.addImageLike("1");
        //PublicImageObject mockImage = publicImageObjectRepository.findByOwner(newTweetter).get(0);
    //    PublicImageObject testObject = new PublicImageObject();
    //    testObject.setDescription("description");
    //    assertEquals(testObject, publicImageObjectRepository.findAll().get(0));
    //}
    
    @Test
    public void messageCanBeAdded() {
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username3");
        wepaTweetterRepository.save(newTweetter);
        defaultService.addMessage(newTweetter, "message");
        assertEquals("message", wepaMessageRepository.findAll().get(0).getMessageContent());
    }
    
    @Test
    public void messageCommentCanBeAdded() {
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username4");
        wepaTweetterRepository.save(newTweetter);
        defaultService.addMessage(newTweetter, "message");
        WepaComment testComment = defaultService.addMessageComment("comment", Long.toString(wepaMessageRepository.findAll().get(0).getId()));
        assertEquals("comment", testComment.getCommentContent());
    }
    
    @Test
    public void tweetterCanBeBlocked() {
        WepaTweetter newTweetter = new WepaTweetter();
        newTweetter.setUsername("username5");
        wepaTweetterRepository.save(newTweetter);
        WepaTweetter otherTweetter = new WepaTweetter();
        otherTweetter.setUsername("username6");
        wepaTweetterRepository.save(otherTweetter);
        WepaFollower testFollower = new WepaFollower();
        testFollower.setFollowed(newTweetter);
        testFollower.setFollowedBy(otherTweetter);
        newTweetter.getFollowing().add(testFollower);
        wepaTweetterRepository.save(newTweetter);
        defaultService.blockTweetter(newTweetter, otherTweetter);
        assertFalse(otherTweetter.getFollowingBy().contains(testFollower));
    }
    
    @Test
    public void tweetterCanBeRegistered() throws Exception {
        WepaTweetter registered = new WepaTweetter();
        registered.setUsername("registered");
        registered.setPassword("password");
        defaultService.registerTweetter(registered);
        WepaTweetter tested = wepaTweetterRepository.findByUsername("registered");
        assertNotEquals("password", tested.getPassword()); //password should be hashed
    }
    
    //@Test
    //public void followerCanBeRemoved() {
    //    WepaTweetter newTweetter = new WepaTweetter();
    //    newTweetter.setUsername("username7");
    //    wepaTweetterRepository.save(newTweetter);
    //    WepaTweetter otherTweetter = new WepaTweetter();
    //    otherTweetter.setUsername("username8");
    //    wepaTweetterRepository.save(otherTweetter);
    //    WepaFollower testFollower = new WepaFollower();
    //    testFollower.setFollowed(newTweetter);
    //    testFollower.setFollowedBy(otherTweetter);
    //    newTweetter.getFollowing().add(testFollower);
    //    wepaTweetterRepository.save(newTweetter);
    //    defaultService.removeFollowing(otherTweetter, newTweetter);
    //    assertFalse(newTweetter.getFollowing().contains(testFollower));
    //}
    
    @Test
    public void profileImageCanBeSet() throws IOException {
        WepaTweetter imageTweetter = new WepaTweetter();
        imageTweetter.setUsername("imageTweetter");
        wepaTweetterRepository.save(imageTweetter);
        defaultService.addImage(imageTweetter, mockFile, "description");
        WepaTweetter testTweetter = defaultService.setProfileImage("imageTweetter", Long.toString(publicImageObjectRepository.findByOwner(imageTweetter).get(0).getId()));
        assertEquals("description", testTweetter.getProfileImage().getDescription());
    }
}
