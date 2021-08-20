package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DefaultController {
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    //@GetMapping("*")
    //public String helloWorld(Model model) {
    //    model.addAttribute("message", "World!");
    //    return "index";
    //}
    
    @GetMapping("/")
    public String index(Model model) {
        
        return "index";
    }
    
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
}
