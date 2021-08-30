package projekti;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.tools.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;
    
    @Autowired
    private PublicImageObjectRepository publicImageObjectRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    //@GetMapping("*")
    //public String helloWorld(Model model) {
    //    model.addAttribute("message", "World!");
    //    return "index";
    //}
    
    @GetMapping("/")
    public String index(Model model) {
        //kansoita etusivulla viimeisimmillä viesteillä
        return "index";
    }
    
    //@GetMapping("/login")
    //public String login() {
    //    return "login";
    //}
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerPost(@ModelAttribute WepaTweetter newTweetter) {
        newTweetter.setPassword(this.passwordEncoder.encode(newTweetter.getPassword()));
        this.wepaTweetterRepository.save(newTweetter);
        return "redirect:/";
    }
    
    @GetMapping("/wepa-tweetter/{random}")
    public String tweetter(Model model, @PathVariable String random) {
        WepaTweetter tweetter = this.wepaTweetterRepository.findByRandom(random);
        model.addAttribute("tweetter", tweetter);
        List<WepaFollower> tweettersFollowed = tweetter.getFollowingBy();
        model.addAttribute("tweettersFollowed", tweettersFollowed);
        //List<WepaFollower> tweettersFollowedBy = tweetter.getFollowingBy();
        //List<WepaFollower> tweettersFollowedBy = this.wepaFollowerRepository.findByFollowed(tweetter);
        //model.addAttribute("tweettersFollowedBy", tweettersFollowedBy);
        List<WepaFollower> tweettersFollowedBy = tweetter.getFollowing();
        model.addAttribute("tweettersFollowedBy", tweettersFollowedBy);
        return "tweetter";
    }
    
    @GetMapping("/wepa-tweetter/{random}/album")
    public String album(Model model, @PathVariable String random) {
        WepaTweetter owner = this.wepaTweetterRepository.findByRandom(random);
        model.addAttribute("owner", owner);
        List<PublicImageObject> images = this.publicImageObjectRepository.findByOwner(owner);
        model.addAttribute("count", images.size());
        model.addAttribute("images", images);
        return "album";
    }
    
    //@GetMapping(path = "/images/{id}", produces = "image/gif")
    @ResponseBody
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> returnImage(@PathVariable Long id) {
        PublicImageObject publicImageObject = this.publicImageObjectRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(publicImageObject.getMediaType()));
        ResponseEntity<byte[]> returnedImage = new ResponseEntity<>(publicImageObject.getImageContent(),
                                                headers, HttpStatus.CREATED);
        return returnedImage;
    }
    
    //@PostMapping("/search")
    //public String search(@RequestParam String searchterm) {
        //haetaan vain username-kentän perusteella
    //    WepaTweetter found = this.wepaTweetterRepository.findByUsername(searchterm);
    //    return "redirect:/";
    //}
    
    @ResponseBody
    @GetMapping("/search")
    public List<WepaTweetter> search(@RequestParam String searchterm) {
        //this.wepaTweetterRepository.findByUsername("admin").setFollowing(new ArrayList<>());
        //liitostaulu sotkee JSON-datan välityksen javascriptille, yllä tyhjennetään se
        List<WepaTweetter> listOfTweetters = new ArrayList<>();
        WepaTweetter foundTweetter = this.wepaTweetterRepository.findByUsername(searchterm);
        if (foundTweetter != null) {
            //foundTweetter.setFollowing(new ArrayList<WepaFollower>());
            //foundTweetter.setFollowingBy(new ArrayList<WepaFollower>());
            listOfTweetters.add(foundTweetter);
        } else {
            listOfTweetters.add(new WepaTweetter());
        }
        System.out.println("koko on: " + listOfTweetters.size());
        //System.out.println(listOfTweetters.get(0));
        return listOfTweetters;
    }
    
    @PostAuthorize("returnObject.username == authentication.principal.username")
    //tämän toimivuus on 50-50, täytyy vielä testata
    @ResponseBody
    @GetMapping("/follow")
    public WepaTweetter follow(@RequestParam String follow) {
        //WepaTweetter testi = new WepaTweetter();
        //testi.setUsername("testiNull");
        //return testi;
        WepaTweetter followed = this.wepaTweetterRepository.findByUsername(follow);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("sec context returns: " + username);
        //UserDetails user = this.userDetailsService.loadUserByUsername(username);
        //kokonaista Useria ei ehkä tarvitakaan?
        WepaTweetter user = this.wepaTweetterRepository.findByUsername(username);
        if (user.equals(followed)) {
            System.out.println("same hit");
            return new WepaTweetter();
        }
        //System.out.println("userin listan pituus on: " + user.getFollowing().size());
        //for (WepaFollower w : user.getFollowing()) {
        //    System.out.println("verratta: " + w.getFollowed().getUsername());
        //    System.out.println("kohde: " + followed.getUsername());
        //    if (w.getFollowed().equals(followed)) {
        //        System.out.println("existing hit");
        //        return new WepaTweetter();
        //    }
        //}
        //täytyy korvata yläpuolen tarkistus seuraavalla:
        if (this.wepaFollowerRepository.findByFollowedAndFollowedBy(followed, user) != null) {
            return new WepaTweetter();
        }
        
        //check blocklist
        if (followed.getBlocked().contains(user) || user.getBlocked().contains(followed)) {
            return new WepaTweetter();
        }
        
        LocalDateTime now = LocalDateTime.now();
        WepaFollower newFollower = new WepaFollower(followed, user, now);
        this.wepaFollowerRepository.save(newFollower);
        //ilmeisesti tämä tallennus riittää?
        
        //WepaFollower newFollowedBy = new WepaFollower(user, now);
        //this.wepaFollowerRepository.save(newFollowedBy);
        //record WepaFollower both to the one who follows and the one being followed
        //followed.getFollowingBy().add(newFollowedBy);
        //this.wepaTweetterRepository.save(followed);
        //followed.getFollowingBy().add(newFollower);
        //this.wepaTweetterRepository.save(followed);
        //user.getFollowing().add(newFollower);
        System.out.println("tällä listan pituus on: " + user.getFollowing().size());
        //this.wepaTweetterRepository.save(user);
        System.out.println("tällä listan pituus on: " + user.getFollowing().size());
        //user.setFollowing(new ArrayList<WepaFollower>());
        //user.setFollowingBy(new ArrayList<WepaFollower>());
        return user;
    }
    
    @PreAuthorize("#returnToUnfollow == authentication.principal.random")
    @PostMapping("/unfollow")
    public String unfollow(@RequestParam String returnToUnfollow, @RequestParam String unfollow) {
        WepaTweetter followed = this.wepaTweetterRepository.findByUsername(unfollow);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WepaTweetter user = this.wepaTweetterRepository.findByUsername(username);
        WepaFollower unfollowed = this.wepaFollowerRepository.findByFollowedAndFollowedBy(followed, user);
        this.wepaFollowerRepository.delete(unfollowed);
        return "redirect:/wepa-tweetter/" + returnToUnfollow;
    }
    
    @PreAuthorize("#returnToBlock == authentication.principal.random")
    @PostMapping("/block")
    public String block(@RequestParam String returnToBlock, @RequestParam String block) {
        //ajatus: lisätään blokkilistaan, sitten kutsutaan unfollow()
        //joka poistaa seuraamisen (ja blokkilista estää uudestaan lisäämisen)
        //ei toimi, täytyy muutenkin eriyttää erillinen @Service-palvelu
        WepaTweetter blocked = this.wepaTweetterRepository.findByUsername(block);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WepaTweetter user = this.wepaTweetterRepository.findByUsername(username);
        user.getBlocked().add(blocked);
        this.wepaTweetterRepository.save(user);
        WepaFollower unfollowed = this.wepaFollowerRepository.findByFollowedAndFollowedBy(blocked, user);
        WepaFollower unfollowedMirror = this.wepaFollowerRepository.findByFollowedAndFollowedBy(user, blocked);
        if (unfollowed != null) {
            this.wepaFollowerRepository.delete(unfollowed);
        }
        if (unfollowedMirror != null) {
            this.wepaFollowerRepository.delete(unfollowedMirror);
        }
        return "redirect:/wepa-tweetter/" + returnToBlock;
    }
    
    @Secured("USER")
    @PostMapping("/addimage")
    public String addImage(@RequestParam("image") MultipartFile image, @RequestParam("description") String description) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WepaTweetter user = this.wepaTweetterRepository.findByUsername(username);
        if (this.publicImageObjectRepository.findByOwner(user).size() < 10) {
            PublicImageObject publicImageObject = new PublicImageObject();
            publicImageObject.setMediaType(image.getContentType());
            publicImageObject.setDescription(description);
            publicImageObject.setImageContent(image.getBytes());
            publicImageObject.setOwner(user);
        this.publicImageObjectRepository.save(publicImageObject);
        }
        return "redirect:/wepa-tweetter/" + user.getRandom() + "/album";
    }
    
    @PreAuthorize("#removeOwner == authentication.principal.username")
    @PostMapping("/removeimage")
    public String removeImage(@RequestParam String removeOwner, @RequestParam String imageId) {
        this.publicImageObjectRepository.deleteById(Long.parseLong(imageId));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WepaTweetter user = this.wepaTweetterRepository.findByUsername(username);
        return "redirect:/wepa-tweetter/" + user.getRandom() + "/album";
    }
    
    @PreAuthorize("#profileOwner == authentication.principal.username")
    @PostMapping("setprofile")
    public String setProfileImage(@RequestParam String profileOwner, @RequestParam String profileId) {
        PublicImageObject newProfileImage = this.publicImageObjectRepository.findById(Long.parseLong(profileId)).get();
        WepaTweetter tweetter = this.wepaTweetterRepository.findByUsername(profileOwner);
        tweetter.setProfileImage(newProfileImage);
        this.wepaTweetterRepository.save(tweetter);
        return "redirect:/wepa-tweetter/" + tweetter.getRandom();
    }
    
}
