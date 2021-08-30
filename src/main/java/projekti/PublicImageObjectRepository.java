package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tspaanan
 */
public interface PublicImageObjectRepository extends JpaRepository<PublicImageObject, Long> {
    List<PublicImageObject> findByOwner(WepaTweetter owner);
    
}
