package projekti;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author tspaanan
 */
@Service
public class DefaultService {
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;
    
    @Autowired
    private PublicImageObjectRepository publicImageObjectRepository;
    
    @Autowired
    private WepaMessageRepository wepaMessageRepository;
    
    @Autowired
    private WepaCommentRepository wepaCommentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    public void addImage(WepaTweetter user, MultipartFile image, String description) throws IOException {
        if (this.publicImageObjectRepository.findByOwner(user).size() < 10) {
            PublicImageObject publicImageObject = new PublicImageObject();
            publicImageObject.setMediaType(image.getContentType());
            publicImageObject.setDescription(description);
            publicImageObject.setImageContent(image.getBytes());
            publicImageObject.setOwner(user);
            this.publicImageObjectRepository.save(publicImageObject);
        }
    }
    
    public WepaComment addImageComment(String comment, String commentImageId) {
        WepaTweetter user = this.getAuthorizedUser();
        PublicImageObject image = this.publicImageObjectRepository.getOne(Long.parseLong(commentImageId));
        if (image.getOwner().getBlocked().contains(user)) {
            return new WepaComment();
        }
        WepaComment newComment = new WepaComment(user, LocalDateTime.now(), comment, null, image);
        this.wepaCommentRepository.save(newComment);
        return newComment;
    }
    
    public boolean addImageLike(String likeId) {
        WepaTweetter user = this.getAuthorizedUser();
        PublicImageObject likedImage = this.publicImageObjectRepository.getOne(Long.parseLong(likeId));
        if (likedImage.getLikes().contains(user)) {
            return false;
        }
        likedImage.getLikes().add(user);
        this.publicImageObjectRepository.save(likedImage);
        return true;
    }
    
    public void addMessage(WepaTweetter user, String message) {
        this.wepaMessageRepository.save(new WepaMessage(user, LocalDateTime.now(), message, new ArrayList<>(), new ArrayList<>()));
    }
    
    public WepaComment addMessageComment(String comment, String commentMessageId) {
        WepaTweetter user = this.getAuthorizedUser();
        WepaMessage message = this.wepaMessageRepository.getOne(Long.parseLong(commentMessageId));
        if (message.getTweetter().getBlocked().contains(user)) {
            return new WepaComment();
        }
        WepaComment newComment = new WepaComment(user, LocalDateTime.now(), comment, message, null);
        this.wepaCommentRepository.save(newComment);
        return newComment;
    }
    
    public boolean addMessageLike(String likeId) {
        WepaTweetter user = this.getAuthorizedUser();
        WepaMessage likedMessage = this.wepaMessageRepository.getOne(Long.parseLong(likeId));
        if (likedMessage.getLikes().contains(user)) {
            return false;
        }
        likedMessage.getLikes().add(user);
        this.wepaMessageRepository.save(likedMessage);
        return true;
    }
    
    public void blockTweetter(WepaTweetter blocked, WepaTweetter user) {
        if (blocked.equals(user)) {
            return;
        }
        if (this.checkExistingFollowing(blocked, user)) {
            user.getBlocked().add(blocked);
            this.wepaTweetterRepository.save(user);
            this.removeFollowing(blocked, user);
        }
        if (this.checkExistingFollowing(user, blocked)) {
            this.removeFollowing(user, blocked);
        }
    }
    
    public boolean checkExistingFollowing(WepaTweetter candidate, WepaTweetter user) {
        return this.wepaFollowerRepository.findByFollowedAndFollowedBy(candidate, user) != null;
    }
    
    public WepaTweetter getAuthorizedUser() {
        return this.wepaTweetterRepository.findByUsername(SecurityContextHolder
                                                            .getContext()
                                                            .getAuthentication()
                                                            .getName());
    }
    
    public List<WepaComment> getCommentsByTweetter(WepaTweetter tweetter) {
        List<WepaMessage> messages = this.getMessagesByTweetter(tweetter);
        return this.wepaCommentRepository.findByMessageIn(messages, this.createPageable(10, "timestamp"));
    }
    
    public List<WepaComment> getCommentsFollowed(WepaTweetter tweetter) {
        List<WepaMessage> messages = this.getMessagesFollowed(tweetter);
        return this.wepaCommentRepository.findByMessageIn(messages, this.createPageable(10, "timestamp"));
    }
    
    public PublicImageObject getImageById(Long id) {
        return this.publicImageObjectRepository.getOne(id);
    }
    
    public List<WepaComment> getImageComments(List<PublicImageObject> images) {
        return this.wepaCommentRepository.findByImageIn(images, this.createPageable(10, "timestamp"));
    }
    
    public List<PublicImageObject> getImagesByOwner(WepaTweetter owner) {
        return this.publicImageObjectRepository.findByOwner(owner);
    }
    
    public List<WepaMessage> getMessagesByTweetter(WepaTweetter tweetter) {
        return this.wepaMessageRepository.findByTweetter(tweetter, this.createPageable(25, "timestamp"));
    }
    
    public List<WepaMessage> getMessagesFollowed(WepaTweetter tweetter) {
        List<WepaTweetter> followedTweetters = tweetter.getFollowingBy().stream()
                                                                        .map((follower) -> follower.getFollowed())
                                                                        .collect(Collectors.toCollection(ArrayList::new));
        return this.wepaMessageRepository.findByTweetterIn(followedTweetters, this.createPageable(25, "timestamp"));
    }
    
    public List<WepaComment> getRootComments() {
        return this.wepaCommentRepository.findByMessageIn(this.getRootMessages(), this.createPageable(10, "timestamp"));
    }
    
    public List<WepaMessage> getRootMessages() {
        return this.wepaMessageRepository.findAll(this.createPageable(25, "timestamp")).getContent();
    }
    
    public WepaTweetter getTweetterByRandom(String random) {
        return this.wepaTweetterRepository.findByRandom(random);
    }
    
    public WepaTweetter getTweetterByUsername(String username) {
        return this.wepaTweetterRepository.findByUsername(username);
    }
    
    public void registerTweetter(WepaTweetter newTweetter) throws Exception {
        newTweetter.setPassword(this.passwordEncoder.encode(newTweetter.getPassword()));
        this.wepaTweetterRepository.save(newTweetter);
    }
    
    public void removeFollowing(WepaTweetter followed, WepaTweetter user) {
        this.wepaFollowerRepository.delete(this.wepaFollowerRepository.findByFollowedAndFollowedBy(followed, user));
    }
    
    public WepaTweetter removeImage(String imageId) {
        this.publicImageObjectRepository.deleteById(Long.parseLong(imageId));
        return this.getAuthorizedUser();
    }
    
    public void saveNewWepaFollower(WepaTweetter followed, WepaTweetter user) {
        this.wepaFollowerRepository.save(new WepaFollower(followed, user, LocalDateTime.now()));
    }
    
    public WepaTweetter setProfileImage(String profileOwner, String profileId) {
        PublicImageObject newProfileImage = this.publicImageObjectRepository.findById(Long.parseLong(profileId)).get();
        WepaTweetter tweetter = this.getTweetterByUsername(profileOwner);
        tweetter.setProfileImage(newProfileImage);
        this.wepaTweetterRepository.save(tweetter);
        return tweetter;
    }
    
    private Pageable createPageable(int maxResult, String sortBy) {
        return PageRequest.of(0, maxResult, Sort.by(sortBy).descending());
    }
}
