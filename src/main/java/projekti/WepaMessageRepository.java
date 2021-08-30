package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tspaanan
 */
public interface WepaMessageRepository extends JpaRepository<WepaMessage, Long> {
    List<WepaMessage> findByTweetter(WepaTweetter wepaTweetter);
    List<WepaMessage> findByTweetterIn(List<WepaTweetter> wepaTweetters);
}
