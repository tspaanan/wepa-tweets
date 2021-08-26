package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class DefaultController {
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private WepaFollowerRepository wepaFollowerRepository;
    
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
        List<WepaFollower> tweettersFollowed = tweetter.getFollowing();
        model.addAttribute("tweettersFollowed", tweettersFollowed);
        //List<WepaFollower> tweettersFollowedBy = tweetter.getFollowingBy();
        List<WepaFollower> tweettersFollowedBy = this.wepaFollowerRepository.findByFollowed(tweetter);
        model.addAttribute("tweettersFollowedBy", tweettersFollowedBy);
        return "tweetter";
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
        LocalDateTime now = LocalDateTime.now();
        WepaFollower newFollower = new WepaFollower(followed, user, now);
        this.wepaFollowerRepository.save(newFollower);
        //WepaFollower newFollowedBy = new WepaFollower(user, now);
        //this.wepaFollowerRepository.save(newFollowedBy);
        //record WepaFollower both to the one who follows and the one being followed
        //followed.getFollowingBy().add(newFollowedBy);
        //this.wepaTweetterRepository.save(followed);
        //followed.getFollowingBy().add(newFollower);
        //this.wepaTweetterRepository.save(followed);
        user.getFollowing().add(newFollower);
        System.out.println("tällä listan pituus on: " + user.getFollowing().size());
        this.wepaTweetterRepository.save(user);
        System.out.println("tällä listan pituus on: " + user.getFollowing().size());
        //user.setFollowing(new ArrayList<WepaFollower>());
        //user.setFollowingBy(new ArrayList<WepaFollower>());
        return user;
    }
    
}
