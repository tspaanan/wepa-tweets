package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tspaanan
 */
public interface WepaCommentRepository extends JpaRepository<WepaComment, Long> {
    List<WepaComment> findByMessageIn(List<WepaMessage> wepaMessages, Pageable pageable);
    List<WepaComment> findByImageIn(List<PublicImageObject> images, Pageable pageable);
}
