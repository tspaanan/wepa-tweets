package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tspaanan
 */
public interface WepaTweetterRepository extends JpaRepository<WepaTweetter, Long> {
    WepaTweetter findByRandom(String random);
    WepaTweetter findByUsername(String username);
}
