package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tspaanan
 */
@Profile("development")
@RestController
public class DatabaseRestController {
    
    @Autowired
    private WepaTweetterRepository wepaTweetterRepository;
    
    @PostMapping("/search/path/to/nowhere")
    public List<WepaTweetter> search(@RequestParam String value) {
        System.out.println("searchTesti");
        List<WepaTweetter> listOfTweetters = new ArrayList<>();
        listOfTweetters.add(this.wepaTweetterRepository.findByUsername(value));
        return listOfTweetters;
    }
}
