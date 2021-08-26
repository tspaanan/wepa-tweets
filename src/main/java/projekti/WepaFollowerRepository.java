package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tspaanan
 */
public interface WepaFollowerRepository extends JpaRepository<WepaFollower, Long> {
    List<WepaFollower> findByFollowed(WepaTweetter wepaTweetter);
    WepaFollower findByFollowedAndFollowedBy(WepaTweetter followed, WepaTweetter followedBy);
}
