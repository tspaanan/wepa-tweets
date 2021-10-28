package projekti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DefaultController {
    
    @Autowired
    private DefaultService defaultService;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("messages", this.defaultService.getRootMessages());
        model.addAttribute("comments", this.defaultService.getRootComments());
        model.addAttribute("tweetterSelf", this.defaultService.getAuthorizedUser());
        return "index";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("tweetterSelf", this.defaultService.getAuthorizedUser());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerPost(@ModelAttribute WepaTweetter newTweetter) {
        try {
            this.defaultService.registerTweetter(newTweetter);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/login";
    }
    
    @GetMapping("/wepa-tweetter/{random}")
    public String tweetter(Model model, @PathVariable String random) {
        WepaTweetter tweetter = this.defaultService.getTweetterByRandom(random);
        model.addAttribute("tweetter", tweetter);
        model.addAttribute("messages", this.defaultService.getMessagesByTweetter(tweetter));
        model.addAttribute("tweettersFollowed", tweetter.getFollowingBy());
        model.addAttribute("followedMessages", this.defaultService.getMessagesFollowed(tweetter));
        model.addAttribute("tweettersFollowedBy", tweetter.getFollowing());
        model.addAttribute("comments", this.defaultService.getCommentsByTweetter(tweetter));
        model.addAttribute("followedComments", this.defaultService.getCommentsFollowed(tweetter));
        model.addAttribute("tweetterSelf", this.defaultService.getAuthorizedUser());
        return "tweetter";
    }
    
    @GetMapping("/wepa-tweetter/{random}/album")
    public String album(Model model, @PathVariable String random) {
        WepaTweetter owner = this.defaultService.getTweetterByRandom(random);
        model.addAttribute("owner", owner);
        List<PublicImageObject> images = this.defaultService.getImagesByOwner(owner);
        model.addAttribute("count", images.size());
        model.addAttribute("images", images);
        model.addAttribute("comments", this.defaultService.getImageComments(images));
        model.addAttribute("tweetterSelf", this.defaultService.getAuthorizedUser());
        return "album";
    }
    
    @ResponseBody
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> returnImage(@PathVariable Long id) {
        PublicImageObject publicImageObject = this.defaultService.getImageById(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(publicImageObject.getMediaType()));
        ResponseEntity<byte[]> returnedImage = new ResponseEntity<>(publicImageObject.getImageContent(),
                                                headers, HttpStatus.CREATED);
        return returnedImage;
    }
    
    @ResponseBody
    @GetMapping("/search")
    public List<WepaTweetter> search(@RequestParam String searchterm) {
        //designed to return a list of results
        List<WepaTweetter> listOfTweetters = new ArrayList<>();
        //implemented to return a single result (for now)
        WepaTweetter foundTweetter = this.defaultService.getTweetterByUsername(searchterm);
        if (foundTweetter != null) {
            listOfTweetters.add(foundTweetter);
        } else {
            listOfTweetters.add(new WepaTweetter());
        }
        return listOfTweetters;
    }
    
    @PostAuthorize("returnObject.username == authentication.principal.username")
    @ResponseBody
    @GetMapping("/follow")
    public WepaTweetter follow(@RequestParam String follow) {
        WepaTweetter followed = this.defaultService.getTweetterByUsername(follow);
        WepaTweetter user = this.defaultService.getAuthorizedUser();
        if (user == null) {
            return user;
        }
        
        //prevent user from following themselves
        if (user.equals(followed)) {
            return new WepaTweetter();
        }
        
        //prevent user from following someone they are already following
        if (this.defaultService.checkExistingFollowing(followed, user)) {
            return new WepaTweetter();
        }
        
        //prevent user from following someone they have blocked or been blocked by
        if (followed.getBlocked().contains(user) || user.getBlocked().contains(followed)) {
            return new WepaTweetter();
        }
       
        this.defaultService.saveNewWepaFollower(followed, user);
        return user;
    }
    
    @PreAuthorize("#returnToUnfollow == authentication.principal.random")
    @PostMapping("/unfollow")
    public String unfollow(@RequestParam String returnToUnfollow, @RequestParam String unfollow) {
        WepaTweetter followed = this.defaultService.getTweetterByUsername(unfollow);
        WepaTweetter user = this.defaultService.getAuthorizedUser();
        if (this.defaultService.checkExistingFollowing(followed, user)) {
            this.defaultService.removeFollowing(followed, user);
        }
        return "redirect:/wepa-tweetter/" + returnToUnfollow;
    }
    
    @PreAuthorize("#returnToBlock == authentication.principal.random")
    @PostMapping("/block")
    public String block(@RequestParam String returnToBlock, @RequestParam String block) {
        WepaTweetter blocked = this.defaultService.getTweetterByUsername(block);
        WepaTweetter user = this.defaultService.getAuthorizedUser();
        this.defaultService.blockTweetter(blocked, user);
        return "redirect:/wepa-tweetter/" + returnToBlock;
    }
    
    @Secured("USER")
    @PostMapping("/addimage")
    public String addImage(@RequestParam("image") MultipartFile image, @RequestParam("description") String description) throws IOException {
        WepaTweetter user = this.defaultService.getAuthorizedUser();
        this.defaultService.addImage(user, image, description);
        return "redirect:/wepa-tweetter/" + user.getRandom() + "/album";
    }
    
    @Secured("USER")
    @PostMapping("/newmessage")
    public String newMessage(@RequestParam String message) {
        WepaTweetter user = this.defaultService.getAuthorizedUser();
        this.defaultService.addMessage(user, message);
        return "redirect:/wepa-tweetter/" + user.getRandom();
    }
    
    @Secured("USER")
    @ResponseBody
    @GetMapping("/newcomment")
    public WepaComment newComment(@RequestParam String newComment, @RequestParam String commentMessageId) {
        return this.defaultService.addMessageComment(newComment, commentMessageId);
    }
    
    @Secured("USER")
    @ResponseBody
    @GetMapping("/newimagecomment")
    public WepaComment newImageComment(@RequestParam String newComment, @RequestParam String commentImageId) {
        return this.defaultService.addImageComment(newComment, commentImageId);
    }
    
    @Secured("USER")
    @ResponseBody
    @GetMapping("/likeimage")
    public String likeImage(@RequestParam String like) {
        if (!this.defaultService.addImageLike(like)) {
            return null;
        }
        return "ok";
    }
    
    @Secured("USER")
    @ResponseBody
    @GetMapping("/likemessage")
    public String likeMessage(@RequestParam String like) {
        if (!this.defaultService.addMessageLike(like)) {
            return null;
        }
        return "ok";
    }
    
    @PreAuthorize("#removeOwner == authentication.principal.username")
    @PostMapping("/removeimage")
    public String removeImage(@RequestParam String removeOwner, @RequestParam String imageId) {
        WepaTweetter owner = this.defaultService.removeImage(imageId);
        return "redirect:/wepa-tweetter/" + owner.getRandom() + "/album";
    }
    
    @PreAuthorize("#profileOwner == authentication.principal.username")
    @PostMapping("/setprofile")
    public String setProfileImage(@RequestParam String profileOwner, @RequestParam String profileId) {
        return "redirect:/wepa-tweetter/" + this.defaultService.setProfileImage(profileOwner, profileId).getRandom();
    }
    
}
