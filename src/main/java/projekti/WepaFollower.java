package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author tspaanan
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class WepaFollower extends AbstractPersistable<Long> {
    
    @ManyToOne
    private WepaTweetter followed;
    @ManyToOne
    private WepaTweetter followedBy;
    private LocalDateTime startFollowing;
}
